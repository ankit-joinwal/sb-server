package com.bitlogic.sociallbox.data.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name = "meetup_attendee")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "MEETUP_ATTENDEE")
public class MeetupAttendee implements Serializable{

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long attendeeId;

	@XmlElement(name="social_detail")
	@JsonProperty(value="social_detail")
	@ManyToOne
	@JoinColumn(name="SOCIAL_DETAILS_ID")
	private UserSocialDetail socialDetail;
	
	@Column(name="ATTENDEE_NAME")
	@JsonProperty
	private String name;
	
	@Column(name="ATTENDEE_RESPONSE")
	@XmlElement(name="response")
	@JsonProperty(value="response")
	private AttendeeResponse attendeeResponse;
	
	@Column(name="COMMENTS")
	@XmlElement(name="comments")
	@JsonProperty(value="comments")
	private String comments;
	
	@Column(name="IS_ADMIN")
	@XmlElement(name="is_admin")
	@JsonProperty(value="is_admin")
	private String isAdmin;
	
	 @ManyToOne
	 @JsonIgnore
	 @XmlTransient
	 @JoinColumn(name="MEETUP_ID")
	 private Meetup meetup;

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Long getAttendeeId() {
		return attendeeId;
	}

	public void setAttendeeId(Long attendeeId) {
		this.attendeeId = attendeeId;
	}

	public UserSocialDetail getSocialDetail() {
		return socialDetail;
	}

	public void setSocialDetail(UserSocialDetail socialDetail) {
		this.socialDetail = socialDetail;
	}

	public AttendeeResponse getAttendeeResponse() {
		return attendeeResponse;
	}

	public void setAttendeeResponse(AttendeeResponse attendeeResponse) {
		this.attendeeResponse = attendeeResponse;
	}
	
	
	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	

	public Meetup getMeetup() {
		return meetup;
	}

	public void setMeetup(Meetup meetup) {
		this.meetup = meetup;
	}
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "MeetupAttendee [ socialDetail= "+this.socialDetail+" , isAdmin = "+this.isAdmin +" ]";
	}
}
