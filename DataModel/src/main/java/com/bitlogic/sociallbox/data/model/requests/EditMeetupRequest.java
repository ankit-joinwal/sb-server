package com.bitlogic.sociallbox.data.model.requests;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.bitlogic.sociallbox.data.model.MeetupAttendee;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name="edit_meetup")
public class EditMeetupRequest{
	
	@JsonIgnore
	private String uuid;
	
	
	@NotNull(message="error.attendees.mandatory")
	@JsonProperty
	@XmlElement
	private List<MeetupAttendee> attendees;

	public List<MeetupAttendee> getAttendees() {
		return attendees;
	}

	public void setAttendees(List<MeetupAttendee> attendees) {
		this.attendees = attendees;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	
	
	
}
