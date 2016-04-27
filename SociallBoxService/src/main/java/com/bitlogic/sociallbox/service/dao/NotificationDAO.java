package com.bitlogic.sociallbox.service.dao;

import java.util.List;

import com.bitlogic.sociallbox.data.model.notifications.NotificationEntity;

public interface NotificationDAO {
	
	public List<NotificationEntity> getNotificationsForDevice(Long deviceId,Integer page, Long fromId );
}
