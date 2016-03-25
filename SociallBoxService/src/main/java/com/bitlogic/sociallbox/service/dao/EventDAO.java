package com.bitlogic.sociallbox.service.dao;

import java.util.List;
import java.util.Map;

import com.bitlogic.sociallbox.data.model.Event;
import com.bitlogic.sociallbox.data.model.EventAttendee;
import com.bitlogic.sociallbox.data.model.EventImage;
import com.bitlogic.sociallbox.data.model.response.EventResponse;
import com.bitlogic.sociallbox.service.exception.ServiceException;

public interface EventDAO {

	public Event create(Event event);
	
	public Event saveEvent(Event event);
	
	public Event getEvent(String id);
	
	public Event getEventWithoutImage(String id);
	
	public void makeEventLive(Event event);
	
	public List<EventResponse> getEventsByFilter(Map<String,Double> cordinatesMap,List<Long> tagIds,String city,String country,Integer page) throws ServiceException;
	
	public void saveEventImages(List<EventImage> images);
	
	public List<EventImage> getEventImages(String eventId);
	
	public List<EventResponse> getPendingEvents();
	
	public List<Event> getEventsByIds(List<String> eventIds);
	
	public EventAttendee saveAttendee(EventAttendee attendee);
	
	public EventAttendee getAttendee(String eventId , Long userId);
	
	public EventAttendee getAttendeeById(Long attendeeId);
}
