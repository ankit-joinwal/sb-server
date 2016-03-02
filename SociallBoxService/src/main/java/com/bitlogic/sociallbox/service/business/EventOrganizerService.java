package com.bitlogic.sociallbox.service.business;

import org.springframework.security.access.prepost.PreAuthorize;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.requests.CreateEventOrganizerRequest;
import com.bitlogic.sociallbox.data.model.response.EventOrganizerProfile;

public interface EventOrganizerService {

	@PreAuthorize("hasRole('"+Constants.ROLE_TYPE_ADMIN+"')")
	public EventOrganizerProfile create(CreateEventOrganizerRequest organizerRequest);
	
	public EventOrganizerProfile get(String organizerId);
}
