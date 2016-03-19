package com.bitlogic.sociallbox.data.model;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name = "meetup_attendee")
public class MeetupAttendee {

	@JsonProperty
	private Long id;
	
	@JsonProperty(value="user_id")
	@NotNull(message="error.attendee.id.mandatory")
	private Long userId;
	
	@JsonProperty(value="profile_pic")
	private String profilePic;
	
	@JsonProperty
	private String name;
	
	@JsonProperty(value="response")
	private AttendeeResponse attendeeResponse;
	
	@JsonProperty(value="is_admin")
	@NotNull(message="error.attendee.isAdmin.mandatory")
	private Boolean isAdmin;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AttendeeResponse getAttendeeResponse() {
		return attendeeResponse;
	}

	public void setAttendeeResponse(AttendeeResponse attendeeResponse) {
		this.attendeeResponse = attendeeResponse;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	
}
