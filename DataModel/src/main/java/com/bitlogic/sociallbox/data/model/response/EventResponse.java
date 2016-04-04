package com.bitlogic.sociallbox.data.model.response;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.bitlogic.sociallbox.data.model.EventDetails;
import com.bitlogic.sociallbox.data.model.EventImage;
import com.bitlogic.sociallbox.data.model.EventStatus;
import com.bitlogic.sociallbox.data.model.EventTag;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name="event")
@XmlAccessorType(XmlAccessType.NONE)
public class EventResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@NotNull
	@JsonProperty("id")
	private String uuid;

	@NotNull
	private String title;
	
	@NotNull
	private String description;
	
	@JsonProperty("distance_from_src")
	private String distanceFromSource;
	
	@NotNull
	@JsonProperty("event_detail")
	private EventDetailsResponse eventDetails;
	
	@NotNull
	@JsonProperty("start_date")
	private String startDate;
	
	@NotNull
	@JsonProperty("end_date")
	private String endDate;
	
	private Set<EventTag> tags = new HashSet<>();
	
	@JsonProperty("is_user_fav")
	private Boolean userFavEvent = Boolean.FALSE;
	
	@JsonProperty("is_user_going")
	private Boolean isUserGoing = Boolean.FALSE;
	
	@NotNull
	@JsonProperty("display_image")
	private EventImage displayImage;
	
	@JsonProperty("status")
	private EventStatus eventStatus;
	
	@JsonProperty("is_free")
	private Boolean isFree;
	
	
	public Boolean getIsUserGoing() {
		return isUserGoing;
	}

	public void setIsUserGoing(Boolean isUserGoing) {
		this.isUserGoing = isUserGoing;
	}

	public EventStatus getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(EventStatus eventStatus) {
		this.eventStatus = eventStatus;
	}

	public Boolean getIsFree() {
		return isFree;
	}

	public void setIsFree(Boolean isFree) {
		this.isFree = isFree;
	}

	public Boolean getUserFavEvent() {
		return userFavEvent;
	}

	public void setUserFavEvent(Boolean userFavEvent) {
		this.userFavEvent = userFavEvent;
	}

	public String getDistanceFromSource() {
		return distanceFromSource;
	}

	public void setDistanceFromSource(String distanceFromSource) {
		this.distanceFromSource = distanceFromSource;
	}

	public EventImage getDisplayImage() {
		return displayImage;
	}

	public void setDisplayImage(EventImage displayImage) {
		this.displayImage = displayImage;
	}

	public Set<EventTag> getTags() {
		return tags;
	}

	public void setTags(Set<EventTag> tags) {
		this.tags = tags;
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


	public EventDetailsResponse getEventDetails() {
		return eventDetails;
	}

	public void setEventDetails(EventDetailsResponse eventDetails) {
		this.eventDetails = eventDetails;
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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
