package com.bitlogic.sociallbox.data.model;

import com.bitlogic.sociallbox.data.model.response.EventResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserEventActivity {
	
	@JsonProperty("event")
	private EventResponse event;
	
	@JsonProperty("type")
	private ActivityType type;

	public ActivityType getType() {
		return type;
	}

	public void setType(ActivityType type) {
		this.type = type;
	}

	public enum ActivityType{
		GOING , 
		ATTENDED,
		INTERESTED
	}
	public EventResponse getEvent() {
		return event;
	}

	public void setEvent(EventResponse event) {
		this.event = event;
	}
	
	
}
