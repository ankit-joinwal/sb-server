package com.bitlogic.sociallbox.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.SocialBoxConfig;
import com.bitlogic.sociallbox.data.model.notifications.Notification;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.exception.ServiceException;

public class NotificationUtils extends LoggingService implements Constants{

	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationUtils.class);
	
	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	
	public static void send(RestTemplate restTemplate , Notification notification, SocialBoxConfig boxConfig){
		String LOG_PREFIX = "NotificationUtils-send";
		
		HttpEntity<Notification> request = new HttpEntity<Notification>(notification);
		String message = LOG_PREFIX + " :: " + "Invoking request to URL = {}";
		LOGGER.info(message, boxConfig.getNotificationServiceURL());
		ResponseEntity<String> response = restTemplate.exchange(boxConfig.getNotificationServiceURL(), HttpMethod.POST, request, String.class);
		
		HttpStatus returnStatus = response.getStatusCode();
		boolean isSuccess = returnStatus.is2xxSuccessful();
		if (isSuccess) {
			message = LOG_PREFIX + " :: " + "Search returned success response";
			LOGGER.info(message);

		} else {
			if (returnStatus.is4xxClientError()) {
				throw new ClientException(RestErrorCodes.ERR_050,
						Constants.ERROR_NOTIFICATION_CLIENT_REQUEST);
			} else if (returnStatus.is5xxServerError()) {
				throw new ServiceException(NOTIFICATION_SERVICE_NAME, RestErrorCodes.ERR_010,
						Constants.ERROR_NOTIFICATION_WEBSERVICE_ERROR);
			}
		}

	}
}
