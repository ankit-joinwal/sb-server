package com.bitlogic.sociallbox.service.controller.secured;

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
import com.bitlogic.sociallbox.service.business.UserService;
import com.bitlogic.sociallbox.service.controller.BaseController;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.utils.LoginUtil;

@RestController
@RequestMapping("/api/secured/places")
public class GeoSecuredController extends BaseController implements Constants{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GeoSecuredController.class);
	private static final String LIKE_PLACE_REQUEST = "LikePlace API";
	
	@Autowired
	private UserService userService;
	
	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	@RequestMapping(value="/place/{placeId}/like",method=RequestMethod.PUT,produces = { MediaType.APPLICATION_JSON_VALUE },consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public void likePlace(@PathVariable("placeId") String placeId,@RequestHeader(value=Constants.AUTHORIZATION_HEADER) String authHeader){
		final String LOG_PREFIX = LIKE_PLACE_REQUEST;

		logRequestStart(LOG_PREFIX, SECURED_REQUEST_START_LOG_MESSAGE, LIKE_PLACE_REQUEST );
		logInfo(LOG_PREFIX, "Authorization {}", authHeader);
		String userName = LoginUtil.getUserNameFromHeader(authHeader);
		UserTypeBasedOnDevice typeBasedOnDevice = LoginUtil.identifyUserType(userName);
		if(typeBasedOnDevice==UserTypeBasedOnDevice.MOBILE){
			String deviceId = LoginUtil.getDeviceIdFromUserName(userName);
			logInfo(LOG_PREFIX, " Device Id {} ", deviceId);
			this.userService.saveUserPlaceLike(deviceId, placeId);
			logInfo(LOG_PREFIX,"User place like mapping saved successfully");
		}else{
			
			throw new ClientException(RestErrorCodes.ERR_003,ERROR_FEATURE_AVAILABLE_TO_MOBILE_ONLY);
		}
		
		logRequestEnd(LOG_PREFIX,LIKE_PLACE_REQUEST);
	}
	
	
}
