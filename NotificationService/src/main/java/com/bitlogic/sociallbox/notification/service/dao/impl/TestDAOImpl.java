package com.bitlogic.sociallbox.notification.service.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.bitlogic.sociallbox.data.model.EventType;
import com.bitlogic.sociallbox.notification.service.dao.AbstractDAO;
import com.bitlogic.sociallbox.notification.service.dao.TestDAO;

@Repository("testDAO")
public class TestDAOImpl extends AbstractDAO implements TestDAO{

	@Override
	public List<EventType> getAllTypes() {
		Criteria criteria = getSession().createCriteria(EventType.class);
				
		return criteria.list();
	}
}
