package com.bitlogic.sociallbox.service.controller.secured;

import java.util.List;

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
import com.bitlogic.sociallbox.data.model.EventType;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.UserSetting;
import com.bitlogic.sociallbox.data.model.UserTypeBasedOnDevice;
import com.bitlogic.sociallbox.data.model.response.EntityCollectionResponse;
import com.bitlogic.sociallbox.data.model.response.SingleEntityResponse;
import com.bitlogic.sociallbox.data.model.response.UserFriend;
import com.bitlogic.sociallbox.service.business.UserService;
import com.bitlogic.sociallbox.service.controller.BaseController;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.exception.ServiceException;

/**
 * TODO: For All methods, check additionally that one user does not end up updating other user
 * @author Ankit.Joinwal
 */
@RestController
@RequestMapping("/api/secured/users")
public class UserSecuredController extends BaseController implements Constants{

	private static final Logger logger = LoggerFactory.getLogger(UserSecuredController.class);
	
	private static final String REQUEST_SIGNUP_SIGNIN = "SigninOrSignupUser API";
	private static final String GET_USER_REQUEST = "GetUser API";
	private static final String GET_USER_EVENTS_INTEREST_REQUEST = "GetUserEventInterests API";
	private static final String SAVE_USER_EVENTS_INTEREST_REQUEST = "SaveUserEventInterests API";
	private static final String SETUP_FRIENDS_REQUEST = "SetupFriendsForNewUser API";
	private static final String GET_USER_FRIENDS_REQUEST = "GetUserFriends API";
	private static final String GET_USER_SETTINGS = "GetUserSetings api";
	private static final String SAVE_USER_SETTINGS_API = "SaveUserSetings API";
	
