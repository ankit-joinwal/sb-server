package com.bitlogic.sociallbox.data.model.response;

import java.text.SimpleDateFormat;

import com.bitlogic.sociallbox.data.model.EOAdminStatus;
import com.bitlogic.sociallbox.data.model.EventOrganizerAdmin;
import com.bitlogic.sociallbox.data.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EOAdminProfile extends UserPublicProfile{

	SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy hh:mm aa");
	public EOAdminProfile(){
		
	}
	
	public EOAdminProfile(EventOrganizerProfile organizerProfile , EventOrganizerAdmin eoAdmin , User user){
		this.setId(user.getId());
		this.setEventOrganizerProfile(organizerProfile);
		this.setProfilePic(user.getProfilePic());
		this.setName(user.getName());
		this.setEmailId(user.getEmailId());
		this.setStatus(eoAdmin==null ? EOAdminStatus.COMPANY_NOT_LINKED : eoAdmin.getStatus());
		this.setProfileId(eoAdmin ==null ? null : eoAdmin.getId());
		if(eoAdmin!=null){
			
			setCreateDt(dateFormat.format(eoAdmin.getCreateDt()));
		}
	}
	
	@JsonProperty("profile_id")
	private Long profileId;
	
	@JsonProperty("profile_pic")
	private String profilePic;
	
	@JsonProperty("email_id")
	private String emailId;
	
	@JsonProperty("company_profile")
	private EventOrganizerProfile eventOrganizerProfile;
	
	@JsonProperty("create_dt")
	private String createDt;
	
	@JsonProperty("status")
	private EOAdminStatus status;
	
	public String getCreateDt() {
		return createDt;
	}

	public void setCreateDt(String createDt) {
		this.createDt = createDt;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

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
