package com.bitlogic.sociallbox.data.model.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="update_profile_request")
public class UpdateEOAdminProfileRequest {
	
	@JsonIgnore
	private Long userId;
	
	@JsonProperty("new_password")
	private String newPassword;
	
	@JsonProperty("old_password")
	private String oldPassword;
	
	@JsonProperty("name")
	private String name;

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
	
	
}
