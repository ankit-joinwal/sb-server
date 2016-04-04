package com.bitlogic.sociallbox.data.model.response;

import com.bitlogic.sociallbox.data.model.EventType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("user_event_interest")
public class UserEventInterest {
	
	@JsonProperty("type")
	private EventType eventType;
	@JsonProperty("is_interest")
	private Boolean isUserInterest = Boolean.FALSE;
	
	public EventType getEventType() {
		return eventType;
	}
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	public Boolean getIsUserInterest() {
		return isUserInterest;
	}
	public void setIsUserInterest(Boolean isUserInterest) {
		this.isUserInterest = isUserInterest;
	}
	@Override
	public String toString() {
		return "UserEventInterest [eventType = "+this.eventType+" , isUserInterest = "+this.isUserInterest+" ]";
	}
}
