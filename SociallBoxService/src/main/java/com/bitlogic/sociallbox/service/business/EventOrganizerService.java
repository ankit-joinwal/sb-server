package com.bitlogic.sociallbox.service.business;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.EOAdminStatus;
import com.bitlogic.sociallbox.data.model.EventOrganizer;
import com.bitlogic.sociallbox.data.model.EventOrganizerAdmin;
import com.bitlogic.sociallbox.data.model.requests.CreateEventOrganizerRequest;
import com.bitlogic.sociallbox.data.model.response.EOAdminProfile;

public interface EventOrganizerService {

	@PreAuthorize("hasAnyRole('"+Constants.ROLE_TYPE_ADMIN+"','"+Constants.ROLE_ORGANIZER+"')")
	public EventOrganizer create(CreateEventOrganizerRequest organizerRequest);
	
	public EventOrganizer getOrganizerDetails(String organizerId);
	
	@PreAuthorize("hasRole('"+Constants.ROLE_TYPE_ADMIN+"')")
	public List<EOAdminProfile> getPendingProfiles();
	
	@PreAuthorize("hasRole('"+Constants.ROLE_TYPE_ADMIN+"')")
	public void approveOrRejectProfiles(List<Long> profileIds,EOAdminStatus status);
	
	public EventOrganizerAdmin createEOAdmin(EventOrganizerAdmin eventOrganizerAdmin);
	
	public EventOrganizerAdmin getEOAdminById(Long eoAdminId);
	
	public EventOrganizerAdmin getEOAdminByUserId(Long userId);
	
}
