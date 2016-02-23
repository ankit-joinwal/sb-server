package com.bitlogic.sociallbox.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "EVENT_IMAGE")
public class EventImage {

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "EVENT_ID")
	@JsonIgnore
	private Event event;

	@Column(name="NAME")
	private String name;

	@Column(name="URL")
	private String url;

	@Column(name="DISPLAY_ORDER")
	private Integer displayOrder;
	
	@Column(name="MIME_TYPE")
	private String mimeType;
	
	
	
	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
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
		return "EventImage [name=" + this.name + " ]";
	}
}
