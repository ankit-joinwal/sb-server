package com.bitlogic.sociallbox.data.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "MEETUP_IMAGES",indexes = { @Index(name = "IDX_MEETUP_IMAGE", columnList = "MEETUP_ID") })
public class MeetupImage {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="NAME",length=50,nullable=false)
	private String name;

	@Column(name="URL",nullable=false)
	private String url;

	@Column(name="MIME_TYPE",length=20)
	private String mimeType;
	
	@ManyToOne
	@JoinColumn(name = "MEETUP_ID",nullable=false)
	@JsonIgnore
	private Meetup meetup;
	
	@ManyToOne
	@JoinColumn(name = "UPLOADED_BY",nullable=false)
	@JsonIgnore
	private User uploadedBy;
	
	public User getUploadedBy() {
		return uploadedBy;
	}
	public void setUploadedBy(User uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	@Column(name="UPLOAD_DATE",nullable=false)
	private Date uploadDt;
	
	public Date getUploadDt() {
		return uploadDt;
	}
	public void setUploadDt(Date uploadDt) {
		this.uploadDt = uploadDt;
	}
	public Meetup getMeetup() {
		return meetup;
	}

	public void setMeetup(Meetup meetup) {
		this.meetup = meetup;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "MeetupImage [name=" + this.name + " ]";
	}

}
