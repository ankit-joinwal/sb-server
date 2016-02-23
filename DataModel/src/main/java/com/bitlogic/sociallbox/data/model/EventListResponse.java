package com.bitlogic.sociallbox.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name="events")
public class EventListResponse implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@XmlElement
	@JsonProperty
	private List<EventResponse> events = new ArrayList<>();

	private Integer count;

	public List<EventResponse> getEvents() {
		return events;
	}

	public void setEvents(List<EventResponse> events) {
		this.events = events;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
	
	
}
