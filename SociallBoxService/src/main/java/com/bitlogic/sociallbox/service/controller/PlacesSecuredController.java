package com.bitlogic.sociallbox.service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.UserTypeBasedOnDevice;
import com.bitlogic.sociallbox.data.model.response.EntityCollectionResponse;
import com.bitlogic.sociallbox.data.model.response.SingleEntityResponse;
import com.bitlogic.sociallbox.data.model.response.UserFriend;
import com.bitlogic.sociallbox.service.business.PlacesService;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.utils.LoginUtil;
@RestController
@RequestMapping("/api/secured/places")
public class PlacesSecuredController extends BaseController implements Constants{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PlacesSecuredController.class);
	
	private static final String LIKE_PLACE_REQUEST = "LikePlace API";
	private static final String GET_FRIENDS_WHO_LIKE_PLACE_API = "GetFriendsWhoLikePlace API";
	
	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	@Autowired
	private PlacesService placesService;
	
	/**
	 *  @api {post} /api/secured/places/place/:place_id/like Save Like Action for a Place
	 *  @apiName Save Like Action for a Place
	 *  @apiGroup Places
	 *  @apiParam  {String} place_id Place Id
	 *  @apiHeader {String} accept application/json
	 *  @apiHeader {String} Content-Type application/json
	 *  @apiHeader {Number} X-Auth-Date Current Epoch Date
	 *  @apiHeader {String} Authorization Authentication Token
	 *  @apiSuccess (Success - OK 200) {Object}  response  Response.
	 *  @apiSuccess (Success - OK 200) {String}  response.status   Eg.Success.
	 * 	@apiSuccess (Success - OK 200) {Object}  response.data Message 
	 * 	@apiParamExample {json} Request-Example:
	 *  {
			"status": "Success",
			"data": "Use like saved successfully"
		}

	 */
	@RequestMapping(value="/place/{placeId}/like",method=RequestMethod.POST,produces = { MediaType.APPLICATION_JSON_VALUE },consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public SingleEntityResponse<String> saveLikeActionForUser(@PathVariable("placeId") String placeId,@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String authHeader){
		final String LOG_PREFIX = LIKE_PLACE_REQUEST;

		logRequestStart(LOG_PREFIX, SECURED_REQUEST_START_LOG_MESSAGE, LIKE_PLACE_REQUEST );
		logInfo(LOG_PREFIX, "Authorization {}", authHeader);
		String userName = LoginUtil.getUserNameFromHeader(authHeader);
		UserTypeBasedOnDevice typeBasedOnDevice = LoginUtil.identifyUserType(userName);
		if(typeBasedOnDevice==UserTypeBasedOnDevice.MOBILE){
			String deviceId = LoginUtil.getDeviceIdFromUserName(userName);
			logInfo(LOG_PREFIX, " Device Id {} ", deviceId);
			this.placesService.saveUserPlaceLike(deviceId, placeId);
			logInfo(LOG_PREFIX,"User place like mapping saved successfully");
		}else{
			
			throw new ClientException(RestErrorCodes.ERR_003,ERROR_FEATURE_AVAILABLE_TO_MOBILE_ONLY);
		}
		SingleEntityResponse<String> entityResponse = new SingleEntityResponse<String>();
		entityResponse.setStatus(SUCCESS_STATUS);
		entityResponse.setData("Use like saved successfully");
		logRequestEnd(LOG_PREFIX,LIKE_PLACE_REQUEST);
		return entityResponse;
	}
	
	/**
	 *  @api {get} /api/secured/places/place/:place_id/likes Get Friends who like a place
	 *  @apiName Get Friends who like a place
	 *  @apiGroup Places
	 *  @apiHeader {String} accept application/json
	 *  @apiHeader {Number} X-Auth-Date Current Epoch Date
	 *  @apiHeader {String} Authorization Authentication Token
	 *	@apiSuccess (Success 200) {Object}  response  Response.
	 *  @apiSuccess (Success 200) {String}  response.status   Eg.Success.
	 * 	@apiSuccess (Success 200) {Object}  response.data User's Friends List Or Empty Array if no friends Found
	 *  @apiSuccessExample {json} Success-Response:
	 * {
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
	 *	@apiError (Unauthorizes 401) {String}  message  Eg.error.login.invalid.credentials
	 */
	@RequestMapping(value="/place/{placeId}/likes",method=RequestMethod.GET,produces = { MediaType.APPLICATION_JSON_VALUE },consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public EntityCollectionResponse<UserFriend> getFriendsWhoLikePlace(@PathVariable("placeId") String placeId,
								@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String authHeader){
		String LOG_PREFIX = GET_FRIENDS_WHO_LIKE_PLACE_API;
		logRequestStart(LOG_PREFIX, SECURED_REQUEST_START_LOG_MESSAGE, GET_FRIENDS_WHO_LIKE_PLACE_API);
		logInfo(LOG_PREFIX, "Auth header = {}", authHeader);
		logInfo(LOG_PREFIX, "Place Id = {} ", placeId);
		String userName = LoginUtil.getUserNameFromHeader(authHeader);
		UserTypeBasedOnDevice typeBasedOnDevice = LoginUtil.identifyUserType(userName);
		List<UserFriend> friendsWhoLikeThisPlace = null;
		if(typeBasedOnDevice==UserTypeBasedOnDevice.MOBILE){
			String deviceId = LoginUtil.getDeviceIdFromUserName(userName);
			logInfo(LOG_PREFIX, " Device Id {} ", deviceId);
			friendsWhoLikeThisPlace = this.placesService.getFriendsWhoLikePlace(deviceId, placeId);
			
		}else{
			throw new ClientException(RestErrorCodes.ERR_003,ERROR_FEATURE_AVAILABLE_TO_MOBILE_ONLY);
		}
		
		EntityCollectionResponse<UserFriend> collectionResponse = new EntityCollectionResponse<UserFriend>();
		collectionResponse.setData(friendsWhoLikeThisPlace);
		collectionResponse.setPage(1);
		collectionResponse.setTotalRecords(friendsWhoLikeThisPlace.size());
		collectionResponse.setStatus(SUCCESS_STATUS);
		
		logRequestEnd(LOG_PREFIX, GET_FRIENDS_WHO_LIKE_PLACE_API);
		return collectionResponse;
	}
}
