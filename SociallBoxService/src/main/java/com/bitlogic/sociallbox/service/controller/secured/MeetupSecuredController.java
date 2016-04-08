package com.bitlogic.sociallbox.service.controller.secured;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import com.bitlogic.sociallbox.data.model.MeetupAttendee;
import com.bitlogic.sociallbox.data.model.MeetupImage;
import com.bitlogic.sociallbox.data.model.MeetupMessage;
import com.bitlogic.sociallbox.data.model.UserTypeBasedOnDevice;
import com.bitlogic.sociallbox.data.model.requests.AddMeetupAttendeesRequest;
import com.bitlogic.sociallbox.data.model.requests.CreateMeetupRequest;
import com.bitlogic.sociallbox.data.model.requests.EditMeetupRequest;
import com.bitlogic.sociallbox.data.model.requests.MeetupResponse;
import com.bitlogic.sociallbox.data.model.requests.SaveAttendeeResponse;
import com.bitlogic.sociallbox.data.model.response.EntityCollectionResponse;
import com.bitlogic.sociallbox.data.model.response.SingleEntityResponse;
import com.bitlogic.sociallbox.service.business.MeetupService;
import com.bitlogic.sociallbox.service.controller.BaseController;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.exception.ServiceException;
import com.bitlogic.sociallbox.service.exception.UnauthorizedException;
import com.bitlogic.sociallbox.service.transformers.MeetupTransformer;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory.TransformerTypes;
import com.bitlogic.sociallbox.service.utils.LoginUtil;

@RestController
@RequestMapping("/api/secured/meetups")
public class MeetupSecuredController extends BaseController implements Constants{

	private static final Logger logger = LoggerFactory.getLogger(MeetupSecuredController.class);
	private static final String CREATE_MEETUP_API = "CreateMeetup API";
	private static final String UPDATE_MEETUP_API = "EditMeetup API";
	private static final String UPLOAD_MEETUP_IMAGE_API = "UploadMeetupImage API";
	private static final String GET_MEETUP_API = "GetMeetup API";
	private static final String GET_MEETUP_IMAGES_API = "GetMeetupImages API";
	private static final String ADD_ATTENDEES_API = "AddAttendees API";
	private static final String SAVE_ATTENDEE_RESPONSE_API = "SaveResponse API";
	private static final String POST_MESSAGE_TO_MEETUP_API = "PostMessageToMeetup API";
	private static final String GET_MEETUP_MESSAGES_API = "GetMeetupMessages API";
	private static final String CANCEL_MEETUP_API = "CancelMeetup API";
	
	@Override
	public Logger getLogger() {
		return logger;
	}
	
	@Autowired
	private MeetupService meetupService;
	
