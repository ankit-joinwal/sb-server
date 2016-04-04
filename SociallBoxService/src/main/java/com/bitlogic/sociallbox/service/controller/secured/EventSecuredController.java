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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.EventAttendee;
import com.bitlogic.sociallbox.data.model.UserTypeBasedOnDevice;
import com.bitlogic.sociallbox.data.model.requests.CreateEventRequest;
import com.bitlogic.sociallbox.data.model.response.EntityCollectionResponse;
import com.bitlogic.sociallbox.data.model.response.EventResponse;
import com.bitlogic.sociallbox.data.model.response.SingleEntityResponse;
import com.bitlogic.sociallbox.data.model.response.UserFriend;
import com.bitlogic.sociallbox.service.business.EventService;
import com.bitlogic.sociallbox.service.controller.BaseController;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.exception.ServiceException;
import com.bitlogic.sociallbox.service.transformers.EventTransformer;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory.Transformer_Types;
import com.bitlogic.sociallbox.service.utils.LoginUtil;

@RestController
@RequestMapping("/api/secured/events")
public class EventSecuredController extends BaseController implements Constants{

	private static final Logger logger = LoggerFactory.getLogger(EventSecuredController.class);
	private static final String CREATE_EVENT_API = "CreateEvent API";
	private static final String UPLOAD_IMAGE_TO_EVENT_API = "UploadImageToEvent API";
	private static final String MAKE_EVENT_LIVE_API = "MakeEventLive API";
	private static final String REGISTER_FOR_EVENT_API = "RegisterForEvent API";
	private static final String GET_FRIENDS_GOING_API = "GetFriendsGoingToEvent API";
	private static final String ADD_TO_FAV_API = "AddEventToFavourites API";
	private static final String IS_ADDED_TO_FAV_API = "IsEventAddedToFav API";
	@Override
	public Logger getLogger() {
		return logger;
	}
	
	@Autowired
	private EventService eventService;
	
