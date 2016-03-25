package com.bitlogic.sociallbox.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="PUSH_NOTIFICATION_SETTING_MASTER")
public class PushNotificationSettingMaster {

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@Column(nullable=false,name="NAME",length=50)
	private String name;
	
	@Column(nullable=false,name="DISPLAY_NAME",length=50)
	private String displayName;
	
	@Column(nullable=false,name="DISPLAY_ORDER")
	private Integer displayOrder;

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

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	
	
}
