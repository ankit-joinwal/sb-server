package com.bitlogic.sociallbox.service.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bitlogic.sociallbox.data.model.EOAdminStatus;
import com.bitlogic.sociallbox.data.model.EventOrganizer;
import com.bitlogic.sociallbox.data.model.EventOrganizerAdmin;
import com.bitlogic.sociallbox.service.dao.AbstractDAO;
import com.bitlogic.sociallbox.service.dao.EventOrganizerDAO;

@Repository("eventOrganizerDAO")
public class EventOrganizerDAOImpl extends AbstractDAO implements EventOrganizerDAO{

	@Override
	public EventOrganizer createEO(EventOrganizer eventOrganizer) {
		eventOrganizer.setIsEnabled(Boolean.TRUE);
		String id = (String) save(eventOrganizer);
		return getEODetails(id);
	}
	
	@Override
	public EventOrganizer getEODetails(String organizerId) {
		Criteria criteria = getSession().createCriteria(EventOrganizer.class)
				.add(Restrictions.eq("isEnabled", Boolean.TRUE))
				.add(Restrictions.eq("uuid", organizerId));
		EventOrganizer eventOrganizer = (EventOrganizer) criteria.uniqueResult();
		return eventOrganizer;
	}
	
	@Override
	public EventOrganizer getEOByName(String name) {
		Criteria criteria = getSession().createCriteria(EventOrganizer.class)
				.add(Restrictions.eq("isEnabled", Boolean.TRUE))
				.add(Restrictions.eq("name", name));
		EventOrganizer eventOrganizer = (EventOrganizer) criteria.uniqueResult();
		return eventOrganizer;
	}
	
	@Override
	public EventOrganizerAdmin createEOAdmin(
			EventOrganizerAdmin eventOrganizerAdmin) {
		Long id = (Long) getSession().save(eventOrganizerAdmin);
		eventOrganizerAdmin.setId(id);
		return eventOrganizerAdmin;
	}
	
	@Override
	public List<EventOrganizerAdmin> getPendingEOAdminProfiles() {
		Criteria criteria = getSession().createCriteria(EventOrganizerAdmin.class)
								.setFetchMode("user", FetchMode.JOIN)
								.setFetchMode("organizer", FetchMode.JOIN)
								.add(Restrictions.eq("status", EOAdminStatus.PENDING));
		
		return (List<EventOrganizerAdmin>) criteria.list();
	}
	
	@Override
	public List<EventOrganizerAdmin> getEOAdminProfilesByIds(List<Long> profileIds) {
		Criteria criteria = getSession().createCriteria(EventOrganizerAdmin.class)
							.add(Restrictions.in("id", profileIds));
		return criteria.list();
	}
	
	@Override
	public EventOrganizerAdmin getEOAdminProfileById(Long profileId) {
		Criteria criteria = getSession().createCriteria(EventOrganizerAdmin.class)
				.add(Restrictions.eq("id", profileId));
		return (EventOrganizerAdmin) criteria.uniqueResult();
	}
}
