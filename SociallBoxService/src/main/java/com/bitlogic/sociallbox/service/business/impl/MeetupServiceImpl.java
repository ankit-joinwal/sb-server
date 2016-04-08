package com.bitlogic.sociallbox.service.business.impl;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.AddressComponentType;
import com.bitlogic.sociallbox.data.model.AttendeeResponse;
import com.bitlogic.sociallbox.data.model.Event;
import com.bitlogic.sociallbox.data.model.Meetup;
import com.bitlogic.sociallbox.data.model.MeetupAddressInfo;
import com.bitlogic.sociallbox.data.model.MeetupAttendee;
import com.bitlogic.sociallbox.data.model.MeetupAttendeeEntity;
import com.bitlogic.sociallbox.data.model.MeetupImage;
import com.bitlogic.sociallbox.data.model.MeetupMessage;
import com.bitlogic.sociallbox.data.model.MeetupStatus;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.UserMeetupActivity;
import com.bitlogic.sociallbox.data.model.UserSocialActivity;
import com.bitlogic.sociallbox.data.model.UserSocialActivity.ActivityType;
import com.bitlogic.sociallbox.data.model.ext.google.GooglePlace;
import com.bitlogic.sociallbox.data.model.ext.google.GooglePlace.Result.AddressComponent;
import com.bitlogic.sociallbox.data.model.requests.AddMeetupAttendeesRequest;
import com.bitlogic.sociallbox.data.model.requests.CreateMeetupRequest;
import com.bitlogic.sociallbox.data.model.requests.EditMeetupRequest;
import com.bitlogic.sociallbox.data.model.requests.MeetupResponse;
import com.bitlogic.sociallbox.data.model.requests.SaveAttendeeResponse;
import com.bitlogic.sociallbox.image.service.ImageService;
import com.bitlogic.sociallbox.service.business.EventService;
import com.bitlogic.sociallbox.service.business.MeetupService;
import com.bitlogic.sociallbox.service.dao.EventDAO;
import com.bitlogic.sociallbox.service.dao.MeetupDAO;
import com.bitlogic.sociallbox.service.dao.SmartDeviceDAO;
import com.bitlogic.sociallbox.service.dao.UserDAO;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.EntityNotFoundException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.exception.ServiceException;
import com.bitlogic.sociallbox.service.exception.UnauthorizedException;
import com.bitlogic.sociallbox.service.transformers.MeetupActivityTransformer;
import com.bitlogic.sociallbox.service.transformers.MeetupAttendeeTransformer;
import com.bitlogic.sociallbox.service.transformers.MeetupTransformer;
import com.bitlogic.sociallbox.service.transformers.Transformer;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory.TransformerTypes;
import com.bitlogic.sociallbox.service.utils.LoggingService;

@Service
@Transactional
public class MeetupServiceImpl extends LoggingService implements MeetupService,Constants{

	private static final Logger logger = LoggerFactory.getLogger(MeetupServiceImpl.class);
	
	@Override
	public Logger getLogger() {
		return logger;
	}
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private MeetupDAO meetupDAO;
	
	@Autowired
	private EventDAO eventDAO;
	
	@Autowired
	private SmartDeviceDAO smartDeviceDAO;
	
	@Autowired
	private EventService eventService;
	
