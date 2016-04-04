package com.bitlogic.sociallbox.notification.service.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	protected SessionFactory getSessionFactory(){
		return sessionFactory;
	}

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public Object save(Object entity){
		return getSession().save(entity);
	}

	public void saveOrUpdate(Object entity){
		getSession().saveOrUpdate(entity);
	}
	public void persist(Object entity) {
		getSession().persist(entity);
	}

	public void delete(Object entity) {
		getSession().delete(entity);
	}
	
	public void merge(Object entity){
		getSession().merge(entity);
	}
	
}
