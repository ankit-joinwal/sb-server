package com.bitlogic.sociallbox.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ROLE")
public class Role {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="ROLE_TYPE")
	private String userRoleType ;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserRoleType() {
		return userRoleType;
	}

	public void setUserRoleType(String userRoleType) {
		this.userRoleType = userRoleType;
	}

	@Override
	public String toString() {
		return "ROLE="+this.userRoleType;
	}
}
