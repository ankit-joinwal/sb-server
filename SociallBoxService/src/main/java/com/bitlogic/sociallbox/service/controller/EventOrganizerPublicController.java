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
import com.bitlogic.sociallbox.data.model.EventOrganizer;
import com.bitlogic.sociallbox.data.model.response.EventOrganizerProfile;
import com.bitlogic.sociallbox.data.model.response.SingleEntityResponse;
import com.bitlogic.sociallbox.service.business.EventOrganizerService;
import com.bitlogic.sociallbox.service.transformers.Transformer;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory.TransformerTypes;

@RestController
@RequestMapping("/api/public/users/organizers")
public class EventOrganizerPublicController extends BaseController implements Constants{

	private static final Logger LOGGER = LoggerFactory.getLogger(EventOrganizerPublicController.class);
	private static final String GET_ORGANIZER_PROFILE_API = "GetOrganizerProfile API";
	
	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	@Autowired
	private EventOrganizerService eventOrganizerService;
	
	@RequestMapping(value="/{id}",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE}, consumes = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.CREATED)
	public SingleEntityResponse<EventOrganizerProfile> getOrganizerProfile(@PathVariable String id){
		logRequestStart(GET_ORGANIZER_PROFILE_API, PUBLIC_REQUEST_START_LOG, GET_ORGANIZER_PROFILE_API);
		logInfo(GET_ORGANIZER_PROFILE_API, "Organizer Profile Id = {}", id);
		EventOrganizer eventOrganizerResponse = this.eventOrganizerService.getOrganizerDetails(id);
		Transformer<EventOrganizerProfile, EventOrganizer> responseTransformer = 
				(Transformer<EventOrganizerProfile, EventOrganizer>) TransformerFactory.getTransformer(TransformerTypes.EO_TO_EO_RESPONSE_TRANSFORMER);
		EventOrganizerProfile eventOrganizerProfile = responseTransformer.transform(eventOrganizerResponse);
 		SingleEntityResponse<EventOrganizerProfile> entityResponse = new SingleEntityResponse<>();
		entityResponse.setData(eventOrganizerProfile);
		entityResponse.setStatus(SUCCESS_STATUS);
		logRequestEnd(GET_ORGANIZER_PROFILE_API, GET_ORGANIZER_PROFILE_API);
		return entityResponse;
	}
}
