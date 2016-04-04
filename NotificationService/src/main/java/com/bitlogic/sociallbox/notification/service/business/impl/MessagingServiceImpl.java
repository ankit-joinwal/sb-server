package com.bitlogic.sociallbox.notification.service.business.impl;

import java.net.URI;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.GAPIConfig;
import com.bitlogic.sociallbox.data.model.notifications.GCMNotification;
import com.bitlogic.sociallbox.data.model.notifications.GCMNotificationResponse;
import com.bitlogic.sociallbox.data.model.notifications.ResponseStatus;
import com.bitlogic.sociallbox.notification.service.business.MessagingService;
import com.bitlogic.sociallbox.notification.service.util.LoggingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component("messagingService")
public class MessagingServiceImpl extends LoggingService implements
		MessagingService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MessagingService.class);

	@Override
	public Logger getLogger() {
		return LOGGER;
	}

	@Autowired
	private RestTemplate restTemplate;

	ObjectMapper  objectMapper = new ObjectMapper();
	 
	@Autowired
	private GAPIConfig gapiConfig;

	@Override
	public GCMNotificationResponse sendNotification(
			GCMNotification gcmNotification) {
		String LOG_PREFIX = "MessagingServiceImpl-sendNotification";
		
		try {
			logInfo(LOG_PREFIX, "GCM Notification = {}", objectMapper.writeValueAsString(gcmNotification));
		} catch (JsonProcessingException e) {
			logError(LOG_PREFIX, "Error while parsing input GCM Notification {}",gcmNotification, e);
		}
		String url = gapiConfig.getGcmServerURL();
		logInfo(LOG_PREFIX, "GCM URL = {}", url);
		try {
			String authKey = "key="+gapiConfig.getGapiKey();
			URI uri = new URI(url);
			RequestEntity request = RequestEntity
					.post(uri)
					.header(Constants.AUTHORIZATION_HEADER,
							authKey)
					.accept(MediaType.APPLICATION_JSON).body(gcmNotification);
			ResponseEntity<GCMNotificationResponse> responseEntity = restTemplate
					.exchange(request, GCMNotificationResponse.class);
			HttpStatus returnStatus = responseEntity.getStatusCode();
			GCMNotificationResponse notificationResponse = responseEntity.getBody();
			boolean isSuccess = returnStatus.is2xxSuccessful();
			if (isSuccess) {
				logInfo(LOG_PREFIX, "Notifications sent successfully");
				if(notificationResponse.getFailedMessageCount()==0){
					notificationResponse.setStatus(ResponseStatus.SUCCESS);
				}else{
					notificationResponse.setStatus(ResponseStatus.PARTIAL_SUCCESS);
				}
			} else {
				if (returnStatus.is4xxClientError()) {
					logError(LOG_PREFIX, "Error while sending notifications {}", returnStatus);
					notificationResponse.setStatus(ResponseStatus.FAILURE);
				} else if (returnStatus.is5xxServerError()) {
					notificationResponse.setStatus(ResponseStatus.FAILURE);
					logError(LOG_PREFIX, "Error while sending notifications {}", returnStatus);
				}
			}
			return notificationResponse;
		} catch (Exception ex) {
			logError(LOG_PREFIX, "Error while sending notification", ex);
		}
		return null;
	}
}
