package com.bitlogic.sociallbox.notification.service.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.bitlogic.sociallbox.data.model.SmartDevice;
import com.bitlogic.sociallbox.notification.service.dao.AbstractDAO;
import com.bitlogic.sociallbox.notification.service.dao.SmartDeviceDAO;

@Repository("smartDeviceDAO")
public class SmartDeviceDAOImpl extends AbstractDAO implements SmartDeviceDAO{

	
	@Override
	public List<SmartDevice> getDevicesByUserIds(List<Long> userIds) {
		String sql = "SELECT * FROM SMART_DEVICE WHERE USER_ID IN ( :userIds ) AND IS_ENABLED = 'T' ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addEntity(SmartDevice.class);
		query.setParameterList("userIds", userIds);
		
		List results = query.list();
		List<SmartDevice> devices = new ArrayList<SmartDevice>();
		System.out.println("Found : "+results.size()+" devices");
		if(results!=null && !results.isEmpty()){
			for(Iterator iterator = results.iterator(); iterator.hasNext();){
				SmartDevice smartDevice = (SmartDevice) iterator.next();
				devices.add(smartDevice);
			}
		}
		return devices;
	}
}
