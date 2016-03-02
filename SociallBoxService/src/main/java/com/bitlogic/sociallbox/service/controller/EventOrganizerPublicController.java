package com.bitlogic.sociallbox.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.response.EventOrganizerProfile;
import com.bitlogic.sociallbox.data.model.response.SingleEntityResponse;
import com.bitlogic.sociallbox.service.business.EventOrganizerService;

@RestController
@RequestMapping("/api/public/users/organizers")
public class EventOrganizerPublicController implements Constants{

	private static final Logger LOGGER = LoggerFactory.getLogger(EventOrganizerPublicController.class);
	
	@Autowired
	private EventOrganizerService eventOrganizerService;
	
	@RequestMapping(value="/{id}",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE}, consumes = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.CREATED)
	public SingleEntityResponse<EventOrganizerProfile> getOrganizerProfile(@PathVariable String id){
		LOGGER.info("### Request recieved | Create getOrganizerProfile for {} ###",id);
		EventOrganizerProfile eventOrganizerResponse = this.eventOrganizerService.get(id);
		SingleEntityResponse<EventOrganizerProfile> entityResponse = new SingleEntityResponse<>();
		entityResponse.setData(eventOrganizerResponse);
		entityResponse.setStatus(SUCCESS_STATUS);
		
		return entityResponse;
	}
}
