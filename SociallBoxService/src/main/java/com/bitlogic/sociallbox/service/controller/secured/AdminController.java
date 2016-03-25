package com.bitlogic.sociallbox.service.controller.secured;

import java.util.List;

import javax.validation.constraints.NotNull;

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
import com.bitlogic.sociallbox.data.model.EOAdminStatus;
import com.bitlogic.sociallbox.data.model.response.EOAdminProfile;
import com.bitlogic.sociallbox.data.model.response.EntityCollectionResponse;
import com.bitlogic.sociallbox.data.model.response.EventResponse;
import com.bitlogic.sociallbox.data.model.response.SingleEntityResponse;
import com.bitlogic.sociallbox.service.business.EventOrganizerService;
import com.bitlogic.sociallbox.service.business.EventService;
import com.bitlogic.sociallbox.service.controller.BaseController;

@RestController
@RequestMapping("/api/secured/admin/")
public class AdminController extends BaseController implements Constants{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
	private static final String GET_PENDING_PROFILES_API = "GetPendingProfiles API";
	private static final String APPROVE_PENDING_PROFILES_API = "ApprovePendingProfiles API";
	private static final String REJECT_PENDING_PROFILES_API = "RejectPendingProfiles API";
	private static final String GET_EVENTS_PENDING_APPROVAL_API = "GetEventsPendingForApproval API";
	private static final String APPROVE_PENDING_EVENTS = "ApprovePendingEvents API";
	
	@Autowired
	private EventOrganizerService eventOrganizerService;
	
	@Autowired
	private EventService eventService;
	
	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	
	@RequestMapping(value="/eo/profiles/pending",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<EOAdminProfile> getPendingProfiles(){
		
		logRequestStart(GET_PENDING_PROFILES_API, SECURED_REQUEST_START_LOG_MESSAGE, GET_PENDING_PROFILES_API);
		List<EOAdminProfile> pendingProfiles = eventOrganizerService.getPendingProfiles();
		logInfo(GET_PENDING_PROFILES_API, "{} Profiles found", pendingProfiles.size());
		EntityCollectionResponse<EOAdminProfile> collectionResponse = new EntityCollectionResponse<EOAdminProfile>();
		collectionResponse.setStatus(SUCCESS_STATUS);
		collectionResponse.setTotalRecords(pendingProfiles.size());
		collectionResponse.setPage(1);
		collectionResponse.setData(pendingProfiles);
		logRequestEnd(GET_PENDING_PROFILES_API, GET_PENDING_PROFILES_API);
		return collectionResponse;
	}
	
	@RequestMapping(value="/eo/profiles/approve",method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE}, consumes = {
					MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<String> approvePendingProfiles(@NotNull @RequestBody List<Long> profileIds){
		logRequestStart(APPROVE_PENDING_PROFILES_API, SECURED_REQUEST_START_LOG_MESSAGE, APPROVE_PENDING_PROFILES_API);
		logInfo(APPROVE_PENDING_PROFILES_API, "Profiles Recieved {} ", profileIds);
		this.eventOrganizerService.approveOrRejectProfiles(profileIds,EOAdminStatus.APPROVED);
		SingleEntityResponse<String> entityResponse = new SingleEntityResponse<String>();
		entityResponse.setStatus(SUCCESS_STATUS);
		entityResponse.setData("Profiles approved successfully");
		logRequestEnd(APPROVE_PENDING_PROFILES_API, APPROVE_PENDING_PROFILES_API);
		return entityResponse;
	}
	
	@RequestMapping(value="/eo/profiles/reject",method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE}, consumes = {
					MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<String> rejectPendingProfiles(@NotNull @RequestBody List<Long> profileIds){
		logRequestStart(REJECT_PENDING_PROFILES_API, SECURED_REQUEST_START_LOG_MESSAGE, REJECT_PENDING_PROFILES_API);
		logInfo(REJECT_PENDING_PROFILES_API, "Profiles Recieved {} ", profileIds);
		this.eventOrganizerService.approveOrRejectProfiles(profileIds,EOAdminStatus.REJECTED);
		SingleEntityResponse<String> entityResponse = new SingleEntityResponse<String>();
		entityResponse.setStatus(SUCCESS_STATUS);
		entityResponse.setData("Profiles rejected successfully");
		
		logRequestEnd(REJECT_PENDING_PROFILES_API, REJECT_PENDING_PROFILES_API);
		return entityResponse;
	}
	
	@RequestMapping(value="/events/pending",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE}, consumes = {
					MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<EventResponse> getEventsPendingForApproval(){
		logRequestStart(GET_EVENTS_PENDING_APPROVAL_API, SECURED_REQUEST_START_LOG_MESSAGE, GET_EVENTS_PENDING_APPROVAL_API);
		List<EventResponse> pendingEvents = this.eventService.getEventsPendingForApproval();
		EntityCollectionResponse<EventResponse> collectionResponse = new EntityCollectionResponse<EventResponse>();
		collectionResponse.setStatus(SUCCESS_STATUS);
		collectionResponse.setData(pendingEvents);
		collectionResponse.setPage(1);
		collectionResponse.setTotalRecords(pendingEvents.size());
		logRequestEnd(GET_EVENTS_PENDING_APPROVAL_API, GET_EVENTS_PENDING_APPROVAL_API);
		return collectionResponse;
	}
	
	@RequestMapping(value="/events/pending/approve",method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE}, consumes = {
					MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<String> approvePendingEvents(@NotNull @RequestBody List<String> eventIds){
		logRequestStart(APPROVE_PENDING_EVENTS, SECURED_REQUEST_START_LOG_MESSAGE, APPROVE_PENDING_EVENTS);
		logInfo(APPROVE_PENDING_EVENTS, "Event Ids = {}", eventIds);
		this.eventService.approveEvents(eventIds);
		SingleEntityResponse<String> entityResponse = new SingleEntityResponse<String>();
		entityResponse.setStatus(SUCCESS_STATUS);
		entityResponse.setData("Events approved succesfully");
		logRequestEnd(APPROVE_PENDING_EVENTS, APPROVE_PENDING_EVENTS);
		
		return entityResponse;
	}
}