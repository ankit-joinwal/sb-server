package com.bitlogic.sociallbox.data.model;

public enum EventStatus {
	PENDING_APPROVAL,
	APPROVED,
	LIVE,
	OFFLINE,
	CANCELLED;
	
	public static EventStatus getStatusFromValue(String value){
		EventStatus[] values = values();
		for(EventStatus status : values){
			if(status.name().equalsIgnoreCase(value)){
				return status;
			}
		}
		
		return null;
	}
}
