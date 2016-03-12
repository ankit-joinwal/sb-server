package com.bitlogic.sociallbox.service.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bitlogic.sociallbox.data.model.Role;
import com.bitlogic.sociallbox.data.model.SmartDevice;
import com.bitlogic.sociallbox.data.model.User;

@Repository("smartDeviceDAO")
public class SmartDeviceDAOImpl extends AbstractDAO implements SmartDeviceDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(SmartDeviceDAOImpl.class);
	
	@Override
	public SmartDevice getSmartDeviceByDeviceId(String deviceId) {
		Criteria criteria = getSession().createCriteria(SmartDevice.class).add(Restrictions.eq("uniqueId", deviceId));
		return (SmartDevice) criteria.uniqueResult();
	}

	
	@Override
	public List<Role> getUserRolesByDevice(String deviceId) {
		LOGGER.info("### Inside SmartDeviceDAOImpl.getUserRolesByDevice for DeviceId : {} ###",deviceId);
		Criteria criteria = getSession().createCriteria(User.class)
				.createAlias("smartDevices", "sd")
				.add(Restrictions.eq("sd.uniqueId", deviceId))
				.add(Restrictions.eq("sd.isEnabled", Boolean.TRUE))
				.add(Restrictions.eq("isEnabled", Boolean.TRUE))
				.setFetchMode("userroles", FetchMode.JOIN);
		User user = (User) criteria.uniqueResult();
		if(user!=null){
			if(user.getUserroles()!=null && !user.getUserroles().isEmpty()){
				LOGGER.info("User found with roles : {} ",user.getUserroles());
				return new ArrayList<Role>(user.getUserroles());
			}else{
				LOGGER.error("No Roles exist for user corresponding to device");
			}
		}else{
			LOGGER.error("No active user found corresponding to device ");
		}
		return null;
	}
	
	@Override
	public User getUserInfoFromDeviceId(String deviceId) {
		Criteria criteria = getSession().createCriteria(User.class)
				.createAlias("smartDevices", "sd")
				.add(Restrictions.eq("sd.uniqueId", deviceId))
				.add(Restrictions.eq("sd.isEnabled", Boolean.TRUE))
				.add(Restrictions.eq("isEnabled", Boolean.TRUE));
		User user = (User)criteria.uniqueResult();
		
		return user;
	}
}
