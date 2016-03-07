package com.bitlogic.sociallbox.service.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.Category;
import com.bitlogic.sociallbox.data.model.EventType;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;

@Repository("eventTypeDao")
public class EventTypeDAOImpl extends AbstractDAO implements EventTypeDAO {

	@Autowired
	private CategoryDAO categoryDAO;
	
	@Override
	public EventType createEventType(EventType eventType) {
		
		Set<Category> relatedCategories = eventType.getRelatedCategories();
		
		if(relatedCategories!=null && !relatedCategories.isEmpty()){
			List<String> catNameList = new ArrayList<>();
			for(Category category:relatedCategories){
				catNameList.add(category.getName());
				
			}
			List<Category> categories = categoryDAO.getCategoriesByName(catNameList);
			Set<Category> categoriesToSaveWith = new HashSet<>(categories);
			eventType.setRelatedCategories(categoriesToSaveWith);
		}
		saveOrUpdate(eventType);
		return eventType;
	}
	
	@Override
	public EventType save(EventType eventType) {
		saveOrUpdate(eventType);
		return eventType;
	}
	
	@Override
	public List<EventType> getAllEventTypes() {
		Criteria criteria = getSession()
				.createCriteria(EventType.class);
		return (List<EventType>) criteria.list();
	}
	
	@Override
	public List<EventType> getEventTypesByNames(List<String> names) {
		Criteria criteria = getSession().createCriteria(EventType.class).add(Restrictions.in("name", names)).setFetchMode("relatedTags", FetchMode.JOIN);
		List<EventType> eventTypes = (List<EventType>) criteria.list();
		return eventTypes;
	}

	@Override
	public EventType getEventTypeByName(String name) {
		Criteria criteria = getSession().createCriteria(EventType.class).add(Restrictions.eq("name", name));
		EventType eventType = (EventType)criteria.uniqueResult();
		//TODO: This is done to lazy load the tags.
		//If we use join fetch , then m*n records are pulled up.
		if(eventType==null){
			throw new ClientException(RestErrorCodes.ERR_003,Constants.ERROR_EVENT_TYPE_INVALID);
		}
		eventType.getRelatedTags().size();
		return eventType;
	}
	
	@Override
	public List<EventType> getUserInterests(Long userId) {
		 Criteria criteria = getSession().createCriteria(User.class).add(Restrictions.eq("id", userId))
	    		   .createAlias("userEventInterests", "eventType")
				 	.setFetchMode("eventType", FetchMode.JOIN)
	    		   .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
      User user = (User)criteria.uniqueResult();
      
	return new ArrayList<>(user.getUserEventInterests());
	}
	
	@Override
	public List<EventType> saveUserEventInterests(List<EventType> tags,
			Long userId) {
		 Criteria criteria = getSession().createCriteria(User.class).add(Restrictions.eq("id", userId))
	    		   .setFetchMode("userEventInterests", FetchMode.JOIN)
	    		   .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		 User user = (User)criteria.uniqueResult();
		 
		 user.setUserEventInterests(new HashSet<>(tags));
		 
		return tags;
	}
}
