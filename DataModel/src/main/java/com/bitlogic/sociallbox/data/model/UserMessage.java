package com.bitlogic.sociallbox.data.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="message")
@Entity
@Table(name="USER_MESSAGES")
public class UserMessage {

	@Id
	@GeneratedValue
	@Column(name="MESSAGE_ID")
	private Long id;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "USER_ID",nullable=false)
	private User user;
	
	@Column(name="MESSAGE",nullable=false)
	private String message;
	
	@JsonIgnore
	@Column(name="CREATE_DT",nullable=false)
	private Date createDt;
	
	@Transient
	@JsonProperty("time")
	private String time;
	
	@JsonProperty("is_read")
	@Column(name="IS_READ",length=5)
	private Boolean isRead;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}
	
	
	
	
}
