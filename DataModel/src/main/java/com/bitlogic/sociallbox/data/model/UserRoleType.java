package com.bitlogic.sociallbox.data.model;

public enum UserRoleType {
	APP_USER("APP_USER"),EVENT_ORGANIZER("EVENT_ORGANIZER"),ADMIN("ADMIN");
	
	String roleType;
	
	private UserRoleType(String roleType){
		this.roleType = roleType;
	}
	
	public String getRoleType(){
		return this.roleType;
	}
}