	@Autowired
	private UserService userService;
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	/**
	 *  @api {post} /api/secured/users Signup or Login User
	 *  @apiName Signup / Login API
	 *  @apiGroup Users
	 *  @apiHeader {String} accept application/json
	 *  @apiHeader {String} Content-Type application/json
	 *  @apiHeader {String} type M 
	 * 	@apiParamExample {json} Request-Example:
	 *     {
			  "name": "Vardhan Singh",
			  "emailId": "vsingh@gmail.com",
			  "password":"p@ssword",
			  "smartDevices": [
			    {
			      "uniqueId": "SMART_DEVICE_3",
			      "buildVersion": "1.00",
			      "osVersion": "4.0",
			      "deviceType": "ANDROID",
			      "gcmId":"GCM_ID2"
			    }
			  ],
			  "social_details": [
			    {
			      "system": "FACEBOOK",
			      "detail": "10204248372148573",
			      "detailType": "USER_EXTERNAL_ID"
			    },
			    {
			      "system": "FACEBOOK",
			      "detail": "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpf1/v/t1.0-1/p50x50/12316467_10206731945876364_3008257792416820623_n.jpg?oh=99ec98c9f38ab3ee4b05cad802c2e39e&oe=5725C669&__gda__=1466317967_b0131ab2472d9474fa9440cb7fe265bb",
			      "detailType": "USER_PROFILE_PIC"
			    }
			  ]
			}
	 *	@apiSuccess (Success - Created 201) {Object}  response  Response.
	 *  @apiSuccess (Success - Created 201) {String}  response.status   Eg.Success.
	 * 	@apiSuccess (Success - Created 201) {Object}  response.data User Profile Information.
	 *  @apiSuccessExample {json} Success-Response:
	 *  {
		    "status": "Success",
		    "data": {
		        "id": 2,
		        "name": "Vardhan Singh",
		        "emailId": "vsingh@gmail.com",
		        "smartDevices": [
		            {
		                "uniqueId": "SMART_DEVICE_3",
		                "buildVersion": "1.00",
		                "osVersion": "4.0",
		                "deviceType": "ANDROID",
		                "privateKey": "bbcd781d-5a7e-4023-97aa-0e9445e09789"
		            }
		        ],
		        "social_details": [
		            {
		                "system": "FACEBOOK",
		                "detail": "10204248372148573",
		                "detailType": "USER_EXTERNAL_ID"
		            },
				    {
				      "system": "FACEBOOK",
				      "detail": "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpf1/v/t1.0-1/p50x50/12316467_10206731945876364_3008257792416820623_n.jpg?oh=99ec98c9f38ab3ee4b05cad802c2e39e&oe=5725C669&__gda__=1466317967_b0131ab2472d9474fa9440cb7fe265bb",
				      "detailType": "USER_PROFILE_PIC"
				    }
		        ]
		    }
		}
	 *	@apiError (PreconditionFailed 412) {String}  messageType  Eg.ERROR
	 *	@apiError (PreconditionFailed 412) {String} errorCode	Eg. ERR_001
	 *	@apiError (PreconditionFailed 412) {String} message		Eg. Email Id is a required field
	 *	@apiError (PreconditionFailed 412) {Object}  exception  StackTrace
	 */
	@RequestMapping(method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public SingleEntityResponse<User> signinOrSignupUser(@RequestHeader(required = true, value = Constants.USER_TYPE_HEADER) String userType,
			@Valid @RequestBody User user) throws ServiceException{

		logRequestStart(REQUEST_SIGNUP_SIGNIN, PUBLIC_REQUEST_START_LOG, REQUEST_SIGNUP_SIGNIN);
		logInfo(REQUEST_SIGNUP_SIGNIN, "User Id = {}", user.getEmailId());

		logInfo(REQUEST_SIGNUP_SIGNIN, "User Type = {}", userType);
		UserTypeBasedOnDevice userTypeBasedOnDevice = null;
		
		if(userType.equals(UserTypeBasedOnDevice.MOBILE.toString())){
			userTypeBasedOnDevice = UserTypeBasedOnDevice.MOBILE;
		}else if (userType.equals(UserTypeBasedOnDevice.WEB.toString())){
			userTypeBasedOnDevice = UserTypeBasedOnDevice.WEB;
		}else{
			throw new ClientException(RestErrorCodes.ERR_001,ERROR_USER_TYPE_INVALID);
		}
		User createdUser = userService.signupOrSignin(user,userTypeBasedOnDevice);
		SingleEntityResponse<User> entityResponse = new SingleEntityResponse<>();
		entityResponse.setData(createdUser);
		entityResponse.setStatus(SUCCESS_STATUS);
		
		logRequestEnd(REQUEST_SIGNUP_SIGNIN, REQUEST_SIGNUP_SIGNIN);
		return entityResponse;
		
	}

	/**
	 *  @api {get} /api/secured/users/user/:id Get User Info
	 *  @apiName Get User Info
	 *  @apiGroup Users
	 *  @apiParam {Number} id Mandatory User id
	 *  @apiHeader {String} accept application/json
	 *  @apiHeader {String} Content-Type application/json
	 *  @apiHeader {Number} X-Auth-Date Current Epoch Date
	 *  @apiHeader {String} Authorization Authentication Token
	 *  @apiHeaderExample {json} Example Headers
	 *  accept: application/json
		Content-Type: application/json
		X-Auth-Date: 1455988523724
		Authorization: Basic U0R+U01BUlRfREVWSUNFXzI6NCtPU3JRN0tKMzZ2TW9iRmoxbmJEZG5ydVVJVTlwTWFVWmN1V0xxaUFaRT0=
	 *  @apiSuccess (Success - OK 200) {Object}  response  Response.
	 *  @apiSuccess (Success - OK 200) {String}  response.status   Eg.Success.
	 * 	@apiSuccess (Success - OK 200) {Object}  response.data User Profile Information.
	 *  @apiSuccessExample {json} Success-Response:
	 *  {
		    "status": "Success",
		    "data": {
		        "id": 1,
		        "name": "Ankit Joinwal",
		        "emailId": "ajoinwal@gmail.com",
		        "smartDevices": [
		            {
		                "uniqueId": "SMART_DEVICE_2",
		                "buildVersion": "1.00",
		                "osVersion": "4.0",
		                "deviceType": "ANDROID",
		                "privateKey": "2fc9d17b-a4b1-4b75-b3e3-9b75353a3286"
		            }
		        ],
		        "social_details": [
		            {
		                "system": "FACEBOOK",
		                "detail": "10204248372148573",
		                "detailType": "USER_EXTERNAL_ID"
		            },
				    {
				      "system": "FACEBOOK",
				      "detail": "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpf1/v/t1.0-1/p50x50/12316467_10206731945876364_3008257792416820623_n.jpg?oh=99ec98c9f38ab3ee4b05cad802c2e39e&oe=5725C669&__gda__=1466317967_b0131ab2472d9474fa9440cb7fe265bb",
				      "detailType": "USER_PROFILE_PIC"
				    }
		        ]
		    }
		}
	 *	@apiError (NotFound 404) {String}  messageType  Eg.ERROR
	 *	@apiError (NotFound 404) {String} errorCode	Eg. ERR_001
	 *	@apiError (NotFound 404) {String} message		Eg. User Not Found
	 *	@apiError (NotFound 404) {String} entityId		Entity Id which was searched
	 *	@apiError (NotFound 404) {Object}  exception  StackTrace
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<User> getUser(@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String auth,@PathVariable long id) {
		logRequestStart(GET_USER_REQUEST, SECURED_REQUEST_START_LOG_MESSAGE,GET_USER_REQUEST);
		logInfo(GET_USER_REQUEST, "Auth header = {}", auth);
		logInfo(GET_USER_REQUEST, "User id = {}", id);
		//TODO:Check if user can access other user's profile?
		User user = userService.getUser(id);
		SingleEntityResponse<User> entityResponse = new SingleEntityResponse<>();
		entityResponse.setData(user);
		entityResponse.setStatus(SUCCESS_STATUS);
		logRequestEnd(GET_USER_REQUEST, GET_USER_REQUEST);
		return entityResponse;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public List<User> getAllUsers() {
		logger.info("### Request recieved- Get All Users ###");
		List<User> users = userService.getAllUsers();
		return users;
	}
	
	@RequestMapping(value = "/{id}/preferences/interests", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<EventType> getUserEventInterests(
								@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String auth,
								@PathVariable Long id){
		logRequestStart(GET_USER_EVENTS_INTEREST_REQUEST, SECURED_REQUEST_START_LOG_MESSAGE, GET_USER_EVENTS_INTEREST_REQUEST);
		logInfo(GET_USER_EVENTS_INTEREST_REQUEST, "Auth header = {}", auth);
		logInfo(GET_USER_EVENTS_INTEREST_REQUEST, "User id = {}", id);
		List<EventType> userInterests = this.userService.getUserEventInterests(id);
		EntityCollectionResponse<EventType> collectionResponse = new EntityCollectionResponse<>();
		collectionResponse.setData(userInterests);
		collectionResponse.setPage(1);
		collectionResponse.setStatus("Success");
		logRequestEnd(GET_USER_EVENTS_INTEREST_REQUEST, GET_USER_EVENTS_INTEREST_REQUEST);
		return collectionResponse;
	}
	
	@RequestMapping(value = "/{id}/preferences/interests", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public EntityCollectionResponse<EventType> saveUserEventInterests(
			@RequestHeader(value=Constants.AUTHORIZATION_HEADER)String auth,
			@PathVariable Long id,@RequestBody List<EventType> types){
		logRequestStart(SAVE_USER_EVENTS_INTEREST_REQUEST, SECURED_REQUEST_START_LOG_MESSAGE, SAVE_USER_EVENTS_INTEREST_REQUEST);
		logInfo(SAVE_USER_EVENTS_INTEREST_REQUEST, "Auth Header = {}", auth);
		logInfo(SAVE_USER_EVENTS_INTEREST_REQUEST, "User id {}", id);
		List<EventType> eventTags = this.userService.saveUserEventInterests(id, types);
		EntityCollectionResponse<EventType> collectionResponse = new EntityCollectionResponse<>();
		collectionResponse.setData(eventTags);
		collectionResponse.setPage(1);
		collectionResponse.setStatus(SUCCESS_STATUS);
		logRequestEnd(SAVE_USER_EVENTS_INTEREST_REQUEST, SAVE_USER_EVENTS_INTEREST_REQUEST);
		return collectionResponse;
	}

	/**
	 *  @api {post} /api/secured/users/:id/friends Setup User friends
	 *  @apiName Setup User friends
	 *  @apiGroup Users
	 *  @apiHeader {String} accept application/json
	 *  @apiHeader {String} Content-Type application/json
	 *  @apiHeader {String} Authorization Authentication Token
	 * 	@apiParamExample {json} Request-Example:
	 *     JSON-Array of Friends Facebook IDs
	 *     [
			"10204248372148573",
			"4567"
			]
	 *	@apiSuccess (Success 201) {Object}  response  Response.
	 *  @apiSuccess (Success 201) {String}  response.status   Eg.Success.
	 * 	@apiSuccess (Success 201) {Object}  response.data User's Friends List Or Empty Array if no friends Found
	 *  @apiSuccessExample {json} Success-Response:
	 *  {
		    "status": "Success",
		    "data": [
		        {
		            "profilePic": "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpf1/v/t1.0-1/p50x50/12316467_10206731945876364_3008257792416820623_n.jpg?oh=99ec98c9f38ab3ee4b05cad802c2e39e&oe=5725C669&__gda__=1466317967_b0131ab2472d9474fa9440cb7fe265bb",
		            "name": "Vardhan Singh",
		            "emailId": "vsingh@gmail.com"
		        }
		    ],
		    "page": 1,
		    "nextPage": null
		}
	 *	@apiError (Unauthorizes 401) {String}  message  Eg.error.login.invalid.credentials
	 */
	@RequestMapping(value = "/{id}/friends", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE},consumes={MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.CREATED)
	public EntityCollectionResponse<UserFriend> setupFriendsForNewUser(
			@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String auth ,
			@PathVariable Long id,
			@RequestBody String[] friendsSocialIds){
		logRequestStart(SETUP_FRIENDS_REQUEST, SECURED_REQUEST_START_LOG_MESSAGE, SETUP_FRIENDS_REQUEST);
		logInfo(SETUP_FRIENDS_REQUEST, "Auth header = {}", auth);
		logInfo(SETUP_FRIENDS_REQUEST, "User id = {}", id);
		EntityCollectionResponse<UserFriend> collectionResponse = new EntityCollectionResponse<>();
		if(friendsSocialIds!=null && friendsSocialIds.length>0){
			List<UserFriend> userFriends = this.userService.setupUserFriendsForNewUser(id, friendsSocialIds);
			
			collectionResponse.setData(userFriends);
			collectionResponse.setPage(1);
			collectionResponse.setStatus("Success");
			
		}
		logRequestEnd(SETUP_FRIENDS_REQUEST, SETUP_FRIENDS_REQUEST);
		return collectionResponse;
	}
	
	/**
	 *  @api {get} /api/secured/users/:id/friends Get User friends
	 *  @apiName Get User friends
	 *  @apiGroup Users
	 *  @apiHeader {String} accept application/json
	 *  
	 *  @apiHeader {String} Authorization Authentication Token
	 *	@apiSuccess (Success 200) {Object}  response  Response.
	 *  @apiSuccess (Success 200) {String}  response.status   Eg.Success.
	 * 	@apiSuccess (Success 200) {Object}  response.data User's Friends List Or Empty Array if no friends Found
	 *  @apiSuccessExample {json} Success-Response:
	 *  {
		    "status": "Success",
		    "data": [
		        {
		            "profilePic": "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpf1/v/t1.0-1/p50x50/12316467_10206731945876364_3008257792416820623_n.jpg?oh=99ec98c9f38ab3ee4b05cad802c2e39e&oe=5725C669&__gda__=1466317967_b0131ab2472d9474fa9440cb7fe265bb",
		            "name": "Vardhan Singh",
		            "emailId": "vsingh@gmail.com"
		        }
		    ],
		    "page": 1,
		    "nextPage": null
		}
	 *	@apiError (Unauthorizes 401) {String}  message  Eg.error.login.invalid.credentials
	 */
	@RequestMapping(value = "/{id}/friends", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<UserFriend> getUserFriends(
			@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String auth,
			@PathVariable Long id){
		logRequestStart(GET_USER_FRIENDS_REQUEST, SECURED_REQUEST_START_LOG_MESSAGE, GET_USER_FRIENDS_REQUEST);
		logInfo(GET_USER_FRIENDS_REQUEST, "Auth header = {}", auth);
		logInfo(GET_USER_FRIENDS_REQUEST, "User id = {}", id);
		EntityCollectionResponse<UserFriend> collectionResponse = new EntityCollectionResponse<>();
		List<UserFriend> userFriends = userService.getUserFriends(id);
		collectionResponse.setData(userFriends);
		collectionResponse.setPage(1);
		collectionResponse.setStatus(SUCCESS_STATUS);
		logRequestEnd(GET_USER_FRIENDS_REQUEST, GET_USER_FRIENDS_REQUEST);
		return collectionResponse;
	}
	/**
	 *  @api {get} /api/secured/users/user/:id/settings Get User Settings
	 *  @apiName Get User Settings
	 *  @apiGroup Users
	 *  @apiParam {Number} id Mandatory User id
	 *  @apiHeader {String} accept application/json
	 *  @apiHeader {Number} X-Auth-Date Current Epoch Date
	 *  @apiHeader {String} Authorization Authentication Token
	 *  @apiSuccess (Success - OK 200) {Object}  response  Response.
	 *  @apiSuccess (Success - OK 200) {String}  response.status   Eg.Success.
	 * 	@apiSuccess (Success - OK 200) {Object}  response.data User Profile Information.
	 *  @apiSuccessExample {json} Success-Response:
	 *  {
		    "status": "Success",
		    "data": [
		        {
		            "id": 1,
		            "settingType": "PUSH_NOTIFICATION",
		            "name": "newFriendNot",
		            "displayName": "Notify me when my friend joins SociallBox",
		            "value": "ON"
		        },
		        {
		            "id": 2,
		            "settingType": "PUSH_NOTIFICATION",
		            "name": "meetupInvite",
		            "displayName": "Notify me when I'm invited for meetup",
		            "value": "ON"
		        }
		    ],
		    "page": 1,
		    "nextPage": null
		}
	 *	@apiError (NotFound 404) {String}  messageType  Eg.ERROR
	 *	@apiError (NotFound 404) {String} errorCode	Eg. ERR_001
	 *	@apiError (NotFound 404) {String} message		Eg. User Not Found
	 *	@apiError (NotFound 404) {String} entityId		Entity Id which was searched
	 *	@apiError (NotFound 404) {Object}  exception  StackTrace
	 */
	@RequestMapping(value = "/{id}/settings", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<UserSetting> getUserSetings(@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String auth ,@PathVariable Long id){
		logRequestStart(GET_USER_SETTINGS, SECURED_REQUEST_START_LOG_MESSAGE, GET_USER_SETTINGS);
		logInfo(GET_USER_SETTINGS, "Auth header = {}", auth);
		List<UserSetting> userSettings = this.userService.getUserSettings(id);
		EntityCollectionResponse<UserSetting> collectionResponse = new EntityCollectionResponse<>();
		collectionResponse.setData(userSettings);
		collectionResponse.setPage(1);
		collectionResponse.setStatus("Success");
		collectionResponse.setTotalRecords(userSettings.size());
		logRequestEnd(GET_USER_SETTINGS, GET_USER_SETTINGS);
		return  collectionResponse;
	}
	
	/**
	 *  @api {post} /api/secured/users/user/:id/settings Save User Settings
	 *  @apiName Save User Settings
	 *  @apiGroup Users
	 *  @apiParam {Number} id Mandatory User id
	 *  @apiHeader {String} accept application/json
	 *  @apiHeader {String} Content-Type application/json
	 *  @apiHeader {Number} X-Auth-Date Current Epoch Date
	 *  @apiHeader {String} Authorization Authentication Token
	 *  @apiSuccess (Success - OK 200) {Object}  response  Response.
	 *  @apiSuccess (Success - OK 200) {String}  response.status   Eg.Success.
	 * 	@apiSuccess (Success - OK 200) {Object}  response.data User Profile Information.
	 * 	@apiParamExample {json} Request-Example:
	 *  {
		     [
		        {
		            "id": 1,
		            "settingType": "PUSH_NOTIFICATION",
		            "name": "newFriendNot",
		            "displayName": "Notify me when my friend joins SociallBox",
		            "value": "OFF"
		        },
		        {
		            "id": 2,
		            "settingType": "PUSH_NOTIFICATION",
		            "name": "meetupInvite",
		            "displayName": "Notify me when I'm invited for meetup",
		            "value": "ON"
		        }
		    ]
	 *	@apiError (NotFound 404) {String}  messageType  Eg.ERROR
	 *	@apiError (NotFound 404) {String} errorCode	Eg. ERR_001
	 *	@apiError (NotFound 404) {String} message		Eg. User Not Found
	 *	@apiError (NotFound 404) {String} entityId		Entity Id which was searched
	 *	@apiError (NotFound 404) {Object}  exception  StackTrace
	 */
	@RequestMapping(value = "/{id}/settings", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE},consumes={MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<UserSetting> saveUserSetings(@RequestHeader(value=Constants.AUTHORIZATION_HEADER)String auth,@PathVariable Long id,@RequestBody List<UserSetting> settings){
		logRequestStart(SAVE_USER_SETTINGS_API, SECURED_REQUEST_START_LOG_MESSAGE, SAVE_USER_SETTINGS_API);
		logInfo(SAVE_USER_SETTINGS_API, "Auth header = {}", auth);
		logInfo(SAVE_USER_SETTINGS_API, "User id = {}", id);
		List<UserSetting> userSettings = this.userService.setUserSettings(id, settings);
		EntityCollectionResponse<UserSetting> collectionResponse = new EntityCollectionResponse<>();
		collectionResponse.setData(userSettings);
		collectionResponse.setPage(1);
		collectionResponse.setTotalRecords(userSettings.size());
		collectionResponse.setStatus("Success");
		logRequestEnd(SAVE_USER_SETTINGS_API, SAVE_USER_SETTINGS_API);
		
		return collectionResponse;
	}
	
}
