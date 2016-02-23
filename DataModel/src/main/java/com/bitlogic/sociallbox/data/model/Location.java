package com.bitlogic.sociallbox.data.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@Embeddable
public class Location implements Serializable{

	private static final long serialVersionUID = 1L;

	@NotNull
	@XmlElement
	private String name;
	
	@NotNull
	@XmlElement
	@Column( precision=8, scale=2)
	private Double lattitude;
	
	@NotNull
	@XmlElement
	@Column( precision=8, scale=2)
	private Double longitude;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public Double getLattitude() {
		return lattitude;
	}

	public void setLattitude(Double lattitude) {
		this.lattitude = lattitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	
	
	public Double getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return "Location [ name = "+this.name + " , lattitude = "+ this.lattitude + " , longitude = "+this.longitude + " ]";
	}
	
}
