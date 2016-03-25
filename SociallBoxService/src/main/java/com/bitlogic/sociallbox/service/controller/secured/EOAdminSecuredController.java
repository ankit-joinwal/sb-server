package com.bitlogic.sociallbox.service.controller.secured;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.requests.AddCompanyToProfileRequest;
import com.bitlogic.sociallbox.data.model.response.EOAdminProfile;
import com.bitlogic.sociallbox.data.model.response.SingleEntityResponse;
import com.bitlogic.sociallbox.service.business.EOAdminService;
import com.bitlogic.sociallbox.service.controller.BaseController;

@RestController
@RequestMapping("/api/secured/users/organizers/admins")
public class EOAdminSecuredController extends BaseController implements Constants{

	private static final Logger LOGGER = LoggerFactory.getLogger(EOAdminSecuredController.class);
	private static final String ADD_COMPANY_TO_PROFILE = "AddCompanyToProfile API";
	private static final String GET_ORGANIZER_ADMIN_INFO_API = "GetOrganizerAdminProfile API";
	
	
	@Autowired
	private EOAdminService eventOrganizerAdminService;
	
	
	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	
	
	@RequestMapping(value="/{id}/company",method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE}, consumes = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.CREATED)
	public SingleEntityResponse<EOAdminProfile> addCompanyToProfile(@Valid @RequestBody AddCompanyToProfileRequest addCompanyRequest,
			@PathVariable Long id,
			@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String auth){
		logRequestStart(ADD_COMPANY_TO_PROFILE, SECURED_REQUEST_START_LOG_MESSAGE, ADD_COMPANY_TO_PROFILE);
		logInfo(ADD_COMPANY_TO_PROFILE, "User Id = {}", id);
		//TODO : Validate that auth token is for intended user only
		EOAdminProfile adminProfile = this.eventOrganizerAdminService.addCompany(addCompanyRequest, id);
		
		SingleEntityResponse<EOAdminProfile> entityResponse = new SingleEntityResponse<EOAdminProfile>();
		entityResponse.setStatus(SUCCESS_STATUS);
		entityResponse.setData(adminProfile);
		logRequestEnd(ADD_COMPANY_TO_PROFILE, ADD_COMPANY_TO_PROFILE);
		return entityResponse;
	}
	
	@RequestMapping(value="/{id}",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<EOAdminProfile> getAdminInfo(@PathVariable Long id){
		logRequestStart(GET_ORGANIZER_ADMIN_INFO_API, SECURED_REQUEST_START_LOG_MESSAGE, GET_ORGANIZER_ADMIN_INFO_API);
		EOAdminProfile admin = this.eventOrganizerAdminService.getProfile(id);
		SingleEntityResponse<EOAdminProfile> entityResponse = new SingleEntityResponse<EOAdminProfile>();
		entityResponse.setStatus(SUCCESS_STATUS);
		entityResponse.setData(admin);
		logRequestEnd(GET_ORGANIZER_ADMIN_INFO_API, GET_ORGANIZER_ADMIN_INFO_API);
		return entityResponse;
	}
	
}
