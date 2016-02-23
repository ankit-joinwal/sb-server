package com.bitlogic.sociallbox.service.business;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.bitlogic.sociallbox.data.model.Event;
import com.bitlogic.sociallbox.data.model.EventAddressInfo;
import com.bitlogic.sociallbox.data.model.EventDetails;
import com.bitlogic.sociallbox.data.model.EventImage;
import com.bitlogic.sociallbox.data.model.EventResponse;
import com.bitlogic.sociallbox.data.model.EventTag;
import com.bitlogic.sociallbox.data.model.EventType;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.ext.PlaceDetails;
import com.bitlogic.sociallbox.data.model.ext.PlaceDetails.Result.AddressComponent;
import com.bitlogic.sociallbox.data.model.requests.CreateEventRequest;
import com.bitlogic.sociallbox.data.model.requests.CreateEventRequest.MockEventDetails;
import com.bitlogic.sociallbox.service.dao.EventDAO;
import com.bitlogic.sociallbox.service.dao.EventTagDAO;
import com.bitlogic.sociallbox.service.dao.EventTypeDAO;
import com.bitlogic.sociallbox.service.dao.MeetupDAO;
import com.bitlogic.sociallbox.service.dao.UserDAO;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.EntityNotFoundException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.exception.ServiceException;
import com.bitlogic.sociallbox.service.transformers.Transformer;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory.Transformer_Types;
import com.bitlogic.sociallbox.service.utils.ImageUtils;

@Service
@Transactional
public class EventServiceImpl implements EventService ,Constants{

