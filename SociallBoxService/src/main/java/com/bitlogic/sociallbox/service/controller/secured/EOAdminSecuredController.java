package com.bitlogic.sociallbox.service.controller.secured;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.EventStatus;
import com.bitlogic.sociallbox.data.model.UserMessage;
import com.bitlogic.sociallbox.data.model.UserTypeBasedOnDevice;
import com.bitlogic.sociallbox.data.model.requests.AddCompanyToProfileRequest;
import com.bitlogic.sociallbox.data.model.requests.UpdateEOAdminProfileRequest;
import com.bitlogic.sociallbox.data.model.response.EOAdminProfile;
import com.bitlogic.sociallbox.data.model.response.EODashboardResponse;
import com.bitlogic.sociallbox.data.model.response.EntityCollectionResponse;
import com.bitlogic.sociallbox.data.model.response.EventResponse;
import com.bitlogic.sociallbox.data.model.response.SingleEntityResponse;
import com.bitlogic.sociallbox.service.business.EOAdminService;
import com.bitlogic.sociallbox.service.business.UserService;
import com.bitlogic.sociallbox.service.controller.BaseController;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.utils.LoginUtil;

@RestController
@RequestMapping("/api/secured/users/organizers/admins")
public class EOAdminSecuredController extends BaseController implements Constants{

	private static final Logger LOGGER = LoggerFactory.getLogger(EOAdminSecuredController.class);
	private static final String ADD_COMPANY_TO_PROFILE = "AddCompanyToProfile API";
	private static final String GET_ORGANIZER_ADMIN_INFO_API = "GetOrganizerAdminProfile API";
	private static final String SIGNIN_ORGANIZER_ADMIN_API = "SigninOrganizerAdmin API";
	private static final String UPDATE_EO_ADMIN_PROFILE_API = "UpdateEOAdminProfile API";
	private static final String UPDATE_EO_ADMIN_PROFILE_PIC_API = "UpdateEOAdminProfilePic API";
	private static final String GET_USER_MESSAGES_API = "GetUserMessages API";
	private static final String MARK_USER_MESSAGE_AS_READ_API = "MarkMessageAsRead API";
	private static final String GET_ORGANIZER_EVENTS_API = "GetEventsForOrganizer API";
	
	@Autowired
	private EOAdminService eventOrganizerAdminService;
	
