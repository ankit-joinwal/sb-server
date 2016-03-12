package com.bitlogic.sociallbox.data.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name="social_detail")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name="USER_SOCIAL_DETAILS")
public class UserSocialDetail implements Serializable,Cloneable{
	

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@XmlTransient
	@JsonIgnore
	@Column(name="ID")
	private Long id;
	
	@XmlTransient
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;

	@XmlElement(name="system")
	@JsonProperty(value="system")
	@Column(nullable=false,name="SOCIAL_SYSTEM")
	@Enumerated(EnumType.STRING)
	private SocialSystem socialSystem;
	
	@XmlElement(name="detail")
	@JsonProperty(value="detail")
	@Column(nullable=false,name="SOCIAL_DETAIL")
	private String userSocialDetail;
	
	@XmlElement(name="detailType")
	@JsonProperty(value="detailType")
	@Column(nullable=false,name="DETAIL_TYPE")
	@Enumerated(EnumType.STRING)
	private SocialDetailType socialDetailType;

	
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

	public SocialSystem getSocialSystem() {
		return socialSystem;
	}

	public void setSocialSystem(SocialSystem socialSystem) {
		this.socialSystem = socialSystem;
	}

	public String getUserSocialDetail() {
		return userSocialDetail;
	}

	public void setUserSocialDetail(String userSocialDetail) {
		this.userSocialDetail = userSocialDetail;
	}

	public SocialDetailType getSocialDetailType() {
		return socialDetailType;
	}

	public void setSocialDetailType(SocialDetailType socialDetailType) {
		this.socialDetailType = socialDetailType;
	}
	
	@Override
	public String toString() {
		return "SocialDetail [ socialSystem="+this.socialSystem+ " , userSocialDetail = "
				+ this.userSocialDetail+ " , socialDetailType = "+ this.socialDetailType
				+ " ]";
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		UserSocialDetail clone = new UserSocialDetail();
		clone.setId(getId());
		clone.setSocialDetailType(getSocialDetailType());
		clone.setSocialSystem(getSocialSystem());
		clone.setUserSocialDetail(getUserSocialDetail());
		return clone;
	}
}
