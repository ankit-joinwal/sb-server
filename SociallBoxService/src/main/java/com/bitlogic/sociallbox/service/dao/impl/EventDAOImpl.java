package com.bitlogic.sociallbox.service.dao.impl;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import com.bitlogic.sociallbox.data.model.EventOrganizerAdmin;
import com.bitlogic.sociallbox.data.model.EventStatus;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.UserFavouriteEvents;
import com.bitlogic.sociallbox.data.model.response.EODashboardResponse.AttendeesInMonth;
import com.bitlogic.sociallbox.data.model.response.EventResponse;
import com.bitlogic.sociallbox.service.dao.AbstractDAO;
import com.bitlogic.sociallbox.service.dao.EventDAO;
import com.bitlogic.sociallbox.service.transformers.EventTransformer;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory.TransformerTypes;
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
					.getTransformer(TransformerTypes.EVENT_TRANS);
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
	public List<EventResponse> getUpcomingEventsOfOrg(Long userId,Map<String, Double> cordinatesMap,
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
			List<String> userFavEvents = new ArrayList<String>();
			if(userId!=null){
				Criteria userFavEventsCrit = getSession().createCriteria(UserFavouriteEvents.class)
									.setProjection(Projections.property("eventId"))
									.add(Restrictions.eq("userId", userId));
				userFavEvents = userFavEventsCrit.list();
			}	
			EventTransformer transformer = (EventTransformer) TransformerFactory
					.getTransformer(TransformerTypes.EVENT_TRANS);
			EventResponse eventInCity = null;
			
			Double sourceLatt = cordinatesMap.get(Constants.LATTITUDE_KEY);
			Double sourceLng = cordinatesMap.get(Constants.LONGITUDE_KEY);
			
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
					.getTransformer(TransformerTypes.EVENT_TRANS);
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
	
	@Override
	public void removeEventFromFav(UserFavouriteEvents favouriteEvents) {
		
		Criteria criteria = getSession().createCriteria(UserFavouriteEvents.class)
				.add(Restrictions.eq("userId", favouriteEvents.getUserId()))
				.add(Restrictions.eq("eventId", favouriteEvents.getEventId()));
		UserFavouriteEvents favouriteEvent = (UserFavouriteEvents) criteria.uniqueResult();
		if(favouriteEvent!=null){
			getSession().delete(favouriteEvent);
		}
	}
	
	@Override
	public void deRegisterForEvent(String eventId, Long userId) {
		EventAttendee attendee = getAttendee(eventId, userId);
		if(attendee!=null){
			getSession().delete(attendee);
		}
	}
	
	@Override
	public void deRegisterMeetupAtEvent(String meetupId, String eventId) {
		String sql = "DELETE FROM EVENT_ATTENDEES WHERE EVENT_ID = :eventId AND USER_ID IN (SELECT USER_ID FROM MEETUP_ATTENDEES "+
					" WHERE MEETUP_ID = :meetupId)";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("eventId", eventId);
		query.setParameter("meetupId", meetupId);
		query.executeUpdate();
	}
	
	@Override
	public List<Event> getUserPastRegisteredEvents(User user) {
		Date now = new Date();
		String sql = "SELECT * FROM EVENT EVENT"
				+ "	INNER JOIN EVENT_DETAILS ED ON EVENT.ID = ED.EVENT_ID "
				+ " INNER JOIN EVENT_IMAGES EI ON EVENT.ID = EI.EVENT_ID "
				+ "	INNER JOIN EVENT_ATTENDEES EA ON EVENT.ID = EA.EVENT_ID "
				+ "	WHERE EVENT.EVENT_STATUS = :eventStatus "
				+ "	AND EI.DISPLAY_ORDER = 1 " + "	AND EA.USER_ID = :userId "
				+ "	AND EVENT.END_DT < :startDt ";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.addEntity(Event.class);
		sqlQuery.setParameter("eventStatus", EventStatus.LIVE.name());
		sqlQuery.setParameter("userId", user.getId());
		sqlQuery.setParameter("startDt", now);
		List results = sqlQuery.list();
		List<Event> events = new ArrayList<Event>();
		
		if (results != null && !results.isEmpty()) {
			for (Iterator iterator = results.iterator(); iterator.hasNext();) {
				Event event = (Event) iterator.next();
				event.getTags().size();
				events.add(event);
			}
		}
						
		return events;
	}
	
	@Override
	public List<Event> getUserPastFavouriteEvents(User user) {
		Date now = new Date();
		String sql = "SELECT * FROM EVENT EVENT"
				+ "	INNER JOIN EVENT_DETAILS ED ON EVENT.ID = ED.EVENT_ID "
				+ " INNER JOIN EVENT_IMAGES EI ON EVENT.ID = EI.EVENT_ID "
				+ "	INNER JOIN USER_FAVOURITE_EVENTS UFE ON EVENT.ID = UFE.EVENT_ID "
				+ "	WHERE EVENT.EVENT_STATUS = :eventStatus "
				+ "	AND EI.DISPLAY_ORDER = 1 " + "	AND UFE.USER_ID = :userId "
				+ "	AND EVENT.END_DT < :startDt ";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.addEntity(Event.class);
		sqlQuery.setParameter("eventStatus", EventStatus.LIVE.name());
		sqlQuery.setParameter("userId", user.getId());
		sqlQuery.setParameter("startDt", now);
		List results = sqlQuery.list();
		List<Event> events = new ArrayList<Event>();
		
		if (results != null && !results.isEmpty()) {
			for (Iterator iterator = results.iterator(); iterator.hasNext();) {
				Event event = (Event) iterator.next();
				event.getTags().size();
				events.add(event);
			}
		}
						
		return events;
	}
	
	@Override
	public List<Event> getUserUpcomingRegisteredEvents(User user) {
		Date now = new Date();
		String sql = "SELECT * FROM EVENT EVENT"
				+ "	INNER JOIN EVENT_DETAILS ED ON EVENT.ID = ED.EVENT_ID "
				+ " INNER JOIN EVENT_IMAGES EI ON EVENT.ID = EI.EVENT_ID "
				+ "	INNER JOIN EVENT_ATTENDEES EA ON EVENT.ID = EA.EVENT_ID "
				+ "	WHERE EVENT.EVENT_STATUS = :eventStatus "
				+ "	AND EI.DISPLAY_ORDER = 1 " + "	AND EA.USER_ID = :userId "
				+ "	AND EVENT.END_DT >= :startDt ";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.addEntity(Event.class);
		sqlQuery.setParameter("eventStatus", EventStatus.LIVE.name());
		sqlQuery.setParameter("userId", user.getId());
		sqlQuery.setParameter("startDt", now);
		List results = sqlQuery.list();
		List<Event> events = new ArrayList<Event>();
		
		if (results != null && !results.isEmpty()) {
			for (Iterator iterator = results.iterator(); iterator.hasNext();) {
				Event event = (Event) iterator.next();
				event.getTags().size();
				events.add(event);
			}
		}
						
		return events;
	}
	
	@Override
	public List<Event> getUserUpcomingFavouriteEvents(User user) {

		Date now = new Date();
		String sql = "SELECT * FROM EVENT EVENT"
				+ "	INNER JOIN EVENT_DETAILS ED ON EVENT.ID = ED.EVENT_ID "
				+ " INNER JOIN EVENT_IMAGES EI ON EVENT.ID = EI.EVENT_ID "
				+ "	INNER JOIN USER_FAVOURITE_EVENTS UFE ON EVENT.ID = UFE.EVENT_ID "
				+ "	WHERE EVENT.EVENT_STATUS = :eventStatus "
				+ "	AND EI.DISPLAY_ORDER = 1 " + "	AND UFE.USER_ID = :userId "
				+ "	AND EVENT.END_DT > :startDt ";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.addEntity(Event.class);
		sqlQuery.setParameter("eventStatus", EventStatus.LIVE.name());
		sqlQuery.setParameter("userId", user.getId());
		sqlQuery.setParameter("startDt", now);
		List results = sqlQuery.list();
		List<Event> events = new ArrayList<Event>();
		
		if (results != null && !results.isEmpty()) {
			for (Iterator iterator = results.iterator(); iterator.hasNext();) {
				Event event = (Event) iterator.next();
				event.getTags().size();
				events.add(event);
			}
		}
						
		return events;
	
	}
	
	@Override
	public List<String> getEventCountPastSixMonth(Long profileId) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -6);
		cal.set(Calendar.DAY_OF_MONTH, 1);
        String dateBeforeSixMonthStr =  "" + cal.get(Calendar.DAY_OF_MONTH) +"/" +
                (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.YEAR);
        List<String> eventIds = new ArrayList<>();
        try{
        	Date dateBeforeSixMonth = dateFormat.parse(dateBeforeSixMonthStr);
        	Date now = new Date();
        	
        	String sql = "SELECT COUNT(1),EVENT.ID,DTLS.ORGANIZER_ADMIN_ID,EVENT.EVENT_STATUS,EVENT.START_DT"
        			+ "		 FROM EVENT EVENT INNER JOIN EVENT_DETAILS DTLS  "+
        					"	ON EVENT.ID = DTLS.EVENT_ID	" +
        					"	GROUP BY EVENT.ID "+
        					"	HAVING DTLS.ORGANIZER_ADMIN_ID = :profileId "
        					+ "	AND EVENT.EVENT_STATUS = :status "
        					+ " AND EVENT.START_DT > :dateStart "
        					+ "	AND EVENT.START_DT < :dateEnd ";	
        	SQLQuery query = getSession().createSQLQuery(sql);
        	query.setParameter("profileId", profileId);
        	query.setParameter("status", EventStatus.LIVE.name());
        	query.setParameter("dateStart", dateBeforeSixMonth);
        	query.setParameter("dateEnd", now);
        	
        	List results = query.list();
        	
        	
        	 if(results!=null && !results.isEmpty()){
    			 for (Iterator iterator = results.iterator(); iterator.hasNext();) {
    				 Object[] resultArr = (Object[]) iterator.next();
    				 eventIds.add((String) resultArr[1]);
    			 }
        	 }
        }catch(ParseException ex){
        	ex.printStackTrace();
        }
		
		
		
		return eventIds;
	}
	
	@Override
	public Integer getAttendeesCountForEvents(List<String> eventIds) {
		String sql = "SELECT DISTINCT(USER_ID) FROM EVENT_ATTENDEES WHERE EVENT_ID IN ( :eventIds )";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameterList("eventIds", eventIds);
		
		List result = query.list();
		Integer count = 0;
		count = result.size();
		return count;
	}
	
	@Override
	public Integer getInterestedUsersCountForEvents(List<String> eventIds) {
		String sql = "SELECT DISTINCT(USER_ID) FROM USER_FAVOURITE_EVENTS WHERE EVENT_ID IN ( :eventIds )";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameterList("eventIds", eventIds);
		
		List result = query.list();
		Integer count = 0;
		count = result.size();
		return count;
	}
	
	@Override
	public Integer getMeetupsAtEvents(List<String> eventIds) {
		String sql = "SELECT DISTINCT(ID) FROM MEETUP WHERE EVENT_ID IN ( :eventIds )";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameterList("eventIds", eventIds);
		
		List result = query.list();
		Integer count = 0;
		count = result.size();
		return count;
	}
	
	@Override
	public List<AttendeesInMonth> getAttendeesByMonth(Long profileId) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -6);
		cal.set(Calendar.DAY_OF_MONTH, 1);
        String dateBeforeSixMonthStr =  "" + cal.get(Calendar.DAY_OF_MONTH) +"/" +
                (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.YEAR);
        Map<String,List<String>> monthAndEvents = new LinkedHashMap<>();
        
        for(int i = 5;i>=1;i--){
	        Calendar cal1 =  Calendar.getInstance();
	        cal1.add(Calendar.MONTH ,-i);
	        //format it to MMM-yyyy // January-2012
	        String previousMonthYear  = new SimpleDateFormat("MMM").format(cal1.getTime());
	        List<String> dummyIds = new ArrayList<>();
	        dummyIds.add("dummy");
	        monthAndEvents.put(previousMonthYear, dummyIds);
        }
        List<AttendeesInMonth> attendees = new ArrayList<>();
        
        try{
        	Date dateBeforeSixMonth = dateFormat.parse(dateBeforeSixMonthStr);
        	Date now = new Date();
        	String sql = "SELECT DATE_FORMAT(EVENT.START_DT,'%b') EVENT_DATE,EVENT.ID,DTLS.ORGANIZER_ADMIN_ID,EVENT.EVENT_STATUS,EVENT.START_DT "
        			+ "		 FROM EVENT EVENT INNER JOIN EVENT_DETAILS DTLS  "+
        					"	ON EVENT.ID = DTLS.EVENT_ID	" +
        					"	GROUP BY EVENT_DATE,EVENT.ID "+
        					"	HAVING DTLS.ORGANIZER_ADMIN_ID = :profileId "
        					+ "	AND EVENT.EVENT_STATUS = :status "
        					+ " AND EVENT.START_DT > :dateStart "
        					+ "	AND EVENT.START_DT < :dateEnd "
        					+ "	ORDER BY EVENT.START_DT ";	
        	SQLQuery query = getSession().createSQLQuery(sql);
        	query.setParameter("profileId", profileId);
        	query.setParameter("status", EventStatus.LIVE.name());
        	query.setParameter("dateStart", dateBeforeSixMonth);
        	query.setParameter("dateEnd", now);
        	
        	List results = query.list();
        	
        	String attendeeSql = "SELECT DISTINCT(USER_ID) FROM EVENT_ATTENDEES WHERE EVENT_ID IN :eventIds";
        	SQLQuery  query2 = getSession().createSQLQuery(attendeeSql);
        	
	       	 if(results!=null && !results.isEmpty()){
	   			 for (Iterator iterator = results.iterator(); iterator.hasNext();) {
	   				 Object[] resultArr = (Object[]) iterator.next();
	   				 String month = (String) resultArr[0];
	   				 String eventId = (String) resultArr[1];
	   				 
	   				 if(monthAndEvents.containsKey(month)){
	   					 List<String> eventIds = monthAndEvents.get(month);
	   					 eventIds.add(eventId);
	   				 }else{
	   					 List<String> eventIds = new ArrayList<>();
	   					 monthAndEvents.put(month, eventIds);
	   				 }
	   			 }
	   			 
	   			Iterator<Map.Entry<String, List<String>>> iter = monthAndEvents.entrySet().iterator();
	   			while(iter.hasNext()){
	   				Map.Entry<String, List<String>> entry = iter.next();
	   				
	   				query2.setParameterList("eventIds", entry.getValue());
	   				
	   				List attendeeResults = query2.list();
	   				AttendeesInMonth attendeesInMonth = new AttendeesInMonth();
	   				attendeesInMonth.setMonth(entry.getKey());
	   				attendeesInMonth.setAttendees(attendeeResults.size());
	   				attendees.add(attendeesInMonth);
	   			}
	   			
	   			 
	       	 }
        	
        }catch(ParseException ex){
        	ex.printStackTrace();
        }
			
        
		return attendees;
	}
}
