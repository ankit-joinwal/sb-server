package com.bitlogic.sociallbox.data.model;

public enum UserSettingType {
	PUSH_NOTIFICATION("PUSH_NOTIFICATION");
	private String settingType;
	private UserSettingType(String settingType) {
		this.settingType = settingType;
	}
	
	public String getSettingType(){
		return this.settingType;
	}
}
