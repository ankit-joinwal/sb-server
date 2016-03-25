package com.bitlogic.sociallbox.service.business;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.Event;
import com.bitlogic.sociallbox.data.model.EventAttendee;
import com.bitlogic.sociallbox.data.model.EventImage;
import com.bitlogic.sociallbox.data.model.MeetupAttendee;
import com.bitlogic.sociallbox.data.model.requests.CreateEventRequest;
import com.bitlogic.sociallbox.data.model.response.EventResponse;

public interface EventService {

	@PreAuthorize("hasAnyRole('"+Constants.ROLE_TYPE_ADMIN+"','"+Constants.ROLE_ORGANIZER+"')")
	public Event create(String userEmail,CreateEventRequest createEventRequest);
	
	public Event get(String uuid);
	
	@PreAuthorize("hasAnyRole('"+Constants.ROLE_TYPE_ADMIN+"','"+Constants.ROLE_ORGANIZER+"')")
	public void makeEventLive(String eventId);
	
	public List<EventResponse> getEventsForUser(String userLocation,Long userId,String city,String country,Integer page);
	
	public List<EventResponse> getEventsByType(String userLocation,String eventType,String city,String country,Integer page);
	
	@PreAuthorize("hasAnyRole('"+Constants.ROLE_TYPE_ADMIN+"','"+Constants.ROLE_ORGANIZER+"')")
	public void storeEventImages(String imagesURL,List<MultipartFile> images , String eventId) ;

	public List<EventImage> getEventImages(String eventId);
	
	@PreAuthorize("hasRole('"+Constants.ROLE_TYPE_ADMIN+"')")
	public List<EventResponse> getEventsPendingForApproval();
	
	@PreAuthorize("hasRole('"+Constants.ROLE_TYPE_ADMIN+"')")
	public void approveEvents(List<String> eventIds);
	
	public EventAttendee registerForEvent(String eventId, String deviceId);
}