	private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);
	
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
	
	@Override
	public Event create(CreateEventRequest createEventRequest) {
		logger.info("### Inside CreateEventRequest.create ###");
		Event event = new Event();
		MockEventDetails mockEventDetails = createEventRequest.getEventDetails();
		
		
		User organizer = this.userDAO.getUserByEmailId(createEventRequest.getOrganizerId(), false); 
		logger.info("   Found organizer details in DB for {} : Id {}",organizer.getEmailId(),organizer.getId());
		
		Set<EventTag> tags = createEventRequest.getTags();
		if(tags!=null && !tags.isEmpty()){
			List<String> tagNames = new ArrayList<>();
			for(EventTag eventTag : tags){
				tagNames.add(eventTag.getName());
			}
			List<EventTag> tagsInDB = eventTagDAO.getTagsByNames(tagNames);
			event.setTags(new HashSet<>(tagsInDB));
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.MEETUP_DATE_FORMAT);
		try {
			event.setStartDate(dateFormat.parse(createEventRequest.getStartDate()));
			event.setEndDate(dateFormat.parse(createEventRequest.getEndDate()));
		} catch (ParseException e) {

			logger.error("ParseException",e);
		}
		
		EventDetails eventDetails = new EventDetails();
		eventDetails.setLocation(mockEventDetails.getLocation());
		eventDetails.setOrganizer(organizer);
		event.setTitle(createEventRequest.getTitle());
		event.setDescription(createEventRequest.getDescription());
		event.setEventDetails(eventDetails);
		eventDetails.setEvent(event);
		Event created = this.eventDAO.create(event);
		created.getEventDetails().setAddressComponents(this.getEventAddressInfo(eventDetails,mockEventDetails.getAddressComponents()));
		this.eventDAO.saveEvent(created);
		return created;
	}
	
	private Set<EventAddressInfo> getEventAddressInfo(EventDetails eventDetails,Set<PlaceDetails.Result.AddressComponent> addressComponents){
		List<AddressComponentType> addressComponentTypes = this.meetupDAO.getAddressTypes();
		logger.info("Inside getEventAddressInfo. addressComponentTypes : {}  ",addressComponentTypes);
		Map<String,AddressComponentType> addressComponentTypesMap = new HashMap<>();
		for(AddressComponentType addressComponentType: addressComponentTypes){
			addressComponentTypesMap.put(addressComponentType.getName(), addressComponentType);
		}
		Set<EventAddressInfo> eventAddresses = new HashSet<>();
		for(AddressComponent addressComponent : addressComponents){
			logger.info("Address Component {} "+addressComponent.getLongName());
			List<String> types = addressComponent.getTypes();
			logger.info("Types : {} ",types);
			for(String type : types){
				if(addressComponentTypes.contains(new AddressComponentType(type))){
					logger.info("Address component type found : {}",type);
					
					AddressComponentType addressComponentType = addressComponentTypesMap.get(type);
					EventAddressInfo eventAddressInfo  = new EventAddressInfo();
					eventAddressInfo.setAddressComponentType(addressComponentType);
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
		Event event = this.eventDAO.getEvent(uuid);
		if(event == null){
			throw new EntityNotFoundException(uuid,RestErrorCodes.ERR_020,ERROR_INVALID_EVENT_IN_REQUEST);
		}
		return event;
	}
	
	@Override
	public void makeEventLive(String eventId) {
		logger.info("### Inside Make vent Live ###");
		Event event = this.eventDAO.getEvent(eventId);
		if(event == null){
			throw new ClientException(RestErrorCodes.ERR_020,ERROR_INVALID_EVENT_IN_REQUEST);
		}
		this.eventDAO.makeEventLive(event);
	}
	
	@Override
	public List<EventResponse> getEventsForUser(Long userId,String city, String country,Integer page) {
		logger.info("### Inside getEventsForUser . ###");
		List<Long> userTags = null;
		if(userId!=null){
			userTags = this.eventTagDAO.getUserTagIds(userId);
		}else{
			userTags = this.eventTagDAO.getAllTagIds();
		}
		return this.eventDAO.getEventsByFilter(userTags, city, country,page);
		
	}
	
	@Override
	public List<EventResponse> getEventsByType(String eventTypeName, String city,
			String country,Integer page){

		logger.info("### Inside getEventsByType .Type {}, City {} , Country {} ###",eventTypeName,city,country);
		EventType eventType = this.eventTypeDAO.getEventTypeByName(eventTypeName);
		if(eventType==null){
			throw new ClientException(RestErrorCodes.ERR_003, ERROR_EVENT_TYPE_INVALID);
		}
	
		logger.info("Found Event Type by name {}",eventTypeName);
		Set<EventTag> tags = eventType.getRelatedTags();
		
		List<Long> tagIds = new ArrayList<Long>(tags.size());
		for(EventTag eventTag : tags){
			tagIds.add(eventTag.getId());
		}

		return this.eventDAO.getEventsByFilter(tagIds, city, country,page);
	}
	
	@Override
	public void storeEventImages(String imagesURL,List<MultipartFile> images, String eventId){
		logger.info("### Inside EventServiceImpl.storeEventImages ###");
		 List<EventImage> imagesToSave = new ArrayList<>();
		 Event event = this.eventDAO.getEventWithoutImage(eventId);
		 if(event==null){
			 throw new ClientException(RestErrorCodes.ERR_003,ERROR_INVALID_EVENT_IN_REQUEST);
		 }
		 int displayOrder = 1;
		 String eventImagesPath = Constants.EVENT_IMAGE_STORE_PATH+File.separator+eventId;
		 String imagesPublicURL = imagesURL.replaceAll("secured", "public");
		 logger.info("Event images path : {}",eventImagesPath);
         for(MultipartFile multipartFile : images){
        	 
      	   logger.info("File to process : {} ",multipartFile.getOriginalFilename());
      	   logger.info("File size : {} ", multipartFile.getSize());
      	   Transformer<EventImage, MultipartFile> transformer = 
      			   (Transformer<EventImage, MultipartFile>)TransformerFactory.getTransformer(Transformer_Types.MULTIPART_TO_EVENT_IMAGE_TRANFORMER);
      	   try{
      		   File created = ImageUtils.storeImageOnServer(eventImagesPath, multipartFile);
      		   
      		   String imageURL = imagesPublicURL +File.separator+ multipartFile.getOriginalFilename();
      		   EventImage eventImage = transformer.transform(multipartFile);
      		   eventImage.setEvent(event);
      		   eventImage.setDisplayOrder(displayOrder);
      		   eventImage.setUrl(imageURL);
      		   Path source = Paths.get(created.getAbsolutePath());
      		   eventImage.setMimeType(Files.probeContentType(source));
      		   imagesToSave.add(eventImage);
      		   displayOrder = displayOrder+1;
      	   }catch(ServiceException serviceException){
      		   logger.error("Error occurred while processing event image",serviceException);
      	   }catch(Exception ex){
      		 logger.error("Error occurred while processing event image",ex);
      	   }
         }
         
         if(!imagesToSave.isEmpty()){
        	 this.eventDAO.saveEventImages(imagesToSave);
         }
	}
	
	@Override
	public List<EventImage> getEventImages(String eventId) {
		List<EventImage> eventImages = this.eventDAO.getEventImages(eventId);
		
		return eventImages;
	}
	
}
