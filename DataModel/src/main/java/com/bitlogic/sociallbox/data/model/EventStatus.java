package com.bitlogic.sociallbox.data.model;

public enum EventStatus {

	CREATED("CREATED"),READY_TO_GO_LIVE("READY_TO_GO_LIVE"),LIVE("LIVE"),CANCELLED("CANCELLED"),OFFLINE("OFFLINE");
	
	private String status;
	private EventStatus(String status){
		this.status = status;
	}
	
	public String getStatusInfo(){
		return this.status;
	}
	
	@Override
	public String toString() {
		return this.status;
	}
}
