package com.bitlogic.sociallbox.notification.service.business;

import org.springframework.util.concurrent.ListenableFuture;

import com.bitlogic.sociallbox.data.model.notifications.Notification;

public interface NotificationService {

	public ListenableFuture<String> sendNotifications(Notification notification);
}
