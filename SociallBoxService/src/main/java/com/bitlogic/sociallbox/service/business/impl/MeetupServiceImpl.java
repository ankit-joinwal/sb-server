package com.bitlogic.sociallbox.service.business.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import com.bitlogic.sociallbox.data.model.UserSocialDetail;
import com.bitlogic.sociallbox.data.model.ext.google.GooglePlace;
import com.bitlogic.sociallbox.data.model.ext.google.GooglePlace.Result.AddressComponent;
import com.bitlogic.sociallbox.data.model.requests.AddMeetupAttendeesRequest;
import com.bitlogic.sociallbox.data.model.requests.CreateMeetupRequest;
import com.bitlogic.sociallbox.data.model.requests.SaveAttendeeResponse;
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
import com.bitlogic.sociallbox.service.transformers.Transformer;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory.Transformer_Types;
import com.bitlogic.sociallbox.service.utils.ImageUtils;
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
	
	@Override
	public Meetup createMetup(CreateMeetupRequest createMeetupRequest) {

		logger.info("### Inside MeetupServiceImpl.createMetup ###");
		Meetup meetup = new Meetup();
		User organizer = this.smartDeviceDAO.getUserInfoFromDeviceId(createMeetupRequest.getDeviceId()); 
		if(organizer==null){
			throw new ClientException(RestErrorCodes.ERR_003,Constants.ERROR_USER_INVALID);
		}
		logger.info("Found organizer details in DB for {} : Id {} ",organizer.getEmailId(),organizer.getId());

		Event eventAtMeetup = null;
		boolean isMeetupAtEvent = false;
		if(createMeetupRequest.getEventAtMeetup()!=null && !createMeetupRequest.getEventAtMeetup().isEmpty()){
			isMeetupAtEvent = true;
			eventAtMeetup = this.eventDAO.getEvent(createMeetupRequest.getEventAtMeetup());
			meetup.setEventAtMeetup(eventAtMeetup);
		}
		Date now = new Date();
		//Setting values into meetup
		meetup.setTitle(createMeetupRequest.getTitle());
		meetup.setDescription(createMeetupRequest.getDescription());
		meetup.setLocation(createMeetupRequest.getLocation());
		meetup.setIsPublic(createMeetupRequest.getIsPublic());
		meetup.setStatus(MeetupStatus.CREATED);
		meetup.setCreatedDt(now);
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.MEETUP_DATE_FORMAT);
			meetup.setStartDate(dateFormat.parse(createMeetupRequest.getStartDate()));
			meetup.setEndDate(dateFormat.parse(createMeetupRequest.getEndDate()));
		} catch (ParseException e) {
			logger.error("ParseException",e);
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
			//TODO: Set address components
		}else{
			//created.setAddressComponents(this.getMeetupAddressInfo(created,createMeetupRequest.getAddressComponents()));
		}
		
		//meetupDAO.saveMeetup(created);
		return created;
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
	public Meetup getMeetup(String meetupId) {
		 logger.info("### Inside MeetupServiceImpl.getMeetup ###");
		 Meetup meetup = meetupDAO.getMeetup(meetupId);
		 if(meetup==null){
			 throw new EntityNotFoundException(meetup, RestErrorCodes.ERR_020, ERROR_MEETUP_NOT_FOUND);
		 }
		return meetup;
	}
	 
	 
	 @Override
	public void addAttendees(AddMeetupAttendeesRequest addAttendeesRequest) {
		 logger.info("### Inside addAttendees ###");
		 List<MeetupAttendee> meetupAttendees = addAttendeesRequest.getAttendees();
		 Meetup meetup = this.meetupDAO.getMeetup(addAttendeesRequest.getMeetupId());
		 if(meetup==null){
			 logger.error("No meetup found with id {}",addAttendeesRequest.getMeetupId());
			 throw new ClientException(RestErrorCodes.ERR_003, ERROR_MEETUP_NOT_FOUND);
		 }
		 List<Long> userIds = new ArrayList<>();
		 Map<Long,MeetupAttendee> meetupAttendeesMap = new HashMap<>();
		 for(MeetupAttendee meetupAttendee : meetupAttendees){
			 userIds.add(meetupAttendee.getUserId());
			 meetupAttendeesMap.put(meetupAttendee.getUserId(), meetupAttendee);
		 }
		 logger.info("Loading users corresponding to attendees");
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
				 logger.warn("No user found for attendee {}",entry.getKey());
				 continue;
			 }
			 meetupAttendeeEntity.setUser(user);
			 attendeeEntities.add(meetupAttendeeEntity);
		 }
		 meetup.getAttendees().addAll(new HashSet<>(attendeeEntities));
		 meetup = this.meetupDAO.saveMeetup(meetup);
		 
	}
	 
	 @Override
	public void saveAttendeeResponse(SaveAttendeeResponse attendeeResponse) {
		this.meetupDAO.saveAttendeeResponse(attendeeResponse);
	}
	 
	 @Override
	public void sendMessageInMeetup(MeetupMessage meetupMessage,
			String meetupId, String userSocialId) {

		 UserSocialDetail userSocialDetail = this.userDAO.getSocialDetail(userSocialId);
		 if(userSocialDetail==null){
			 throw new ClientException(RestErrorCodes.ERR_003, ERROR_SOCIAL_DETAILS_NOT_FOUND);
		 }
		 MeetupAttendeeEntity meetupAttendee = this.userDAO.getAttendeeByMeetupIdAndSocialId(meetupId, userSocialDetail.getId());
		 if(meetupAttendee==null){
			 logger.error("MeetupAttendee not found for social id {} , meetup {} ",userSocialId,meetupId);
		 }else{
			 this.meetupDAO.sendMessageInMeetup(meetupMessage, meetupId, meetupAttendee.getAttendeeId());
		 }
	}
	 
	 @Override
	public List<MeetupMessage> getMeetupMessages(String meetupId,Integer page) {
		 String LOG_PREFIX = "MeetupServiceImpl.getMeetupMessages";
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
					meetupMessage.setTimeToDisplay(diffDays + "Day ago");
					continue;
				}

				int diffHours = (int) (diff / (60 * 60 * 1000) % 24);
				if (diffHours > 0) {
					meetupMessage.setTimeToDisplay(diffHours + "Hour ago");
					continue;
				}

				int diffMinutes = (int) (diff / (60 * 1000) % 60);
				if (diffMinutes > 0) {
					meetupMessage.setTimeToDisplay(diffMinutes + "Min ago");
					continue;
				}

				meetupMessage.setTimeToDisplay("Just Now");
			}
		 }
		 logInfo(LOG_PREFIX, "Returning {} messages for meetup = {}", meetupId);
		 
		 
		 return messages;
	}
	 
	 @Override
	public void uploadImageToMeetup(String deviceId,String imagesURL, List<MultipartFile> images,
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
		 String meetupImagesPath = Constants.MEETUP_IMAGE_STORE_PATH +File.separator+meetupId;
		 String imagesPublicURL = imagesURL.replaceAll("secured", "public");
		 logger.info("Meetup images path : {}",meetupImagesPath);
		 for(MultipartFile multipartFile : images){
			 logger.info("File to process : {} ",multipartFile.getOriginalFilename());
	      	 logger.info("File size : {} ", multipartFile.getSize());
	      	Transformer<MeetupImage, MultipartFile> transformer = 
	      			   (Transformer<MeetupImage, MultipartFile>)TransformerFactory.getTransformer(Transformer_Types.MULTIPART_TO_MEETUP_IMAGE_TRANFORMER);
	      	 try{
	      		   File created = ImageUtils.storeImageOnServer(meetupImagesPath, multipartFile);
	      		   Date now = new Date();
	      		   String imageURL = imagesPublicURL +File.separator+ multipartFile.getOriginalFilename();
	      		   MeetupImage meetupImage = transformer.transform(multipartFile);
	      		   meetupImage.setMeetup(meetup);
	      		   meetupImage.setUrl(imageURL);
	      		   meetupImage.setUploadDt(now);
	      		   meetupImage.setUploadedBy(uploadedBy);
	      		   Path source = Paths.get(created.getAbsolutePath());
	      		   meetupImage.setMimeType(Files.probeContentType(source));
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
	}
}
