package com.bitlogic.sociallbox.service.business;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.Event;
import com.bitlogic.sociallbox.data.model.EventAttendee;
import com.bitlogic.sociallbox.data.model.EventImage;
import com.bitlogic.sociallbox.data.model.EventTag;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.UserEventActivity;
import com.bitlogic.sociallbox.data.model.UserSocialActivity;
import com.bitlogic.sociallbox.data.model.requests.CreateEventRequest;
import com.bitlogic.sociallbox.data.model.response.EventResponse;
import com.bitlogic.sociallbox.data.model.response.UserFriend;

public interface EventService {

	@PreAuthorize("hasAnyRole('"+Constants.ROLE_TYPE_ADMIN+"','"+Constants.ROLE_ORGANIZER+"')")
	public Event create(String userEmail,CreateEventRequest createEventRequest);
	
	public EventResponse get(String uuid,Long userId);
	
	@PreAuthorize("hasAnyRole('"+Constants.ROLE_TYPE_ADMIN+"','"+Constants.ROLE_ORGANIZER+"')")
	public void makeEventLive(String eventId);
	
	public List<EventResponse> getEventsForUser(String userLocation,Long userId,String city,String country,Integer page);
	
	public List<EventResponse> getRetailEvents(String userLocation,String tagIds,Long userId,String city,String country,Integer page);
	
	public List<EventResponse> getEventsByType(String userLocation,Long userId,String eventType,String city,String country,Integer page);
	
	public List<EventResponse> getUpcomingEventsByOrg(String organizerId,String filterEventId);
	
	@PreAuthorize("hasAnyRole('"+Constants.ROLE_TYPE_ADMIN+"','"+Constants.ROLE_ORGANIZER+"')")
	public void storeEventImages(String imagesURL,List<MultipartFile> images , String eventId) ;

	public List<EventImage> getEventImages(String eventId);
	
	@PreAuthorize("hasRole('"+Constants.ROLE_TYPE_ADMIN+"')")
	public List<EventResponse> getEventsPendingForApproval();
	
	@PreAuthorize("hasRole('"+Constants.ROLE_TYPE_ADMIN+"')")
	public void approveEvents(List<String> eventIds);
	
	public EventAttendee registerForEvent(String eventId, String deviceId);
	
	public EventAttendee registerForEvent(String eventId, User user);
	
	public EventAttendee registerForEvent(Event event, User user);
	
	public void deRegisterForEvent(String eventId, String deviceId);
	
	public void deRegisterMeetupAtEvent(String meetupId,String eventId);
	
	public List<UserFriend> getFriendsGoingToEvent(String deviceId,String eventId);
	
	public void addEventToUserFav(String deviceId, String eventId);
	
	public void removeEventFromFav(String deviceId, String eventId);

	public List<EventTag> getRetailTags();
	
	public  List<UserSocialActivity<UserEventActivity>> getUserPastEvents(User user,Map<String, Double> cordinatesMap);
	
	public  List<UserSocialActivity<UserEventActivity>> getUserUpcomingEvents(User user,Map<String, Double> cordinatesMap);
}
