package com.bitlogic.sociallbox.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.codec.Base64;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.SmartDevice;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.UserTypeBasedOnDevice;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;

public class LoginUtil implements Constants{

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginUtil.class);
	
	public static String[] getUsernameParts(String username){
		if(username==null)
			return null;
		
		String[] nameArr = username.split(UNAME_DELIM);
		return nameArr;
	}
	
	public static String getUserNameFromHeader(String authHeader){
		String credentials = authHeader.replaceAll("Basic ", "");
        
        String base64Decoded = new String(Base64.decode(credentials.getBytes()));
        
        // Authorization header is in the form <public_access_key>:<signature>
        String auth[] = base64Decoded.split(":");
        String userName = auth[0];
       
		return userName;
	}
	
	public static UserTypeBasedOnDevice identifyUserType(String userName){
		String[] secretParts = getUsernameParts(userName);
		if (secretParts == null || secretParts.length != 2) {
			throw new ClientException(RestErrorCodes.ERR_002,ERROR_LOGIN_INVALID_CREDENTIALS);
		}
		String userType = secretParts[0];

		LOGGER.info("UserType = {}", userType);
		if (userType.equals(Constants.DEVICE_PREFIX)) {
			return UserTypeBasedOnDevice.MOBILE;
		}else{
			return UserTypeBasedOnDevice.WEB;
		}
	}
	
	public static String getDeviceIdFromUserName(String userName){
		String[] secretParts = getUsernameParts(userName);
		if (secretParts == null || secretParts.length != 2) {
			throw new ClientException(RestErrorCodes.ERR_002,ERROR_LOGIN_INVALID_CREDENTIALS);
		}
		String userType = secretParts[0];
		String deviceId = secretParts[1];
		
		return deviceId;
	}
	
	public static String getUserEmailIdFromUserName(String username){
		String[] secretParts = getUsernameParts(username);
		if (secretParts == null || secretParts.length != 2) {
			throw new ClientException(RestErrorCodes.ERR_002,ERROR_LOGIN_INVALID_CREDENTIALS);
		}
		String userEmail = secretParts[1];
		return userEmail;
	}
	
	public static void validateMobileUser(User user) throws ClientException{
		LOGGER.info("Validating user:"+user);
		String msg =  null;
		//check for mandatory values
		if(user.getSocialDetails()==null || user.getSocialDetails().isEmpty()){
			msg = ERROR_LOGIN_SOCIAL_DETAILS_MISSING;
			LOGGER.error(msg);
			throw new ClientException(RestErrorCodes.ERR_001,msg);
		}
		
		if(user.getSmartDevices()==null || user.getSmartDevices().isEmpty()){
			msg = ERROR_LOGIN_INVALID_DEVICES_IN_REQ;
			LOGGER.error(msg);
			throw new ClientException(RestErrorCodes.ERR_001,msg);
		}
		
		if(user.getSmartDevices().size() > 1){
			msg =  ERROR_LOGIN_INVALID_DEVICES_IN_REQ;
			LOGGER.error(msg);
			throw new ClientException(RestErrorCodes.ERR_001,msg);
		}else{
			SmartDevice smartDevice = null;
			//Expecting only one device details in request event when there are multiple devices with user.
			for(SmartDevice device : user.getSmartDevices()){
				smartDevice = device;
				break;
			}
			if(smartDevice==null){
				msg = ERROR_LOGIN_DEVICE_MISSING;
				throw new ClientException(RestErrorCodes.ERR_001,msg);
			}
			if(smartDevice.getUniqueId()==null || smartDevice.getUniqueId().isEmpty()){
				msg = ERROR_LOGIN_SD_ID_MISSING;
				throw new ClientException(RestErrorCodes.ERR_001,msg);
			}
		}
		
	}
	
	public static void validateWebUser(User user) throws ClientException{
		LOGGER.info("Validating user:"+user);
		String msg =  null;
		//check for mandatory values
		if(user.getSocialDetails()==null || user.getSocialDetails().isEmpty()){
			msg = ERROR_LOGIN_SOCIAL_DETAILS_MISSING;
			LOGGER.error(msg);
			throw new ClientException(RestErrorCodes.ERR_001,msg);
		}
	}
	
	public static void validateOrganizerAdmin(User user){
		LOGGER.info("Validating user:"+user);
		String msg =  null;
		//check for mandatory values
		if(user.getSmartDevices()!=null && !user.getSmartDevices().isEmpty()){
			msg = ERROR_FEATURE_AVAILABLE_TO_WEB_ONLY;
			LOGGER.error(msg);
			throw new ClientException(RestErrorCodes.ERR_001,msg);
		}
	}
	
}