	@Override
	public MeetupResponse createMetup(CreateMeetupRequest createMeetupRequest) {
		String LOG_PREFIX = "MeetupServiceImpl-createMetup";
		
		Meetup meetup = new Meetup();
		logInfo(LOG_PREFIX, "Getting User from device id {}", createMeetupRequest.getDeviceId());
		User organizer = this.smartDeviceDAO.getUserInfoFromDeviceId(createMeetupRequest.getDeviceId()); 
		if(organizer==null){
			logError(LOG_PREFIX, "User cannot be found");
			throw new ClientException(RestErrorCodes.ERR_003,Constants.ERROR_USER_INVALID);
		}
		
		Event eventAtMeetup = null;
		boolean isMeetupAtEvent = false;
		
		if(createMeetupRequest.getEventAtMeetup()!=null && !createMeetupRequest.getEventAtMeetup().isEmpty()){
			isMeetupAtEvent = true;
			
			eventAtMeetup = this.eventDAO.getEvent(createMeetupRequest.getEventAtMeetup());
			if(eventAtMeetup==null){
				logError(LOG_PREFIX, "Invalid event id {}", createMeetupRequest.getEventAtMeetup());
				throw new ClientException(RestErrorCodes.ERR_003, ERROR_INVALID_EVENT_IN_REQUEST);
			}
			logInfo(LOG_PREFIX, "Meetup is at event = {}", eventAtMeetup.getTitle());
			meetup.setEventAtMeetup(eventAtMeetup);
		}
		Date now = new Date();
		//Setting values into meetup
		meetup.setTitle(createMeetupRequest.getTitle());
		meetup.setDescription(createMeetupRequest.getDescription());
		meetup.setLocation(createMeetupRequest.getLocation());
		meetup.setIsPrivate(createMeetupRequest.getIsPrivate());
		meetup.setStatus(MeetupStatus.CREATED);
		meetup.setCreatedDt(now);
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.MEETUP_DATE_FORMAT);
			meetup.setStartDate(dateFormat.parse(createMeetupRequest.getStartDate()));
			meetup.setEndDate(dateFormat.parse(createMeetupRequest.getEndDate()));
		} catch (ParseException e) {
			logger.error("ParseException in startDate/endDate of meetup",e);
		}
		meetup.setOrganizer(organizer);
		Meetup created = meetupDAO.createMeetup(meetup);

		MeetupAttendeeEntity organizerAsAttendee = new MeetupAttendeeEntity();
		organizerAsAttendee.setAttendeeResponse(AttendeeResponse.YES);
		organizerAsAttendee.setIsAdmin(Boolean.TRUE);
		organizerAsAttendee.setUser(organizer);
		organizerAsAttendee.setMeetup(created);
		organizerAsAttendee.setCreateDt(now);
		organizerAsAttendee = this.meetupDAO.addAttendee(organizerAsAttendee);
		
		if(isMeetupAtEvent){
			logInfo(LOG_PREFIX, "Registering meetup organizer to event {}", eventAtMeetup.getTitle());
			this.eventService.registerForEvent(eventAtMeetup, organizer);
			
		}else{
			//created.setAddressComponents(this.getMeetupAddressInfo(created,createMeetupRequest.getAddressComponents()));
		}
		
		//meetupDAO.saveMeetup(created);
		
		MeetupTransformer transformer = (MeetupTransformer) TransformerFactory.getTransformer(TransformerTypes.MEETUP_TRANS);
		MeetupResponse createMeetupResponse = transformer.transform(created);
		createMeetupResponse.setUserActions(MeetupResponse.UserActionType.getOrganizerActions());
		return createMeetupResponse;
	}
	
	@Override
	public Meetup editMeetup(EditMeetupRequest editMeetupRequest) {
		String LOG_PREFIX = "MeetupServiceImpl-editMeetup";
		
		User user = this.smartDeviceDAO.getUserInfoFromDeviceId(editMeetupRequest.getDeviceId()); 
		if(user==null){
			throw new ClientException(RestErrorCodes.ERR_003,Constants.ERROR_USER_INVALID);
		}
		String meetupId = editMeetupRequest.getMeetupId();
		logInfo(LOG_PREFIX, "Getting meetup info with id = {}", meetupId);
		Meetup meetup = meetupDAO.getMeetup(meetupId);
		
		if(meetup==null){
			logError(LOG_PREFIX, "No meetup found with id {}", meetupId);
			throw new ClientException(RestErrorCodes.ERR_003, ERROR_INVALID_MEETUP_IN_REQUEST);
		}
		
		//Check if updater is admin
		if(meetup.getOrganizer().getId() != user.getId()){
			logError(LOG_PREFIX, "Only meetup organizer can edit meetup");
			throw new ClientException(RestErrorCodes.ERR_003, ERROR_EDIT_MEETUP_INVALID_USER);
		}
		
		boolean isUpdated = false;
		Date now = new Date();
		if(StringUtils.isNotBlank(editMeetupRequest.getTitle()) && !editMeetupRequest.getTitle().equals(meetup.getTitle())){
			logInfo(LOG_PREFIX, "Updating title as {}", editMeetupRequest.getTitle());
			meetup.setTitle(editMeetupRequest.getTitle());
			isUpdated = true;
		}
		
		if(StringUtils.isNotBlank(editMeetupRequest.getDescription()) && !editMeetupRequest.getDescription().equals(meetup.getDescription())){
			logInfo(LOG_PREFIX, "Updating description as {}", editMeetupRequest.getDescription());
			meetup.setDescription(editMeetupRequest.getDescription());
			isUpdated = true;
		}
		
		if(editMeetupRequest.getLocation()!=null && StringUtils.isNotBlank(editMeetupRequest.getLocation().getName())
				&& editMeetupRequest.getLocation().getLattitude()!=null
				&& editMeetupRequest.getLocation().getLongitude()!=null
				&& !editMeetupRequest.getLocation().getName().equals(meetup.getLocation().getName())){
			logInfo(LOG_PREFIX, "Updating location as {}", editMeetupRequest.getLocation());
			meetup.setLocation(editMeetupRequest.getLocation());
			isUpdated = true;
		}
		
		if(StringUtils.isNotBlank(editMeetupRequest.getStartDate())){
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.MEETUP_DATE_FORMAT);
				Date startDate = dateFormat.parse(editMeetupRequest.getStartDate());
				Date meetupStartDate = meetup.getStartDate();
				if(meetupStartDate.compareTo(startDate)!=0){
					logInfo(LOG_PREFIX, "Updating Start Date as {}", startDate);
					meetup.setStartDate(startDate);
					isUpdated = true;
				}
			} catch (ParseException e) {
				logger.error("ParseException",e);
			}
		}
		
		if(StringUtils.isNotBlank(editMeetupRequest.getEndDate())){
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.MEETUP_DATE_FORMAT);
				Date endDate = dateFormat.parse(editMeetupRequest.getEndDate());
				Date meetupEndDate = meetup.getEndDate();
				if(meetupEndDate.compareTo(endDate)!=0){
					logInfo(LOG_PREFIX, "Updating End Date as {}", endDate);
					meetup.setEndDate(endDate);
					isUpdated = true;
				}
			} catch (ParseException e) {
				logger.error("ParseException",e);
			}
		}
		
		if(editMeetupRequest.getIsPrivate()!= meetup.getIsPrivate()){
			logInfo(LOG_PREFIX, "Updating isPrivate to {}", editMeetupRequest.getIsPrivate());
			meetup.setIsPrivate(editMeetupRequest.getIsPrivate());
			isUpdated = true;
		}
		
		if(isUpdated){
			meetup.setUpdateDt(now);
			meetup.setUpdBy(user);
			Meetup edited = this.meetupDAO.getMeetup(meetupId);
			logInfo(LOG_PREFIX, "Edit Meetup completed");
			return edited;
		}
		return meetup;
		
	}
	private Set<MeetupAddressInfo> getMeetupAddressInfo(Meetup meetup,Set<GooglePlace.Result.AddressComponent> addressComponents){
		List<AddressComponentType> addressComponentTypes = this.meetupDAO.getAddressTypes();
		logger.info("Inside getMeetupAddressInfo. addressComponentTypes : {}  ",addressComponentTypes);
		Map<String,AddressComponentType> addressComponentTypesMap = new HashMap<>();
		for(AddressComponentType addressComponentType: addressComponentTypes){
			addressComponentTypesMap.put(addressComponentType.getName(), addressComponentType);
		}
		Set<MeetupAddressInfo> meetupAddresses = new HashSet<>();
		for(AddressComponent addressComponent : addressComponents){
			logger.info("Address Component {} "+addressComponent.getLongName());
			List<String> types = addressComponent.getTypes();
			logger.info("Types : {} ",types);
			for(String type : types){
				if(addressComponentTypes.contains(new AddressComponentType(type))){
					logger.info("Address component type found : {}",type);
					
					AddressComponentType addressComponentType = addressComponentTypesMap.get(type);
					MeetupAddressInfo meetupAddressInfo  = new MeetupAddressInfo();
					meetupAddressInfo.setAddressComponentType(addressComponentType);
					meetupAddressInfo.setValue(addressComponent.getLongName());
					meetupAddressInfo.setMeetup(meetup);
					meetupAddresses.add(meetupAddressInfo);
				
					continue;
				}
			}
		}
		
		logger.info("Meetup Address Components : " + meetupAddresses);
		
		return meetupAddresses;
	}
	
	 @Override
	public MeetupResponse getMeetup(String deviceId,String meetupId) {
		 String LOG_PREFIX = "MeetupServiceImpl-getMeetup";
		 User user = this.smartDeviceDAO.getUserInfoFromDeviceId(deviceId); 
		 if(user==null){
			throw new ClientException(RestErrorCodes.ERR_003,Constants.ERROR_USER_INVALID);
		 }
		 logInfo(LOG_PREFIX, "Getting meetup with id = {}", meetupId);
		 Meetup meetup = meetupDAO.getMeetup(meetupId);
		 if(meetup==null){
			 logError(LOG_PREFIX, "Meetup not found ");
			 throw new EntityNotFoundException(meetup, RestErrorCodes.ERR_020, ERROR_MEETUP_NOT_FOUND);
		 }
		 MeetupTransformer transformer = (MeetupTransformer) TransformerFactory.getTransformer(TransformerTypes.MEETUP_TRANS);
		 MeetupResponse meetupResponse = transformer.transform(meetup);
		 /*
		  * Scenarios :
		  * 				Case									|				Result 
		  * 1. When requesting user is not meetup attendee 			|				Error
		  *    and meetup is private								|				
		  * 2. When requesting user is not meetup attendee 			|				Read Only Actions
		  *    and meetup is not private							|
		  * 3. When requesting user is a non admin					|				Non admin Actions
		  * 4. When requesting user is admin						|				Admin Actions
		  * 5. When requesting user is organizer					|				Organizer Actions (Full Access)
		  */
		 MeetupAttendeeEntity attendeeEntity = this.meetupDAO.getMeetupAttendee(user.getId(), meetupId);
		 if(attendeeEntity==null){
			logError(LOG_PREFIX, "User {} is not an attendee for meetup {}.", user, meetupId);
			if(meetup.getIsPrivate()){
				throw new ClientException(RestErrorCodes.ERR_002, ERROR_ACTION_NOT_ALLOWED);
			}else{
				meetupResponse.setUserActions(MeetupResponse.UserActionType.getReadOnlyActions());
				return meetupResponse;
			}
		 }
		
		if(user.getId() == meetup.getOrganizer().getId()){
			meetupResponse.setUserActions(MeetupResponse.UserActionType.getOrganizerActions());
			return meetupResponse;
		}
		 
		if(attendeeEntity.getIsAdmin()){
			meetupResponse.setUserActions(MeetupResponse.UserActionType.getAdminActions());
			return meetupResponse;
		}else{
			meetupResponse.setUserActions(MeetupResponse.UserActionType.getNonAdminActions());
			return meetupResponse;
		}
		
		
	}
	 
	 @Override
	public List<MeetupImage> getMeetupImages(String deviceId, String meetupId) {
		String LOG_PREFIX = "MeetupServiceImpl-getMeetupImages";
		User user = this.smartDeviceDAO.getUserInfoFromDeviceId(deviceId); 
		if(user==null){
			throw new ClientException(RestErrorCodes.ERR_003,Constants.ERROR_USER_INVALID);
		}
		MeetupAttendeeEntity attendeeEntity = this.meetupDAO.getMeetupAttendee(user.getId(), meetupId);
		if(attendeeEntity==null){
			logError(LOG_PREFIX, "User {} is not an attendee for meetup {}. Hence cannot view images", user, meetupId);
			return new ArrayList<MeetupImage>();
		}
		return this.meetupDAO.getMeetupImages(meetupId);
	}
	 
	 @Override
	public List<MeetupAttendee> addAttendees(AddMeetupAttendeesRequest addAttendeesRequest) {
		 String LOG_PREFIX = "MeetupServiceImpl-addAttendees";
		 
		 List<MeetupAttendee> meetupAttendees = addAttendeesRequest.getAttendees();
		 Meetup meetup = this.meetupDAO.getMeetup(addAttendeesRequest.getMeetupId());
		 if(meetup==null){
			 logError(LOG_PREFIX,"No meetup found with id {}",addAttendeesRequest.getMeetupId());
			 throw new ClientException(RestErrorCodes.ERR_003, ERROR_MEETUP_NOT_FOUND);
		 }
		 List<Long> userIds = new ArrayList<>();
		 Map<Long,MeetupAttendee> meetupAttendeesMap = new HashMap<>();
		 for(MeetupAttendee meetupAttendee : meetupAttendees){
			 userIds.add(meetupAttendee.getUserId());
			 meetupAttendeesMap.put(meetupAttendee.getUserId(), meetupAttendee);
		 }
		 logInfo(LOG_PREFIX,"Loading users corresponding to attendees");
		 Map<Long,User> usersMap = this.userDAO.getUsersMapFromUserIds(userIds);
		 
		 List<MeetupAttendeeEntity> attendeeEntities = new ArrayList<>(userIds.size());

		 Iterator<Map.Entry<Long, MeetupAttendee>> iterator = meetupAttendeesMap.entrySet().iterator();
		 Date now = new Date();
		 while(iterator.hasNext()){
			 Entry<Long, MeetupAttendee> entry = iterator.next();
			 MeetupAttendeeEntity meetupAttendeeEntity = new MeetupAttendeeEntity(entry.getValue());
			 meetupAttendeeEntity.setMeetup(meetup);
			 meetupAttendeeEntity.setCreateDt(now);
			 
			 User user = usersMap.get(entry.getKey());
			 if(user == null ){
				 logError(LOG_PREFIX,"No user found for attendee {}",entry.getKey());
				 continue;
			 }
			 meetupAttendeeEntity.setUser(user);
			 attendeeEntities.add(meetupAttendeeEntity);
		 }
		 meetup.getAttendees().addAll(new HashSet<>(attendeeEntities));
		// meetup = this.meetupDAO.saveMeetup(meetup);
		 List<MeetupAttendeeEntity> attendees = this.meetupDAO.getAttendees(meetup);
		 
		MeetupAttendeeTransformer transformer = (MeetupAttendeeTransformer) TransformerFactory.getTransformer(TransformerTypes.MEETUP_ATTENDEE_TRANSFORMER);
		List<MeetupAttendee> attendeesToReturn = transformer.transform(attendees);
		 
		return attendeesToReturn;
	}
	 
	 @Override
	public List<MeetupAttendee> getMeetupAttendees(String meetupId) {
		String LOG_PREFIX = "MeetupServiceImpl-getMeetupAttendees";

		Meetup meetup = this.meetupDAO.getMeetup(meetupId);
		if (meetup == null) {
			logError(LOG_PREFIX, "No meetup found with id {}", meetupId);
			throw new ClientException(RestErrorCodes.ERR_003,
					ERROR_MEETUP_NOT_FOUND);
		}

		List<MeetupAttendeeEntity> attendees = this.meetupDAO
				.getAttendees(meetup);

		MeetupAttendeeTransformer transformer = (MeetupAttendeeTransformer) TransformerFactory
				.getTransformer(TransformerTypes.MEETUP_ATTENDEE_TRANSFORMER);
		List<MeetupAttendee> attendeesToReturn = transformer
				.transform(attendees);
		logInfo(LOG_PREFIX, "Found {} attendees for meetup {}", attendees.size(),meetup.getTitle());
		return attendeesToReturn;
	}
	 
	 @Override
	public void saveAttendeeResponse(SaveAttendeeResponse attendeeResponse,String deviceId) {
		String LOG_PREFIX = "MeetupServiceImpl-saveAttendeeResponse";
		logInfo(LOG_PREFIX, "Attendee Response = {}", attendeeResponse);
		
		MeetupAttendeeEntity attendeeEntity = this.meetupDAO.getMeetupAttendeeByAttendeeId(attendeeResponse.getAttendeeId());
		if(attendeeEntity==null){
			logError(LOG_PREFIX, "Invalid attendee {} for meetup {}", attendeeResponse.getAttendeeId(),attendeeResponse.getMeetupId());
			return;
		}
		attendeeEntity.setAttendeeResponse(attendeeResponse.getAttendeeResponse());
		logInfo(LOG_PREFIX, "Attendee response saved successfully");
		Meetup meetup = attendeeEntity.getMeetup();
		Event eventAtMeetup = meetup.getEventAtMeetup();
		if(eventAtMeetup !=null){
			logInfo(LOG_PREFIX, "Since meetup is at event, hence using attendee response to register/de register from event"  );
			if(attendeeResponse.getAttendeeResponse()== AttendeeResponse.YES){
				logInfo(LOG_PREFIX, "Registering meetup attendee for event {}",eventAtMeetup.getTitle() );
				this.eventService.registerForEvent(eventAtMeetup, attendeeEntity.getUser());
			}else{
				logInfo(LOG_PREFIX, "De-registering attendee from event {}", eventAtMeetup.getTitle());
				this.eventService.deRegisterForEvent(eventAtMeetup.getUuid(), deviceId);
			}
		}
	}
	 
	 @Override
	public void sendMessageInMeetup(MeetupMessage meetupMessage,
			String meetupId, Long attendeeId) {

		String LOG_PREFIX = "MeetupServiceImpl-sendMessageInMeetup";
		logInfo(LOG_PREFIX, "Getting attendee for Meetup = {}. Attendee Id = {}", meetupId,attendeeId);
		
		 MeetupAttendeeEntity meetupAttendee = this.meetupDAO.getMeetupAttendeeByAttendeeId(attendeeId);
		 if(meetupAttendee==null){
			 logError(LOG_PREFIX, "Invalid Attendee Id");
			 throw new ClientException(RestErrorCodes.ERR_003,ERROR_USER_NOT_ATTENDEE_OF_MEETUP);
		 }else{
			 Meetup meetup = meetupAttendee.getMeetup();
			 if(meetup.getUuid().equals(meetupId)){
				 this.meetupDAO.sendMessageInMeetup(meetupMessage, meetup, meetupAttendee);
			 }else{
				 logError(LOG_PREFIX, "Invalid meetup id in request {}", meetupId);
				 throw new ClientException(RestErrorCodes.ERR_003, ERROR_INVALID_MEETUP_IN_REQUEST);
			 }
		 }
	}
	 
	 @Override
	public List<MeetupMessage> getMeetupMessages(String meetupId,Integer page) {
		 String LOG_PREFIX = "MeetupServiceImpl-getMeetupMessages";
		 Meetup meetup = this.meetupDAO.getMeetup(meetupId);
		 if(meetup==null){
			 logError(LOG_PREFIX, "Meetup not found = {}", meetupId);
			 throw new ClientException(RestErrorCodes.ERR_003,ERROR_INVALID_MEETUP_IN_REQUEST);
		 }
		 
		 List<MeetupMessage> messages = this.meetupDAO.getMeetupMessages(meetup, page);
		 if(messages!=null && !messages.isEmpty()){
			 Date now = new Date();
			for (MeetupMessage meetupMessage : messages) {
				Date messageTime = meetupMessage.getCreateDt();
				long diff = now.getTime() - messageTime.getTime();// in
																	// millisecons

				int diffDays = (int) (diff / (24 * 60 * 60 * 1000));

				if (diffDays > 0) {
					meetupMessage.setTimeToDisplay(diffDays + " Day ago");
					continue;
				}

				int diffHours = (int) (diff / (60 * 60 * 1000) % 24);
				if (diffHours > 0) {
					meetupMessage.setTimeToDisplay(diffHours + " Hour ago");
					continue;
				}

				int diffMinutes = (int) (diff / (60 * 1000) % 60);
				if (diffMinutes > 0) {
					meetupMessage.setTimeToDisplay(diffMinutes + " Min ago");
					continue;
				}

				meetupMessage.setTimeToDisplay("Just Now");
			}
		 }
		 logInfo(LOG_PREFIX, "Returning {} messages for meetup = {}", meetupId);
		 
		 
		 return messages;
	}
	 
	 @Override
	public List<MeetupImage> uploadImageToMeetup(Boolean isDp,String deviceId,String imagesURL, List<MultipartFile> images,
			String meetupId) {
		 logger.info("### Inside MeetupServiceImpl.uploadImageToMeetup ###");
		 List<MeetupImage> imagesToSave = new ArrayList<>();
		 User uploadedBy = this.smartDeviceDAO.getUserInfoFromDeviceId(deviceId);
		 if(uploadedBy==null){
			 throw new UnauthorizedException(RestErrorCodes.ERR_002, ERROR_LOGIN_USER_UNAUTHORIZED);
		 }
		 Meetup meetup = this.meetupDAO.getMeetup(meetupId);
		 if(meetup==null){
			 throw new ClientException(RestErrorCodes.ERR_003,ERROR_INVALID_MEETUP_IN_REQUEST);
		 }
		 if(isDp == null){
			 isDp = Boolean.FALSE;
		 }
		 for(MultipartFile multipartFile : images){
			 String fileName = multipartFile.getOriginalFilename();
				logger.info("File to process : {} ", fileName);
	      	 logger.info("File size : {} ", multipartFile.getSize());
	      	Transformer<MeetupImage, MultipartFile> transformer = 
	      			   (Transformer<MeetupImage, MultipartFile>)TransformerFactory.getTransformer(TransformerTypes.MULTIPART_TO_MEETUP_IMAGE_TRANFORMER);
	      	 try{
	      		ByteArrayInputStream imageStream = new ByteArrayInputStream(
						multipartFile.getBytes());
				Map<String, ?> uploadedImageInfo = ImageService.uploadImageToMeetup(meetupId, 
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
	      		   Date now = new Date();
	      		   MeetupImage meetupImage = transformer.transform(multipartFile);
	      		   meetupImage.setMeetup(meetup);
	      		   meetupImage.setUrl(imageURL);
	      		   meetupImage.setUploadDt(now);
	      		   meetupImage.setUploadedBy(uploadedBy);
	      		   meetupImage.setIsDisplayImage(isDp);
	      		   imagesToSave.add(meetupImage);
	      	   }catch(ServiceException serviceException){
	      		   logger.error("Error occurred while processing meetup image",serviceException);
	      	   }catch(Exception ex){
	      		 logger.error("Error occurred while processing meetup image",ex);
	      	   }
		 }
		 
		 if(!imagesToSave.isEmpty()){
			 this.meetupDAO.saveMeetupImages(imagesToSave);
		 }
		 
		 return imagesToSave;
	}
	 
	 @Override
	public void cancelMeetup(String deviceId, String meetupId) {
		 String LOG_PREFIX = "MeetupServiceImpl-cancelMeetup";
		 
		 User user = this.smartDeviceDAO.getUserInfoFromDeviceId(deviceId);
		 if(user==null){
			 throw new UnauthorizedException(RestErrorCodes.ERR_002, ERROR_LOGIN_USER_UNAUTHORIZED);
		 }
		 
		 Meetup meetup = this.meetupDAO.getMeetup(meetupId);
		 if(meetup==null){
			 throw new ClientException(RestErrorCodes.ERR_003,ERROR_INVALID_MEETUP_IN_REQUEST);
		 }
		 
		 if(meetup.getOrganizer().getId() != user.getId()){
			 logError(LOG_PREFIX, "Only meetup organizer can cancel meetup");
			 throw new ClientException(RestErrorCodes.ERR_003, ERROR_CANCEL_MEETUP_INVALID_USER);
		 }
		 
		 logInfo(LOG_PREFIX, "User {} cancelled meetup {}", user.getName(),meetup.getTitle());
		 
		 meetup.setStatus(MeetupStatus.CANCELLED);
		 Event eventAtMeetup = meetup.getEventAtMeetup();
		 if(eventAtMeetup!=null){
			 logInfo(LOG_PREFIX, "Since meetup was at an event, hence de registering all meetup attendees from event " );
			 this.eventService.deRegisterMeetupAtEvent(meetupId, eventAtMeetup.getUuid());
		 }
		 
	}
	 
	 @Override
	public List<UserSocialActivity<UserMeetupActivity>> getUserPastMeetupActivities(User user) {
		 String LOG_PREFIX = "MeetupServiceImpl-getUserPastMeetupActivities";
		 
		 
		 //Get past meetups organized by user from last one month
		 List<Meetup> pastMeetups = this.meetupDAO.getPastMeetupsOfUser(user);
		 logInfo(LOG_PREFIX, "Past Meetup Activities of user = {}", pastMeetups.size());
		 List<UserSocialActivity<UserMeetupActivity>> activities = new ArrayList<UserSocialActivity<UserMeetupActivity>>();
		
		
		 if(pastMeetups!=null&& !pastMeetups.isEmpty()){
			 MeetupTransformer meetupTransformer = (MeetupTransformer) TransformerFactory.getTransformer(TransformerTypes.MEETUP_TRANS);
			 SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.ACTIVITY_RESPONSE_DATE_FORMAT);
				for(Meetup meetup : pastMeetups){
					UserSocialActivity<UserMeetupActivity> socialActivity = new UserSocialActivity<UserMeetupActivity>();
					socialActivity.setActivityTime(dateFormat.format(meetup.getCreatedDt()));
					socialActivity.setActivityDate(meetup.getCreatedDt());
					socialActivity.setActivityType(ActivityType.MEETUP);
					UserMeetupActivity meetupActivity = new UserMeetupActivity();
					if(user.getId() == meetup.getOrganizer().getId()){
						meetupActivity.setIsOrganizer(Boolean.TRUE);
						
					}else{
						meetupActivity.setIsOrganizer(Boolean.FALSE);
					}
					meetupActivity.setMeetup(meetupTransformer.transform(meetup));
					socialActivity.setDetail(meetupActivity);
					activities.add(socialActivity);
				}
			}
		 return activities;
	}
	 
	 @Override
	public List<UserSocialActivity<UserMeetupActivity>> getUserUpcomingMeetupActivities(
			User user) {

		 String LOG_PREFIX = "MeetupServiceImpl-getUserUpcomingMeetupActivities";
		 
		 
		 //Get past meetups organized by user from last one month
		 List<Meetup> pastMeetups = this.meetupDAO.getUpcomingMeetupsOfUser(user);
		 logInfo(LOG_PREFIX, "Upcoming Meetup Activities of user = {}", pastMeetups.size());
		 List<UserSocialActivity<UserMeetupActivity>> activities = new ArrayList<UserSocialActivity<UserMeetupActivity>>();
		 MeetupTransformer meetupTransformer = (MeetupTransformer) TransformerFactory.getTransformer(TransformerTypes.MEETUP_TRANS);
			
		 if(pastMeetups!=null&& !pastMeetups.isEmpty()){
				for(Meetup meetup : pastMeetups){
					UserSocialActivity<UserMeetupActivity> socialActivity = new UserSocialActivity<UserMeetupActivity>();
					 SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.ACTIVITY_RESPONSE_DATE_FORMAT);
					socialActivity.setActivityTime(dateFormat.format(meetup.getCreatedDt()));
					socialActivity.setActivityDate(meetup.getCreatedDt());
					socialActivity.setActivityType(ActivityType.MEETUP);
					UserMeetupActivity meetupActivity = new UserMeetupActivity();
					if(user.getId() == meetup.getOrganizer().getId()){
						meetupActivity.setIsOrganizer(Boolean.TRUE);
						
					}else{
						meetupActivity.setIsOrganizer(Boolean.FALSE);
					}
					meetupActivity.setMeetup(meetupTransformer.transform(meetup));
					socialActivity.setDetail(meetupActivity);
					activities.add(socialActivity);
				}
			}
		 return activities;
	
	}
	 
	 @Override
	public List<MeetupResponse> getPendingMeetupInvites(String deviceId) {
		 String LOG_PREFIX = "MeetupServiceImpl-getPendingMeetupInvites";
		User user = this.smartDeviceDAO.getUserInfoFromDeviceId(deviceId);
		if(user==null){
			logError(LOG_PREFIX, "User cannot be found");
			throw new ClientException(RestErrorCodes.ERR_003,Constants.ERROR_USER_INVALID);
		}
		List<Meetup> meetups = this.meetupDAO.getPendingMeetupInvites(user);
		List<MeetupResponse> pendingMeetups = new ArrayList<MeetupResponse>(); 
		MeetupTransformer transformer = (MeetupTransformer) TransformerFactory.getTransformer(TransformerTypes.MEETUP_TRANS);
		 for(Meetup meetup : meetups){
			 MeetupResponse meetupResponse = transformer.transform(meetup);
			 pendingMeetups.add(meetupResponse);
		 }
		 
		logInfo(LOG_PREFIX, "Found {} pending meetups", meetups.size());
		 return pendingMeetups;
	}
}
