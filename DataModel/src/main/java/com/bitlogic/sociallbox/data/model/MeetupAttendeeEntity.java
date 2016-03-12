package com.bitlogic.sociallbox.data.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "MEETUP_ATTENDEES")
public class MeetupAttendeeEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public MeetupAttendeeEntity() {
		super();
	}

	public MeetupAttendeeEntity(MeetupAttendee meetupAttendee) {
		super();
		this.attendeeResponse = meetupAttendee.getAttendeeResponse()==null ? AttendeeResponse.MAYBE:meetupAttendee.getAttendeeResponse();
		this.isAdmin = meetupAttendee.getIsAdmin();
	}

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long attendeeId;

	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	@Column(name = "ATTENDEE_RESPONSE")
	@Enumerated(EnumType.STRING)
	private AttendeeResponse attendeeResponse;

	@Column(name = "COMMENTS")
	private String comments;

	@Column(name = "IS_ADMIN")
	private Boolean isAdmin;

	@ManyToOne
	@JoinColumn(name = "MEETUP_ID")
	private Meetup meetup;
	
	@Column(name="CREATE_DT")
	private Date createDt;
	
	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public AttendeeResponse getAttendeeResponse() {
		return attendeeResponse;
	}

	public void setAttendeeResponse(AttendeeResponse attendeeResponse) {
		this.attendeeResponse = attendeeResponse;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Meetup getMeetup() {
		return meetup;
	}

	public void setMeetup(Meetup meetup) {
		this.meetup = meetup;
	}

}
