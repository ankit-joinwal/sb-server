package com.bitlogic.sociallbox.service.business.impl;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.EventOrganizer;
import com.bitlogic.sociallbox.data.model.requests.CreateEventOrganizerRequest;
import com.bitlogic.sociallbox.data.model.response.EventOrganizerProfile;
import com.bitlogic.sociallbox.service.business.EventOrganizerService;
import com.bitlogic.sociallbox.service.dao.EventOrganizerDAO;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.EntityNotFoundException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.transformers.Transformer;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory.Transformer_Types;

@Service("eventOrganizerService")
@Transactional
public class EventOrganizerServiceImpl implements EventOrganizerService,Constants{

	private static final Logger LOGGER = LoggerFactory.getLogger(EventOrganizerServiceImpl.class);
	
	@Autowired
	private EventOrganizerDAO eventOrganizerDAO;
	
	@Override
	public EventOrganizerProfile create(
			CreateEventOrganizerRequest organizerRequest) {
		LOGGER.info("### Inside EventOrganizerServiceImpl.create ###");
		EventOrganizer eventOrganizer = this.eventOrganizerDAO.getOrganizerByName(organizerRequest.getName());
		if(eventOrganizer!=null){
			throw new ClientException(RestErrorCodes.ERR_002, ERROR_ORGANIZER_EXISTS);
		}
		Transformer<EventOrganizer, CreateEventOrganizerRequest> transformer = 
				(Transformer<EventOrganizer, CreateEventOrganizerRequest>) TransformerFactory.getTransformer(Transformer_Types.CREATE_EO_TO_EO_TRANSFORMER);
		Transformer<EventOrganizerProfile, EventOrganizer> responseTransformer = 
				(Transformer<EventOrganizerProfile, EventOrganizer>) TransformerFactory.getTransformer(Transformer_Types.EO_TO_EO_RESPONSE_TRANSFORMER);
		eventOrganizer = transformer.transform(organizerRequest);
		EventOrganizer created = this.eventOrganizerDAO.create(eventOrganizer);
		
		
		return responseTransformer.transform(created);
	}
	
	@Override
	public EventOrganizerProfile get(String organizerId) {
		EventOrganizer eventOrganizer = this.eventOrganizerDAO.getOrganizerDetails(organizerId);
		if(eventOrganizer==null){
			throw new EntityNotFoundException(organizerId, RestErrorCodes.ERR_020, ERROR_ORGANIZER_NOT_FOUND);
		}
		Transformer<EventOrganizerProfile, EventOrganizer> responseTransformer = 
				(Transformer<EventOrganizerProfile, EventOrganizer>) TransformerFactory.getTransformer(Transformer_Types.EO_TO_EO_RESPONSE_TRANSFORMER);
		
		return responseTransformer.transform(eventOrganizer);
	}
}
