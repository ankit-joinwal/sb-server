package com.bitlogic.sociallbox.data.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name = "friend")
@XmlAccessorType(XmlAccessType.NONE)
public class UserFriend implements Serializable{


	private static final long serialVersionUID = 1L;

	@JsonProperty
	private String profilePic;
	
	@JsonProperty
	private String name;
	
	@JsonProperty
	private String emailId;

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
