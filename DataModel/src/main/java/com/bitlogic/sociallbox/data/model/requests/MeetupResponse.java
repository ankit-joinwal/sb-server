package com.bitlogic.sociallbox.data.model.requests;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.bitlogic.sociallbox.data.model.Location;
import com.bitlogic.sociallbox.data.model.MeetupAttendeeEntity;
import com.bitlogic.sociallbox.data.model.MeetupImage;
import com.bitlogic.sociallbox.data.model.MeetupMessage;
import com.bitlogic.sociallbox.data.model.MeetupStatus;
import com.bitlogic.sociallbox.data.model.response.UserPublicProfile;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class MeetupResponse  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XmlElement
	@JsonProperty("id")
	private String uuid;

	@XmlElement
	@JsonProperty("title")
	private String title;

	@XmlElement
	private String description;

	@XmlElement
	@JsonProperty("location")
	private Location location;

	@XmlElement
	@JsonProperty("start_date")
	private String startDate;

	@XmlElement
	@JsonProperty("end_date")
	private String endDate;

	@XmlElement
	@JsonProperty("organizer")
	private UserPublicProfile organizer;
	
	@JsonProperty("status")
	private MeetupStatus status;
	
	@XmlElement
	@JsonProperty("meetup_access_url")
	private String url;
	
	@JsonProperty("display_pic")
	private MeetupImage displayImage;
	
	@XmlElement
	@JsonProperty("event_at_meetup")
	private String eventAtMeetup;
	
	
	public MeetupStatus getStatus() {
		return status;
	}

	public void setStatus(MeetupStatus status) {
		this.status = status;
	}

	public MeetupImage getDisplayImage() {
		return displayImage;
	}

	public void setDisplayImage(MeetupImage displayImage) {
		this.displayImage = displayImage;
	}

	public String getEventAtMeetup() {
		return eventAtMeetup;
	}

	public void setEventAtMeetup(String eventAtMeetup) {
		this.eventAtMeetup = eventAtMeetup;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
	
	

	public UserPublicProfile getOrganizer() {
		return organizer;
	}

	public void setOrganizer(UserPublicProfile organizer) {
		this.organizer = organizer;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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



}
