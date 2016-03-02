package com.bitlogic.sociallbox.service.dao;

import com.bitlogic.sociallbox.data.model.EventOrganizer;


public interface EventOrganizerDAO {

	public EventOrganizer create(EventOrganizer eventOrganizer);
	
	public EventOrganizer getOrganizerDetails(String organizerId);
	
	public EventOrganizer getOrganizerByName(String name);
}
