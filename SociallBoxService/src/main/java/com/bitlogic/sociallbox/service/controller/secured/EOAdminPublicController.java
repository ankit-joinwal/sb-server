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
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.response.EOAdminProfile;
import com.bitlogic.sociallbox.data.model.response.SingleEntityResponse;
import com.bitlogic.sociallbox.service.business.EOAdminService;
import com.bitlogic.sociallbox.service.business.UserService;
import com.bitlogic.sociallbox.service.controller.BaseController;

@RestController
@RequestMapping("/api/public/users/organizers/admins")
public class EOAdminPublicController extends BaseController implements Constants{

	private static final Logger LOGGER = LoggerFactory.getLogger(EOAdminPublicController.class);
	private static final String SIGNUP_ORGANIZER_ADMIN_API = "SignupOrganizerAdmin API";
	
	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	
	@Autowired
	private EOAdminService eventOrganizerAdminService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/signup",method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE}, consumes = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.CREATED)
	public SingleEntityResponse<EOAdminProfile> signup(@Valid @RequestBody User user){
		logRequestStart(SIGNUP_ORGANIZER_ADMIN_API, PUBLIC_REQUEST_START_LOG, SIGNUP_ORGANIZER_ADMIN_API);
		logInfo(SIGNUP_ORGANIZER_ADMIN_API, "User id = {}", user.getEmailId());
		EOAdminProfile profile = this.eventOrganizerAdminService.signup(user);
		SingleEntityResponse<EOAdminProfile> entityResponse = new SingleEntityResponse<EOAdminProfile>();
		entityResponse.setStatus(SUCCESS_STATUS);
		entityResponse.setData(profile);
		logRequestEnd(SIGNUP_ORGANIZER_ADMIN_API, SIGNUP_ORGANIZER_ADMIN_API);
		return entityResponse;
	}
	
	
}
