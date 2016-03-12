package com.bitlogic.sociallbox.service.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.Event;
import com.bitlogic.sociallbox.data.model.EventImage;
import com.bitlogic.sociallbox.data.model.response.EntityCollectionResponse;
import com.bitlogic.sociallbox.data.model.response.EventResponse;
import com.bitlogic.sociallbox.data.model.response.SingleEntityResponse;
import com.bitlogic.sociallbox.service.business.EventService;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.transformers.Transformer;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory.Transformer_Types;

@RestController
@RequestMapping("/api/public/events")
public class EventPublicController extends BaseController implements Constants{
	private static final Logger logger = LoggerFactory.getLogger(EventPublicController.class);
	private static final String GET_EVENT_API = "GetEvent API";
	private static final String GET_EVENT_IMAGES_API = "GetEventImages API";
	private static final String GET_EVENT_IMAGE_BY_NAME_API = "GetEventImageByName API";
	private static final String GET_PERSONALIZEZ_EVENT_FOR_USER_API = "GetPersonalizedEventsForUser API";
	private static final String GET_EVENTS_BY_TYPE_API = "GetEventsOfType API";
	@Override
	public Logger getLogger() {
		return logger;
	}
	
	@Autowired
	private EventService eventService;
	
	@RequestMapping(value="/{eventId}",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<EventResponse> getEvent(@PathVariable String eventId) {
		logRequestStart(GET_EVENT_API, PUBLIC_REQUEST_START_LOG, GET_EVENT_API);
		logInfo(GET_EVENT_API, "event id = {}", eventId);
		Transformer<EventResponse, Event> transformer = (Transformer<EventResponse, Event>) TransformerFactory.getTransformer(Transformer_Types.EVENT_TRANS);
		EventResponse createEventResponse = transformer.transform(eventService.get(eventId));
		SingleEntityResponse<EventResponse> entityResponse = new SingleEntityResponse<>();
		entityResponse.setData(createEventResponse);
		entityResponse.setStatus(SUCCESS_STATUS);
		logRequestEnd(GET_EVENT_API, GET_EVENT_API);
		return entityResponse;
	}
	
	@RequestMapping(value="/{eventId}/images",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<EventImage> getEventImages(@PathVariable String eventId){
		logRequestStart(GET_EVENT_IMAGES_API, PUBLIC_REQUEST_START_LOG, GET_EVENT_IMAGES_API);
		logInfo(GET_EVENT_IMAGES_API, "Event id = {} ", eventId);
		List<EventImage> eventImages = this.eventService.getEventImages(eventId);
		EntityCollectionResponse<EventImage> collectionResponse = new EntityCollectionResponse<>();
		collectionResponse.setPage(1);
		collectionResponse.setStatus(SUCCESS_STATUS);
		collectionResponse.setData(eventImages);
		logRequestEnd(GET_EVENT_IMAGES_API, GET_EVENT_IMAGES_API);
		return collectionResponse;
	}
	
	@RequestMapping(value="/{eventId}/images/{imageName}",method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<InputStreamResource> getEventImage(@PathVariable String eventId,
								@PathVariable String imageName){
		
		logRequestStart(GET_EVENT_IMAGE_BY_NAME_API, PUBLIC_REQUEST_START_LOG, GET_EVENT_IMAGE_BY_NAME_API);
		logInfo(GET_EVENT_IMAGE_BY_NAME_API, "Event id ={} , image name = {} ",eventId,imageName  );
		String filePath = Constants.EVENT_IMAGE_STORE_PATH+File.separator+eventId+File.separator+imageName+".jpg";
		logInfo(GET_EVENT_IMAGE_BY_NAME_API, "File Path = {}", filePath);
		File file = new File(filePath);
		if(!file.exists()){
			logError(GET_EVENT_IMAGE_BY_NAME_API, "Image not found with name = {}", imageName);
			throw new ClientException(RestErrorCodes.ERR_003, ERROR_IMAGE_NOT_FOUND);
		}
		InputStream inputStream = null;
		try{
			inputStream = new FileInputStream(file);
		}catch(FileNotFoundException exception){
			logError(GET_EVENT_IMAGE_BY_NAME_API, "Image not found with name = {}", imageName);
			throw new ClientException(RestErrorCodes.ERR_003, ERROR_IMAGE_NOT_FOUND);
		}
		logRequestEnd(GET_EVENT_IMAGE_BY_NAME_API, GET_EVENT_IMAGE_BY_NAME_API);
		 return ResponseEntity.ok()
		            .body(new InputStreamResource(inputStream));
	}
	
	@RequestMapping(value="/personalized",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<EventResponse> getPersonalizedEvents(
												@RequestParam(required = true, value = "location") String location,
												@RequestParam(required = false, value = "id") Long userId,
												@RequestParam(required = true, value = "city") String city,
												@RequestParam(required = true, value = "country") String country,
												@RequestParam(required=false,value="page") Integer page){

		logRequestStart(GET_PERSONALIZEZ_EVENT_FOR_USER_API, PUBLIC_REQUEST_START_LOG, GET_PERSONALIZEZ_EVENT_FOR_USER_API);
		logInfo(GET_PERSONALIZEZ_EVENT_FOR_USER_API, " City {} , Country {} ,user {} ", city,country,userId);
		if(page==null){
			page = new Integer(1);
		}
		
		List<EventResponse> events = this.eventService.getEventsForUser(location,userId,city, country,page);
		EntityCollectionResponse<EventResponse> collectionResponse = new EntityCollectionResponse<>();
		collectionResponse.setData(events);
		collectionResponse.setStatus("Success");
		collectionResponse.setPage(page);
		
		logRequestEnd(GET_PERSONALIZEZ_EVENT_FOR_USER_API, GET_PERSONALIZEZ_EVENT_FOR_USER_API);
		return collectionResponse;
	}
	
	

	@RequestMapping(value="/types/{eventType}",method=RequestMethod.GET,produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<EventResponse> getEventsOfType(
												@RequestParam(required = true, value = "location") String location,
												@PathVariable String eventType,
												@RequestParam(required = true, value = "city") String city,
												@RequestParam(required = true, value = "country") String country,
												@RequestParam(required=false,value="page") Integer page) {
		logRequestStart(GET_EVENTS_BY_TYPE_API, PUBLIC_REQUEST_START_LOG, GET_EVENTS_BY_TYPE_API);
		
		logInfo(GET_EVENTS_BY_TYPE_API,"Params [ Type :{} , City : {} , Country : {} ",eventType,city,country);
		if(page==null){
			page = new Integer(1);
		}
		List<EventResponse> eventsList = this.eventService.getEventsByType(location,eventType, city, country,page);
		
		EntityCollectionResponse<EventResponse> collectionResponse = new EntityCollectionResponse<>();
		collectionResponse.setData(eventsList);
		collectionResponse.setStatus("Success");
		collectionResponse.setPage(page);
		logRequestEnd(GET_EVENTS_BY_TYPE_API, GET_EVENTS_BY_TYPE_API);
		return collectionResponse;
	}
	
}
