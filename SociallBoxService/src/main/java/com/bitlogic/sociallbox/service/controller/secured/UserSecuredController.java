package com.bitlogic.sociallbox.service.controller.secured;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import com.bitlogic.sociallbox.data.model.EventTag;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.UserSetting;
import com.bitlogic.sociallbox.data.model.UserTypeBasedOnDevice;
import com.bitlogic.sociallbox.data.model.response.EntityCollectionResponse;
import com.bitlogic.sociallbox.data.model.response.SingleEntityResponse;
import com.bitlogic.sociallbox.data.model.response.UserFriend;
import com.bitlogic.sociallbox.service.business.UserService;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.exception.ServiceException;

/**
 * TODO: For All methods, check additionally that one user does not end up updating other user
 * @author Ankit.Joinwal
 */
@RestController
@RequestMapping("/api/secured/users")
public class UserSecuredController implements Constants{

	private static final Logger logger = LoggerFactory.getLogger(UserSecuredController.class);
	
	@Autowired
	UserService userService;

	

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
			  "isEnabled":"true",
			  "password":"p@ssword",
			  "smartDevices": [
			    {
			      "uniqueId": "SMART_DEVICE_3",
			      "buildVersion": "1.00",
			      "osVersion": "4.0",
			      "deviceType": "ANDROID",
			      "isEnabled":"true",
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
			@Valid @RequestBody User user, HttpServletRequest  request) throws ServiceException{
		
		logger.info("### Request recieved- signinOrSignupUser. Arguments : {} ###"+user);
		logger.info("   Social Details : {} ",user.getSocialDetails());
		
		logger.info("	User Type : "+userType);
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
		logger.info("### Signup/Signin successfull for user : {} ",user.getEmailId());
		
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
	public SingleEntityResponse<User> getUser(@PathVariable long id) {
		logger.debug("### Inside getUser method.Arguments {} ###",id);
		//TODO:Check if user can access other user's profile?
		User user = userService.getUser(id);
		SingleEntityResponse<User> entityResponse = new SingleEntityResponse<>();
		entityResponse.setData(user);
		entityResponse.setStatus(SUCCESS_STATUS);
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
	
	@RequestMapping(value = "/{id}/preferences/tags", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<EventTag> getUserTagPreferences(@PathVariable Long id){
		logger.info("### Request recieved- getUserTagPreferences ###");
		List<EventTag> eventTags = this.userService.getUserTagPreferences(id);
		EntityCollectionResponse<EventTag> collectionResponse = new EntityCollectionResponse<>();
		collectionResponse.setData(eventTags);
		collectionResponse.setPage(1);
		collectionResponse.setStatus("Success");
		return collectionResponse;
	}
	
	@RequestMapping(value = "/{id}/preferences/tags", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public EntityCollectionResponse<EventTag> saveUserTagPreferences(@PathVariable Long id,@RequestBody List<EventTag> tags){
		logger.info("### Request recieved- saveUserTagPreferences ###");
		List<EventTag> eventTags = this.userService.saveUserTagPreferences(id, tags);
		EntityCollectionResponse<EventTag> collectionResponse = new EntityCollectionResponse<>();
		collectionResponse.setData(eventTags);
		collectionResponse.setPage(1);
		collectionResponse.setStatus("Success");
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
	public EntityCollectionResponse<UserFriend> setupFriendsForNewUser(@PathVariable Long id,
			@RequestBody String[] friendsSocialIds){
		logger.info("### Request recieved- setupFriendsForNewUser for user {} ###",id);
		EntityCollectionResponse<UserFriend> collectionResponse = new EntityCollectionResponse<>();
		if(friendsSocialIds!=null && friendsSocialIds.length>0){
			List<UserFriend> userFriends = this.userService.setupUserFriendsForNewUser(id, friendsSocialIds);
			
			collectionResponse.setData(userFriends);
			collectionResponse.setPage(1);
			collectionResponse.setStatus("Success");
			
		}
		return collectionResponse;
	}
	
	/**
	 *  @api {get} /api/secured/users/:id/friends Get User friends
	 *  @apiName Get User friends
	 *  @apiGroup Users
	 *  @apiHeader {String} accept application/json
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
	public EntityCollectionResponse<UserFriend> getUserFriends(@PathVariable Long id){
		logger.info("### Request recieved- getUserFriends for user {} ###",id);
		EntityCollectionResponse<UserFriend> collectionResponse = new EntityCollectionResponse<>();
		List<UserFriend> userFriends = userService.getUserFriends(id);
		collectionResponse.setData(userFriends);
		collectionResponse.setPage(1);
		collectionResponse.setStatus(SUCCESS_STATUS);
		
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
	public EntityCollectionResponse<UserSetting> getUserSetings(@PathVariable Long id){
		logger.info("### Request recieved- getUserSetings for user {} ###",id);
		List<UserSetting> userSettings = this.userService.getUserSettings(id);
		EntityCollectionResponse<UserSetting> collectionResponse = new EntityCollectionResponse<>();
		collectionResponse.setData(userSettings);
		collectionResponse.setPage(1);
		collectionResponse.setStatus("Success");
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
	public void saveUserSetings(@PathVariable Long id,@RequestBody List<UserSetting> settings){
		logger.info("### Request recieved- saveUserSetings for user {} ###",id);
		this.userService.setUserSettings(id, settings);
	}
	
}
