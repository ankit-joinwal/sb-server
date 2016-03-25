package com.bitlogic.sociallbox.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ROLE")
public class Role {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="ROLE_TYPE",length=20)
	@Enumerated(EnumType.STRING)
	private UserRoleType userRoleType ;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserRoleType getUserRoleType() {
		return userRoleType;
	}

	public void setUserRoleType(UserRoleType userRoleType) {
		this.userRoleType = userRoleType;
	}

	@Override
	public String toString() {
		return "ROLE="+this.userRoleType;
	}
}
