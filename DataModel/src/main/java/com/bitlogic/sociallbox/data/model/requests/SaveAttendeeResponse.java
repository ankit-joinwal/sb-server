package com.bitlogic.sociallbox.data.model.requests;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.bitlogic.sociallbox.data.model.AttendeeResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name="attendee_response_req")
public class SaveAttendeeResponse {
	
	@XmlElement
	@JsonProperty
	private AttendeeResponse attendeeResponse;
	
	@JsonIgnore
	@XmlTransient
	private String meetupId;
	
	@JsonIgnore
	@XmlTransient
	private Long attendeeId;

	public AttendeeResponse getAttendeeResponse() {
		return attendeeResponse;
	}

	public void setAttendeeResponse(AttendeeResponse attendeeResponse) {
		this.attendeeResponse = attendeeResponse;
	}

	public String getMeetupId() {
		return meetupId;
	}

	public void setMeetupId(String meetupId) {
		this.meetupId = meetupId;
	}

	public Long getAttendeeId() {
		return attendeeId;
	}

	public void setAttendeeId(Long attendeeId) {
		this.attendeeId = attendeeId;
	}
	
	@Override
	public String toString() {
		return "AttendeeResponse = "+this.attendeeResponse;
	}
	
}
