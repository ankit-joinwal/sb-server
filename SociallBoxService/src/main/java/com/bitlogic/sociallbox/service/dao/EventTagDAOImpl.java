package com.bitlogic.sociallbox.service.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bitlogic.sociallbox.data.model.EventTag;
import com.bitlogic.sociallbox.data.model.EventType;
import com.bitlogic.sociallbox.data.model.User;

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
	    		   .setFetchMode("tagPreferences", FetchMode.JOIN)
	    		   .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        User user = (User)criteria.uniqueResult();
        
		return new ArrayList<>(user.getTagPreferences());
	}
	
	@Override
	public List<Long> getUserTagIds(Long userId) {
		 Criteria criteria = getSession().createCriteria(User.class).add(Restrictions.eq("id", userId))
	    		   .setFetchMode("tagPreferences", FetchMode.JOIN)
	    		   .createAlias("tagPreferences", "tag")
	    		   .setProjection(Projections.property("tag.id"))
	    		   .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Long> tagIds = criteria.list();
		return tagIds;
	}
	
	@Override
	public List<Long> getAllTagIds() {
		 Criteria criteria = getSession().createCriteria(User.class)
	    		   .setFetchMode("tagPreferences", FetchMode.JOIN)
	    		   .createAlias("tagPreferences", "tag")
	    		   .setProjection(Projections.property("tag.id"))
	    		   .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Long> tagIds = criteria.list();
		return tagIds;
	}
	
	@Override
	public List<EventTag> saveUserTagPreferences(List<EventTag> tags,
			Long userId) {
		 Criteria criteria = getSession().createCriteria(User.class).add(Restrictions.eq("id", userId))
	    		   .setFetchMode("tagPreferences", FetchMode.JOIN)
	    		   .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		 User user = (User)criteria.uniqueResult();
		 
		 user.setTagPreferences(new HashSet<>(tags));
		 
		return tags;
	}
}
