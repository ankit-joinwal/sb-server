package com.bitlogic.sociallbox.data.model.requests;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.bitlogic.sociallbox.data.model.EventImage;
import com.bitlogic.sociallbox.data.model.EventTag;
import com.bitlogic.sociallbox.data.model.Location;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.ext.google.GooglePlace;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name="createEvent")
@XmlAccessorType(XmlAccessType.NONE)
public class CreateEventRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@NotNull(message="error.title.mandatory")
	private String title;
	
	@NotNull(message="error.description.mandatory")
	private String description;
	
	private EventImage image;
	
	@NotNull(message="error.event.details.mandatory")
	@JsonProperty("event_details")
	private MockEventDetails eventDetails;
	
	public static final class MockEventDetails{

		private Location location;

		private Set<GooglePlace.Result.AddressComponent> addressComponents = new HashSet<>();
		
		public Set<GooglePlace.Result.AddressComponent> getAddressComponents() {
			return addressComponents;
		}

		public void setAddressComponents(Set<GooglePlace.Result.AddressComponent> addressComponents) {
			this.addressComponents = addressComponents;
		}

		public Location getLocation() {
			return location;
		}

		public void setLocation(Location location) {
			this.location = location;
		}

	}
	
	@NotNull(message="error.start.date.mandatory")
	private String startDate;
	
	@NotNull(message="error.end.date.mandatory")
	private String endDate;
	
	@NotNull(message="error.event.tags.mandatory")
	private Set<EventTag> tags = new HashSet<>();
	
	@NotNull(message="error.event.organizer.mandatory")
	@JsonProperty("profile_id")
	private Long organizerProfileId;

	public Long getOrganizerProfileId() {
		return organizerProfileId;
	}

	public void setOrganizerProfileId(Long profileId) {
		this.organizerProfileId = profileId;
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

	public EventImage getImage() {
		return image;
	}

	public void setImage(EventImage image) {
		this.image = image;
	}

	public MockEventDetails getEventDetails() {
		return eventDetails;
	}

	public void setEventDetails(MockEventDetails eventDetails) {
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


	@Override
	public String toString() {
		return "CreateEventRequest [title="+this.title+ " , description="+this.description
				+" , eventDetails="+this.eventDetails+" , image="+this.image+ " , start="+this.startDate+" , ends="+this.endDate+ " ]";
	}
	
}
