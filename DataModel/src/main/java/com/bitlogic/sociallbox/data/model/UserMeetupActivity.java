package com.bitlogic.sociallbox.data.model;

import com.bitlogic.sociallbox.data.model.requests.MeetupResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserMeetupActivity {

	@JsonProperty("meetup")
	private MeetupResponse meetup;

	private Boolean isOrganizer;
	
	public Boolean getIsOrganizer() {
		return isOrganizer;
	}

	public void setIsOrganizer(Boolean isOrganizer) {
		this.isOrganizer = isOrganizer;
	}

	public MeetupResponse getMeetup() {
		return meetup;
	}

	public void setMeetup(MeetupResponse meetup) {
		this.meetup = meetup;
	}

		
	
}
