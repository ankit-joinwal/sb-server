package com.bitlogic.sociallbox.data.model.notifications;

import java.util.List;

import com.bitlogic.sociallbox.data.model.notifications.NotificationMessage.DataPayload;
import com.bitlogic.sociallbox.data.model.notifications.NotificationMessage.NotificationPayload;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GCMNotification {

	@JsonProperty("to")
	private String reciever;
	
	@JsonProperty("registration_ids")
	private List<String> recievers;
	
	@JsonProperty("collapse_key")
	private String collapseKey;
	
	@JsonProperty("priority")
	private String priority;
	
	@JsonProperty("delay_while_idle")
	private Boolean delayWhileIdle = Boolean.FALSE;
	
	@JsonProperty("restricted_package_name")
	private String restrictedPackageName ;
	
	@JsonProperty("dry_run")
	private Boolean dryRun = Boolean.TRUE;

	@JsonProperty("notification")
	private NotificationPayload notificationPayload;
	
	@JsonProperty("data")
	private DataPayload dataPayload;
	
	
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

	public String getReciever() {
		return reciever;
	}

	public void setReciever(String reciever) {
		this.reciever = reciever;
	}

	public List<String> getRecievers() {
		return recievers;
	}

	public void setRecievers(List<String> recievers) {
		this.recievers = recievers;
	}

	public String getCollapseKey() {
		return collapseKey;
	}

	public void setCollapseKey(String collapseKey) {
		this.collapseKey = collapseKey;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Boolean getDelayWhileIdle() {
		return delayWhileIdle;
	}

	public void setDelayWhileIdle(Boolean delayWhileIdle) {
		this.delayWhileIdle = delayWhileIdle;
	}


	public String getRestrictedPackageName() {
		return restrictedPackageName;
	}

	public void setRestrictedPackageName(String restrictedPackageName) {
		this.restrictedPackageName = restrictedPackageName;
	}

	public Boolean getDryRun() {
		return dryRun;
	}

	public void setDryRun(Boolean dryRun) {
		this.dryRun = dryRun;
	}
	
	@Override
	public String toString() {
		return "GCMNotification [to = "+this.reciever +
				" , registration_ids = "+this.recievers+
				" , collapseKey = "+this.collapseKey+
				" , priority = "+this.priority+
				" , delay_while_idle = "+this.delayWhileIdle+
				" , restricted_package_name = "+this.restrictedPackageName+
				" , dry_run = "+this.dryRun+
				" , data = "+this.dataPayload+
				" , notification = "+this.notificationPayload+
				" ]";
	}
	
}
