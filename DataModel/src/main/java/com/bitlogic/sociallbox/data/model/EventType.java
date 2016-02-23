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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "EVENT_TYPE")
@XmlRootElement(name="eventType")
public class EventType {

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	@Column(name="NAME")
	@XmlElement
	@JsonProperty
	private String name;

	@Column(name="DESCRIPTION")
	@XmlElement
	@JsonProperty
	private String description;

	@JsonIgnore
	@XmlTransient
	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "CATEGORY_EVENT_TYPE", joinColumns = { @JoinColumn(name = "EVENT_TYPE_ID") }, inverseJoinColumns = { @JoinColumn(name = "CATEGORY_ID") })
	private Set<Category> relatedCategories = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany(mappedBy="relatedEventTypes")
	private Set<EventTag> relatedTags = new HashSet<>();
	
	@Column(name="DISPLAY_ORDER")
	@JsonProperty
	private Integer displayOrder;
	
	
	
	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Set<EventTag> getRelatedTags() {
		return relatedTags;
	}

	public void setRelatedTags(Set<EventTag> relatedTags) {
		this.relatedTags = relatedTags;
	}

	@JsonIgnore
	public Set<Category> getRelatedCategories() {
		return relatedCategories;
	}

	@JsonProperty
	public void setRelatedCategories(Set<Category> relatedCategories) {
		this.relatedCategories = relatedCategories;
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

	@Override
	public String toString() {
		return "EventType [name = " + name + " ]";
	}
}
