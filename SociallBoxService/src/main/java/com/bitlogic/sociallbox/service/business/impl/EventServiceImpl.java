package com.bitlogic.sociallbox.service.business.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.AddressComponentType;
import com.bitlogic.sociallbox.data.model.EOAdminStatus;
import com.bitlogic.sociallbox.data.model.Event;
import com.bitlogic.sociallbox.data.model.EventAddressInfo;
import com.bitlogic.sociallbox.data.model.EventAttendee;
import com.bitlogic.sociallbox.data.model.EventDetails;
import com.bitlogic.sociallbox.data.model.EventImage;
import com.bitlogic.sociallbox.data.model.EventOrganizer;
import com.bitlogic.sociallbox.data.model.EventOrganizerAdmin;
import com.bitlogic.sociallbox.data.model.EventStatus;
import com.bitlogic.sociallbox.data.model.EventTag;
import com.bitlogic.sociallbox.data.model.EventType;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.UserFavouriteEvents;
import com.bitlogic.sociallbox.data.model.ext.google.GooglePlace;
import com.bitlogic.sociallbox.data.model.ext.google.GooglePlace.Result.AddressComponent;
import com.bitlogic.sociallbox.data.model.requests.CreateEventRequest;
import com.bitlogic.sociallbox.data.model.requests.CreateEventRequest.MockEventDetails;
import com.bitlogic.sociallbox.data.model.response.EntityCollectionResponse;
import com.bitlogic.sociallbox.data.model.response.EventResponse;
import com.bitlogic.sociallbox.data.model.response.UserFriend;
import com.bitlogic.sociallbox.image.service.ImageService;
import com.bitlogic.sociallbox.service.business.EventService;
import com.bitlogic.sociallbox.service.dao.EventDAO;
import com.bitlogic.sociallbox.service.dao.EventOrganizerDAO;
import com.bitlogic.sociallbox.service.dao.EventTagDAO;
import com.bitlogic.sociallbox.service.dao.EventTypeDAO;
import com.bitlogic.sociallbox.service.dao.MeetupDAO;
import com.bitlogic.sociallbox.service.dao.SmartDeviceDAO;
import com.bitlogic.sociallbox.service.dao.UserDAO;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.EntityNotFoundException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.exception.ServiceException;
import com.bitlogic.sociallbox.service.exception.UnauthorizedException;
import com.bitlogic.sociallbox.service.transformers.MultipartToEventImageTransformer;
import com.bitlogic.sociallbox.service.transformers.Transformer;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory;
import com.bitlogic.sociallbox.service.transformers.UsersToFriendsTransformer;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory.Transformer_Types;
import com.bitlogic.sociallbox.service.utils.GeoUtils;
import com.bitlogic.sociallbox.service.utils.LoggingService;

