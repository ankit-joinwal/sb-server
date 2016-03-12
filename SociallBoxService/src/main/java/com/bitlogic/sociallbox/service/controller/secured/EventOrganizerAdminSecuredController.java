package com.bitlogic.sociallbox.service.controller.secured;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.response.SingleEntityResponse;
import com.bitlogic.sociallbox.data.model.response.UserPublicProfile;
import com.bitlogic.sociallbox.service.business.EventOrganizerAdminService;
import com.bitlogic.sociallbox.service.business.UserService;
import com.bitlogic.sociallbox.service.controller.BaseController;

@RestController
@RequestMapping("/api/secured/users/organizers/admins")
public class EventOrganizerAdminSecuredController extends BaseController implements Constants{

	private static final Logger LOGGER = LoggerFactory.getLogger(EventOrganizerAdminSecuredController.class);
	private static final String SIGNUP_ORGANIZER_ADMIN_API = "SignupOrganizerAdmin API";
	private static final String GET_ORGANIZER_ADMIN_INFO_API = "GetOrganizerAdminProfile API";
	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	
	@Autowired
	private EventOrganizerAdminService eventOrganizerAdminService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/signup",method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE}, consumes = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.CREATED)
	public SingleEntityResponse<UserPublicProfile> signup(@Valid @RequestBody User user, HttpServletRequest  request){
		logRequestStart(SIGNUP_ORGANIZER_ADMIN_API, SECURED_REQUEST_START_LOG_MESSAGE, SIGNUP_ORGANIZER_ADMIN_API);
		logInfo(SIGNUP_ORGANIZER_ADMIN_API, "User id = {}", user.getEmailId());
		UserPublicProfile profile = this.eventOrganizerAdminService.signup(user);
		SingleEntityResponse<UserPublicProfile> entityResponse = new SingleEntityResponse<>();
		entityResponse.setStatus(SUCCESS_STATUS);
		entityResponse.setData(profile);
		logRequestEnd(SIGNUP_ORGANIZER_ADMIN_API, SIGNUP_ORGANIZER_ADMIN_API);
		return entityResponse;
	}
	
	@RequestMapping(value="/{id}",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<UserPublicProfile> getAdminInfo(@PathVariable Long id){
		logRequestStart(GET_ORGANIZER_ADMIN_INFO_API, SECURED_REQUEST_START_LOG_MESSAGE, GET_ORGANIZER_ADMIN_INFO_API);
		User admin = this.userService.getUser(id);
		UserPublicProfile profile = new UserPublicProfile(admin);
		SingleEntityResponse<UserPublicProfile> entityResponse = new SingleEntityResponse<>();
		entityResponse.setStatus(SUCCESS_STATUS);
		entityResponse.setData(profile);
		logRequestEnd(GET_ORGANIZER_ADMIN_INFO_API, GET_ORGANIZER_ADMIN_INFO_API);
		return entityResponse;
	}
	
}
