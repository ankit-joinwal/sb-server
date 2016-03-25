package com.bitlogic.sociallbox.service.dao;

import java.util.List;

import com.bitlogic.sociallbox.data.model.EventOrganizer;
import com.bitlogic.sociallbox.data.model.EventOrganizerAdmin;


public interface EventOrganizerDAO {

	public EventOrganizer createEO(EventOrganizer eventOrganizer);
	
	public EventOrganizer getEODetails(String organizerId);
	
	public EventOrganizer getEOByName(String name);
	
	public EventOrganizerAdmin createEOAdmin(EventOrganizerAdmin eventOrganizerAdmin);
	
	public List<EventOrganizerAdmin> getPendingEOAdminProfiles();
	
	public List<EventOrganizerAdmin> getEOAdminProfilesByIds(List<Long> profileIds);
	
	public EventOrganizerAdmin getEOAdminProfileById(Long profileId);
}
