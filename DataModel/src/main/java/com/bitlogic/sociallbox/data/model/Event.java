package com.bitlogic.sociallbox.data.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "EVENT")
@XmlRootElement(name = "event")
public class Event {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID", unique = true)
	private String uuid;

	@Column(length = 75,name="TITLE")
	private String title;

	@Column(length = 1000,name="DESCRIPTION")
	private String description;

	@OneToMany(mappedBy="event",cascade=CascadeType.ALL)
	@JsonIgnore
	private List<EventImage> eventImages;
	
	@OneToOne(mappedBy="event",cascade = CascadeType.ALL)
	private EventDetails eventDetails;

	@ManyToMany
	@JoinTable(name = "EVENT_TAGS", joinColumns = { @JoinColumn(name = "EVENT_ID") }, inverseJoinColumns = { @JoinColumn(name = "TAG_ID") })
	private Set<EventTag> tags = new HashSet<>();
	
	@OneToMany(mappedBy="eventAtMeetup",fetch= FetchType.LAZY)
	private Set<Meetup> meetupsAtEvent = new HashSet<>();
	
	
	@Column(name="IS_LIVE")
	private String isLive;
	
	@Column(name="START_DT")
	private Date startDate;

	@Column(name="END_DT")
	private Date endDate;

	
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

	public String getIsLive() {
		return isLive==null ? "false":isLive;
	}

	public void setIsLive(String isLive) {
		this.isLive = isLive;
	}
	
	@Override
	public String toString() {
		return "Event [title="+this.title+" , organizer="+this.eventDetails.getOrganizer().getId()+" ]";
	}

}
