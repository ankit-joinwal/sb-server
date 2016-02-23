package com.bitlogic.sociallbox.data.model.requests;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.bitlogic.sociallbox.data.model.Location;
import com.bitlogic.sociallbox.data.model.MeetupAttendee;
import com.bitlogic.sociallbox.data.model.MeetupImage;
import com.bitlogic.sociallbox.data.model.MeetupMessage;
import com.bitlogic.sociallbox.data.model.User;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class MeetupResponse  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XmlElement
	private String uuid;

	@XmlElement
	private String title;

	@XmlElement
	private String description;

	@XmlElement
	private Location location;

	@XmlElement
	private String startDate;

	@XmlElement
	private String endDate;

	@XmlElement
	private User organizer;
	
	@XmlElement
	private String url;
	
	private MeetupImage displayImage;
	
	@XmlElement
	private String eventAtMeetup;
	
	
	
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

	@XmlElement
	private Set<MeetupAttendee> attendees;
	
	@XmlElement
	private Set<MeetupMessage> messages = new HashSet<>();

	
	
	public Set<MeetupMessage> getMessages() {
		return messages;
	}

	public void setMessages(Set<MeetupMessage> messages) {
		this.messages = messages;
	}

	public Set<MeetupAttendee> getAttendees() {
		return attendees;
	}

	public void setAttendees(Set<MeetupAttendee> attendees) {
		this.attendees = attendees;
	}

	public User getOrganizer() {
		return organizer;
	}

	public void setOrganizer(User organizer) {
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