	@RequestMapping(method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public SingleEntityResponse<EventResponse> create(@RequestHeader(value=Constants.AUTHORIZATION_HEADER)String auth,@Valid @RequestBody CreateEventRequest createEventRequest) throws ServiceException{
		logRequestStart(CREATE_EVENT_API, SECURED_REQUEST_START_LOG_MESSAGE, CREATE_EVENT_API);
		logInfo(CREATE_EVENT_API, "Auth header = {}", auth);
		logInfo(CREATE_EVENT_API, "Request = {}", createEventRequest);
		String userName = LoginUtil.getUserNameFromHeader(auth);
		UserTypeBasedOnDevice typeBasedOnDevice = LoginUtil.identifyUserType(userName);
		if(typeBasedOnDevice==UserTypeBasedOnDevice.WEB){
			String userEmail = LoginUtil.getUserEmailIdFromUserName(userName);
			EventTransformer transformer = (EventTransformer) TransformerFactory.getTransformer(Transformer_Types.EVENT_TRANS);
			EventResponse createEventResponse = transformer.transform(eventService.create(userEmail,createEventRequest));
			SingleEntityResponse<EventResponse> entityResponse = new SingleEntityResponse<>();
			entityResponse.setData(createEventResponse);
			entityResponse.setStatus(SUCCESS_STATUS);
			logRequestEnd(CREATE_EVENT_API, CREATE_EVENT_API);
			return entityResponse;
		}else{
			throw new ClientException(RestErrorCodes.ERR_003,ERROR_FEATURE_AVAILABLE_TO_WEB_ONLY);
		}
	}
	
	@RequestMapping(value = "/{eventId}/images", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
    public void upload(@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String auth,@PathVariable String eventId,MultipartHttpServletRequest request,
			HttpServletResponse response) {
		logRequestStart(UPLOAD_IMAGE_TO_EVENT_API,
				SECURED_REQUEST_START_LOG_MESSAGE, UPLOAD_IMAGE_TO_EVENT_API);
		logInfo(UPLOAD_IMAGE_TO_EVENT_API, "Auth header = {}", auth);
		logInfo(UPLOAD_IMAGE_TO_EVENT_API, "Event id = {}", eventId);
		logger.info("Request recieved to upload event images for event {} ",
				eventId);
		String imagesURL = request.getRequestURL() + "";
		logInfo(UPLOAD_IMAGE_TO_EVENT_API, "Image url = {}", imagesURL);
		Map<String, MultipartFile> fileMap = request.getFileMap();
		if (fileMap.values() != null && !fileMap.values().isEmpty()) {
			List<MultipartFile> files = new ArrayList<MultipartFile>(
					fileMap.values());
			this.eventService.storeEventImages(imagesURL, files, eventId);
		}
		logRequestEnd(UPLOAD_IMAGE_TO_EVENT_API, UPLOAD_IMAGE_TO_EVENT_API);
	}
	
	@RequestMapping(value="/{eventId}/live",method = RequestMethod.PUT, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<String> makeEventLive(@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String auth,@PathVariable String eventId){
		logRequestStart(MAKE_EVENT_LIVE_API, SECURED_REQUEST_START_LOG_MESSAGE, MAKE_EVENT_LIVE_API);
		logInfo(MAKE_EVENT_LIVE_API, "Auth header = {}", auth);
		logInfo(MAKE_EVENT_LIVE_API, "Event id ", eventId);
		logger.info("### Request Recieved - make Event Live ###");
		this.eventService.makeEventLive(eventId);
		SingleEntityResponse<String> entityResponse = new SingleEntityResponse<String>();
		entityResponse.setStatus(SUCCESS_STATUS);
		entityResponse.setData("Succesfully made event live");
		logRequestEnd(MAKE_EVENT_LIVE_API, MAKE_EVENT_LIVE_API);
		return entityResponse;
	}
	
	/**
	 *  @api {post} /api/secured/events/:id/register Going to Event API
	 *  @apiName Going to event
	 *  @apiGroup Events
	 *  @apiParam {String} id Mandatory Event id
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
	 * 	@apiSuccess (Success - OK 200) {Object}  response.data Success Response
	 *  @apiSuccessExample {json} Success-Response:
	 *  {
		  "status": "Success",
		  "data": {
			"id": null,
			"user_id": 17,
			"event_id": "4028918853bd6ca10153bd738d100000"
		  }
		}
	 
	 */
	@RequestMapping(value="/{eventId}/register",method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<EventAttendee> registerToEvent(@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String auth,
			@PathVariable String eventId){
		logRequestStart(REGISTER_FOR_EVENT_API, SECURED_REQUEST_START_LOG_MESSAGE, REGISTER_FOR_EVENT_API);
		logInfo(REGISTER_FOR_EVENT_API, "Event Id {}", eventId);
		logInfo(REGISTER_FOR_EVENT_API, "Auth header = {}", auth);
		String userName = LoginUtil.getUserNameFromHeader(auth);
		UserTypeBasedOnDevice typeBasedOnDevice = LoginUtil.identifyUserType(userName);
		if(typeBasedOnDevice==UserTypeBasedOnDevice.MOBILE){
			String deviceId = LoginUtil.getDeviceIdFromUserName(userName);
			logInfo(REGISTER_FOR_EVENT_API, " Device Id {} ", deviceId);
			EventAttendee attendee = this.eventService.registerForEvent(eventId, deviceId);
			
			SingleEntityResponse<EventAttendee> entityResponse = new SingleEntityResponse<EventAttendee>();
			entityResponse.setStatus(SUCCESS_STATUS);
			entityResponse.setData(attendee);
			
			return entityResponse;
		}else{
			throw new ClientException(RestErrorCodes.ERR_003,ERROR_FEATURE_AVAILABLE_TO_MOBILE_ONLY);
		}
	}
	
	/**
	 *  @api {post} /api/secured/events/:id/friends Get Friends going to event
	 *  @apiName Get Friends going to event
	 *  @apiGroup Events
	 *  @apiParam {String} id Mandatory Event id
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
	 * 	@apiSuccess (Success - OK 200) {Object}  response.data Success Response
	 *  @apiSuccessExample {json} Success-Response:
	 *  {
		  "status": "Success",
		  "data": [
			{
			  "profilePic": "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xla1/v/t1.0-1/p200x200/12316467_10206731945876364_3008257792416820623_n.jpg?oh=cece300cd2db2d885c81f2c00b6a7d84&oe=578A9FB4&__gda__=1465178287_36f0dafbe70beb7506ebd22f8f089edf",
			  "name": "Ankit Joinwal",
			  "emailId": "anki_join@yahoo.in"
			}
		  ],
		  "page": 1,
		  "nextPage": null,
		  "total_records": 1
		}
	 
	 */
	@RequestMapping(value="/{eventId}/friends",method=RequestMethod.GET,produces = { MediaType.APPLICATION_JSON_VALUE },consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<UserFriend> getFriendsGoingToEvent(
								@PathVariable("eventId") String eventId,
								@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String auth){
		logRequestStart(GET_FRIENDS_GOING_API, SECURED_REQUEST_START_LOG_MESSAGE, GET_FRIENDS_GOING_API);
		logInfo(GET_FRIENDS_GOING_API, "Event Id {}", eventId);
		logInfo(GET_FRIENDS_GOING_API, "Auth header = {}", auth);
		String userName = LoginUtil.getUserNameFromHeader(auth);
		UserTypeBasedOnDevice typeBasedOnDevice = LoginUtil.identifyUserType(userName);
		if(typeBasedOnDevice==UserTypeBasedOnDevice.MOBILE){
			String deviceId = LoginUtil.getDeviceIdFromUserName(userName);
			logInfo(GET_FRIENDS_GOING_API, " Device Id {} ", deviceId);
			List<UserFriend> friends = this.eventService.getFriendsGoingToEvent(deviceId, eventId);
			EntityCollectionResponse<UserFriend> collectionResponse = new EntityCollectionResponse<UserFriend>();
			collectionResponse.setData(friends);
			collectionResponse.setPage(1);
			collectionResponse.setStatus(SUCCESS_STATUS);
			collectionResponse.setTotalRecords(friends.size());
			return collectionResponse;
		}else{
			throw new ClientException(RestErrorCodes.ERR_003,ERROR_FEATURE_AVAILABLE_TO_MOBILE_ONLY);
		}
	}
	
	/**
	 *  @api {post} /api/secured/events/:eventId/addToFavourite Add event to favourites
	 *  @apiName Add event to favourites
	 *  @apiGroup Events
	 *  @apiParam  {String} event_id Event Id
	 *  @apiHeader {String} accept application/json
	 *  @apiHeader {String} Content-Type application/json
	 *  @apiHeader {Number} X-Auth-Date Current Epoch Date
	 *  @apiHeader {String} Authorization Authentication Token
	 *  @apiSuccess (Success - OK 200) {Object}  response  Response.
	 *  @apiSuccess (Success - OK 200) {String}  response.status   Eg.Success.
	 * 	@apiSuccess (Success - OK 200) {Object}  response.data Message 
	 *  @apiSuccessExample {json} Success-Response:
	 *  {
			"status": "Success",
			"data": "User like saved successfully"
		}

	 */
	@RequestMapping(value="/{eventId}/addToFavourite",method=RequestMethod.POST,produces = { MediaType.APPLICATION_JSON_VALUE },consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<String> addToFavouriteEvents(@PathVariable("eventId") String eventId,@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String authHeader){
		final String LOG_PREFIX = ADD_TO_FAV_API;
		logRequestStart(LOG_PREFIX, SECURED_REQUEST_START_LOG_MESSAGE, ADD_TO_FAV_API );
		logInfo(LOG_PREFIX, "Authorization {}", authHeader);
		String userName = LoginUtil.getUserNameFromHeader(authHeader);
		UserTypeBasedOnDevice typeBasedOnDevice = LoginUtil.identifyUserType(userName);
		if(typeBasedOnDevice==UserTypeBasedOnDevice.MOBILE){
			String deviceId = LoginUtil.getDeviceIdFromUserName(userName);
			logInfo(LOG_PREFIX, " Device Id {} ", deviceId);
			this.eventService.addEventToUserFav(deviceId, eventId);
			logInfo(LOG_PREFIX,"Event added to user favourites");
		}else{
			
			throw new ClientException(RestErrorCodes.ERR_003,ERROR_FEATURE_AVAILABLE_TO_MOBILE_ONLY);
		}
		SingleEntityResponse<String> entityResponse = new SingleEntityResponse<String>();
		entityResponse.setStatus(SUCCESS_STATUS);
		entityResponse.setData("Event marked as favourite");
		logRequestEnd(LOG_PREFIX,ADD_TO_FAV_API);
		return entityResponse;
	}
	
	
	
	
}
