package com.bitlogic.sociallbox.notification.service.dao;

import java.util.List;

import com.bitlogic.sociallbox.data.model.notifications.NotificationEntity;

public interface NotificationDAO {

	public void createNotifications(List<NotificationEntity> notifications);
}
