package com.bitlogic.sociallbox.service.business.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.EOAdminStatus;
import com.bitlogic.sociallbox.data.model.EventOrganizer;
import com.bitlogic.sociallbox.data.model.EventOrganizerAdmin;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.requests.CreateEventOrganizerRequest;
import com.bitlogic.sociallbox.data.model.response.EOAdminProfile;
import com.bitlogic.sociallbox.data.model.response.EventOrganizerProfile;
import com.bitlogic.sociallbox.service.business.EventOrganizerService;
import com.bitlogic.sociallbox.service.dao.EventOrganizerDAO;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.EntityNotFoundException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.transformers.Transformer;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory.TransformerTypes;
import com.bitlogic.sociallbox.service.utils.LoggingService;

@Service("eventOrganizerService")
@Transactional
public class EventOrganizerServiceImpl extends LoggingService implements EventOrganizerService,Constants{

	private static final Logger LOGGER = LoggerFactory.getLogger(EventOrganizerServiceImpl.class);
	
	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	
	@Autowired
	private EventOrganizerDAO eventOrganizerDAO;
	
	@Override
	public EventOrganizer create(
			CreateEventOrganizerRequest organizerRequest) {
		String LOG_PREFIX = "EventOrganizerServiceImpl-createOrganizerCompany";
		
		EventOrganizer eventOrganizer = this.eventOrganizerDAO.getEOByName(organizerRequest.getName());
		if(eventOrganizer!=null){
			throw new ClientException(RestErrorCodes.ERR_002, ERROR_ORGANIZER_EXISTS);
		}
		Transformer<EventOrganizer, CreateEventOrganizerRequest> transformer = 
				(Transformer<EventOrganizer, CreateEventOrganizerRequest>) TransformerFactory.getTransformer(TransformerTypes.CREATE_EO_TO_EO_TRANSFORMER);
		eventOrganizer = transformer.transform(organizerRequest);
		eventOrganizer.setCreateDt(new Date());
		EventOrganizer created = this.eventOrganizerDAO.createEO(eventOrganizer);
		logInfo(LOG_PREFIX, "EventOrganizer Company created successfully {} ", created);
		
		return created;
	}
	
	
	@Override
	public EventOrganizer getOrganizerDetails(String organizerId) {
		String LOG_PREFIX = "EventOrganizerServiceImpl-getOrganizerDetails";
		logInfo(LOG_PREFIX, "Getting Organizer Details with id = {}", organizerId);
		EventOrganizer eventOrganizer = this.eventOrganizerDAO.getEODetails(organizerId);
		if(eventOrganizer==null){
			logError(LOG_PREFIX, "Organizer not found for id = {}", organizerId);
			throw new EntityNotFoundException(organizerId, RestErrorCodes.ERR_020, ERROR_ORGANIZER_NOT_FOUND);
		}
		logInfo(LOG_PREFIX, "Found Organizer Details = {}", eventOrganizer);
		return eventOrganizer;
		
	}
	
	
	
	
	
}
