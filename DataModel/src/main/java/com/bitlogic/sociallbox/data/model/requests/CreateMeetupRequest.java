package com.bitlogic.sociallbox.data.model.requests;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.bitlogic.sociallbox.data.model.Location;
import com.bitlogic.sociallbox.data.model.MeetupAttendeeEntity;
import com.bitlogic.sociallbox.data.model.ext.google.GooglePlace;

@XmlRootElement(name="meetup")
@XmlAccessorType(XmlAccessType.NONE)
public class CreateMeetupRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@NotNull(message="error.title.mandatory")
	private String title;
	
	@NotNull(message="error.description.mandatory")
	private String description;
	
	@NotNull(message="error.location.mandatory")
	private Location location;
	
	@NotNull(message="error.start.date.mandatory")
	private String startDate;
	
	@NotNull(message="error.end.date.mandatory")
	private String endDate;
	
	private String organizerId;
	
	private String eventAtMeetup;
	
	private Boolean isPublic;
	
	private List<MeetupAttendeeEntity> attendees;
	
	private Set<GooglePlace.Result.AddressComponent> addressComponents;
	
	private String deviceId;


	public String getDeviceId() {
		return deviceId;
	}


	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}


	public Boolean getIsPublic() {
		return isPublic;
	}


	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}


	public Set<GooglePlace.Result.AddressComponent> getAddressComponents() {
		return addressComponents;
	}

	
	public String getEventAtMeetup() {
		return eventAtMeetup;
	}


	public void setEventAtMeetup(String eventAtMeetup) {
		this.eventAtMeetup = eventAtMeetup;
	}


	public void setAddressComponents(
			Set<GooglePlace.Result.AddressComponent> addressComponents) {
		this.addressComponents = addressComponents;
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

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	public List<MeetupAttendeeEntity> getAttendees() {
		return attendees;
	}

	public void setAttendees(List<MeetupAttendeeEntity> attendees) {
		this.attendees = attendees;
	}
	
	
	@Override
	public String toString() {
		return "meetupRequest [ title = "+this.title+" , description = "+this.description+" , location = "+ location.toString()+ 
				" , organizer = "+this.organizerId + " , startDate = "+this.startDate + " , endDate = "+this.endDate + " ]";
	}

}
