package com.bitlogic.sociallbox.data.model.response;

import javax.xml.bind.annotation.XmlRootElement;

import com.bitlogic.sociallbox.data.model.Address;
import com.bitlogic.sociallbox.data.model.EventOrganizer;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name="event_organizer")
public class EventOrganizerProfile {
	
	public EventOrganizerProfile() {
		super();
	}
	
	public EventOrganizerProfile(EventOrganizer eventOrganizer){
		super();
		this.address = eventOrganizer.getAddress();
		this.emailId = eventOrganizer.getEmailId();
		this.name = eventOrganizer.getName();
		this.phone1 = eventOrganizer.getPhone1();
		this.phone2 = eventOrganizer.getPhone2();
		this.phone3 = eventOrganizer.getPhone3();
		this.uuid = eventOrganizer.getUuid();
	}
	
	@JsonProperty("id")
	private String uuid;

	@JsonProperty
	private String name;

	@JsonProperty
	private Address address;
	
	@JsonProperty
	private String phone1;
	
	@JsonProperty
	private String phone2;

	@JsonProperty
	private String phone3;

	@JsonProperty("email_id")
	private String emailId;
	
	

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getPhone3() {
		return phone3;
	}

	public void setPhone3(String phone3) {
		this.phone3 = phone3;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	
	
}
