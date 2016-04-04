package com.bitlogic.sociallbox.notification.service.business.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.SmartDevice;
import com.bitlogic.sociallbox.data.model.notifications.GCMNotification;
import com.bitlogic.sociallbox.data.model.notifications.GCMNotificationResponse;
import com.bitlogic.sociallbox.data.model.notifications.GCMNotificationResponse.Result;
import com.bitlogic.sociallbox.data.model.notifications.Notification;
import com.bitlogic.sociallbox.data.model.notifications.NotificationEntity;
import com.bitlogic.sociallbox.data.model.notifications.NotificationMessage;
import com.bitlogic.sociallbox.data.model.notifications.NotificationMessage.DataPayload;
import com.bitlogic.sociallbox.data.model.notifications.NotificationMessage.NotificationPayload;
import com.bitlogic.sociallbox.data.model.notifications.NotificationType;
import com.bitlogic.sociallbox.data.model.notifications.ResponseStatus;
import com.bitlogic.sociallbox.notification.service.business.MessagingService;
import com.bitlogic.sociallbox.notification.service.business.NotificationService;
import com.bitlogic.sociallbox.notification.service.dao.NotificationDAO;
import com.bitlogic.sociallbox.notification.service.dao.SmartDeviceDAO;
import com.bitlogic.sociallbox.notification.service.util.LoggingService;

@Service("notificationService")
@Transactional
public class NotificationServiceImpl extends LoggingService implements NotificationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);
	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	
	@Autowired
	private SmartDeviceDAO smartDeviceDAO;
	
	@Autowired
	private MessagingService messagingService;
	
	@Autowired
	private NotificationDAO notificationDAO;
	
	@Override
	@Async
	public ListenableFuture<String> sendNotifications(Notification notification) {
		String LOG_PREFIX = "NotificationServiceImpl-sendNotifications";
		
		logInfo(LOG_PREFIX, " {}", notification);
		/*
		 * Get NotificationType 
		 */
		String notificationTypeString = notification.getType();
		NotificationType notificationType = NotificationType.getTypeFrom(notificationTypeString);
		if(notificationType==null){
			logError(LOG_PREFIX, "Invalid NotificationType in request {}", notificationTypeString);
			//TODO
			return null;
		}
		
		/*
		 * Get Recieved Message
		 */
		NotificationMessage recievedMessage = notification.getNotificationMessage();
		
		/*
		 * Get User Ids from request
		 */
		List<Long> userIds = notification.getRecieverIds();
		if(userIds==null || userIds.isEmpty()){
			logWarning(LOG_PREFIX, "No Users in request");
			return null;
		}
		/*
		 * Construct OutPut Message
		 */
		NotificationMessage outputMessage = constructOutputMessage(recievedMessage, notificationType);
		
		/*
		 * GCMNotification to build
		 */
		GCMNotification gcmNotification = new GCMNotification();
		
		/*
		 * Set Recievers in Notification
		 */
		List<SmartDevice> devicesList = this.smartDeviceDAO.getDevicesByUserIds(userIds);
		
		setRecievers(gcmNotification, devicesList);
		
		/*
		 * Set Priority of Message
		 */
		gcmNotification.setPriority(notificationType.getPriority());
		
		/*
		 * Restricted Package Name
		 */
		gcmNotification.setRestrictedPackageName(Constants.PACKAGE_NAME_FOR_NOTIFICATIONS);
		
		/*
		 * Notification Payload
		 */
		gcmNotification.setNotificationPayload(outputMessage.getNotificationPayload());
		
		/*
		 * Data Payload
		 */
		gcmNotification.setDataPayload(outputMessage.getDataPayload());
		Date now = new Date();
		GCMNotificationResponse notificationResponse = this.messagingService.sendNotification(gcmNotification);
		logInfo(LOG_PREFIX, "Response recieved for Notification {}", notificationResponse);
		
		/*
		 * Process Response & Persist
		 */
		if(notificationResponse.getStatus() != ResponseStatus.FAILURE){
			processResponse(notificationResponse, devicesList, gcmNotification, now);
		}else{
			logError(LOG_PREFIX, "Error recieved from GCM service for Notification {} as {}", gcmNotification,notificationResponse);
		}
		return new AsyncResult<String>("Notifications sent successfully");
	}
	
	private void processResponse(GCMNotificationResponse gcmNotificationResponse , List<SmartDevice> deviceList,GCMNotification notification,Date notificationDate){
		String LOG_PREFIX = "NotificationServiceImpl-processResponse";
		List<Result> results = gcmNotificationResponse.getResults();
		Integer notificationId = gcmNotificationResponse.getMulticastId();
		logInfo(LOG_PREFIX, "Success Message Count {}", gcmNotificationResponse.getSuccessMessageCount());
		logInfo(LOG_PREFIX, "Failed Message Count {}", gcmNotificationResponse.getFailedMessageCount());
		Integer count = 0;
		List<NotificationEntity> notifications = new ArrayList<NotificationEntity>(deviceList.size());
		for (SmartDevice device : deviceList){
			String errorResponse = (count > results.size()-1) ? null : results.get(count).getErrorMessage();
			NotificationEntity notificationEntity = new NotificationEntity();
			notificationEntity.setCreateDate(notificationDate);
			notificationEntity.setDataPayload(notification.getDataPayload());
			notificationEntity.setNotificationPayload(notification.getNotificationPayload());
			notificationEntity.setDeviceId(device.getId());
			notificationEntity.setErrorMessage(errorResponse);
			notificationEntity.setGcmId(device.getGcmId());
			notificationEntity.setGcmNotificationId(notificationId);
			notifications.add(notificationEntity);
			count++;
		}
		logInfo(LOG_PREFIX, "Creating Notifications in DB");
		this.notificationDAO.createNotifications(notifications);
		logInfo(LOG_PREFIX, "Create notification successfully");
		
	}
	
	private void setRecievers(GCMNotification gcmNotification ,List<SmartDevice> devicesList){
		String LOG_PREFIX = "NotificationServiceImpl-setRecievers";
		if(devicesList ==null || devicesList.isEmpty() ){
			logWarning(LOG_PREFIX, "No devices found for users ");
		}else if (devicesList.size()==1){
			logInfo(LOG_PREFIX, "GCM IDs of Devices to send notification to :{}", devicesList);
			gcmNotification.setReciever(devicesList.get(0).getGcmId());
			
		}else if (devicesList.size() >1){
			logInfo(LOG_PREFIX, "GCM IDs of Devices to send notification to :{}", devicesList);
			List<String> gcmIds = new ArrayList<String>(devicesList.size());
			for(SmartDevice device : devicesList){
				gcmIds.add(device.getGcmId());
			}
			gcmNotification.setRecievers(gcmIds);
		}
	}
	
	
	private NotificationMessage constructOutputMessage(NotificationMessage recievedMessage,NotificationType notificationType){
		NotificationMessage outputMessage = new NotificationMessage();
		NotificationPayload notificationPayload = recievedMessage.getNotificationPayload();
		//Set icon from Notification Type. This is used to display notification item on phone notifications tray
		notificationPayload.setIcon(notificationType.getIcon());
		notificationPayload.setClickAction(notificationType.getClickAction());

		DataPayload dataPayload = recievedMessage.getDataPayload();
		dataPayload.setType(notificationType.getType());
		dataPayload.setVerb(notificationType.getVerb());
		
		outputMessage.setDataPayload(dataPayload);
		outputMessage.setNotificationPayload(notificationPayload);
		
		return outputMessage;
	}
}
