package com.bitlogic.sociallbox.image.service;

public enum ImageFolderType {
	EVENT ,
	MEETUP;
	
	public String getRootFolderPath(AmazonS3Config s3Config){
		if(this == EVENT){
			return s3Config.getEventsRootFolder();
		}else if (this == MEETUP){
			return s3Config.getMeetupsRootFolder();
		}
		return null;
	}
}
