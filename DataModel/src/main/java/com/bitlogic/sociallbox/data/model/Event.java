package com.bitlogic.sociallbox.data.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "EVENT",indexes = { @Index(name = "IDX_EVENT", columnList = "TITLE,END_DT") })
public class Event {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID", unique = true,length=50)
	private String uuid;

	@Column(length = 75,name="TITLE",nullable=false)
	private String title;

	@Column(length = 1000,name="DESCRIPTION",nullable=false)
	private String description;

	@OneToMany(mappedBy="event",cascade=CascadeType.ALL)
	@JsonIgnore
	private List<EventImage> eventImages;
	
	@OneToOne(mappedBy="event",cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	private EventDetails eventDetails;

	@ManyToMany
	@JoinTable(name = "EVENT_TAGS", joinColumns = { @JoinColumn(name = "EVENT_ID") }, inverseJoinColumns = { @JoinColumn(name = "TAG_ID") })
	private Set<EventTag> tags = new HashSet<>();
	
	@OneToMany(mappedBy="eventAtMeetup",fetch= FetchType.LAZY)
	private Set<Meetup> meetupsAtEvent = new HashSet<>();
	
	@Column(name="EVENT_STATUS",nullable=false,length=20)
	@Enumerated(EnumType.STRING)
	private EventStatus eventStatus;
	
	@Column(name="ALLOW_EVENT_TO_GO_LIVE",length=5)
	private Boolean isAllowedEventToGoLive;
	
	@Column(name="START_DT",nullable=false)
	private Date startDate;

	@Column(name="END_DT",nullable=false)
	private Date endDate;


	public EventStatus getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(EventStatus eventStatus) {
		this.eventStatus = eventStatus;
	}

	public Boolean getIsAllowedEventToGoLive() {
		return isAllowedEventToGoLive;
	}

	public void setIsAllowedEventToGoLive(Boolean isAllowedEventToGoLive) {
		this.isAllowedEventToGoLive = isAllowedEventToGoLive;
	}

	public List<EventImage> getEventImages() {
		return eventImages;
	}

	public void setEventImages(List<EventImage> eventImages) {
		this.eventImages = eventImages;
	}

	public Set<Meetup> getMeetupsAtEvent() {
		return meetupsAtEvent;
	}

	public void setMeetupsAtEvent(Set<Meetup> meetupsAtEvent) {
		this.meetupsAtEvent = meetupsAtEvent;
	}

	public Set<EventTag> getTags() {
		return tags;
	}

	public void setTags(Set<EventTag> tags) {
		this.tags = tags;
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

	

	

	public EventDetails getEventDetails() {
		return eventDetails;
	}

	public void setEventDetails(EventDetails eventDetails) {
		this.eventDetails = eventDetails;
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


	
	@Override
	public String toString() {
		return "Event [title="+this.title+" , organizer="+this.eventDetails.getOrganizer().getUuid()+" ]";
	}

}
