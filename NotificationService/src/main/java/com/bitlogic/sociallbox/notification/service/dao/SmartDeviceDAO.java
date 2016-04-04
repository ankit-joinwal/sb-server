package com.bitlogic.sociallbox.notification.service.dao;

import java.util.List;

import com.bitlogic.sociallbox.data.model.SmartDevice;

public interface SmartDeviceDAO {

	public List<SmartDevice> getDevicesByUserIds(List<Long> userIds);
}
