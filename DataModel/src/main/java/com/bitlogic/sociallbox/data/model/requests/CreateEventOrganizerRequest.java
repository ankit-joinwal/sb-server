package com.bitlogic.sociallbox.data.model.requests;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.bitlogic.sociallbox.data.model.Address;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name="event_organizer")
public class CreateEventOrganizerRequest implements Serializable{

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private String uuid;
	
	@NotNull(message="error.name.mandatory")
	@JsonProperty("name")
	private String name;
	
	@NotNull(message="error.address.mandatory")
	@JsonProperty("address")
	private Address address;

	@NotNull(message="error.phone.mandatory")
	@JsonProperty("phone1")
	private String phone1;
	
	@JsonProperty("phone2")
	private String phone2;
	
	@JsonProperty("phone3")
	private String phone3;
	
	@NotNull(message="error.email.mandatory")
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
	
	@Override
	public String toString() {
		return "CreateEventOrganizerRequest [name="+this.name+" , email= "+this.emailId+ " ]";
	}
}
