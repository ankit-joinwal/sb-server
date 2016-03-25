package com.bitlogic.sociallbox.service.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.bitlogic.sociallbox.data.model.PushNotificationSettingMaster;
import com.bitlogic.sociallbox.service.dao.AbstractDAO;
import com.bitlogic.sociallbox.service.dao.PushNotificationDAO;

@Repository("pushNotificationDAO")
public class PushNotificationDAOImpl extends AbstractDAO implements PushNotificationDAO{

	@Override
	public List<PushNotificationSettingMaster> getPushNotTypes() {
		Criteria criteria = getSession().createCriteria(PushNotificationSettingMaster.class)
				.addOrder(Order.asc("displayOrder"));
		return criteria.list();
	}
	
	
}
