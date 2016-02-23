package com.bitlogic.sociallbox.data.model;

public enum UserTypeBasedOnDevice {
	MOBILE("M"), WEB("W");
	private String type;
	private UserTypeBasedOnDevice(String type){
		this.type = type;
	}
	public String getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return this.type;
	}
}
