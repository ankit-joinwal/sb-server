package com.bitlogic.sociallbox.data.model.response;

import com.bitlogic.sociallbox.data.model.EOAdminStatus;
import com.bitlogic.sociallbox.data.model.EventOrganizerAdmin;
import com.bitlogic.sociallbox.data.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EOAdminProfile extends UserPublicProfile{

	public EOAdminProfile(){
		
	}
	
	public EOAdminProfile(EventOrganizerProfile organizerProfile , EventOrganizerAdmin eoAdmin , User user){
		this.setId(user.getId());
		this.setEventOrganizerProfile(organizerProfile );
		this.setName(user.getName());
		this.setStatus(eoAdmin==null ? null : eoAdmin.getStatus());
		this.setProfileId(eoAdmin ==null ? null : eoAdmin.getId());
	}
	@JsonProperty("profile_id")
	private Long profileId;
	
	@JsonProperty("company_profile")
	private EventOrganizerProfile eventOrganizerProfile;
	
	@JsonProperty("status")
	private EOAdminStatus status;
	
	public Long getProfileId() {
		return profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	public EventOrganizerProfile getEventOrganizerProfile() {
		return eventOrganizerProfile;
	}

	public void setEventOrganizerProfile(EventOrganizerProfile eventOrganizerProfile) {
		this.eventOrganizerProfile = eventOrganizerProfile;
	}

	public EOAdminStatus getStatus() {
		return status;
	}

	public void setStatus(EOAdminStatus status) {
		this.status = status;
	}
	
	
}
