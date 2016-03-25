package com.bitlogic.sociallbox.data.model;

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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "USER_SETTINGS")
@XmlRootElement(name = "settings")
public class UserSetting {
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	@Column(nullable=false,name="SETTING_TYPE",length=50)
	@Enumerated(EnumType.STRING)
	private UserSettingType settingType;

	@Column(nullable=false,name="NAME",length=50)
	private String name;

	@Column(nullable=false,name="DISPLAY_NAME",length=100)
	private String displayName;
	
	@Column(nullable=false,name="VALUE",length=100)
	private String value;

	@ManyToOne
	@JsonIgnore
	@XmlTransient
	@JoinColumn(name = "USER_ID",nullable=false)
	private User user;

	@Column(nullable=false,name="CREATE_DT")
	@JsonIgnore
	private Date createDt;
	
	@Column(name="UPDATE_DT")
	@JsonIgnore
	private Date updateDt;
	
	public Date getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserSettingType getSettingType() {
		return settingType;
	}

	public void setSettingType(UserSettingType settingType) {
		this.settingType = settingType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Setting [name=" + this.name + " ,type=" + this.settingType
				+ " ,value=" + this.value + " ]";
	}

}
