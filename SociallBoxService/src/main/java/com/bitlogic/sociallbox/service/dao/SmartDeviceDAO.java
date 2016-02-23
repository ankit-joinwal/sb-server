package com.bitlogic.sociallbox.service.dao;

import java.util.List;

import com.bitlogic.sociallbox.data.model.Role;
import com.bitlogic.sociallbox.data.model.SmartDevice;
import com.bitlogic.sociallbox.data.model.User;

public interface SmartDeviceDAO {

	public SmartDevice getSmartDeviceByDeviceId(String deviceId);
	
	public List<Role> getUserRolesByDevice(String deviceId);
	
	public User getUserInfoFromDeviceId(String deviceId);
}
