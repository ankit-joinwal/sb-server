package com.bitlogic.sociallbox.service.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.Event;
import com.bitlogic.sociallbox.data.model.EventImage;
import com.bitlogic.sociallbox.data.model.EventResponse;
import com.bitlogic.sociallbox.service.transformers.Transformer;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory.Transformer_Types;

@Repository("eventDAO")
public class EventDAOImpl extends AbstractDAO implements EventDAO {

	private static final Logger logger = LoggerFactory.getLogger(EventDAOImpl.class);
	@Override
	public Event create(Event event) {

		Date now = new Date();
		event.getEventDetails().setCreateDt(now);
		String eventId = (String) getSession().save(event);
		Event eventInDb = getEventWithoutImage(eventId);
		return eventInDb;
	}

	@Override
	public Event saveEvent(Event event) {
		 saveOrUpdate(event);
		 return event;
	}
	
	@Override
	public Event getEvent(String id) {
		Criteria criteria = getSession().createCriteria(Event.class,"event")
				.add(Restrictions.eq("event.uuid", id))
				.setFetchMode("event.eventDetails", FetchMode.JOIN)
				.setFetchMode("event.tags", FetchMode.JOIN)
				.setFetchMode("event.eventImages", FetchMode.JOIN)
				.createAlias("event.eventImages", "image")
				.add(Restrictions.eq("image.displayOrder", 1))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		Event event = (Event) criteria.uniqueResult();
		if(event!=null && event.getEventImages()!=null){
			//To Load images
			event.getEventImages().size();
			event.getTags().size();
			event.getEventDetails().toString();
		}
		
		return event;
	}
	
	@Override
	public Event getEventWithoutImage(String id) {
		Criteria criteria = getSession().createCriteria(Event.class,"event")
				.add(Restrictions.eq("event.uuid", id))
				.setFetchMode("event.eventDetails", FetchMode.JOIN)
				.setFetchMode("event.tags", FetchMode.JOIN)
				//.setFetchMode("event.eventImages", FetchMode.JOIN)
				//.createAlias("event.eventImages", "image")
				//.add(Restrictions.eq("image.displayOrder", 1))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		Event event = (Event) criteria.uniqueResult();
		event.getTags().size();
		event.getEventDetails().toString();
		
		return event;
	}
	
	@Override
	public void saveEventImages(List<EventImage> images) {
		for(EventImage eventImage : images){
			saveOrUpdate(eventImage);
		}
	}
	
	@Override
	public List<EventImage> getEventImages(String eventId) {
	
		 Criteria cr = getSession().createCriteria(EventImage.class)
	        	    .setFetchMode("event", FetchMode.JOIN)
	        	    .add(Restrictions.eqOrIsNull("event.uuid", eventId))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        List<EventImage> images= (List<EventImage>) cr.list();
		
		return images;
	}
	
	@Override
	public void makeEventLive(Event event) {
	
		
		event.setIsLive("true");
		
	}
	
	@Override
	public List<EventResponse> getEventsByFilter(List<Long> tagIds,
			String city, String country,Integer page) {
		 Criteria criteria = getSession().createCriteria(Event.class,"event")
					.setFetchMode("event.eventDetails", FetchMode.JOIN)
					.setFetchMode("event.eventDetails.organizer", FetchMode.JOIN)
					.setFetchMode("event.tags", FetchMode.JOIN)
					.createAlias("event.eventDetails", "ed")
					.createAlias("event.tags", "eventTag")
					.setFetchMode("event.eventImages", FetchMode.JOIN)
					.createAlias("event.eventImages", "image")
					.add(Restrictions.eq("image.displayOrder", 1))
					.add(Restrictions.eq("isLive", "true"))
					.add((tagIds==null || tagIds.isEmpty() ) ?  Restrictions.like("eventTag.name","%") : Restrictions.in("eventTag.id",tagIds))
					.add(Restrictions.and(Restrictions.like("ed.location.name", city,MatchMode.ANYWHERE)
							,Restrictions.like("ed.location.name", country,MatchMode.ANYWHERE)))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		 List<Event> events = criteria.list();
		 List<EventResponse> eventsResponse = new ArrayList<EventResponse>();
		 if(events!=null && !events.isEmpty()){
			 	logger.info("Found : {} events",events.size());
			
			 	int totalEvents = events.size();
			 	int startIdx = (page-1)*Constants.RECORDS_PER_PAGE;
			 	int endIdx = (page*Constants.RECORDS_PER_PAGE)>totalEvents ? totalEvents : (page*Constants.RECORDS_PER_PAGE);
			 	List<Event> paginatedEvents = events.subList(startIdx, endIdx);
			 	
				Transformer<EventResponse, Event> transformer = (Transformer<EventResponse, Event>) TransformerFactory.getTransformer(Transformer_Types.EVENT_TRANS);
				EventResponse eventInCity = null;
				for(Event event : paginatedEvents){
					//TODO: This is done to lazy load the tags.
					event.getTags().size();
					if(event.getEventImages()!=null){
						//To Load images
						event.getEventImages().size();
					}
					eventInCity = transformer.transform(event);
					eventsResponse.add(eventInCity);
				}
				
			}
		 
		return eventsResponse;
	}
	
}
