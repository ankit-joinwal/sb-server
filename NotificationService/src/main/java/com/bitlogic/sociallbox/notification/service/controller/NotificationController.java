package com.bitlogic.sociallbox.notification.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.notifications.Notification;
import com.bitlogic.sociallbox.notification.service.business.NotificationService;

@RestController
@RequestMapping("/api/public/notifications")
public class NotificationController extends BaseController implements Constants{

	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);
	private static final String SEND_NOTIFICATIONS_API = "SendNotifications API";
	
	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	
	@Autowired
	private NotificationService notificationService;
	
	@Async
	@RequestMapping(method= RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE}, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public DeferredResult<ResponseEntity<?>> sendNotifications(@RequestBody Notification notification){
		logRequestStart(SEND_NOTIFICATIONS_API, SECURED_REQUEST_START_LOG_MESSAGE, SEND_NOTIFICATIONS_API);
		logInfo(SEND_NOTIFICATIONS_API, "Reciever Ids = {} ",  notification.getRecieverIds());
		logInfo(SEND_NOTIFICATIONS_API, "Message = {}", notification.getNotificationMessage());
		final Notification notificationRequest = notification;
		final DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();
		logInfo(SEND_NOTIFICATIONS_API, "Before Calling  Service Timestamp {}",
				System.currentTimeMillis());
		ListenableFuture<String> futureResponse = this.notificationService.sendNotifications(notification);
		logInfo(SEND_NOTIFICATIONS_API, "Just after Calling Service Timestamp {}",
				System.currentTimeMillis());
		
		futureResponse.addCallback(new ListenableFutureCallback<String>() {
			
			@Override
			public void onSuccess(String arg0) {
				logInfo(SEND_NOTIFICATIONS_API, "Notification Task completed successfully for notification {}", notificationRequest);
			}
			
			@Override
			public void onFailure(Throwable arg0) {
				logInfo(SEND_NOTIFICATIONS_API, "Notification Task failed for notification {}", notificationRequest);
			}
			
		});
		ResponseEntity<String> responseEntity = new ResponseEntity<>(
				"Request accepted succesfully", HttpStatus.OK);
		deferredResult.setResult(responseEntity);
		return deferredResult;
	}
}
