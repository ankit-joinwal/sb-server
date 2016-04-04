package com.bitlogic.sociallbox.notification.service.util;

import java.util.Arrays;

import com.bitlogic.sociallbox.data.model.notifications.Notification;
import com.bitlogic.sociallbox.data.model.notifications.NotificationMessage;
import com.bitlogic.sociallbox.data.model.notifications.NotificationMessage.DataPayload;
import com.bitlogic.sociallbox.data.model.notifications.NotificationMessage.NotificationPayload;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {

	public static void main(String[] args) throws Exception{

		Notification notification = new Notification();
		notification.setType("NEW_FRIEND_NOTIFICATION");
		notification.setRecieverIds(Arrays.asList(17L,16L));
		
		NotificationMessage notificationMessage = new NotificationMessage();
		DataPayload dataPayload = new DataPayload();
		dataPayload.setActionURL("");
		dataPayload.setImage("");
		dataPayload.setActor("John Doe");
		dataPayload.setType("NEW_FRIEND_NOTIFICATION");
		dataPayload.setTarget("SociallBox");
		dataPayload.setIcon("https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xla1/v/t1.0-1/p200x200/12316467_10206731945876364_3008257792416820623_n.jpg?oh=cece300cd2db2d885c81f2c00b6a7d84&oe=578A9FB4&__gda__=1465178287_36f0dafbe70beb7506ebd22f8f089edf");

		NotificationPayload notificationPayload = new NotificationPayload();
		notificationPayload.setTitle("Friend Joined You");
		notificationPayload.setBody("John Doe is now on SociallBox");
		
		notificationMessage.setDataPayload(dataPayload);
		notificationMessage.setNotificationPayload(notificationPayload);
		
		notification.setNotificationMessage(notificationMessage);
		
		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println(objectMapper.writeValueAsString(notification));
		
	}

}
