package com.bitlogic.sociallbox.notification.service.util;

import org.slf4j.Logger;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.User;

public abstract class LoggingService {

	public abstract Logger getLogger();
	
	private static final String COMMA = " , ";
	public void logInfo(String logPrefix, String logMessage,Object... args){
		Logger logger = this.getLogger();
		String message = logPrefix+ " :: "+logMessage;
		logger.info(message,args);
	}
	
	public void logError(String logPrefix, String logMessage,Object... args){
		Logger logger = this.getLogger();
		String message = logPrefix+ Constants.DOUBLE_COLON+logMessage;
		logger.error(message,args);
	}
	
	public void logWarning(String logPrefix, String logMessage,Object... args){
		Logger logger = this.getLogger();
		String message = logPrefix+ Constants.DOUBLE_COLON+logMessage;
		logger.warn(message,args);
	}
	
	public void logUserObject(User user,String logPrefix, String logMessage,Object... args){
		Logger logger = this.getLogger();
		StringBuilder builder = new StringBuilder();
		if(user==null){
			throw new IllegalArgumentException("User object is null in logUserObject method");
		}
		builder.append("User [name="+user.getName()+COMMA)
		.append("email="+user.getEmailId()+COMMA)
		.append("isEnabled="+user.getIsEnabled()+COMMA)
		.append("socialDetails="+user.getSocialDetails()+COMMA);
		
		logger.info(logMessage+Constants.DOUBLE_COLON+builder.toString());
	}
}
