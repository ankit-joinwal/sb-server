package com.bitlogic.sociallbox.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="MEETUP")
@XmlRootElement(name = "meetup")
@XmlAccessorType(XmlAccessType.NONE)
public class Meetup implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID", unique = true)
	private String uuid;

	@Column(name="TITLE",nullable=false)
	@XmlElement
	private String title;

	@Column(length=500,name="DESCRIPTION",nullable=false)
	@XmlElement
	private String description;

	@Embedded
	@Column(name="LOCATION",nullable=false)
	@XmlElement
	private Location location;

	@Column(name="START_DT",nullable=false)
	private Date startDate;

	@Column(name="END_DT",nullable=false)
	private Date endDate;
	
	@Column(name="IS_PUBLIC",nullable=false)
	private String isPublic;
	
	@OneToOne
	@JoinColumn(name="ORGANIZER_ID",nullable=false)
	private User organizer;
	
	@OneToMany(mappedBy="meetup")
	@Cascade(value=CascadeType.ALL)
	private Set<MeetupAttendeeEntity> attendees = new HashSet<>();
	
	@OneToMany(mappedBy="meetup")
	@Cascade(value=CascadeType.ALL)
	private Set<MeetupMessage> messages = new HashSet<>();
	
	@OneToMany(mappedBy="meetup",fetch=FetchType.LAZY)
	@Cascade(value=CascadeType.ALL)
	@JsonIgnore
	private Set<MeetupAddressInfo> addressComponents = new HashSet<>();

	@OneToMany(mappedBy="meetup")
	@Cascade(value=CascadeType.ALL)
	@JsonIgnore
	private List<MeetupImage> images = new ArrayList<>();
	
	@OneToOne
	@JoinColumn(name="EVENT_ID")
	private Event eventAtMeetup;
	
	@Column(name="CREATE_DT",nullable=false)
	private Date createdDt;
	
	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}

	public List<MeetupImage> getImages() {
		return images;
	}

	public void setImages(List<MeetupImage> images) {
		this.images = images;
	}

	public Event getEventAtMeetup() {
		return eventAtMeetup;
	}

	public void setEventAtMeetup(Event eventAtMeetup) {
		this.eventAtMeetup = eventAtMeetup;
	}

	@JsonIgnore
	public Set<MeetupAddressInfo> getAddressComponents() {
		return addressComponents;
	}

	@JsonProperty
	public void setAddressComponents(Set<MeetupAddressInfo> addressComponents) {
		this.addressComponents = addressComponents;
	}

	public Set<MeetupMessage> getMessages() {
		return messages;
	}

	public void setMessages(Set<MeetupMessage> messages) {
		this.messages = messages;
	}

	public Set<MeetupAttendeeEntity> getAttendees() {
		return attendees;
	}

	public void setAttendees(Set<MeetupAttendeeEntity> attendees) {
		this.attendees = attendees;
	}

	public User getOrganizer() {
		return organizer;
	}

	public void setOrganizer(User organizer) {
		this.organizer = organizer;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


}
