package com.bitlogic.sociallbox.service.dao;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.EventOrganizer;

@Repository("eventOrganizerDAO")
public class EventOrganizerDAOImpl extends AbstractDAO implements EventOrganizerDAO{

	@Override
	public EventOrganizer create(EventOrganizer eventOrganizer) {
		Date now = new Date();
		eventOrganizer.setCreateDt(now);
		eventOrganizer.setIsEnabled(Constants.IS_ENABLED_TRUE);
		String id = (String) save(eventOrganizer);
		return getOrganizerDetails(id);
	}
	
	@Override
	public EventOrganizer getOrganizerDetails(String organizerId) {
		Criteria criteria = getSession().createCriteria(EventOrganizer.class)
				.add(Restrictions.eq("isEnabled", Constants.IS_ENABLED_TRUE))
				.add(Restrictions.eq("uuid", organizerId));
		EventOrganizer eventOrganizer = (EventOrganizer) criteria.uniqueResult();
		return eventOrganizer;
	}
	
	@Override
	public EventOrganizer getOrganizerByName(String name) {
		Criteria criteria = getSession().createCriteria(EventOrganizer.class)
				.add(Restrictions.eq("isEnabled", Constants.IS_ENABLED_TRUE))
				.add(Restrictions.eq("name", name));
		EventOrganizer eventOrganizer = (EventOrganizer) criteria.uniqueResult();
		return eventOrganizer;
	}
}
