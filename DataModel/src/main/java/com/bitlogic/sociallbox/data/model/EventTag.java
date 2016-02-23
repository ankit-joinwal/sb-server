package com.bitlogic.sociallbox.data.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="TAG")
@XmlRootElement(name="tag")
public class EventTag {

	@Id
	@GeneratedValue
	@XmlTransient
	@JsonIgnore
	@Column(name="ID")
	private Long id;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@JsonIgnore
	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "EVENT_TYPE_TAGS", joinColumns = { @JoinColumn(name = "TAG_ID") }, inverseJoinColumns = { @JoinColumn(name = "EVENT_TYPE_ID") })
	private Set<EventType> relatedEventTypes = new HashSet<>();
	
	@ManyToMany(mappedBy="tags")
	@JsonIgnore
	@XmlTransient
	private Set<Event> taggedEvents =  new HashSet<>();
	

	public Set<Event> getTaggedEvents() {
		return taggedEvents;
	}

	public void setTaggedEvents(Set<Event> taggedEvents) {
		this.taggedEvents = taggedEvents;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonIgnore
	public Set<EventType> getRelatedEventTypes() {
		return relatedEventTypes;
	}

	@JsonProperty
	public void setRelatedEventTypes(Set<EventType> relatedEventTypes) {
		this.relatedEventTypes = relatedEventTypes;
	}
	
	@Override
	public String toString() {
		return "Tag [name="+this.name+" , description="+this.description+ " ]";
	}
	
}
