package com.bitlogic.sociallbox.data.model.notifications;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.bitlogic.sociallbox.data.model.notifications.NotificationMessage.DataPayload;
import com.bitlogic.sociallbox.data.model.notifications.NotificationMessage.NotificationPayload;

@Entity
@Table(name="NOTIFICATIONS",indexes = { @Index(name = "IDX_NOTIFICATION", columnList = "DEVICE_ID,CREATE_DATE") })
public class NotificationEntity {

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@Column(name="GCM_NOTIFICATION_ID")
	private Integer gcmNotificationId;
	
	@Column(name="DEVICE_ID",nullable=false)
	private Long deviceId;
	
	@Column(name="GCM_ID",nullable=false)
	private String gcmId;
	
	@Column(name="NOTIFICATION_PAYLOAD",nullable=false)
	private NotificationPayload notificationPayload;
	
	@Column(name="DATA_PAYLOAD")
	private DataPayload dataPayload;
	
	@Column(name="CREATE_DATE",nullable=false)
	private Date createDate;
	
	@Column(name="ERROR",length=100)
	private String errorMessage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public String getGcmId() {
		return gcmId;
	}

	public void setGcmId(String gcmId) {
		this.gcmId = gcmId;
	}

	public NotificationPayload getNotificationPayload() {
		return notificationPayload;
	}

	public void setNotificationPayload(NotificationPayload notificationPayload) {
		this.notificationPayload = notificationPayload;
	}

	public DataPayload getDataPayload() {
		return dataPayload;
	}

	public void setDataPayload(DataPayload dataPayload) {
		this.dataPayload = dataPayload;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Integer getGcmNotificationId() {
		return gcmNotificationId;
	}

	public void setGcmNotificationId(Integer gcmNotificationId) {
		this.gcmNotificationId = gcmNotificationId;
	}
	
	
}
