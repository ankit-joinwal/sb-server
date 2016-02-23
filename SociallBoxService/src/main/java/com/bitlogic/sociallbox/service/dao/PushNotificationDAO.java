package com.bitlogic.sociallbox.service.dao;

import java.util.List;

import com.bitlogic.sociallbox.data.model.PushNotificationSettingMaster;

public interface PushNotificationDAO {

	public List<PushNotificationSettingMaster> getPushNotTypes();
	
}
