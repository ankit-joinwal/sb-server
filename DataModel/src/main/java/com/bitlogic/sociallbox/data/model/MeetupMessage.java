package com.bitlogic.sociallbox.data.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "MEETUP_MESSAGES",indexes = { @Index(name = "IDX_MEET_MSG", columnList = "MEETUP_ID") })
@XmlRootElement
public class MeetupMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	@Column(nullable=false,name="MESSAGE")
	@JsonProperty(value = "message")
	private String message;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SENDER_ID")
	@JsonProperty
	private MeetupAttendeeEntity meetupAttendee;

	@ManyToOne
	@JsonIgnore
	@XmlTransient
	@JoinColumn(name = "MEETUP_ID")
	private Meetup meetup;

	@XmlTransient
	@JsonIgnore
	@Column(nullable = false,name="CREATE_DT")
	private Date createDt;

	@Transient
	@JsonProperty
	@XmlTransient
	private String timeToDisplay;
	
	public String getTimeToDisplay() {
		return timeToDisplay;
	}

	public void setTimeToDisplay(String timeToDisplay) {
		this.timeToDisplay = timeToDisplay;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public Meetup getMeetup() {
		return meetup;
	}

	public void setMeetup(Meetup meetup) {
		this.meetup = meetup;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MeetupAttendeeEntity getMeetupAttendee() {
		return meetupAttendee;
	}

	public void setMeetupAttendee(MeetupAttendeeEntity meetupAttendee) {
		this.meetupAttendee = meetupAttendee;
	}

	@Override
	public String toString() {
		return "Message =" + this.message;
	}

}
