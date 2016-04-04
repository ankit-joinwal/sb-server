package com.bitlogic.sociallbox.notification.service.exception;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;


public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler  {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncExceptionHandler.class);
	@Override
	public void handleUncaughtException(Throwable ex, Method method,
			Object... params) {
		
		LOGGER.error("Error occured in {}'s method {} ",method.getClass().getName(),method.getName(), ex);
	}
}
