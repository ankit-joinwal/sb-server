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
public class EventPublicController implements Constants{
	private static final Logger logger = LoggerFactory.getLogger(EventPublicController.class);
	
	@Autowired
	private EventService eventService;
	
	@RequestMapping(value="/{eventId}",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<EventResponse> getEvent(@PathVariable String eventId) {
		logger.info("### Request recieved - get event {} ",eventId);
		Transformer<EventResponse, Event> transformer = (Transformer<EventResponse, Event>) TransformerFactory.getTransformer(Transformer_Types.EVENT_TRANS);
		EventResponse createEventResponse = transformer.transform(eventService.get(eventId));
		SingleEntityResponse<EventResponse> entityResponse = new SingleEntityResponse<>();
		entityResponse.setData(createEventResponse);
		entityResponse.setStatus(SUCCESS_STATUS);
		return entityResponse;
	}
	
	@RequestMapping(value="/{eventId}/images",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<EventImage> getEventImages(@PathVariable String eventId){
		
		List<EventImage> eventImages = this.eventService.getEventImages(eventId);
		EntityCollectionResponse<EventImage> collectionResponse = new EntityCollectionResponse<>();
		collectionResponse.setPage(1);
		collectionResponse.setStatus(SUCCESS_STATUS);
		collectionResponse.setData(eventImages);
		return collectionResponse;
	}
	
	@RequestMapping(value="/{eventId}/images/{imageName}",method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<InputStreamResource> getEventImage(@PathVariable String eventId,
								@PathVariable String imageName){
		logger.info("### Request Recieved to get image for event {} with name {} ",eventId,imageName);
		String filePath = Constants.EVENT_IMAGE_STORE_PATH+File.separator+eventId+File.separator+imageName+".jpg";
		
		File file = new File(filePath);
		if(!file.exists()){
			throw new ClientException(RestErrorCodes.ERR_003, ERROR_IMAGE_NOT_FOUND);
		}
		InputStream inputStream = null;
		try{
			inputStream = new FileInputStream(file);
		}catch(FileNotFoundException exception){
			throw new ClientException(RestErrorCodes.ERR_003, ERROR_IMAGE_NOT_FOUND);
		}
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

		logger.info("### Request Recieved - getPersonalizedEvents. City {} , Country {} ,user {} ###",city,country,userId);
		if(page==null){
			page = new Integer(1);
		}
		
		List<EventResponse> events = this.eventService.getEventsForUser(location,userId,city, country,page);
		EntityCollectionResponse<EventResponse> collectionResponse = new EntityCollectionResponse<>();
		collectionResponse.setData(events);
		collectionResponse.setStatus("Success");
		collectionResponse.setPage(page);
		
		
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
		logger.info("### Request Recieved - getEventsOfType - Params [ Type :{} , City : {} , Country : {} ###",eventType,city,country);
		if(page==null){
			page = new Integer(1);
		}
		List<EventResponse> eventsList = this.eventService.getEventsByType(location,eventType, city, country,page);
		
		EntityCollectionResponse<EventResponse> collectionResponse = new EntityCollectionResponse<>();
		collectionResponse.setData(eventsList);
		collectionResponse.setStatus("Success");
		collectionResponse.setPage(page);
		return collectionResponse;
	}
	
}
