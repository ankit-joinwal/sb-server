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
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="EVENT_ORGANIZER",indexes = { @Index(name = "IDX_EVENT_ORG", columnList = "NAME") })
public class EventOrganizer {
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID", unique = true,length=50)
	private String uuid;
	
	@Column(name="NAME",nullable=false,length=100)
	private String name;
	
	@Column(name="WEBSITE")
	private String website;
	
	@Column(name="PROFILE_PIC_URL")
	private String profilePic;
	
	@Column(name="COVER_PIC_URL")
	private String coverPic;
	
	@Embedded
	@Column(name="ADDRESS",nullable=false)
	private Address address;
	
	@Column(name="PHONE1",nullable=false,length=20)
	private String phone1;
	
	@Column(name="PHONE2",length=20)
	private String phone2;
	
	@Column(name="PHONE3",length=20)
	private String phone3;
	
	@Column(name="EMAIL_ID",nullable=false,length=50)
	private String emailId;
	
	@Column(name="CREATE_DT",nullable=false)
	private Date createDt;
	
	@Column(name="IS_ENABLED",nullable=false,length=5)
	private Boolean isEnabled;
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="organizer")
	@JsonIgnore
	private Set<EventOrganizerAdmin> organizerAdmins = new HashSet<EventOrganizerAdmin>();
	
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getCoverPic() {
		return coverPic;
	}

	public void setCoverPic(String coverPic) {
		this.coverPic = coverPic;
	}

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


	public Set<EventOrganizerAdmin> getOrganizerAdmins() {
		return organizerAdmins;
	}

	public void setOrganizerAdmins(Set<EventOrganizerAdmin> organizerAdmins) {
		this.organizerAdmins = organizerAdmins;
	}

	@Override
	public String toString() {
		return "EventOrganizer = [id = "+this.uuid+ " , name = "+this.name+ " ]";
	}
	
	
}
