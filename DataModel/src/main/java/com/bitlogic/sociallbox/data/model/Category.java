package com.bitlogic.sociallbox.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	private Date createDt;
	
	@Column(name="DISPLAY_ORDER")
	private Integer displayOrder;
	
	@Column(name="NAV_URL")
	private String navURL;
	
	@JsonIgnore
	@ManyToMany(mappedBy="relatedCategories")
	private Set<EventType> relatedEventTypes = new HashSet<>();
	
	public String getNavURL() {
		return navURL;
	}

	public void setNavURL(String navURL) {
		this.navURL = navURL;
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
