package com.bitlogic.sociallbox.service.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;



import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.Event;
import com.bitlogic.sociallbox.data.model.EventAttendee;
import com.bitlogic.sociallbox.data.model.EventImage;
import com.bitlogic.sociallbox.data.model.EventOrganizer;
import com.bitlogic.sociallbox.data.model.EventOrganizerAdmin;
import com.bitlogic.sociallbox.data.model.EventStatus;
import com.bitlogic.sociallbox.data.model.UserFavouriteEvents;
import com.bitlogic.sociallbox.data.model.response.EventResponse;
import com.bitlogic.sociallbox.service.dao.AbstractDAO;
import com.bitlogic.sociallbox.service.dao.EventDAO;
import com.bitlogic.sociallbox.service.transformers.EventTransformer;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory.Transformer_Types;
import com.bitlogic.sociallbox.service.utils.GeoUtils;

@Repository("eventDAO")
public class EventDAOImpl extends AbstractDAO implements EventDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(EventDAOImpl.class);

	@Override
	public Event create(Event event) {

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
		Criteria criteria = getSession().createCriteria(Event.class, "event")
				.add(Restrictions.eq("event.uuid", id))
				.setFetchMode("event.tags", FetchMode.JOIN)
				.setFetchMode("event.eventImages", FetchMode.JOIN)
				.createAlias("event.eventImages", "image")
				.add(Restrictions.eq("image.displayOrder", 1))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		Event event = (Event) criteria.uniqueResult();
		if (event != null && event.getEventImages() != null) {
			// To Load images
			event.getEventImages().size();
			event.getTags().size();
			event.getEventDetails().toString();
		}

		return event;
	}

	@Override
	public Event getEventWithoutImage(String id) {
		Criteria criteria = getSession().createCriteria(Event.class, "event")
				.add(Restrictions.eq("event.uuid", id))
				.setFetchMode("event.tags", FetchMode.JOIN)

				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		Event event = (Event) criteria.uniqueResult();
		event.getTags().size();
		event.getEventDetails().toString();

		return event;
	}

	@Override
	public void saveEventImages(List<EventImage> images) {
		for (EventImage eventImage : images) {
			saveOrUpdate(eventImage);
		}
	}

	@Override
	public List<EventImage> getEventImages(String eventId) {

		Criteria cr = getSession().createCriteria(EventImage.class)
				.setFetchMode("event", FetchMode.JOIN)
				.add(Restrictions.eqOrIsNull("event.uuid", eventId))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<EventImage> images = (List<EventImage>) cr.list();

		return images;
	}

	@Override
	public void makeEventLive(Event event) {
		event.setEventStatus(EventStatus.LIVE);
	}

	@Override
	public List<EventResponse> getEventsByFilter(Long userId,
			Map<String, Double> cordinatesMap, List<Long> tagIds, String city,
			String country, Integer page) {
		int startIdx = (page - 1) * Constants.RECORDS_PER_PAGE;
		logger.info("Getting paginated events . Start :{}", startIdx);
		/*
		 * Criteria criteria = getSession().createCriteria(Event.class,"event")
		 * .setFetchMode("event.eventDetails.organizer", FetchMode.JOIN)
		 * .setFetchMode("event.tags", FetchMode.JOIN)
		 * .createAlias("event.eventDetails", "ed") .createAlias("event.tags",
		 * "eventTag") .setFetchMode("event.eventImages", FetchMode.JOIN)
		 * .createAlias("event.eventImages", "image")
		 * .add(Restrictions.eq("image.displayOrder", 1))
		 * .add(Restrictions.eq("event.eventStatus", EventStatus.LIVE))
		 * .add((tagIds==null || tagIds.isEmpty() ) ?
		 * Restrictions.like("eventTag.name","%") :
		 * Restrictions.in("eventTag.id",tagIds))
		 * .add(Restrictions.and(Restrictions.like("ed.location.name",
		 * city,MatchMode.ANYWHERE) ,Restrictions.like("ed.location.name",
		 * country,MatchMode.ANYWHERE))) .setFirstResult(startIdx)
		 * .setMaxResults(Constants.RECORDS_PER_PAGE)
		 * .addOrder(Order.asc("event.title"))
		 * .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); List<Event>
		 * events = criteria.list();
		 */

		Criteria criteria1 = getSession().createCriteria(Event.class, "event")
				.add(Restrictions.eq("event.eventStatus", EventStatus.LIVE))
				.setFirstResult(startIdx)
				.setMaxResults(Constants.RECORDS_PER_PAGE)
				.addOrder(Order.asc("event.title"));
		// Image Criteria
		Criteria imageCrit = criteria1.createCriteria("event.eventImages",
				"image");
		imageCrit.add(Restrictions.eq("image.displayOrder", 1));

		// Tag criteria
		Criteria tagCrit = criteria1.createCriteria("event.tags", "eventTag");
		tagCrit.add((tagIds == null || tagIds.isEmpty()) ? Restrictions.like(
				"eventTag.name", "%") : Restrictions.in("eventTag.id", tagIds));

		// Location Criteria
		Criteria locationCrit = criteria1.createCriteria("event.eventDetails",
				"ed");
		locationCrit
				.add(Restrictions.and(Restrictions.like("ed.location.name",
						city, MatchMode.ANYWHERE), Restrictions.like(
						"ed.location.name", country, MatchMode.ANYWHERE)));

		criteria1.setProjection(Projections.distinct(Projections
				.projectionList().add(Projections.id())

		));
		List list = criteria1.list();
		List idlist = new ArrayList<String>();
		for (Iterator iditer = list.iterator(); iditer.hasNext();) {
			String record = (String) iditer.next();
			idlist.add(record);
		}

		List<Event> events = new ArrayList<Event>();
		
		if (idlist.size() > 0) {
			criteria1 = getSession().createCriteria(Event.class);
			criteria1.add(Restrictions.in("id", idlist));
			events = criteria1.list();
		}
		
		List<EventResponse> eventsResponse = new ArrayList<EventResponse>();
		if (events != null && !events.isEmpty()) {
			List<String> userFavEvents = new ArrayList<String>();
			if(userId!=null){
				Criteria userFavEventsCrit = getSession().createCriteria(UserFavouriteEvents.class)
									.setProjection(Projections.property("eventId"))
									.add(Restrictions.eq("userId", userId));
				userFavEvents = userFavEventsCrit.list();
			}			
			logger.info("Found : {} events", events.size());

			/*
			 * int totalEvents = events.size(); int startIdx =
			 * (page-1)*Constants.RECORDS_PER_PAGE; int endIdx =
			 * (page*Constants.RECORDS_PER_PAGE) > totalEvents ? totalEvents :
			 * (page*Constants.RECORDS_PER_PAGE); List<Event> paginatedEvents =
			 * events.subList(startIdx, endIdx);
			 */

			Double sourceLatt = cordinatesMap.get(Constants.LATTITUDE_KEY);
			Double sourceLng = cordinatesMap.get(Constants.LONGITUDE_KEY);

			EventTransformer transformer = (EventTransformer) TransformerFactory
					.getTransformer(Transformer_Types.EVENT_TRANS);
			EventResponse eventInCity = null;
			for (Event event : events) {
				// TODO: This is done to lazy load the tags.
				event.getTags().size();
				if (event.getEventImages() != null) {
					// To Load images
					event.getEventImages().size();
				}
				
				
				eventInCity = transformer.transform(event);
				if(userFavEvents.contains(event.getUuid())){
					eventInCity.setUserFavEvent(Boolean.TRUE);
				}
				eventInCity.setDistanceFromSource(GeoUtils
						.calculateDistance(sourceLatt, sourceLng,
								event.getEventDetails().getLocation()
										.getLattitude(), event
										.getEventDetails().getLocation()
										.getLongitude()));
				eventsResponse.add(eventInCity);
			}

		}

		return eventsResponse;
	}
	
	@Override
	public List<EventResponse> getUpcomingEventsOfOrg(
			Set<EventOrganizerAdmin> eventOrganizerAdmins,String filterEventId) {
		
		Criteria criteria = getSession().createCriteria(Event.class,"event")
							.createAlias("event.eventDetails", "detail")
							.add(Restrictions.in("detail.organizerAdmin", eventOrganizerAdmins))
							
							.add(Restrictions.ge("event.startDate", new Date()));
		if(!StringUtils.isBlank(filterEventId) ){
			criteria.add(Restrictions.ne("uuid", filterEventId));
		}
		List<Event> events = criteria.list();
		List<EventResponse> eventsResponse = new ArrayList<EventResponse>();
		if (events != null && !events.isEmpty()) {
			EventTransformer transformer = (EventTransformer) TransformerFactory
					.getTransformer(Transformer_Types.EVENT_TRANS);
			EventResponse eventInCity = null;
			for (Event event : events) {
				// TODO: This is done to lazy load the tags.
				event.getTags().size();
				if (event.getEventImages() != null) {
					// To Load images
					event.getEventImages().size();
				}
				
				eventInCity = transformer.transform(event);
				
				
				eventsResponse.add(eventInCity);
			}
		}
		return eventsResponse;
	}

	@Override
	public List<EventResponse> getPendingEvents() {
		Criteria criteria = getSession().createCriteria(Event.class, "event")
				.setFetchMode("event.eventDetails.organizer", FetchMode.JOIN)
				.setFetchMode("event.tags", FetchMode.JOIN)
				.createAlias("event.eventDetails", "ed")
				.createAlias("event.tags", "eventTag")
				.setFetchMode("event.eventImages", FetchMode.JOIN)
				.createAlias("event.eventImages", "image")
				.add(Restrictions.eq("image.displayOrder", 1))
				.add(Restrictions.eq("event.eventStatus", EventStatus.CREATED))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Event> events = criteria.list();
		List<EventResponse> eventsResponse = new ArrayList<EventResponse>();
		if (events != null && !events.isEmpty()) {
			logger.info("Found : {} events", events.size());

			EventTransformer transformer = (EventTransformer) TransformerFactory
					.getTransformer(Transformer_Types.EVENT_TRANS);
			EventResponse eventInCity = null;
			for (Event event : events) {
				// TODO: This is done to lazy load the tags.
				event.getTags().size();
				if (event.getEventImages() != null) {
					// To Load images
					event.getEventImages().size();
				}

				eventInCity = transformer.transform(event);
				eventsResponse.add(eventInCity);
			}
		}
		return eventsResponse;
	}

	@Override
	public List<Event> getEventsByIds(List<String> eventIds) {
		Criteria criteria = getSession().createCriteria(Event.class)
				.add(Restrictions.in("uuid", eventIds))
				.add(Restrictions.eq("eventStatus", EventStatus.CREATED));

		return criteria.list();
	}

	@Override
	public EventAttendee saveAttendee(EventAttendee attendee) {
		if(!checkIfUserRegisteredForEvent(attendee)){
			Long id = (Long) getSession().save(attendee);
			return getAttendeeById(id);
		}else{
			return attendee;
		}
		
	}
	@Override
	public Boolean checkIfUserRegisteredForEvent(EventAttendee attendee) {
		EventAttendee registered = getAttendee(attendee.getEvent().getUuid(), attendee.getUser().getId());
		if(registered==null){
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	@Override
	public EventAttendee getAttendee(String eventId, Long userId) {
		String sql = "SELECT * FROM EVENT_ATTENDEES WHERE USER_ID = :userId AND EVENT_ID = :eventId";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addEntity(EventAttendee.class);
		query.setParameter("userId", userId);
		query.setParameter("eventId", eventId);

		List attendees = query.list();
		EventAttendee eventAttendee = null;
		for (Iterator iterator = attendees.iterator(); iterator.hasNext();) {
			eventAttendee = (EventAttendee) iterator.next();
			break;
		}

		return eventAttendee;
	}
	
	@Override
	public List<EventAttendee> getEventAttendees(Event event) {
		Criteria criteria = getSession().createCriteria(EventAttendee.class)
							.add(Restrictions.eq("event", event));
		
		return criteria.list();
	}
	
	@Override
	public List<Long> getEventAttendeesIds(Event event) {
		Criteria criteria = getSession().createCriteria(EventAttendee.class)
				.setFetchMode("event", FetchMode.JOIN)
				.add(Restrictions.eq("event", event))
				.setProjection(Projections.property("user.id"));

		return criteria.list();
	}

	@Override
	public EventAttendee getAttendeeById(Long attendeeId) {
		Criteria criteria = getSession().createCriteria(EventAttendee.class)
				.add(Restrictions.eq("id", attendeeId));
		return (EventAttendee) criteria.uniqueResult();
	}
	
	@Override
	public Boolean checkIfUserFavEvent(UserFavouriteEvents userFavouriteEvents) {
		Criteria criteria = getSession().createCriteria(UserFavouriteEvents.class)
				.add(Restrictions.eq("userId", userFavouriteEvents.getUserId()))
				.add(Restrictions.eq("eventId", userFavouriteEvents.getEventId()));
		UserFavouriteEvents favouriteEvent = (UserFavouriteEvents) criteria.uniqueResult();
		if(favouriteEvent!=null){
			return Boolean.TRUE;
		}else{
			return Boolean.FALSE;
		}
				
	}
	
	@Override
	public void addEventToFav(UserFavouriteEvents favouriteEvents) {

		Boolean isEventFav = checkIfUserFavEvent(favouriteEvents);
		if(!isEventFav){
			this.getSession().save(favouriteEvents);
		}
	}
}
