package com.bitlogic.sociallbox.service.controller.secured;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.requests.CreateEventOrganizerRequest;
import com.bitlogic.sociallbox.data.model.response.EventOrganizerProfile;
import com.bitlogic.sociallbox.data.model.response.SingleEntityResponse;
import com.bitlogic.sociallbox.service.business.EventOrganizerService;

@RestController
@RequestMapping("/api/secured/users/organizers")
public class EventOrganizerSecuredController implements Constants{

	private static final Logger LOGGER = LoggerFactory.getLogger(EventOrganizerSecuredController.class);
	
	@Autowired
	private EventOrganizerService eventOrganizerService;
	
	@RequestMapping(method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE}, consumes = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.CREATED)
	public SingleEntityResponse<EventOrganizerProfile> create(@Valid @RequestBody CreateEventOrganizerRequest request){
		LOGGER.info("### Request recieved | Create Event Organizer ###");
		LOGGER.info("Request Payload : {} ",request);
		EventOrganizerProfile eventOrganizerResponse = this.eventOrganizerService.create(request);
		SingleEntityResponse<EventOrganizerProfile> entityResponse = new SingleEntityResponse<>();
		entityResponse.setData(eventOrganizerResponse);
		entityResponse.setStatus(SUCCESS_STATUS);
		
		return entityResponse;
	}
}
