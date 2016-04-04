package com.bitlogic.sociallbox.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Ankit.Joinwal
 *
 */
@Entity
@Table(name="SMART_DEVICE",indexes = { @Index(name = "IDX_SM_DEVICE", columnList = "UNIQUE_ID",unique=true) })
@XmlRootElement(name="smartDevice")
@XmlAccessorType(XmlAccessType.NONE)
public class SmartDevice implements Serializable{


	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@XmlTransient
	@JsonIgnore
	@Column(name="ID")
	private Long id;
	
	@Column(nullable=false,name="UNIQUE_ID",length=100)
	@XmlElement
	@NotNull(message="error.uniqueid.mandatory")
	private String uniqueId;
	
	@Column(nullable=false,name="BUILD_VERSION",length=20)
	@XmlElement
	@NotNull(message="error.build.version.mandatory")
	private String buildVersion;
	
	@Column(nullable=false,name="OS_VERSION",length=20)
	@XmlElement
	@NotNull(message="error.osversion.mandatory")
	private String osVersion;
	
	@Column(nullable=false,name="DEVICE_TYPE",length=20)
	@XmlElement
	@NotNull(message="error.device.type.mandatory")
	@Enumerated(EnumType.STRING)
	private DeviceType deviceType;
	
	@Column(nullable=false,name="GCM_ID")
	@JsonIgnore
	@NotNull(message="error.gcm.id.mandatory")
	private String gcmId;
	
	
	 @ManyToOne(fetch=FetchType.LAZY)
	 @JsonIgnore
	 @XmlTransient
	 @JoinColumn(name="USER_ID",nullable=false)
	private User user;
	
	@Column(nullable=false,name="IS_ENABLED",length=5)
	@XmlTransient
	@JsonIgnore
	private Boolean isEnabled;
	
	@XmlTransient
	@JsonIgnore
	@Column(nullable=false,name="CREATE_DT")
	private Date createDt;
	
	@XmlTransient
	@JsonIgnore
	@Column(nullable=false,name="PRIVATE_KEY",length=50)
	private String privateKey;
	
	@JsonProperty
	public String getPrivateKey() {
		return privateKey;
	}

	@JsonIgnore
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	@JsonIgnore
	public Long getId() {
		return id;
	}

	@JsonProperty
	public void setId(Long id) {
		this.id = id;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public String getBuildVersion() {
		return buildVersion;
	}

	public void setBuildVersion(String buildVersion) {
		this.buildVersion = buildVersion;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@JsonIgnore
	public Date getCreateDt() {
		return createDt;
	}

	@JsonProperty
	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}
	
	@JsonIgnore
	public String getGcmId() {
		return gcmId;
	}

	@JsonProperty
	public void setGcmId(String gcmId) {
		this.gcmId = gcmId;
	}

	@Override
	public boolean equals(Object obj) {
		 if (obj == null)
	      {
	         return false;
	      }
	      if (getClass() != obj.getClass())
	      {
	         return false;
	      }
	      final SmartDevice sd = (SmartDevice) obj;
	      
		return Objects.equals(this.uniqueId,sd.uniqueId);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.uniqueId);
	}
	@Override
	public String toString() {
		return "SmartDevice : [ uniqueId = "  + uniqueId+ " , " +
				"buildVersion = " + buildVersion+ " , " +
				"osVersion = "  + osVersion + " , " +
				"deviceType = " + deviceType.toString() + " , " +
				"isEnabled = " + isEnabled +" , " +
				"]";
	}
	
	
}
