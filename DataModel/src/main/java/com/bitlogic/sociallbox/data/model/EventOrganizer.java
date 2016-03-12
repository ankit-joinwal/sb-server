package com.bitlogic.sociallbox.data.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="EVENT_ORGANIZER")
public class EventOrganizer {
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID", unique = true)
	private String uuid;
	
	@Column(name="NAME",nullable=false)
	private String name;
	
	@Embedded
	@Column(name="ADDRESS",nullable=false)
	private Address address;
	
	@Column(name="PHONE1",nullable=false)
	private String phone1;
	
	@Column(name="PHONE2")
	private String phone2;
	
	@Column(name="PHONE3")
	private String phone3;
	
	@Column(name="EMAIL_ID",nullable=false)
	private String emailId;
	
	@Column(name="CREATE_DT",nullable=false)
	private Date createDt;
	
	@Column(name="IS_ENABLED",nullable=false)
	private Boolean isEnabled;
	
	@OneToMany(mappedBy="organizer",cascade=CascadeType.ALL)
	@JsonIgnore
	private Set<EventDetails> events = new HashSet<EventDetails>();

	@OneToMany(cascade=CascadeType.ALL)
	@JoinTable(
	            name="ORGANIZER_ADMINS",
	            joinColumns = @JoinColumn( name="ORGANIZER_ID"),
	            inverseJoinColumns = @JoinColumn( name="USER_ID")
	    )
	@JsonIgnore
	private Set<User> organizerAdmins = new HashSet<>();

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

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Set<EventDetails> getEvents() {
		return events;
	}

	public void setEvents(Set<EventDetails> events) {
		this.events = events;
	}

	public Set<User> getOrganizerAdmins() {
		return organizerAdmins;
	}

	public void setOrganizerAdmins(Set<User> organizerAdmins) {
		this.organizerAdmins = organizerAdmins;
	}
	
	
	
}
