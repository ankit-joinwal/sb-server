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
import com.bitlogic.sociallbox.service.transformers.TransformerFactory.Transformer_Types;
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
				(Transformer<EventOrganizer, CreateEventOrganizerRequest>) TransformerFactory.getTransformer(Transformer_Types.CREATE_EO_TO_EO_TRANSFORMER);
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
	
	@Override
	public EventOrganizerAdmin createEOAdmin(
			EventOrganizerAdmin eventOrganizerAdmin) {
		String LOG_PREFIX = "EventOrganizerServiceImpl-createEOAdmin";
		EventOrganizerAdmin created = this.eventOrganizerDAO.createEOAdmin(eventOrganizerAdmin);
		logInfo(LOG_PREFIX, "Created EO Admin {}", created);
		
		return created;
	}
	
	@Override
	public EventOrganizerAdmin getEOAdminById(Long eoAdminId) {
		String LOG_PREFIX = "EventOrganizerServiceImpl-getEOAdminById";
		EventOrganizerAdmin organizerAdmin = this.eventOrganizerDAO.getEOAdminProfileById(eoAdminId);
		if(organizerAdmin==null){
			logError(LOG_PREFIX, "No EO Admin found for Id {}", eoAdminId);
			
			throw new EntityNotFoundException(eoAdminId, RestErrorCodes.ERR_020, ERROR_INVALID_EOADMIN_ID);
		}
		return organizerAdmin;
	}
	
	
	@Override
	public List<EOAdminProfile> getPendingProfiles() {
		String LOG_PREFIX = "EventOrganizerServiceImpl-getPendingProfiles";
		List<EventOrganizerAdmin> pendingEOs = this.eventOrganizerDAO.getPendingEOAdminProfiles();
		List<EOAdminProfile> pendingProfiles = new ArrayList<EOAdminProfile>();
		logInfo(LOG_PREFIX, "Preparing Pending Profiles ");
		for(EventOrganizerAdmin admin : pendingEOs){
			User user = admin.getUser();
			EventOrganizer organizer = admin.getOrganizer();
			Transformer<EventOrganizerProfile, EventOrganizer> eoProfileTransformer = 
					(Transformer<EventOrganizerProfile, EventOrganizer>) TransformerFactory.getTransformer(Transformer_Types.EO_TO_EO_RESPONSE_TRANSFORMER);
			EventOrganizerProfile eventOrganizerProfile = eoProfileTransformer.transform(organizer);
			EOAdminProfile adminProfile = new EOAdminProfile(eventOrganizerProfile, admin, user);
			pendingProfiles.add(adminProfile);
		}
		return pendingProfiles;
	}
	
	@Override
	public void approveOrRejectProfiles(List<Long> profileIds,EOAdminStatus status) {
		String LOG_PREFIX = "EventOrganizerServiceImpl-approveOrRejectProfiles";
		List<EventOrganizerAdmin> eoAdmins = this.eventOrganizerDAO.getEOAdminProfilesByIds(profileIds);
		Date now = new Date();
		if(eoAdmins!=null){
			for(EventOrganizerAdmin organizerAdmin: eoAdmins){
				organizerAdmin.setStatus(status);
				organizerAdmin.setUpdateDt(now);
			}
		}
		logInfo(LOG_PREFIX, "Updated profiles successfully");
	}
	
}
