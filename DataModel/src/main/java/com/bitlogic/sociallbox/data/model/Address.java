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

	@Column(name="STREET",nullable=false,length=100)
	@JsonProperty
	@NotNull(message="error.street.mandatory")
	private String street;
	
	@Column(name="CITY",nullable=false,length=50)
	@JsonProperty
	@NotNull(message="error.city.mandatory")
	private String city;
	
	@Column(name="STATE",nullable=false,length=50)
	@JsonProperty
	@NotNull(message="error.state.mandatory")
	private String state;
	
	@Column(name="COUNTRY",nullable=false,length=50)
	@JsonProperty
	@NotNull(message="error.country.mandatory")
	private String country;
	
	@Column(name="ZIP_CODE",nullable=false,length=20)
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
