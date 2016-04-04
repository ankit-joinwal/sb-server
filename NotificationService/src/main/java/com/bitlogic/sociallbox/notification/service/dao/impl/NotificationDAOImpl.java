package com.bitlogic.sociallbox.notification.service.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.notifications.NotificationEntity;
import com.bitlogic.sociallbox.notification.service.dao.AbstractDAO;
import com.bitlogic.sociallbox.notification.service.dao.NotificationDAO;

@Repository("notificationDAO")
public class NotificationDAOImpl extends AbstractDAO implements NotificationDAO {

	@Autowired
	private Environment environment;
	
	public void createNotifications(List<NotificationEntity> notifications) {

		Session session = getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Integer batchSize = Integer.parseInt(environment.getRequiredProperty(Constants.HIBERNATE_JDBC_BATCH_SIZE));
			Integer count = 0;
			
			for (NotificationEntity notification : notifications) {
				System.out.println("Processing "+count+" item in notification list");
				session.save(notification);
				if (count % batchSize == 0) {
					session.flush();
					session.clear();
				}

				count = count + 1;
			}
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		
		tx.commit();
		session.close();
	};
}
