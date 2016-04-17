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
	
	/**
	 *  @api {post} /api/public/users/organizers/admins/signup Signup Event Organizer Admin
	 *  @apiName Signup Event Organizer Admin
	 *  @apiGroup Dashboard
	 *  @apiHeader {String} accept application/json
	 *  @apiHeader {String} Content-Type application/json
	 * 	@apiParamExample {json} Request-Example:
	 *     {
				"name" : "Dummy EO",
				"emailId": "dummy.eo@gmail.com",
				"password":"p@ssword"
			}
	 *	@apiSuccess (Success - Created 201) {Object}  response  Response.
	 *  @apiSuccess (Success - Created 201) {String}  response.status   Eg.Success.
	 * 	@apiSuccess (Success - Created 201) {Object}  response.data User Profile Information.
	 *  @apiSuccessExample {json} Success-Response:
	 *  {
		  "status": "Success",
		  "data": {
			"id": 25,
			"name": "Dummy EO",
			"social_details": [],
			"profile_id": null,
			"profile_pic": "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xla1/v/t1.0-1/c15.0.50.50/p50x50/1379841_10150004552801901_469209496895221757_n.jpg?oh=1ecfea0dda4f046e7d518ce2243c1a61&oe=57789C33&__gda__=1471731386_863525fcdf1cee40b0e9562b633a5197",
			"email_id": "dummy.eo@gmail.com",
			"company_profile": null,
			"status": "COMPANY_NOT_LINKED"
		  }
		}
	 *	@apiError (PreconditionFailed 412) {String}  messageType  Eg.ERROR
	 *	@apiError (PreconditionFailed 412) {String} errorCode	Eg. ERR_001
	 *	@apiError (PreconditionFailed 412) {String} message		Eg. Email Id is a required field
	 *	@apiError (PreconditionFailed 412) {Object}  exception  StackTrace
	 *	@apiError (BadRequest 400) {String}  messageType  Eg.ERROR
	 *	@apiError (BadRequest 400) {String} errorCode	Eg. ERR_002
	 *	@apiError (BadRequest 400) {String} message		Eg. User already exists
	 *	@apiError (BadRequest 400) {Object}  exception  StackTrace
	 */
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
