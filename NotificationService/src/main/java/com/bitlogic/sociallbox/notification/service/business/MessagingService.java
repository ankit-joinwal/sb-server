package com.bitlogic.sociallbox.notification.service.business;

import com.bitlogic.sociallbox.data.model.notifications.GCMNotification;
import com.bitlogic.sociallbox.data.model.notifications.GCMNotificationResponse;

public interface MessagingService {

	public GCMNotificationResponse sendNotification(GCMNotification gcmNotification);
}
