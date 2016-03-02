package com.bitlogic.sociallbox.data.model.response;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.UserSocialDetail;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name="user_profile")
public class UserPublicProfile {

	public UserPublicProfile() {
		super();
	}
	
	public UserPublicProfile(User user){
		super();
		this.id = user.getId();
		this.name = user.getName();
	}
	
	@JsonProperty
	private Long id;
	
	@JsonProperty
	private String name;
	
	@JsonProperty("social_details")
	private Set<UserSocialDetail> socialDetails = new HashSet<>();

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

	public Set<UserSocialDetail> getSocialDetails() {
		return socialDetails;
	}

	public void setSocialDetails(Set<UserSocialDetail> socialDetails) {
		this.socialDetails = socialDetails;
	}
	
	
}
