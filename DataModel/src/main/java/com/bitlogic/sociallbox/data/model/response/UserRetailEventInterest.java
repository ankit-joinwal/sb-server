package com.bitlogic.sociallbox.data.model.response;

import com.bitlogic.sociallbox.data.model.EventTag;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("user_retail_interest")
public class UserRetailEventInterest {

	@JsonProperty("tag")
	private EventTag eventTag;
	
	@JsonProperty("is_interest")
	private Boolean isUserInterest = Boolean.FALSE;

	public EventTag getEventTag() {
		return eventTag;
	}

	public void setEventTag(EventTag eventTag) {
		this.eventTag = eventTag;
	}

	public Boolean getIsUserInterest() {
		return isUserInterest;
	}

	public void setIsUserInterest(Boolean isUserInterest) {
		this.isUserInterest = isUserInterest;
	}
	
}
