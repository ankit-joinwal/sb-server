package com.bitlogic.sociallbox.service.transformers;

import java.util.ArrayList;
import java.util.List;

import com.bitlogic.sociallbox.data.model.notifications.Notification;
import com.bitlogic.sociallbox.data.model.notifications.NotificationEntity;
import com.bitlogic.sociallbox.service.exception.ServiceException;

public class NotificationTransformer implements Transformer<List<Notification>, List<NotificationEntity>>{

	@Override
	public List<Notification> transform(List<NotificationEntity> notifications)
			throws ServiceException {
		List<Notification> notificationsResponse = new ArrayList<>();
		if(notifications!=null && !notifications.isEmpty()){
			for(NotificationEntity notificationEntity : notifications){
				
			}
		}
		return null;
	}
}
