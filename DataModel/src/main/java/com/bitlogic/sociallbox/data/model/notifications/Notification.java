package com.bitlogic.sociallbox.data.model.notifications;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="notification")
public class Notification {

	@JsonProperty("type")
	private String type;
	
	@JsonProperty("reciever_ids")
	private List<Long> recieverIds;
	
	@JsonProperty(value="message")
	private NotificationMessage notificationMessage;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Long> getRecieverIds() {
		return recieverIds;
	}

	public void setRecieverIds(List<Long> recieverIds) {
		this.recieverIds = recieverIds;
	}

	public NotificationMessage getNotificationMessage() {
		return notificationMessage;
	}

	public void setNotificationMessage(NotificationMessage notificationMessage) {
		this.notificationMessage = notificationMessage;
	}
	
	@Override
	public String toString() {
		return "Notification [type = "+this.type+
				" , userIds = "+this.recieverIds+
				" , notificationMessage = "+this.notificationMessage+ " ]";
	}
	
}
