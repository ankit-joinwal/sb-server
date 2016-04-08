package com.bitlogic.sociallbox.data.model.requests;

import javax.validation.constraints.NotNull;

import com.bitlogic.sociallbox.data.model.Location;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("meetup")
public class EditMeetupRequest {

	private String meetupId;
	
	@NotNull(message="error.title.mandatory")
	private String title;
	
	@NotNull(message="error.description.mandatory")
	private String description;
	
	@NotNull(message="error.location.mandatory")
	private Location location;
	
	@NotNull(message="error.start.date.mandatory")
	@JsonProperty("start_date")
	private String startDate;
	
	@NotNull(message="error.end.date.mandatory")
	@JsonProperty("end_date")
	private String endDate;
	
	@JsonProperty("device_id")
	private String deviceId;
	
	@JsonProperty("is_private")
	private Boolean isPrivate;
	
	

	public String getMeetupId() {
		return meetupId;
	}

	public void setMeetupId(String meetupId) {
		this.meetupId = meetupId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Boolean getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	
	
	
}
