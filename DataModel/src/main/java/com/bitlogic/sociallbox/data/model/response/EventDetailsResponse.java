package com.bitlogic.sociallbox.data.model.response;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.bitlogic.sociallbox.data.model.EventAddressInfo;
import com.bitlogic.sociallbox.data.model.EventDetails;
import com.bitlogic.sociallbox.data.model.Location;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name="event_details")
public class EventDetailsResponse {

	
	public EventDetailsResponse() {
		super();
	}
	
	public EventDetailsResponse(EventDetails eventDetails){
		super();
		this.location = eventDetails.getLocation();
		this.addressComponents = eventDetails.getAddressComponents();
		this.bookingUrl = eventDetails.getBookingUrl();
	}
	
	@XmlElement
	@JsonProperty
	private Location location;

	@JsonProperty
	private EventOrganizerProfile organizer;
	
	@JsonProperty("booking_url")
	private String bookingUrl;
	
	@JsonIgnore
	private Set<EventAddressInfo> addressComponents = new HashSet<>();
	
	
	public String getBookingUrl() {
		return bookingUrl;
	}

	public void setBookingUrl(String bookingUrl) {
		this.bookingUrl = bookingUrl;
	}

	public Set<EventAddressInfo> getAddressComponents() {
		return addressComponents;
	}

	public void setAddressComponents(Set<EventAddressInfo> addressComponents) {
		this.addressComponents = addressComponents;
	}


	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	

	public EventOrganizerProfile getOrganizer() {
		return organizer;
	}

	public void setOrganizer(EventOrganizerProfile organizer) {
		this.organizer = organizer;
	}

	
	@Override
	public String toString() {
		return "EventDetails [location="+this.location+ " , organizer="+this.organizer+" ]";
	}

}
