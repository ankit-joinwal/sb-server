package com.bitlogic.sociallbox.service.business;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.Event;
import com.bitlogic.sociallbox.data.model.EventImage;
import com.bitlogic.sociallbox.data.model.EventResponse;
import com.bitlogic.sociallbox.data.model.requests.CreateEventRequest;

public interface EventService {

	@PreAuthorize("hasAnyRole('"+Constants.ROLE_TYPE_ADMIN+"','"+Constants.ROLE_ORGANIZER+"')")
	public Event create(CreateEventRequest createEventRequest);
	
	public Event get(String uuid);
	
	@PreAuthorize("hasAnyRole('"+Constants.ROLE_TYPE_ADMIN+"','"+Constants.ROLE_ORGANIZER+"')")
	public void makeEventLive(String eventId);
	
	public List<EventResponse> getEventsForUser(Long userId,String city,String country,Integer page);
	
	public List<EventResponse> getEventsByType(String eventType,String city,String country,Integer page);
	
	@PreAuthorize("hasAnyRole('"+Constants.ROLE_TYPE_ADMIN+"','"+Constants.ROLE_ORGANIZER+"')")
	public void storeEventImages(String imagesURL,List<MultipartFile> images , String eventId) ;

	public List<EventImage> getEventImages(String eventId);
}
