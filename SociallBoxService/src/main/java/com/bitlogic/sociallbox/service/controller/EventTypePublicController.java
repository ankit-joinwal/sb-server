package com.bitlogic.sociallbox.service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bitlogic.sociallbox.data.model.EventType;
import com.bitlogic.sociallbox.data.model.response.EntityCollectionResponse;
import com.bitlogic.sociallbox.service.business.EventTypeService;

@RestController
@RequestMapping("/api/public/events/types")
public class EventTypePublicController {

private static final Logger logger = LoggerFactory.getLogger(EventTypePublicController.class);
	
	@Autowired
	private EventTypeService eventService;
	
	@RequestMapping(method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<EventType> getEventTypes(){
		logger.info("### Request recieved- getEventTypes. ###");
		List<EventType> eventTypes = eventService.getAllEventTypes();
		EntityCollectionResponse<EventType> collectionResponse = new EntityCollectionResponse<>();
		collectionResponse.setData(eventTypes);
		collectionResponse.setPage(1);
		collectionResponse.setStatus("Success");
		return collectionResponse;
	}
	
}