	@Autowired
	private UserService userService;
	
	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	
	/**
	 *  @api {post} /api/secured/users/organizers/admins/:userId/company Add Company Profile
	 *  @apiName Add Company Profile
	 *  @apiGroup Dashboard
	 *  @apiParam {Number} userId Mandatory User id
	 *  @apiHeader {String} accept application/json
	 *  @apiHeader {String} Content-Type Content-Type: application/json
	 *	@apiHeader {String} Authorization Auth Token 
	 *	@apiHeader {String} X-Auth-Date	Epoch time 
	 *  @apiHeaderExample {json} Example Headers
	 *  accept: application/json
		Content-Type: application/json
		X-Auth-Date: 1455988523724
		Authorization: Basic U0R+U01BUlRfREVWSUNFXzI6NCtPU3JRN0tKMzZ2TW9iRmoxbmJEZG5ydVVJVTlwTWFVWmN1V0xxaUFaRT0=
	 *
	 *  @apiParamExample {application/json} Request-Example:
	 *	{
    		"event_organizer":{
			  "name": "XYZ Entertainment",
			  "address": {
				"street": "Kalkaji",
				"city": "New Delhi",
				"state": "Delhi",
				"country": "India",
				"zip_code": "110019"
			  },
			  "phone1": "+91 7838250407",
			  "phone2": null,
			  "phone3": null,
			  "email_id": "xyz@gmail.com",
			  "website": "http://www.test.com"
			},
			"is_existing_company":false
		}
	 *	@apiSuccess (Success - Ok 200) {Object}  response  Response.
	 *  @apiSuccess (Success - Ok 200) {String}  response.status   Eg.Success.
	 * 	@apiSuccess (Success - Ok 200) {Object}  response.data Event Organizer Profile with company information included
	 *  @apiSuccessExample {json} Success-Response:
	 *  {
			"status": "Success",
			"data": {
				"id": 25,
				"name": "Dummy EO Admin",
				"social_details": [],
				"profile_id": 3,
				"profile_pic": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/users/25/16.png",
				"email_id": "dummy.eo@gmail.com",
				"company_profile": {
					"name": "XYZ Entertainment",
					"address": {
						"street": "Kalkaji",
						"city": "New Delhi",
						"state": "Delhi",
						"country": "India",
						"zip_code": "110019"
					},
					"phone1": "+91 7838250407",
					"phone2": "",
					"phone3": "",
					"id": "40289187542367fb015423685ee70000",
					"email_id": "xyz@gmail.com",
					"profile_pic": null,
					"cover_pic": null,
					"website": "http://www.test.com"
				},
				"status": "PENDING"
			}
		}
	
	 */
	@RequestMapping(value="/{userId}/company",method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE}, consumes = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.CREATED)
	public SingleEntityResponse<EOAdminProfile> addCompanyToProfile(@Valid @RequestBody AddCompanyToProfileRequest addCompanyRequest,
			@PathVariable Long userId,
			@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String auth){
		logRequestStart(ADD_COMPANY_TO_PROFILE, SECURED_REQUEST_START_LOG_MESSAGE, ADD_COMPANY_TO_PROFILE);
		logInfo(ADD_COMPANY_TO_PROFILE, "User Id = {}", userId);
		//TODO : Validate that auth token is for intended user only
		EOAdminProfile adminProfile = this.eventOrganizerAdminService.addCompany(addCompanyRequest, userId);
		
		SingleEntityResponse<EOAdminProfile> entityResponse = new SingleEntityResponse<EOAdminProfile>();
		entityResponse.setStatus(SUCCESS_STATUS);
		entityResponse.setData(adminProfile);
		logRequestEnd(ADD_COMPANY_TO_PROFILE, ADD_COMPANY_TO_PROFILE);
		return entityResponse;
	}
	
	/**
	 *  @api {post} /api/secured/users/organizers/admins/:userId/company/:companyId/picture?type=:pictureType Add Company Picture
	 *  @apiName Add Company Picture
	 *  @apiGroup Dashboard
	 *  @apiParam {Number} userId Mandatory User id
	 *  @apiParam {String} companyId Mandatory Organizer Id (aka Company id)
	 *  @apiParam {String} pictureType Mandatory Can be one of the two - profilePic or coverPic
	 *  @apiHeader {String} accept application/json
	 *  @apiHeader {String} Content-Type Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW
	 *	@apiHeader {String} Authorization Auth Token 
	 *	@apiHeader {String} X-Auth-Date	Epoch time 
	 *  @apiHeaderExample {json} Example Headers
	 *  accept: application/json
		Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW
		X-Auth-Date: 1455988523724
		Authorization: Basic U0R+U01BUlRfREVWSUNFXzI6NCtPU3JRN0tKMzZ2TW9iRmoxbmJEZG5ydVVJVTlwTWFVWmN1V0xxaUFaRT0=
	 *
	 *  @apiParamExample {multipart/form-data} Request-Example:
	 *	----WebKitFormBoundary7MA4YWxkTrZu0gW
		Content-Disposition: form-data; name="files"; filename=""
		Content-Type: 


		----WebKitFormBoundary7MA4YWxkTrZu0gW
	 *	@apiSuccess (Success - Ok 200) {Object}  response  Response.
	 *  @apiSuccess (Success - Ok 200) {String}  response.status   Eg.Success.
	 * 	@apiSuccess (Success - Ok 200) {Object}  response.data Image URL
	 *  @apiSuccessExample {json} Success-Response:
	 *  {
		  "status": "Success",
		  "data": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/companies/40289187542367fb015423685ee70000/adidas_234x60_1.jpg"
		}
	
	 */
	@RequestMapping(value="/{userId}/company/{orgId}/picture",method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.CREATED)
	public SingleEntityResponse<String> addCompanyPicture(
			@PathVariable Long userId,
			@PathVariable String orgId,
			@RequestParam(value="type",required=true) String type,
			@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String auth,
			MultipartHttpServletRequest request,
			HttpServletResponse response){
		logRequestStart(ADD_COMPANY_TO_PROFILE, SECURED_REQUEST_START_LOG_MESSAGE, ADD_COMPANY_TO_PROFILE);
		logInfo(ADD_COMPANY_TO_PROFILE, "User Id = {} , OrgId = {} , type = {}", userId,orgId,type);
		if("profilePic".equals(type) && "coverPic".equals(type)){
			throw new ClientException(RestErrorCodes.ERR_003, ERROR_INVALID_COMPANY_PIC_TYPE);
		}
		//TODO : Validate that auth token is for intended user only
		Map<String, MultipartFile> fileMap = request.getFileMap();
		SingleEntityResponse<String> entityResponse = new SingleEntityResponse<String>();
		if (fileMap.values() != null && !fileMap.values().isEmpty()) {
			List<MultipartFile> images = new ArrayList<MultipartFile>(fileMap.values());
			String imageUrl = this.eventOrganizerAdminService.updateCompanyPic(
					userId, orgId, images, type);
			entityResponse.setStatus(SUCCESS_STATUS);
			entityResponse.setData(imageUrl);
			logRequestEnd(ADD_COMPANY_TO_PROFILE, ADD_COMPANY_TO_PROFILE);
		}
		return entityResponse;
	}
	
	/**
	 *  @api {post} /api/secured/users/organizers/admins/:userId/profile Update Event Organizer Admin Profile
	 *  @apiName Update Event Organizer Admin Profile
	 *  @apiGroup Dashboard
	 *  @apiParam {Number} userId Mandatory User id
	 *  @apiHeader {String} accept application/json
	 *  @apiHeader {String} Content-Type application/json
	 *	@apiHeader {String} Authorization Auth Token 
	 *	@apiHeader {String} X-Auth-Date	Epoch time 
	 *  @apiHeaderExample {json} Example Headers
	 *  accept: application/json
		Content-Type: application/json
		X-Auth-Date: 1455988523724
		Authorization: Basic U0R+U01BUlRfREVWSUNFXzI6NCtPU3JRN0tKMzZ2TW9iRmoxbmJEZG5ydVVJVTlwTWFVWmN1V0xxaUFaRT0=
	 * 	@apiParamExample {json} Request-Example :
			Sample Request Body for Updating name :
			{
			  "name": "Dummy EO Admin"
			}
			Sample Request Body for updating password
			{
				"new_password": "pass123"
			}
			Sample Request Body for updating name & password
			{
				"name" : "Dummy EO Admin",
				"new_password": "pass123"
			}
	 *	@apiSuccess (Success - Ok 200) {Object}  response  Response.
	 *  @apiSuccess (Success - Ok 200) {String}  response.status   Eg.Success.
	 * 	@apiSuccess (Success - Ok 200) {Object}  response.data Updated User Profile Information.
	 *  @apiSuccessExample {json} Success-Response:
	 *  {
		  "status": "Success",
		  "data": {
			"id": 25,
			"name": "Dummy EO Admin",
			"social_details": [],
			"profile_id": null,
			"profile_pic": "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xla1/v/t1.0-1/c15.0.50.50/p50x50/1379841_10150004552801901_469209496895221757_n.jpg?oh=1ecfea0dda4f046e7d518ce2243c1a61&oe=57789C33&__gda__=1471731386_863525fcdf1cee40b0e9562b633a5197",
			"email_id": "dummy.eo@gmail.com",
			"company_profile": null,
			"status": "COMPANY_NOT_LINKED"
		  }
		}
	
	 */
	@RequestMapping(value="/{userId}/profile",method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public  SingleEntityResponse<EOAdminProfile> updateEOAdminProfile(@PathVariable Long userId,
			@Valid @RequestBody UpdateEOAdminProfileRequest updateProfileRequest){
		logInfo(UPDATE_EO_ADMIN_PROFILE_API, SECURED_REQUEST_START_LOG_MESSAGE, UPDATE_EO_ADMIN_PROFILE_API);
		updateProfileRequest.setUserId(userId);
		EOAdminProfile adminProfile = this.eventOrganizerAdminService.updateProfile(updateProfileRequest);
		SingleEntityResponse<EOAdminProfile> entityResponse = new SingleEntityResponse<EOAdminProfile>();
		entityResponse.setStatus(SUCCESS_STATUS);
		entityResponse.setData(adminProfile);
		logRequestEnd(UPDATE_EO_ADMIN_PROFILE_API, UPDATE_EO_ADMIN_PROFILE_API);
		return entityResponse;
		
	}
	
	
	/**
	 *  @api {post} /api/secured/users/organizers/admins/:userId/profile/picture Change Event Organizer Admin Profile Picture
	 *  @apiName Change Event Organizer Admin Profile Picture
	 *  @apiGroup Dashboard
	 *  @apiParam {Number} userId Mandatory User id
	 *  @apiHeader {String} accept application/json
	 *  @apiHeader {String} Content-Type Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW
	 *	@apiHeader {String} Authorization Auth Token 
	 *	@apiHeader {String} X-Auth-Date	Epoch time 
	 *  @apiHeaderExample {json} Example Headers
	 *  accept: application/json
		Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW
		X-Auth-Date: 1455988523724
		Authorization: Basic U0R+U01BUlRfREVWSUNFXzI6NCtPU3JRN0tKMzZ2TW9iRmoxbmJEZG5ydVVJVTlwTWFVWmN1V0xxaUFaRT0=
	 *
	 *  @apiParamExample {multipart/form-data} Request-Example:
	 *	----WebKitFormBoundary7MA4YWxkTrZu0gW
		Content-Disposition: form-data; name="files"; filename=""
		Content-Type: 


		----WebKitFormBoundary7MA4YWxkTrZu0gW
	 *	@apiSuccess (Success - Ok 200) {Object}  response  Response.
	 *  @apiSuccess (Success - Ok 200) {String}  response.status   Eg.Success.
	 * 	@apiSuccess (Success - Ok 200) {Object}  response.data Profile Image Url
	 *  @apiSuccessExample {json} Success-Response:
	 *  {
		  "status": "Success",
		  "data": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/users/25/16.png"
		}
	
	 */
	@RequestMapping(value = "/{userId}/profile/picture", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
    public SingleEntityResponse<String> upload(@PathVariable Long userId,
    				@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String authorization,
    				MultipartHttpServletRequest request,
    				HttpServletResponse response){
		logRequestStart(UPDATE_EO_ADMIN_PROFILE_PIC_API, SECURED_REQUEST_START_LOG_MESSAGE, UPDATE_EO_ADMIN_PROFILE_PIC_API);
		
		Map<String, MultipartFile> fileMap = request.getFileMap();
		SingleEntityResponse<String> entityResponse = new SingleEntityResponse<String>();
        if(fileMap.values()!=null && !fileMap.values().isEmpty()){
      	  List<MultipartFile> files = new ArrayList<MultipartFile>(fileMap.values());
      	  String profilePic = this.eventOrganizerAdminService.updateProfilePic(userId, files);
      	  
      	  entityResponse.setData(profilePic);
      	  entityResponse.setStatus(SUCCESS_STATUS);
      	  logRequestEnd(UPDATE_EO_ADMIN_PROFILE_PIC_API, UPDATE_EO_ADMIN_PROFILE_PIC_API);
        }
        return entityResponse;
	}
	
	/**
	 *  @api {get} /api/secured/users/organizers/admins/:userId/messages Get Unread Messages For EO Admin
	 *  @apiName Get Unread Messages For EO Admin
	 *  @apiGroup Dashboard
	 *  @apiParam {Number} userId Mandatory User id
	 *  @apiHeader {String} accept application/json
	 *	@apiHeader {String} Authorization Auth Token 
	 *	@apiHeader {String} X-Auth-Date	Epoch time 
	 *  @apiHeaderExample {json} Example Headers
	 *  accept: application/json
		X-Auth-Date: 1455988523724
		Authorization: Basic U0R+U01BUlRfREVWSUNFXzI6NCtPU3JRN0tKMzZ2TW9iRmoxbmJEZG5ydVVJVTlwTWFVWmN1V0xxaUFaRT0=
	 *
	 *  
	 *	@apiSuccess (Success - Ok 200) {Object}  response  Response.
	 *  @apiSuccess (Success - Ok 200) {String}  response.status   Eg.Success.
	 * 	@apiSuccess (Success - Ok 200) {Object}  response.data Messages For User
	 *  @apiSuccessExample {json} Success-Response:
	 *  {
			"status": "Success",
			"data": [
				{
					"id": 2,
					"message": "Hi Dummy EO, welcome to SociallBox. As a next step, please setup your company profile via \"Company Profile\" section in dashboard.",
					"message_dt": 1460880156000,
					"is_read": false
				}
			],
			"page": 1,
			"nextPage": null,
			"total_records": 1
		}
	
	 */
	@RequestMapping(value="/{userId}/messages",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<UserMessage> getMessagesForUser(@PathVariable Long userId){
		logRequestStart(GET_USER_MESSAGES_API, SECURED_REQUEST_START_LOG_MESSAGE, GET_USER_MESSAGES_API);
		List<UserMessage> messages = this.userService.getMessagesForUser(userId);
		
		EntityCollectionResponse<UserMessage> collectionResponse = new EntityCollectionResponse<UserMessage>();
		collectionResponse.setData(messages);
		collectionResponse.setStatus(SUCCESS_STATUS);
		collectionResponse.setPage(1);
		collectionResponse.setTotalRecords(messages==null?0 :messages.size());
		
		logRequestEnd(GET_USER_MESSAGES_API, GET_USER_MESSAGES_API);
		return collectionResponse;
	}
	
	/**
	 *  @api {post} /api/secured/users/organizers/admins/:userId/messages/:messageId Mark user message as read
	 *  @apiName Mark user message as read
	 *  @apiGroup Dashboard
	 *  @apiParam {Number} userId Mandatory User id
	 *  @apiParam {Number} messageId Mandatory Message id
	 *  @apiHeader {String} accept application/json
	 *	@apiHeader {String} Authorization Auth Token 
	 *	@apiHeader {String} X-Auth-Date	Epoch time 
	 *  @apiHeaderExample {json} Example Headers
	 *  accept: application/json
		X-Auth-Date: 1455988523724
		Authorization: Basic U0R+U01BUlRfREVWSUNFXzI6NCtPU3JRN0tKMzZ2TW9iRmoxbmJEZG5ydVVJVTlwTWFVWmN1V0xxaUFaRT0=
	 *
	 *  
	 *	@apiSuccess (Success - Ok 200) {Object}  response  Response.
	 *  @apiSuccess (Success - Ok 200) {String}  response.status   Eg.Success.
	 * 	@apiSuccess (Success - Ok 200) {Object}  response.data Result
	 *  @apiSuccessExample {json} Success-Response:
	 *  {
		  "status": "Success",
		  "data": "Message is marked as read"
		}
	
	 */
	@RequestMapping(value="/{userId}/messages/{messageId}",method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<String> markMessageAsRead(@PathVariable Long userId,@PathVariable Long messageId){
		logRequestStart(MARK_USER_MESSAGE_AS_READ_API, SECURED_REQUEST_START_LOG_MESSAGE, MARK_USER_MESSAGE_AS_READ_API);
		logInfo(MARK_USER_MESSAGE_AS_READ_API, "User id = {} Message id = {}", userId,messageId );
		
		String result = this.userService.markMessageAsRead(userId, messageId);
		
		SingleEntityResponse<String> entityResponse = new SingleEntityResponse<String>();
		entityResponse.setData(result);
		entityResponse.setStatus(SUCCESS_STATUS);
		
		logRequestEnd(MARK_USER_MESSAGE_AS_READ_API, MARK_USER_MESSAGE_AS_READ_API);
		
		return entityResponse;
		
	}
	
	@RequestMapping(value="/{profileId}",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<EOAdminProfile> getAdminProfile(@PathVariable Long profileId){
		logRequestStart(GET_ORGANIZER_ADMIN_INFO_API, SECURED_REQUEST_START_LOG_MESSAGE, GET_ORGANIZER_ADMIN_INFO_API);
		EOAdminProfile admin = this.eventOrganizerAdminService.getProfile(profileId);
		SingleEntityResponse<EOAdminProfile> entityResponse = new SingleEntityResponse<EOAdminProfile>();
		entityResponse.setStatus(SUCCESS_STATUS);
		entityResponse.setData(admin);
		logRequestEnd(GET_ORGANIZER_ADMIN_INFO_API, GET_ORGANIZER_ADMIN_INFO_API);
		return entityResponse;
	}
	
	/**
	 *  @api {post} /api/secured/users/organizers/admins/signin Signin Event Organizer Admin
	 *  @apiName Signin Event Organizer Admin
	 *  @apiGroup Dashboard
	 *  @apiHeader {String} accept application/json
	 *  @apiHeader {String} Content-Type application/json
	 *	@apiHeader {String} Authorization Auth Token 
	 *	@apiHeader {String} X-Auth-Date	Epoch time 
	 *  @apiHeaderExample {json} Example Headers
	 *  accept: application/json
		Content-Type: application/json
		X-Auth-Date: 1455988523724
		Authorization: Basic U0R+U01BUlRfREVWSUNFXzI6NCtPU3JRN0tKMzZ2TW9iRmoxbmJEZG5ydVVJVTlwTWFVWmN1V0xxaUFaRT0=
	 *	@apiSuccess (Success - Ok 200) {Object}  response  Response.
	 *  @apiSuccess (Success - Ok 200) {String}  response.status   Eg.Success.
	 * 	@apiSuccess (Success - Ok 200) {Object}  response.data User Profile Information.
	 *  @apiSuccessExample {json} Success-Response:
	 *  Sample Response when company is not linked to event organizer admin :
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
		
		Sample Response after company is linked with admin :
		{
		    "status": "Success",
		    "data": {
		        "id": 25,
		        "name": "Dummy EO Admin",
		        "social_details": [],
		        "profile_id": 3,
		        "profile_pic": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/users/25/16.png",
		        "email_id": "dummy.eo@gmail.com",
		        "company_profile": {
		            "name": "XYZ Entertainment",
		            "address": {
		                "street": "Kalkaji",
		                "city": "New Delhi",
		                "state": "Delhi",
		                "country": "India",
		                "zip_code": "110019"
		            },
		            "phone1": "+91 7838250407",
		            "phone2": "",
		            "phone3": "",
		            "id": "40289187542367fb015423685ee70000",
		            "email_id": "xyz@gmail.com",
		            "profile_pic": null,
		            "cover_pic": null,
		            "website": "http://www.test.com"
		        },
		        "status": "PENDING"
		    }
		} 
	 */
	@RequestMapping(value="/signin",method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE}, consumes = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<EOAdminProfile> signin(@RequestHeader(value=Constants.AUTHORIZATION_HEADER)String auth){
		logRequestStart(SIGNIN_ORGANIZER_ADMIN_API, SECURED_REQUEST_START_LOG_MESSAGE, SIGNIN_ORGANIZER_ADMIN_API);
		
		String userName = LoginUtil.getUserNameFromHeader(auth);
		UserTypeBasedOnDevice typeBasedOnDevice = LoginUtil.identifyUserType(userName);
		if(typeBasedOnDevice==UserTypeBasedOnDevice.WEB){
			String userEmail = LoginUtil.getUserEmailIdFromUserName(userName);
			EOAdminProfile adminProfile = this.eventOrganizerAdminService.signin(userEmail);
			SingleEntityResponse<EOAdminProfile> entityResponse = new SingleEntityResponse<EOAdminProfile>();
			entityResponse.setStatus(SUCCESS_STATUS);
			entityResponse.setData(adminProfile);
			logRequestEnd(SIGNIN_ORGANIZER_ADMIN_API, SIGNIN_ORGANIZER_ADMIN_API);
			return entityResponse;
		}else{
			logRequestEnd(SIGNIN_ORGANIZER_ADMIN_API, SIGNIN_ORGANIZER_ADMIN_API);
			throw new ClientException(RestErrorCodes.ERR_003,ERROR_FEATURE_AVAILABLE_TO_WEB_ONLY);
		}
		
	}
	
	@RequestMapping(value="/{userId}/dashboard",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<EODashboardResponse> getDashboardData(@PathVariable Long userId){
		
		EODashboardResponse dashboardResponse = this.eventOrganizerAdminService.getDashboardData(userId);
		SingleEntityResponse<EODashboardResponse> entityResponse = new SingleEntityResponse<>();
		entityResponse.setStatus(SUCCESS_STATUS);
		entityResponse.setData(dashboardResponse);
		
		return entityResponse;
	}
	
	@RequestMapping(value="/{userId}/dashboard/monthly/attendees",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<EODashboardResponse> getAttendeesData(@PathVariable Long userId){
		
		EODashboardResponse dashboardResponse = this.eventOrganizerAdminService.getAttendeesByMonth(userId);
		SingleEntityResponse<EODashboardResponse> entityResponse = new SingleEntityResponse<>();
		entityResponse.setStatus(SUCCESS_STATUS);
		entityResponse.setData(dashboardResponse);
		
		return entityResponse;
	}
	
	@RequestMapping(value="/{userId}/events",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<EventResponse> getUserEvents(@PathVariable Long userId ,
									@RequestParam(value="timeline",required=true)String timeline,
									@RequestParam(value="status",required=false) String status,
									@RequestParam(value="page",required=false) Integer page){
		logRequestStart(GET_ORGANIZER_EVENTS_API, SECURED_REQUEST_START_LOG_MESSAGE, GET_ORGANIZER_EVENTS_API);
		if(page==null){
			page = new Integer(1);
		}
		
		if(timeline.equalsIgnoreCase(TIMELINE_UPCOMING)){
			timeline = TIMELINE_UPCOMING;
		}else if(timeline.equalsIgnoreCase(TIMELINE_PAST)){
			timeline = TIMELINE_PAST;
		}else{
			throw new ClientException(RestErrorCodes.ERR_001, ERROR_INVALID_TIMELINE);
		}
		
		EventStatus eventStatus = null;
		if(status!=null){
			eventStatus = EventStatus.getStatusFromValue(status);
			if(eventStatus==null){
				throw new ClientException(RestErrorCodes.ERR_001, ERROR_INVALID_EVENT_STATUS);
			}
		}
		Map<String,?> resultsMap = this.eventOrganizerAdminService.getMyEvents(userId, timeline, eventStatus, page);
		
		
		EntityCollectionResponse<EventResponse> collectionResponse = new EntityCollectionResponse<>();
		collectionResponse.setData((List<EventResponse>)resultsMap.get("EVENTS"));
		collectionResponse.setStatus("Success");
		collectionResponse.setPage(page);
		collectionResponse.setTotalRecords((Integer)resultsMap.get("TOTAL_RECORDS"));
		logRequestEnd(GET_ORGANIZER_EVENTS_API, GET_ORGANIZER_ADMIN_INFO_API);
		return collectionResponse;
	}
	
}
