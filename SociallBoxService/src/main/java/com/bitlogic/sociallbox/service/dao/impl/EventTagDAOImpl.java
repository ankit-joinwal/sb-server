package com.bitlogic.sociallbox.service.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.EventTag;
import com.bitlogic.sociallbox.data.model.EventType;
import com.bitlogic.sociallbox.data.model.SmartDevice;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.service.dao.AbstractDAO;
import com.bitlogic.sociallbox.service.dao.EventTagDAO;
import com.bitlogic.sociallbox.service.dao.EventTypeDAO;

@Repository("eventTagDAO")
public class EventTagDAOImpl extends AbstractDAO implements EventTagDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventTagDAOImpl.class);
	@Autowired
	private EventTypeDAO eventTypeDAO;
	@Override
	public EventTag create(EventTag eventTag) {

		LOGGER.info("### Inside EventTagDAOImpl.create to create EventTag {} ###",eventTag);
		
		Set<EventType> eventTypes = eventTag.getRelatedEventTypes();
		
		LOGGER.info(" Related event types for tag {} ",eventTypes);
		if(eventTypes!= null && !eventTypes.isEmpty()){
			List<String> eventTypeNames = new ArrayList<>();
			for(EventType eventType : eventTypes){
				eventTypeNames.add(eventType.getName());
			}
			List<EventType> relatedEventTypes = this.eventTypeDAO.getEventTypesByNames(eventTypeNames);
			LOGGER.info("Event Types pulled from DB {} ",eventTypes);
			Set<EventType> eventTypesSet = new HashSet<>(relatedEventTypes);
			eventTag.setRelatedEventTypes(eventTypesSet);
		}
		saveOrUpdate(eventTag);
		LOGGER.info("Create Tag Complete.");
		return eventTag;
	}
	
	@Override
	public List<EventTag> getAll() {
		Criteria criteria = getSession().createCriteria(EventTag.class);
		return (List<EventTag>) criteria.list();
	}

	
	@Override
	public List<EventTag> getTagsByNames(List<String> names) {
		Criteria criteria = getSession().createCriteria(EventTag.class).add(Restrictions.in("name", names));
		return (List<EventTag>) criteria.list();
	}
	
	@Override
	public List<EventTag> getUserTags(Long userId) {

		 Criteria criteria = getSession().createCriteria(User.class).add(Restrictions.eq("id", userId))
	    		   .createAlias("userEventInterests", "tagPreference")
				 	.setFetchMode("tagPreference", FetchMode.JOIN)
				 	.setFetchMode("tagPreference.relatedEventTypes", FetchMode.JOIN)
	    		   .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        User user = (User)criteria.uniqueResult();
        Set<EventTag> userTags = new HashSet<>();
        for(EventType eventType : user.getUserEventInterests()){
        	userTags.addAll(eventType.getRelatedTags());
        }
		return new ArrayList<>(userTags);
	}
	
	
	
	@Override
	public List<Long> getUserTagIds(Long userId) {
		 String sql = "SELECT ETT.TAG_ID FROM EVENT_TYPE_TAGS ETT INNER JOIN USER_EVENT_INTERESTS UEI ON ETT.EVENT_TYPE_ID = "
		 			+ " UEI.EVENT_TYPE_ID INNER JOIN EVENT_TYPE ET ON UEI.EVENT_TYPE_ID = ET.ID "
		 			+ " WHERE UEI.USER_ID = :userId";
		 SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		 sqlQuery.setParameter("userId", userId);
		 List results = sqlQuery.list();

		 List<Long> tagIds = (List<Long>) results;
		 return tagIds;
	}
	
	@Override
	public List<Long> getRetailTagIdsForUser(Long userId) {
		 String sql = "SELECT ETT.TAG_ID FROM EVENT_TYPE_TAGS ETT INNER JOIN USER_EVENT_INTERESTS UEI ON ETT.EVENT_TYPE_ID = "
		 			+ " UEI.EVENT_TYPE_ID INNER JOIN EVENT_TYPE ET ON UEI.EVENT_TYPE_ID = ET.ID "
		 			+ " WHERE ET.NAME = :shopTypeName AND UEI.USER_ID = :userId";
		 SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		 sqlQuery.setParameter("shopTypeName", Constants.SHOPPING_EVENT_TYPE_NAME);
		 sqlQuery.setParameter("userId", userId);
		 List results = sqlQuery.list();

		 List<Long> tagIds = (List<Long>) results;
		return tagIds;
	}
	
	@Override
	public List<EventTag> getRetailTagsForUser(Long userId) {
		String sql = "SELECT * FROM EVENT_TYPE_TAGS ETT INNER JOIN USER_EVENT_INTERESTS UEI ON ETT.EVENT_TYPE_ID = "
				+ " UEI.EVENT_TYPE_ID INNER JOIN EVENT_TYPE ET ON UEI.EVENT_TYPE_ID = ET.ID "
				+ " WHERE ET.NAME = :shopTypeName AND UEI.USER_ID = :userId";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setParameter("shopTypeName",
				Constants.SHOPPING_EVENT_TYPE_NAME);
		sqlQuery.setParameter("userId", userId);
		List results = sqlQuery.list();
		List<EventTag> tags = new ArrayList<EventTag>();
		System.out.println("Found : " + results.size() + " tags");
		if (results != null && !results.isEmpty()) {
			for (Iterator iterator = results.iterator(); iterator.hasNext();) {
				EventTag tag = (EventTag) iterator.next();
				tags.add(tag);
			}
		}
		return tags;
	}

	@Override
	public List<Long> getAllRetailTagIds() {
		String sql = "SELECT ETT.TAG_ID FROM EVENT_TYPE_TAGS ETT  INNER JOIN EVENT_TYPE ET ON ETT.EVENT_TYPE_ID = ET.ID "
				+ " WHERE ET.NAME = :shopTypeName";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setParameter("shopTypeName",
				Constants.SHOPPING_EVENT_TYPE_NAME);
		List results = sqlQuery.list();

		List<Long> tagIds = (List<Long>) results;
		return tagIds;
	}
	
	@Override
	public List<EventTag> getAllRetailTag() {
		
		String sql = "SELECT * FROM EVENT_TYPE_TAGS ETT  INNER JOIN EVENT_TYPE ET ON ETT.EVENT_TYPE_ID = ET.ID "
				+ " WHERE ET.NAME = :shopTypeName";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.addEntity(EventTag.class);
		
		sqlQuery.setParameter("shopTypeName",
				Constants.SHOPPING_EVENT_TYPE_NAME);
		List results = sqlQuery.list();
		List<EventTag> tags = new ArrayList<EventTag>();
		System.out.println("Found : "+results.size()+" tags");
		if(results!=null && !results.isEmpty()){
			for(Iterator iterator = results.iterator(); iterator.hasNext();){
				EventTag tag = (EventTag) iterator.next();
				tags.add(tag);
			}
		}
		return tags;
	}
	@Override
	public List<Long> getAllTagIds() {
		String sql = "SELECT DISTINCT ETT.TAG_ID FROM EVENT_TYPE_TAGS ETT INNER JOIN EVENT_TYPE ET ON ETT.EVENT_TYPE_ID = ET.ID "
				+ " WHERE ET.NAME != :shopTypeName";

		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setParameter("shopTypeName",
				Constants.SHOPPING_EVENT_TYPE_NAME);
		List results = sqlQuery.list();

		List<Long> tagIds = (List<Long>) results;
		return tagIds;
	}
	

}
