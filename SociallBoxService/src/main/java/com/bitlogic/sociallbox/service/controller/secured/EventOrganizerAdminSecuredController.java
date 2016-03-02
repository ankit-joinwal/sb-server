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
import com.bitlogic.sociallbox.service.dao.UserDAO;

@RestController
@RequestMapping("/api/secured/users/organizers/admins")
public class EventOrganizerAdminSecuredController implements Constants{

	private static final Logger LOGGER = LoggerFactory.getLogger(EventOrganizerAdminSecuredController.class);
	
	@Autowired
	private EventOrganizerAdminService eventOrganizerAdminService;
	
	@Autowired
	private UserDAO userDAO;
	
	@RequestMapping(value="/signup",method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE}, consumes = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.CREATED)
	public SingleEntityResponse<UserPublicProfile> signup(@Valid @RequestBody User user, HttpServletRequest  request){
		LOGGER.info("### Request recieved | signup Event Organizer Admin | {} ###",user.getEmailId());
		UserPublicProfile profile = this.eventOrganizerAdminService.signup(user);
		SingleEntityResponse<UserPublicProfile> entityResponse = new SingleEntityResponse<>();
		entityResponse.setStatus(SUCCESS_STATUS);
		entityResponse.setData(profile);
		return entityResponse;
	}
	
	@RequestMapping(value="/{id}",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<UserPublicProfile> getAdminInfo(@PathVariable Long id){
		LOGGER.info("### Request recieved | Get Event Organizer Admin Info | {} ###",id);
		User admin = this.userDAO.getUserById(id);
		UserPublicProfile profile = new UserPublicProfile(admin);
		SingleEntityResponse<UserPublicProfile> entityResponse = new SingleEntityResponse<>();
		entityResponse.setStatus(SUCCESS_STATUS);
		entityResponse.setData(profile);
		return entityResponse;
	}
	
}
