package com.bitlogic.sociallbox.notification.service.controller;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RestController;

import com.bitlogic.Constants;

@RestController
public abstract class BaseController {

	public abstract Logger getLogger();
	
	
	

	public void logRequestStart(String logPrefix , String logMessage,Object... args){
		Logger logger = getLogger();
		String message = logPrefix + Constants.ONE_WHITESPACE +Constants.DOUBLE_COLON+Constants.ONE_WHITESPACE + logMessage;
		logger.info(message,args);
	}
	
	public void logRequestEnd(String logPrefix , String logMessage,Object... args){
		Logger logger = getLogger();
		String message = logPrefix + " :: Request completed successfully :: " + logMessage;
		logger.info(message,args);
	}
	
	public void logRequestEnd(String logPrefix , String logMessage){
		logRequestEnd(logPrefix, logMessage,new Object());
	}
	public void logInfo(String logPrefix, String logMessage,Object... args){
		Logger logger = this.getLogger();
		String message = logPrefix + Constants.ONE_WHITESPACE +Constants.DOUBLE_COLON+Constants.ONE_WHITESPACE + logMessage;
		logger.info(message,args);
	}
	
	public void logError(String logPrefix, String logMessage,Object... args){
		Logger logger = this.getLogger();
		String message = logPrefix + Constants.ONE_WHITESPACE +Constants.DOUBLE_COLON+Constants.ONE_WHITESPACE +  logMessage;
		logger.error(message,args);
	}
}
