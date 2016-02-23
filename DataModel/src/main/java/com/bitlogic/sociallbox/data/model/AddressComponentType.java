package com.bitlogic.sociallbox.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="address_component_type")
@XmlRootElement
public class AddressComponentType {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String name;
	
	@Column
	private String description;
	
	public AddressComponentType(){
		super();
	}
	
	public AddressComponentType(String name){
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AddressComponentType)
			return this.name.equals(((AddressComponentType) obj).getName());
		else return false;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return "AddressComponentType [name = "+this.name+ " ]";
	}
}