@Service
@Transactional
public class EventServiceImpl extends LoggingService implements EventService,
		Constants {

	private static final Logger logger = LoggerFactory
			.getLogger(EventServiceImpl.class);

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private EventDAO eventDAO;

	@Autowired
	private EventTagDAO eventTagDAO;

	@Autowired
	private EventTypeDAO eventTypeDAO;

	@Autowired
	private MeetupDAO meetupDAO;

	@Autowired
	private SmartDeviceDAO smartDeviceDAO;

	@Autowired
	private EventOrganizerDAO eventOrganizerDAO;

	@Autowired
	private ImageService imageService;

	@Override
	public Event create(String userEmail, CreateEventRequest createEventRequest) {
		String LOG_PREFIX = "EventServiceImpl-create";

		Event event = new Event();
		MockEventDetails mockEventDetails = createEventRequest
				.getEventDetails();

		EventOrganizerAdmin organizer = this.eventOrganizerDAO
				.getEOAdminProfileById(createEventRequest
						.getOrganizerProfileId());
		logInfo(LOG_PREFIX, "Found organizer details {}", organizer);

		User organizerAdmin = this.userDAO.getUserByEmailId(userEmail, false);
		if (organizerAdmin == null) {
			throw new ClientException(RestErrorCodes.ERR_002,
					ERROR_USER_INVALID);
		}
		if (!organizerAdmin.getEmailId().equals(
				organizer.getUser().getEmailId())) {
			logError(
					LOG_PREFIX,
					"Organizer Profile Id in request does not match User who made request",
					organizerAdmin.getEmailId());
			throw new ClientException(RestErrorCodes.ERR_002,
					ERROR_USER_INVALID);
		}

		if (organizer.getStatus() != EOAdminStatus.APPROVED) {
			logError(LOG_PREFIX, "User not authorized yet to create events",
					organizer.getUser().getName());
			throw new ClientException(RestErrorCodes.ERR_002,
					ERROR_EO_ADMIN_UNAPPROVED);
		}

		Set<EventTag> tags = createEventRequest.getTags();
		if (tags != null && !tags.isEmpty()) {
			List<String> tagNames = new ArrayList<>();
			for (EventTag eventTag : tags) {
				tagNames.add(eventTag.getName());
			}
			List<EventTag> tagsInDB = eventTagDAO.getTagsByNames(tagNames);
			event.setTags(new HashSet<>(tagsInDB));
		} else {
			logError(LOG_PREFIX, "Tags not found in request ");
			throw new ClientException(RestErrorCodes.ERR_002,
					ERROR_TAGS_MANDATORY);
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				Constants.MEETUP_DATE_FORMAT);
		try {
			event.setStartDate(dateFormat.parse(createEventRequest
					.getStartDate()));
			event.setEndDate(dateFormat.parse(createEventRequest.getEndDate()));
		} catch (ParseException e) {
			logError(LOG_PREFIX, "Error while parsing event dates {}",
					createEventRequest);
			throw new ClientException(RestErrorCodes.ERR_001,
					ERROR_DATE_INVALID_FORMAT);
		}

		Date now = new Date();

		event.setTitle(createEventRequest.getTitle());
		event.setDescription(createEventRequest.getDescription());
		event.setEventStatus(EventStatus.CREATED);
		event.setIsAllowedEventToGoLive(Boolean.FALSE);
		event.setIsFreeEvent(createEventRequest.getIsFree());

		EventDetails eventDetails = new EventDetails();
		eventDetails.setLocation(mockEventDetails.getLocation());
		eventDetails.setOrganizerAdmin(organizer);
		eventDetails.setEvent(event);
		eventDetails.setCreateDt(now);
		eventDetails.setBookingUrl(mockEventDetails.getBookingUrl());
		event.setEventDetails(eventDetails);

		Event created = this.eventDAO.create(event);

		// TODO : Address components
		/*
		 * created.getEventDetails().setAddressComponents(this.getEventAddressInfo
		 * (eventDetails,mockEventDetails.getAddressComponents()));
		 * this.eventDAO.saveEvent(created);
		 */
		return created;
	}

	private Set<EventAddressInfo> getEventAddressInfo(
			EventDetails eventDetails,
			Set<GooglePlace.Result.AddressComponent> addressComponents) {
		List<AddressComponentType> addressComponentTypes = this.meetupDAO
				.getAddressTypes();
		logger.info("Inside getEventAddressInfo. addressComponentTypes : {}  ",
				addressComponentTypes);
		Map<String, AddressComponentType> addressComponentTypesMap = new HashMap<>();
		for (AddressComponentType addressComponentType : addressComponentTypes) {
			addressComponentTypesMap.put(addressComponentType.getName(),
					addressComponentType);
		}
		Set<EventAddressInfo> eventAddresses = new HashSet<>();
		for (AddressComponent addressComponent : addressComponents) {
			logger.info("Address Component {} "
					+ addressComponent.getLongName());
			List<String> types = addressComponent.getTypes();
			logger.info("Types : {} ", types);
			for (String type : types) {
				if (addressComponentTypes.contains(new AddressComponentType(
						type))) {
					logger.info("Address component type found : {}", type);

					AddressComponentType addressComponentType = addressComponentTypesMap
							.get(type);
					EventAddressInfo eventAddressInfo = new EventAddressInfo();
					eventAddressInfo
							.setAddressComponentType(addressComponentType);
					eventAddressInfo.setValue(addressComponent.getLongName());
					eventAddressInfo.setEventDetails(eventDetails);
					eventAddresses.add(eventAddressInfo);

					continue;
				}
			}
		}

		logger.info("Event Address Components : " + eventAddresses);

		return eventAddresses;
	}

	@Override
	public Event get(String uuid) {
		String LOG_PREFIX = "EventServiceImpl-get";

		Event event = this.eventDAO.getEvent(uuid);
		if (event == null) {
			logError(LOG_PREFIX, "Event not found with uuid {}", uuid);
			throw new EntityNotFoundException(uuid, RestErrorCodes.ERR_020,
					ERROR_INVALID_EVENT_IN_REQUEST);
		}
		return event;
	}

	@Override
	public void makeEventLive(String eventId) {
		String LOG_PREFIX = "EventServiceImpl-makeEventLive";

		Event event = this.eventDAO.getEvent(eventId);
		if (event == null) {
			logError(LOG_PREFIX, "Event not found with uuid {}", eventId);
			throw new ClientException(RestErrorCodes.ERR_020,
					ERROR_INVALID_EVENT_IN_REQUEST);
		}
		Date now = new Date();
		event.setEventStatus(EventStatus.LIVE);
		event.getEventDetails().setUpdateDt(now);
		logInfo(LOG_PREFIX, "Successfully made event live {}", eventId);

	}

	@Override
	public List<EventResponse> getEventsForUser(String userLocation,
			Long userId, String city, String country, Integer page) {
		String LOG_PREFIX = "EventServiceImpl-getEventsForUser";
		

		// Parse Location is Format Lattitude,Longitude
		Map<String, Double> cordinatesMap = GeoUtils
				.getCoordinatesFromLocation(userLocation);

		List<Long> userTags = null;
		if (userId != null) {
			logInfo(LOG_PREFIX,"Found user id in request");
			userTags = this.eventTagDAO.getUserTagIds(userId);
			logInfo(LOG_PREFIX,"User tags : {} ",userTags);
		} else {
			userTags = this.eventTagDAO.getAllTagIds();
			logInfo(LOG_PREFIX,"User tags : {} ",userTags);
		}
		return this.eventDAO.getEventsByFilter(userId,cordinatesMap, userTags, city,
				country, page);

	}

	@Override
	public List<EventResponse> getEventsByType(String userLocation,Long userId,
			String eventTypeName, String city, String country, Integer page) {
		String LOG_PREFIX = "EventServiceImpl-getEventsByType";
		
		// Parse Location is Format Lattitude,Longitude
		Map<String, Double> cordinatesMap = GeoUtils
				.getCoordinatesFromLocation(userLocation);

		logInfo(LOG_PREFIX,
				"### Inside getEventsByType .Type {}, City {} , Country {} ###",
				eventTypeName, city, country);
		EventType eventType = this.eventTypeDAO
				.getEventTypeByName(eventTypeName);
		if (eventType == null) {
			throw new ClientException(RestErrorCodes.ERR_003,
					ERROR_EVENT_TYPE_INVALID);
		}

		logInfo(LOG_PREFIX,"Found Event Type by name {}", eventTypeName);
		Set<EventTag> tags = eventType.getRelatedTags();

		List<Long> tagIds = new ArrayList<Long>(tags.size());
		for (EventTag eventTag : tags) {
			tagIds.add(eventTag.getId());
		}

		return this.eventDAO.getEventsByFilter(userId,cordinatesMap, tagIds, city,
				country, page);
	}
	
	@Override
	public List<EventResponse> getUpcomingEventsByOrg(String organizerId,String filterEventId) {
		String LOG_PREFIX = "EventServiceImpl-getUpcomingEventsByOrg";
		logInfo(LOG_PREFIX, "Getting organizer details");
		EventOrganizer eventOrganizer = this.eventOrganizerDAO.getEODetails(organizerId);
		if(eventOrganizer==null){
			throw new ClientException(RestErrorCodes.ERR_002, ERROR_ORGANIZER_NOT_FOUND);
		}
		
		Set<EventOrganizerAdmin> admins = eventOrganizer.getOrganizerAdmins();
		List<EventResponse> events = this.eventDAO.getUpcomingEventsOfOrg(admins,filterEventId);
		
		return events;
	}

	@Override
	public void storeEventImages(String imagesURL, List<MultipartFile> images,
			String eventId) {
		String LOG_PREFIX = "EventServiceImpl-storeEventImages";
		List<EventImage> imagesToSave = new ArrayList<>();
		
		Event event = this.eventDAO.getEventWithoutImage(eventId);
		if (event == null) {
			logError(LOG_PREFIX, "Event not found with id {}", eventId);
			throw new ClientException(RestErrorCodes.ERR_003,
					ERROR_INVALID_EVENT_IN_REQUEST);
		}
		int displayOrder = 1;
		for (MultipartFile multipartFile : images) {
			String fileName = multipartFile.getOriginalFilename();
			logger.info("File to process : {} ", fileName);
			logger.info("File size : {} ", multipartFile.getSize());
			MultipartToEventImageTransformer transformer = (MultipartToEventImageTransformer) TransformerFactory
					.getTransformer(Transformer_Types.MULTIPART_TO_EVENT_IMAGE_TRANFORMER);
			try {
				ByteArrayInputStream imageStream = new ByteArrayInputStream(
						multipartFile.getBytes());
				Map<String, ?> uploadedImageInfo = ImageService.uploadImageToEvent(eventId, 
								imageStream,
								multipartFile.getContentType(),
								multipartFile.getBytes().length, 
								fileName);
				
				if (uploadedImageInfo == null
						|| !uploadedImageInfo
								.containsKey(Constants.IMAGE_URL_KEY)) {
					throw new ServiceException(IMAGE_SERVICE_NAME,
							RestErrorCodes.ERR_052,
							"Unable to upload image.Please try later");
				}
				String imageURL = (String) uploadedImageInfo
						.get(Constants.IMAGE_URL_KEY);

				EventImage eventImage = transformer.transform(multipartFile);
				eventImage.setEvent(event);
				eventImage.setDisplayOrder(displayOrder);
				eventImage.setUrl(imageURL);

				imagesToSave.add(eventImage);
				displayOrder = displayOrder + 1;
			} catch (ServiceException serviceException) {
				logger.error("Error occurred while processing event image",
						serviceException);
			} catch (Exception ex) {
				logger.error("Error occurred while processing event image", ex);
			}
		}

		if (!imagesToSave.isEmpty()) {
			this.eventDAO.saveEventImages(imagesToSave);
		}
	}

	@Override
	public List<EventImage> getEventImages(String eventId) {
		List<EventImage> eventImages = this.eventDAO.getEventImages(eventId);
		return eventImages;
	}

	@Override
	public List<EventResponse> getEventsPendingForApproval() {
		String LOG_PREFIX = "EventServiceImpl-getEventsPendingForApproval";
		List<EventResponse> pendingEvents = this.eventDAO.getPendingEvents();
		logInfo(LOG_PREFIX, "Total Events Pending = {}", pendingEvents.size());
		return pendingEvents;
	}

	@Override
	public void approveEvents(List<String> eventIds) {

		String LOG_PREFIX = "EventServiceImpl-approveEvents";
		List<Event> events = this.eventDAO.getEventsByIds(eventIds);
		List<String> eventNames = new ArrayList<String>();
		if (events != null) {
			for (Event event : events) {
				eventNames.add(event.getTitle());
				event.setIsAllowedEventToGoLive(Boolean.TRUE);
				event.setEventStatus(EventStatus.READY_TO_GO_LIVE);
			}
		}
		logInfo(LOG_PREFIX, "Following events approved : {}", eventNames);
	}

	@Override
	public EventAttendee registerForEvent(String eventId, String deviceId) {
		String LOG_PREFIX = "EventServiceImpl-registerForEvent";

		logInfo(LOG_PREFIX, "Getting user info from device id : {}", deviceId);
		User user = this.smartDeviceDAO.getUserInfoFromDeviceId(deviceId);
		if (user == null) {
			logError(LOG_PREFIX, "No user exists fro given device Id {}",
					deviceId);
			throw new UnauthorizedException(RestErrorCodes.ERR_002,
					ERROR_LOGIN_USER_UNAUTHORIZED);
		}

		EventAttendee eventAttendee = this.eventDAO.getAttendee(eventId,
				user.getId());
		if (eventAttendee != null) {
			logInfo(LOG_PREFIX, "Event attendee exists already ");
			return eventAttendee;
		}
		Event event = this.eventDAO.getEvent(eventId);
		if (event == null) {
			logInfo(LOG_PREFIX, "Event not found for id = {}", eventId);
			throw new ClientException(RestErrorCodes.ERR_002,
					ERROR_INVALID_EVENT_IN_REQUEST);
		}
		EventAttendee newAttendee = new EventAttendee();
		newAttendee.setEvent(event);
		newAttendee.setUser(user);

		EventAttendee registeredAttendee = this.eventDAO
				.saveAttendee(newAttendee);
		logInfo(LOG_PREFIX, "User {} Registered for Event {} succesfully",
				user.getName(), event.getTitle());
		return registeredAttendee;
	}
	
	@Override
	public List<UserFriend> getFriendsGoingToEvent(String deviceId,
			String eventId) {
		String LOG_PREFIX = "EventServiceImpl-getFriendsGoingToEvent";
		List<UserFriend> userFriends = new ArrayList<UserFriend>();
		logInfo(LOG_PREFIX, "Getting user info from device id : {}", deviceId);
		User user = this.smartDeviceDAO.getUserInfoFromDeviceId(deviceId);
		if (user == null) {
			logError(LOG_PREFIX, "No user exists fro given device Id {}",
					deviceId);
			throw new UnauthorizedException(RestErrorCodes.ERR_002,
					ERROR_LOGIN_USER_UNAUTHORIZED);
		}
		Event event = this.eventDAO.getEvent(eventId);
		if (event == null) {
			logInfo(LOG_PREFIX, "Event not found for id = {}", eventId);
			throw new ClientException(RestErrorCodes.ERR_002,
					ERROR_INVALID_EVENT_IN_REQUEST);
		}
		List<Long> attendeesIds = this.eventDAO.getEventAttendeesIds(event);
		List<User> users = this.userDAO.getUserFriendsByIds(user, attendeesIds);
		if(users!=null){
			UsersToFriendsTransformer transformer = (UsersToFriendsTransformer) TransformerFactory
				.getTransformer(Transformer_Types.USER_TO_FRIEND_TRANSFORMER);
			userFriends = transformer.transform(users);
		}
		
		return userFriends;
	}
	
	@Override
	public void addEventToUserFav(String deviceId, String eventId) {
		String LOG_PREFIX = "EventServiceImpl-addEventToUserFav";
		logInfo(LOG_PREFIX, "Getting user info from device id : {}",deviceId);
		User user = this.smartDeviceDAO.getUserInfoFromDeviceId(deviceId);
		if(user == null){
			logError(LOG_PREFIX, "No user exists fro given device Id {}", deviceId);
			throw new UnauthorizedException(RestErrorCodes.ERR_002, ERROR_LOGIN_USER_UNAUTHORIZED);
		}
		Event event = this.eventDAO.getEvent(eventId);
		if(event==null){
			throw new UnauthorizedException(RestErrorCodes.ERR_002, ERROR_INVALID_EVENT_IN_REQUEST);
		}
		UserFavouriteEvents userFavouriteEvents = new UserFavouriteEvents();
		userFavouriteEvents.setUserId(user.getId());
		userFavouriteEvents.setEventId(eventId);
		this.eventDAO.addEventToFav(userFavouriteEvents);
	}
}
