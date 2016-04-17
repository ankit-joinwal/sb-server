package com.bitlogic.sociallbox.data.model.response;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name = "friend")
public class UserFriend implements Serializable{


	private static final long serialVersionUID = 1L;

	@JsonProperty
	private Long id;
	
	@JsonProperty
	private String profilePic;
	
	@JsonProperty
	private String name;
	
	@JsonProperty
	private String emailId;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	@Override
	public String toString() {
		return "Friend [name = "+this.name +" ]";
	}
	
}
