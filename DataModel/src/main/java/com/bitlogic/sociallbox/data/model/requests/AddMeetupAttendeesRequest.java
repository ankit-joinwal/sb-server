package com.bitlogic.sociallbox.data.model.requests;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.bitlogic.sociallbox.data.model.MeetupAttendee;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name="add_meetup_attendees")
public class AddMeetupAttendeesRequest{
	
	@JsonIgnore
	private String meetupId;

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

	public String getMeetupId() {
		return meetupId;
	}

	public void setMeetupId(String uuid) {
		this.meetupId = uuid;
	}

	
	
	
}