	/**
	*  @api {post} /api/secured/meetups Create Meetup
	*  @apiName Create Meetup
	*  @apiGroup Meetups
	*  @apiHeader {String} accept application/json
	*  @apiHeader {String} accept application/json
	*  @apiHeader {String} Content-Type application/json
	*  @apiHeader {Number} X-Auth-Date Current Epoch Date
	*  @apiHeader {String} Authorization Authentication Token
	*  @apiParamExample {json} Request-Example:
	*	{
			"title" : "College Fest Meetup",
			"description" : "IP University Fest",
			"is_private":"true",
			"location" : {
							"name": "Kalkaji, New Delhi, Delhi, India",
							"longitude": 28.4682917,
							"lattitude": 77.06347870000002,
							"locality" : "Kalkaji"
						},
			
			"start_date" : "01/07/2016 12:30 AM",
			"end_date" : "01/08/2016 12:30 PM"
		}
	*  @apiSuccess (Success - OK 201) {Object}  response  Response.
	*  @apiSuccess (Success - OK 201) {String}  response.status   Eg.Success.
	*  @apiSuccess (Success - OK 201) {Object}  response.data Meetup Details 
	*  @apiSuccessExample {json} Success-Response:
	*  	{
		  "status": "Success",
		  "data": {
		    "description": "IP University Fest",
		    "id": "4028918a53f1e9590153f1ea1db30000",
		    "title": "College Fest Meetup",
		    "location": {
		      "lattitude": 77.06347870000002,
		      "longitude": 28.4682917,
		      "name": "Kalkaji, New Delhi, Delhi, India",
		      "locality": "Kalkaji"
		    },
		    "start_date": "Fri, 1 Jul 2016 12:30 AM",
		    "end_date": "Mon, 1 Aug 2016 12:30 PM",
		    "organizer": {
		      "id": 17,
		      "name": "Ankit Joinwal",
		      "social_details": [
		        {
		          "system": "FACEBOOK",
		          "detail": "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xla1/v/t1.0-1/p200x200/12316467_10206731945876364_3008257792416820623_n.jpg?oh=cece300cd2db2d885c81f2c00b6a7d84&oe=578A9FB4&__gda__=1465178287_36f0dafbe70beb7506ebd22f8f089edf",
		          "detailType": "USER_PROFILE_PIC"
		        }
		      ]
		    },
		    "status": "CREATED",
		    "meetup_access_url": "http://localhost:8080/SociallBox/api/secured/meetups/4028918a53f1e9590153f1ea1db30000",
		    "display_pic": null,
		    "event_at_meetup": null,
		    "user_actions": [
		      {
		        "value": true,
		        "action_type": "CAN_VIEW"
		      },
		      {
		        "value": true,
		        "action_type": "CAN_EDIT"
		      },
		      {
		        "value": true,
		        "action_type": "CAN_INVITE"
		      },
		      {
		        "value": true,
		        "action_type": "CAN_UPLOAD_IMAGE"
		      },
		      {
		        "value": true,
		        "action_type": "CAN_MESSAGE"
		      },
		      {
		        "value": true,
		        "action_type": "CAN_CANCEL"
		      }
		    ]
		  }
		}
		
	*/
	@RequestMapping(method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public SingleEntityResponse<MeetupResponse> create(@RequestHeader(value=Constants.AUTHORIZATION_HEADER)String auth,@Valid @RequestBody CreateMeetupRequest createMeetupRequest,HttpServletRequest  httpRequest){
		logRequestStart(CREATE_MEETUP_API, SECURED_REQUEST_START_LOG_MESSAGE, CREATE_MEETUP_API);
		logInfo(CREATE_MEETUP_API, "Auth header = {}", auth);
		logInfo(CREATE_MEETUP_API, "Request = {}", createMeetupRequest);
		String userName = LoginUtil.getUserNameFromHeader(auth);
		UserTypeBasedOnDevice typeBasedOnDevice = LoginUtil.identifyUserType(userName);
		if(typeBasedOnDevice==UserTypeBasedOnDevice.MOBILE){
			String deviceId = LoginUtil.getDeviceIdFromUserName(userName);
			logInfo(CREATE_MEETUP_API, " Device Id {} ", deviceId);
			createMeetupRequest.setDeviceId(deviceId);
			MeetupResponse createMeetupResponse = this.meetupService.createMetup(createMeetupRequest);
			createMeetupResponse.setUrl(httpRequest.getRequestURL()+"/"+createMeetupResponse.getUuid());
			SingleEntityResponse<MeetupResponse> entityResponse = new SingleEntityResponse<>();
			entityResponse.setData(createMeetupResponse);
			entityResponse.setStatus(SUCCESS_STATUS);
			logRequestEnd(CREATE_MEETUP_API, CREATE_MEETUP_API);
			return entityResponse;
			
		}else{
			logRequestEnd(CREATE_MEETUP_API, CREATE_MEETUP_API);
			throw new ClientException(RestErrorCodes.ERR_003,ERROR_FEATURE_AVAILABLE_TO_MOBILE_ONLY);
		}
		
		
	}
	
	/**
	*  @api {post} /api/secured/meetups/:meetupId Edit Meetup
	*  @apiName Edit Meetup
	*  @apiGroup Meetups
	*  @apiParam {String} meetupId Mandatory Meetup Id
	*  @apiHeader {String} accept application/json
	*  @apiHeader {String} Content-Type application/json
	*  @apiHeader {Number} X-Auth-Date Current Epoch Date
	*  @apiHeader {String} Authorization Authentication Token
	*  @apiParamExample {json} Request-Example:
	*	{
			"title" : "College Fest Meetup with friends",
			"description" : "IP University Fest",
			"is_private":"true",
			"location" : {
							"name": "Kalkaji, New Delhi, Delhi, India",
							"longitude": 28.4682917,
							"lattitude": 77.06347870000002,
							"locality" : "Kalkaji"
						},

			"start_date" : "01/07/2016 12:30 AM",
			"end_date" : "01/08/2016 12:30 PM"
		}
	*  @apiSuccess (Success - OK 200) {Object}  response  Response.
	*  @apiSuccess (Success - OK 200) {String}  response.status   Eg.Success.
	*  @apiSuccess (Success - OK 200) {Object}  response.data Meetup Details after updation
	*  @apiSuccessExample {json} Success-Response:
	*  	{
			"status": "Success",
			"data": {
				"description": "IP University Fest",
				"id": "4028918a53f1e9590153f1ea1db30000",
				"title": "College Fest Meetup with friends",
				"location": {
					"lattitude": 77.06347870000002,
					"longitude": 28.4682917,
					"name": "Kalkaji, New Delhi, Delhi, India",
					"locality": "Kalkaji"
				},
				"start_date": "Fri, 1 Jul 2016 12:30 AM",
				"end_date": "Mon, 1 Aug 2016 12:30 PM",
				"organizer": {
					"id": 17,
					"name": "Ankit Joinwal",
					"social_details": [
						{
							"system": "FACEBOOK",
							"detail": "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xla1/v/t1.0-1/p200x200/12316467_10206731945876364_3008257792416820623_n.jpg?oh=cece300cd2db2d885c81f2c00b6a7d84&oe=578A9FB4&__gda__=1465178287_36f0dafbe70beb7506ebd22f8f089edf",
							"detailType": "USER_PROFILE_PIC"
						}
					]
				},
				"status": "CREATED",
				"meetup_access_url": "http://localhost:8080/SociallBox/api/secured/meetups/4028918a53f1e9590153f1ea1db30000/4028918a53f1e9590153f1ea1db30000",
				"display_pic": {
					"id": 6,
					"name": "smash.jpg",
					"url": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/meetups/4028918a53f1e9590153f1ea1db30000/smash.jpg",
					"mimeType": null,
					"isDisplayImage": true,
					"uploadDt": 1460055659000
				},
				"event_at_meetup": null,
				"user_actions": []
			}
		}
		
	*/
	@RequestMapping(value="/{meetupId}",method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<MeetupResponse> update(@RequestHeader(value=Constants.AUTHORIZATION_HEADER)String auth,
						@PathVariable String meetupId,
						@Valid @RequestBody EditMeetupRequest editMeetupRequest,
						HttpServletRequest  httpRequest){
		logRequestStart(UPDATE_MEETUP_API, SECURED_REQUEST_START_LOG_MESSAGE, UPDATE_MEETUP_API);
		logInfo(UPDATE_MEETUP_API, "Auth header = {}", auth);
		logInfo(UPDATE_MEETUP_API, "Edit Meetup Request = {}", editMeetupRequest);
		String userName = LoginUtil.getUserNameFromHeader(auth);
		UserTypeBasedOnDevice typeBasedOnDevice = LoginUtil.identifyUserType(userName);
		if(typeBasedOnDevice==UserTypeBasedOnDevice.MOBILE){
			String deviceId = LoginUtil.getDeviceIdFromUserName(userName);
			logInfo(UPDATE_MEETUP_API, " Device Id {} ", deviceId);
			editMeetupRequest.setDeviceId(deviceId);
			editMeetupRequest.setMeetupId(meetupId);
			MeetupTransformer transformer = (MeetupTransformer) TransformerFactory.getTransformer(TransformerTypes.MEETUP_TRANS);
			MeetupResponse editMeetupResponse = transformer.transform(meetupService.editMeetup(editMeetupRequest));
			editMeetupResponse.setUrl(httpRequest.getRequestURL()+"/"+editMeetupResponse.getUuid());
			SingleEntityResponse<MeetupResponse> entityResponse = new SingleEntityResponse<>();
			entityResponse.setData(editMeetupResponse);
			entityResponse.setStatus(SUCCESS_STATUS);
			logRequestEnd(UPDATE_MEETUP_API, UPDATE_MEETUP_API);
			return entityResponse;
		}else{
			logRequestEnd(UPDATE_MEETUP_API, UPDATE_MEETUP_API);
			throw new ClientException(RestErrorCodes.ERR_003,ERROR_FEATURE_AVAILABLE_TO_MOBILE_ONLY);
		}
		
	}
	
	/**
	 *  @api {delete} /api/secured/meetups/:meetupId Cancel Meetup
	 *  @apiName Cancel Meetup
	 *  @apiGroup Meetups
	 *  @apiParam {String} meetupId Mandatory Meetup Id
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
	 * 	@apiSuccess (Success - OK 200) {Object}  response.data Success Message
	 *  @apiSuccessExample {json} Success-Response:
	 *  
		{
		  "status": "Success",
		  "data": "Meetup is cancelled successfully"
		}
	 */
	@RequestMapping(value="/{meetupId}",method = RequestMethod.DELETE, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<String> cancelMeetup(@PathVariable String meetupId,
    				@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String authorization){
		logRequestStart(CANCEL_MEETUP_API, SECURED_REQUEST_START_LOG_MESSAGE, CANCEL_MEETUP_API);
		logInfo(CANCEL_MEETUP_API, "Cancel Meetup Request recieved for Meetup Id = {}",meetupId );
		
		String userName = LoginUtil.getUserNameFromHeader(authorization);
		UserTypeBasedOnDevice typeBasedOnDevice = LoginUtil.identifyUserType(userName);
		if(typeBasedOnDevice==UserTypeBasedOnDevice.MOBILE){
			String deviceId = LoginUtil.getDeviceIdFromUserName(userName);
			this.meetupService.cancelMeetup(deviceId, meetupId);
	        SingleEntityResponse<String> entityResponse = new SingleEntityResponse<String>();
			entityResponse.setStatus(SUCCESS_STATUS);
			entityResponse.setData("Meetup is cancelled successfully");
			logRequestEnd(CANCEL_MEETUP_API, CANCEL_MEETUP_API);
			return entityResponse;
		}else{
			throw new ClientException(RestErrorCodes.ERR_003,ERROR_FEATURE_AVAILABLE_TO_MOBILE_ONLY);
		}
		
	}
	
	/**
	*  @api {post} /api/secured/meetups/:meetupId/images?dp=true/false Upload image to meetup
	*  @apiName Upload image to meetup
	*  @apiGroup Meetups
	*  @apiParam {String} meetupId Mandatory Meetup Id
	*  @apiParam {Boolean} dp Optional Whether image being uploaded is Display pic or not
	*  @apiHeader {String} accept application/json
	*  @apiHeader {String} Content-Type multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW
	*  @apiHeader {Number} X-Auth-Date Current Epoch Date
	*  @apiHeader {String} Authorization Authentication Token
	*  @apiParamExample {multipart/form-data} Request-Example:
	*	----WebKitFormBoundary7MA4YWxkTrZu0gW
		Content-Disposition: form-data; name="files"; filename=""
		Content-Type: 


		----WebKitFormBoundary7MA4YWxkTrZu0gW
	*  @apiSuccess (Success - OK 201) {Object}  response  Response.
	*  @apiSuccess (Success - OK 201) {String}  response.status   Eg.Success.
	*  @apiSuccess (Success - OK 201) {Object}  response.data Image Details
	*  @apiSuccessExample {json} Success-Response:
	*  	{
		  "status": "Success",
		  "data": [
			{
			  "id": 5,
			  "name": "smash.jpg",
			  "url": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/meetups/4028918a53f1b5390153f1b561240000/smash.jpg",
			  "mimeType": null,
			  "isDisplayImage": true,
			  "uploadDt": 1460051103699
			}
		  ],
		  "page": null,
		  "nextPage": null,
		  "total_records": 1
		}
		
	*/
	@RequestMapping(value = "/{meetupId}/images", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
    public EntityCollectionResponse<MeetupImage> upload(@PathVariable String meetupId,
    				@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String authorization,
    				@RequestParam(value="dp",required=false) Boolean isDp,
    				MultipartHttpServletRequest request,
    				HttpServletResponse response){
		
		logRequestStart(UPLOAD_MEETUP_IMAGE_API, SECURED_REQUEST_START_LOG_MESSAGE, UPLOAD_MEETUP_IMAGE_API);
		logInfo(UPLOAD_MEETUP_IMAGE_API, "Auth header = {}", authorization);
		if(authorization==null){
			throw new UnauthorizedException(RestErrorCodes.ERR_002,ERROR_USER_INVALID);
		}
		logInfo(UPLOAD_MEETUP_IMAGE_API, "Image recieved for Meetup id = {} , isDisplayPic = {}", meetupId,isDp);
		String userName = LoginUtil.getUserNameFromHeader(authorization);
		UserTypeBasedOnDevice typeBasedOnDevice = LoginUtil.identifyUserType(userName);
		if(typeBasedOnDevice==UserTypeBasedOnDevice.MOBILE){
			String deviceId = LoginUtil.getDeviceIdFromUserName(userName);
			String imagesURL = request.getRequestURL()+"";
			Map<String, MultipartFile> fileMap = request.getFileMap();
			EntityCollectionResponse<MeetupImage> collectionResponse = new EntityCollectionResponse<MeetupImage>();
	        if(fileMap.values()!=null && !fileMap.values().isEmpty()){
	      	  List<MultipartFile> files = new ArrayList<MultipartFile>(fileMap.values());
	      	  List<MeetupImage> images = this.meetupService.uploadImageToMeetup(isDp,deviceId,imagesURL,files, meetupId);
	      	  
	      	  collectionResponse.setData(images);
	      	  collectionResponse.setStatus(SUCCESS_STATUS);
	      	  collectionResponse.setTotalRecords(images.size());
	      	  logRequestEnd(UPLOAD_MEETUP_IMAGE_API, UPLOAD_MEETUP_IMAGE_API);
	        }
	        return collectionResponse;
		}else{
			throw new ClientException(RestErrorCodes.ERR_003,ERROR_FEATURE_AVAILABLE_TO_MOBILE_ONLY);
		}
		
		
	}
	
	/**
	 *  @api {get} /api/secured/meetups/:meetupId/images Get Meetup Photos
	 *  @apiName Get Meetup Photos
	 *  @apiGroup Meetups
	 *  @apiParam {String} meetupId Mandatory Meetup Id
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
	 * 	@apiSuccess (Success - OK 200) {Object}  response.data Meetup Photos
	 *  @apiSuccessExample {json} Success-Response:
	 *  
		{
			"status": "Success",
			"data": [
				{
					"id": 4,
					"name": "wow.jpg",
					"url": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/meetups/4028918a53f1b5390153f1b561240000/wow.jpg",
					"mimeType": null,
					"isDisplayImage": true,
					"uploadDt": 1460050423000
				},
				{
					"id": 5,
					"name": "smash.jpg",
					"url": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/meetups/4028918a53f1b5390153f1b561240000/smash.jpg",
					"mimeType": null,
					"isDisplayImage": true,
					"uploadDt": 1460051103000
				}
			],
			"page": 1,
			"nextPage": null,
			"total_records": null
		}
	 */
	@RequestMapping(value="/{meetupId}/images",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<MeetupImage> getMeetupImages(@PathVariable String meetupId,
			@RequestHeader(value=Constants.AUTHORIZATION_HEADER)String auth){
		logRequestStart(GET_MEETUP_IMAGES_API, SECURED_REQUEST_START_LOG_MESSAGE, GET_MEETUP_IMAGES_API);
		logInfo(GET_MEETUP_IMAGES_API, "Auth header = {}", auth);
		logInfo(GET_MEETUP_IMAGES_API, "Meetup id = {}", meetupId);
		String userName = LoginUtil.getUserNameFromHeader(auth);
		UserTypeBasedOnDevice typeBasedOnDevice = LoginUtil.identifyUserType(userName);
		if(typeBasedOnDevice==UserTypeBasedOnDevice.MOBILE){
			String deviceId = LoginUtil.getDeviceIdFromUserName(userName);
			List<MeetupImage> images = this.meetupService.getMeetupImages(deviceId, meetupId);
			EntityCollectionResponse<MeetupImage> collectionResponse = new EntityCollectionResponse<MeetupImage>();
			collectionResponse.setStatus(SUCCESS_STATUS);
			collectionResponse.setData(images);
			collectionResponse.setPage(1);
			return collectionResponse;
		}else{
			throw new ClientException(RestErrorCodes.ERR_003,ERROR_FEATURE_AVAILABLE_TO_MOBILE_ONLY);
		}
		
	}
	
	/**
	 *  @api {get} /api/secured/meetups/:meetupId Get Meetup Details
	 *  @apiName Get Meetup Details
	 *  @apiGroup Meetups
	 *  @apiParam {String} meetupId Mandatory Meetup Id
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
	 * 	@apiSuccess (Success - OK 200) {Object}  response.data Meetup Details
	 *  @apiSuccessExample {json} Success-Response:
	 *  Sample Response For Meetup Organizer :
	 *  {
			"status": "Success",
			"data": {
				"description": "IP University Fest",
				"id": "4028918a53f1e9590153f1ea1db30000",
				"title": "College Fest Meetup",
				"location": {
					"lattitude": 77.06347870000002,
					"longitude": 28.4682917,
					"name": "Kalkaji, New Delhi, Delhi, India",
					"locality": "Kalkaji"
				},
				"start_date": "Fri, 1 Jul 2016 12:30 AM",
				"end_date": "Mon, 1 Aug 2016 12:30 PM",
				"organizer": {
					"id": 17,
					"name": "Ankit Joinwal",
					"social_details": [
						{
							"system": "FACEBOOK",
							"detail": "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xla1/v/t1.0-1/p200x200/12316467_10206731945876364_3008257792416820623_n.jpg?oh=cece300cd2db2d885c81f2c00b6a7d84&oe=578A9FB4&__gda__=1465178287_36f0dafbe70beb7506ebd22f8f089edf",
							"detailType": "USER_PROFILE_PIC"
						}
					]
				},
				"status": "CREATED",
				"meetup_access_url": null,
				"display_pic": {
					"id": 6,
					"name": "smash.jpg",
					"url": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/meetups/4028918a53f1e9590153f1ea1db30000/smash.jpg",
					"mimeType": null,
					"isDisplayImage": true,
					"uploadDt": 1460055659000
				},
				"event_at_meetup": null,
				"user_actions": [
					{
						"value": true,
						"action_type": "CAN_VIEW"
					},
					{
						"value": true,
						"action_type": "CAN_EDIT"
					},
					{
						"value": true,
						"action_type": "CAN_INVITE"
					},
					{
						"value": true,
						"action_type": "CAN_UPLOAD_IMAGE"
					},
					{
						"value": true,
						"action_type": "CAN_MESSAGE"
					},
					{
						"value": true,
						"action_type": "CAN_CANCEL"
					}
				]
			}
		}
	 Sample Response for Normal Attendees of Meetup:
		{
		    "status": "Success",
		    "data": {
		        "description": "IP University Fest",
		        "id": "4028918a53f1e9590153f1ea1db30000",
		        "title": "College Fest Meetup",
		        "location": {
		            "lattitude": 77.06347870000002,
		            "longitude": 28.4682917,
		            "name": "Kalkaji, New Delhi, Delhi, India",
		            "locality": "Kalkaji"
		        },
		        "start_date": "Fri, 1 Jul 2016 12:30 AM",
		        "end_date": "Mon, 1 Aug 2016 12:30 PM",
		        "organizer": {
		            "id": 17,
		            "name": "Ankit Joinwal",
		            "social_details": [
		                {
		                    "system": "FACEBOOK",
		                    "detail": "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xla1/v/t1.0-1/p200x200/12316467_10206731945876364_3008257792416820623_n.jpg?oh=cece300cd2db2d885c81f2c00b6a7d84&oe=578A9FB4&__gda__=1465178287_36f0dafbe70beb7506ebd22f8f089edf",
		                    "detailType": "USER_PROFILE_PIC"
		                }
		            ]
		        },
		        "status": "CREATED",
		        "meetup_access_url": null,
		        "display_pic": {
		            "id": 6,
		            "name": "smash.jpg",
		            "url": "https://sociallbox.s3-ap-southeast-1.amazonaws.com/public/meetups/4028918a53f1e9590153f1ea1db30000/smash.jpg",
		            "mimeType": null,
		            "isDisplayImage": true,
		            "uploadDt": 1460055659000
		        },
		        "event_at_meetup": null,
		        "user_actions": [
		            {
		                "value": true,
		                "action_type": "CAN_VIEW"
		            },
		            {
		                "value": false,
		                "action_type": "CAN_EDIT"
		            },
		            {
		                "value": false,
		                "action_type": "CAN_INVITE"
		            },
		            {
		                "value": true,
		                "action_type": "CAN_UPLOAD_IMAGE"
		            },
		            {
		                "value": true,
		                "action_type": "CAN_MESSAGE"
		            },
		            {
		                "value": false,
		                "action_type": "CAN_CANCEL"
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
	@RequestMapping(value="/{meetupId}",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<MeetupResponse> getMeetup(
			@RequestHeader(value=Constants.AUTHORIZATION_HEADER)String auth,
			@PathVariable String meetupId){
		logRequestStart(GET_MEETUP_API, SECURED_REQUEST_START_LOG_MESSAGE, GET_MEETUP_API);
		logInfo(GET_MEETUP_API, "Auth header = {}", auth);
		logInfo(GET_MEETUP_API, "Meetup id = {}", meetupId);
		String userName = LoginUtil.getUserNameFromHeader(auth);
		UserTypeBasedOnDevice typeBasedOnDevice = LoginUtil.identifyUserType(userName);
		if(typeBasedOnDevice==UserTypeBasedOnDevice.MOBILE){
			String deviceId = LoginUtil.getDeviceIdFromUserName(userName);
			MeetupResponse meetupResponse = this.meetupService.getMeetup(deviceId, meetupId);
			SingleEntityResponse<MeetupResponse> entityResponse = new SingleEntityResponse<>();
			entityResponse.setData(meetupResponse);
			entityResponse.setStatus(SUCCESS_STATUS);
			logRequestEnd(GET_MEETUP_API, GET_MEETUP_API);
			return entityResponse;
		}else{
			throw new ClientException(RestErrorCodes.ERR_003,ERROR_FEATURE_AVAILABLE_TO_MOBILE_ONLY);
		}
		
		
	}
	
	/**
	*  @api {post} /api/secured/meetups/:meetupId/attendees Invite Friends to meetup
	*  @apiName Invite Friends to meetup
	*  @apiGroup Meetups
	*  @apiParam {String} meetupId Mandatory Meetup Id
	*  @apiHeader {String} accept application/json
	*  @apiHeader {String} Content-Type application/json
	*  @apiHeader {Number} X-Auth-Date Current Epoch Date
	*  @apiHeader {String} Authorization Authentication Token
	*  @apiParamExample {json} Request-Example:
	*	{
			"attendees":[
					{
						"user_id":16,
						"is_admin":"false"
					},
					{
						"user_id":15,
						"is_admin":"false"
					}
				]
		}
	*  @apiSuccess (Success - OK 201) {Object}  response  Response.
	*  @apiSuccess (Success - OK 201) {String}  response.status   Eg.Success.
	*  @apiSuccess (Success - OK 201) {Object}  response.data Attendees
	*  @apiSuccessExample {json} Success-Response:
	*  	{
			"status": "Success",
			"data": [
				{
					"id": 10,
					"name": "Soumya Ranjan Nayak",
					"user_id": 15,
					"profile_pic": "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-frc3/v/t1.0-1/s200x200/539741_10151593876161285_1932943812_n.jpg?oh=91e5fd9cb3e7c726746a735d0603d5b2&oe=575DA8D4&__gda__=1468788536_04b0219db6544b0831aa55d45c7c76d0",
					"response": "MAYBE",
					"is_admin": false
				},
				{
					"id": 11,
					"name": "Vardhan Singh",
					"user_id": 16,
					"profile_pic": "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpa1/v/t1.0-1/s200x200/11224309_10153200429036366_3899281527807886630_n.jpg?oh=fbb67c694bc2bc6c21b7b70d0d25f53a&oe=574D6AFC&__gda__=1468826733_a181d28d9b3f9ed4bc72fac9d0b4d08a",
					"response": "MAYBE",
					"is_admin": false
				}
			],
			"page": 1,
			"nextPage": null,
			"total_records": 2
		}
		
	*/
	@RequestMapping(value="/{meetupId}/attendees",method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public EntityCollectionResponse<MeetupAttendee> addAttendees(
			@RequestHeader(value=Constants.AUTHORIZATION_HEADER)String auth,
			@Valid @RequestBody AddMeetupAttendeesRequest editMeetupRequest,
			@PathVariable String meetupId) {
		logRequestStart(ADD_ATTENDEES_API, SECURED_REQUEST_START_LOG_MESSAGE, ADD_ATTENDEES_API);
		logInfo(ADD_ATTENDEES_API, "Auth header = {}", auth);
		logInfo(ADD_ATTENDEES_API, "Request = {}", editMeetupRequest);
		
		editMeetupRequest.setMeetupId(meetupId);
		List<MeetupAttendee> attendees = meetupService.addAttendees(editMeetupRequest);

		EntityCollectionResponse<MeetupAttendee> collectionResponse = new EntityCollectionResponse<MeetupAttendee>();
		collectionResponse.setStatus(SUCCESS_STATUS);
		collectionResponse.setData(attendees);
		collectionResponse.setTotalRecords(attendees.size());
		collectionResponse.setPage(1);
		logRequestEnd(ADD_ATTENDEES_API, ADD_ATTENDEES_API);
		return collectionResponse;
	}
	
	/**
	*  @api {post} /api/secured/meetups/:meetupId/attendees/:attendeeId/response Save Attendee Response
	*  @apiName Save Attendee Response
	*  @apiGroup Meetups
	*  @apiParam {String} meetupId Mandatory Meetup Id
	*  @apiParam {Number} attendeeId Mandatory Attendee Id
	*  @apiHeader {String} accept application/json
	*  @apiHeader {String} Content-Type application/json
	*  @apiHeader {Number} X-Auth-Date Current Epoch Date
	*  @apiHeader {String} Authorization Authentication Token
	*  @apiParamExample {json} Request-Example:
	*	{
			"response" : "YES"
		}
	*  @apiSuccess (Success - OK 201) {Object}  response  Response.
	*  @apiSuccess (Success - OK 201) {String}  response.status   Eg.Success.
	*  @apiSuccess (Success - OK 201) {Object}  response.data Response message
	*  @apiSuccessExample {json} Success-Response:
	*  	{
		  "status": "Success",
		  "data": "Response saved successfully"
		}
		
	*/
	@RequestMapping(value="/{meetupId}/attendees/{attendeeId}/response",method = RequestMethod.POST,  consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public SingleEntityResponse<String> saveResponse(@RequestHeader(value=Constants.AUTHORIZATION_HEADER)String auth,
							@Valid @RequestBody SaveAttendeeResponse saveAttendeeResponse,
							@PathVariable String meetupId,
							@PathVariable Long attendeeId){
		logRequestStart(SAVE_ATTENDEE_RESPONSE_API, SECURED_REQUEST_START_LOG_MESSAGE, SAVE_ATTENDEE_RESPONSE_API);
		logInfo(SAVE_ATTENDEE_RESPONSE_API, "Auth header = {}", auth);
		logInfo(SAVE_ATTENDEE_RESPONSE_API, "Meetup id = {}, attendee = {} , response = {} ", meetupId,attendeeId,saveAttendeeResponse);
		saveAttendeeResponse.setAttendeeId(attendeeId);
		saveAttendeeResponse.setMeetupId(meetupId);

		this.meetupService.saveAttendeeResponse(saveAttendeeResponse);
		SingleEntityResponse<String> entityResponse = new SingleEntityResponse<String>();
		entityResponse.setData("Response saved successfully");
		entityResponse.setStatus(SUCCESS_STATUS);
		logRequestEnd(SAVE_ATTENDEE_RESPONSE_API, SAVE_ATTENDEE_RESPONSE_API);
		
		return entityResponse;
	}
	
	/**
	*  @api {post} /api/secured/meetups/:meetupId/attendees/:attendeeId/message Send Message To Meetup
	*  @apiName Send Message to meetup
	*  @apiGroup Meetups
	*  @apiParam {String} meetupId Mandatory Meetup Id
	*  @apiParam {String} attendeeId Mandatory Attendee Id
	*  @apiHeader {String} accept application/json
	*  @apiHeader {String} Content-Type application/json
	*  @apiHeader {Number} X-Auth-Date Current Epoch Date
	*  @apiHeader {String} Authorization Authentication Token
	*  @apiParamExample {json} Request-Example:
	*	{
			"message" : "Hey guys lets meetup"
		}
	*  @apiSuccess (Success - OK 201) {Object}  response  Response.
	*  @apiSuccess (Success - OK 201) {String}  response.status   Eg.Success.
	*  @apiSuccess (Success - OK 201) {Object}  response.data Response message
	*  @apiSuccessExample {json} Success-Response:
	*  	{
			"message" : "Hey guys lets meetup"
		}
		
	*/
	@RequestMapping(value="/{meetupId}/attendees/{attendeeId}/message",method = RequestMethod.POST,  consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public SingleEntityResponse<String> postMessageToMeetup(@RequestHeader(value=Constants.AUTHORIZATION_HEADER)String auth,
									@Valid @RequestBody MeetupMessage meetupMessage,
									@PathVariable String meetupId,
									@PathVariable Long attendeeId){
		logRequestStart(POST_MESSAGE_TO_MEETUP_API, SECURED_REQUEST_START_LOG_MESSAGE, POST_MESSAGE_TO_MEETUP_API);
		logInfo(POST_MESSAGE_TO_MEETUP_API, "Auth header = {}", auth);
		logInfo(POST_MESSAGE_TO_MEETUP_API, "Meetup = {} , AttendeeId = {} ",meetupId,attendeeId);
		this.meetupService.sendMessageInMeetup(meetupMessage, meetupId, attendeeId);
		SingleEntityResponse<String> entityResponse = new SingleEntityResponse<String>();
		entityResponse.setData("Posted message to meetup succesfully");
		entityResponse.setStatus(SUCCESS_STATUS);
		logRequestEnd(POST_MESSAGE_TO_MEETUP_API, POST_MESSAGE_TO_MEETUP_API);
		return entityResponse;
	}
	
	/**
	 *  @api {get} /api/secured/meetups/:meetupId/messages Get Meetup Messages
	 *  @apiName Get Meetup messages
	 *  @apiGroup Meetups
	 *  @apiParam {String} meetupId Mandatory Meetup Id
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
	 * 	@apiSuccess (Success - OK 200) {Object}  response.data Messages
	 *  @apiSuccessExample {json} Success-Response:
	 *  
		{
		  "status": "Success",
		  "data": [
			{
			  "id": 1,
			  "timeToDisplay": "1Hour ago",
			  "message": "Hey guys lets meetup",
			  "user_name": "Vardhan Singh",
			  "profile_pic": "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpa1/v/t1.0-1/s200x200/11224309_10153200429036366_3899281527807886630_n.jpg?oh=fbb67c694bc2bc6c21b7b70d0d25f53a&oe=574D6AFC&__gda__=1468826733_a181d28d9b3f9ed4bc72fac9d0b4d08a"
			}
		  ],
		  "page": 1,
		  "nextPage": null,
		  "total_records": 1
		}
	 */
	@RequestMapping(value="/{meetupId}/messages",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<MeetupMessage> getMeetupMessages(@RequestHeader(value=Constants.AUTHORIZATION_HEADER)String auth,
												@PathVariable String meetupId,
												@RequestParam(required=false,value="page") Integer page) throws ServiceException{
		logRequestStart(GET_MEETUP_MESSAGES_API, SECURED_REQUEST_START_LOG_MESSAGE, GET_MEETUP_MESSAGES_API);
		logInfo(GET_MEETUP_MESSAGES_API, "Auth header = {} ", auth);
		logInfo(GET_MEETUP_MESSAGES_API, "Meetup id = {}", meetupId);
		if(page==null){
			page = new Integer(1);
		}
		List<MeetupMessage> messages = this.meetupService.getMeetupMessages(meetupId, page);
		
		EntityCollectionResponse<MeetupMessage> collectionResponse = new EntityCollectionResponse<MeetupMessage>();
		collectionResponse.setData(messages);
		collectionResponse.setStatus("Success");
		collectionResponse.setPage(page);
		collectionResponse.setTotalRecords(messages == null ? 0 : messages.size());
		logRequestEnd(GET_MEETUP_MESSAGES_API, GET_MEETUP_MESSAGES_API);
		return collectionResponse;
		
	}
	
}
