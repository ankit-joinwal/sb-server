package com.bitlogic.sociallbox.data.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="ORGANIZER_ADMINS")
public class EventOrganizerAdmin {

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "USER_ID",nullable=false)
	private User user;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "ORGANIZER_ID",nullable=false)
	private EventOrganizer organizer;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20,name="STATUS",nullable=false)
	private EOAdminStatus status;
	
	@Column(nullable=false,name="CREATE_DT")
	private Date createDt;

	@Column(name="UPD_DT")
	private Date updateDt;
	
	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public Date getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

	public EOAdminStatus getStatus() {
		return status;
	}

	public void setStatus(EOAdminStatus status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public EventOrganizer getOrganizer() {
		return organizer;
	}

	public void setOrganizer(EventOrganizer organizer) {
		this.organizer = organizer;
	}
	
	@Override
	public String toString() {
		return "EventOrganizerAdmin = [profileId = "+this.id+ " ]";
	}
	
}
