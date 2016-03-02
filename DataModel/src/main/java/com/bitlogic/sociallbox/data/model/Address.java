package com.bitlogic.sociallbox.data.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
@Embeddable
public class Address implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name="STREET")
	@JsonProperty
	@NotNull(message="error.street.mandatory")
	private String street;
	
	@Column(name="CITY")
	@JsonProperty
	@NotNull(message="error.city.mandatory")
	private String city;
	
	@Column(name="STATE")
	@JsonProperty
	@NotNull(message="error.state.mandatory")
	private String state;
	
	@Column(name="COUNTRY")
	@JsonProperty
	@NotNull(message="error.country.mandatory")
	private String country;
	
	@Column(name="ZIP_CODE")
	@JsonProperty("zip_code")
	@NotNull(message="error.zipcode.mandatory")
	private String zipcode;

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	
}
