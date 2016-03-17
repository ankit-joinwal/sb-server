package com.bitlogic.sociallbox.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.bitlogic.sociallbox.data.model.requests.SortOrderOption;
import com.bitlogic.sociallbox.data.model.requests.SortOption;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ajoinwal
 *
 */
@Entity
@XmlRootElement(name="CATEGORY")
@XmlAccessorType(XmlAccessType.NONE)
public class Category implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@XmlTransient
	@Column(name="ID")
	private Long id;
	
	@Column(nullable=false,name="NAME")
	@XmlElement
	@NotNull(message="error.name.mandatory")
	private String name;
	
	@Column(name="DESCRIPTION",nullable=false)
	@XmlElement
	@NotNull(message="error.desc.mandatory")
	private String description;
	
	@Column(name="CREATE_DT",nullable=false)
	@XmlTransient
	@JsonIgnore
	private Date createDt;
	
	@Column(name="DISPLAY_ORDER",nullable=false)
	@JsonProperty("display_order")
	private Integer displayOrder;
	
	@Column(name="EXT_ID",nullable=false)
	@JsonIgnore
	private String extId;
	
	@Column(name="SOURCE_SYSTEM",nullable=false)
	@Enumerated(EnumType.STRING)
	private SourceSystemForPlaces systemForPlaces ;
	
	
	
	@JsonIgnore
	@ManyToMany(mappedBy="relatedCategories")
	private Set<EventType> relatedEventTypes = new HashSet<>();
	
	@Transient
	@JsonProperty("sortOptions")
	private List<SortOption> sortByOptions;
	
	@Transient
	@JsonProperty("sortOrderOptions")
	private List<SortOrderOption> sortOrderOptions;
	


	public List<SortOrderOption> getSortOrderOptions() {
		return sortOrderOptions;
	}

	public void setSortOrderOptions(List<SortOrderOption> sortOrderOptions) {
		this.sortOrderOptions = sortOrderOptions;
	}

	public List<SortOption> getSortByOptions() {
		return sortByOptions;
	}

	public void setSortByOptions(List<SortOption> sortByOptions) {
		this.sortByOptions = sortByOptions;
	}

	@JsonIgnore
	public String getExtId() {
		return extId;
	}

	@JsonProperty(value="ext_id")
	public void setExtId(String extId) {
		this.extId = extId;
	}

	@JsonIgnore
	public SourceSystemForPlaces getSystemForPlaces() {
		return systemForPlaces;
	}
	
	@JsonProperty(value="source_system")
	public void setSystemForPlaces(SourceSystemForPlaces systemForPlaces) {
		this.systemForPlaces = systemForPlaces;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Set<EventType> getRelatedEventTypes() {
		return relatedEventTypes;
	}

	public void setRelatedEventTypes(Set<EventType> relatedEventTypes) {
		this.relatedEventTypes = relatedEventTypes;
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


	public Date getCreateDt() {
		return createDt;
	}


	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}


	@Override
	public String toString() {
		return "Category : [ name = "+name+" , description = "+description + " ]";
	}
}
