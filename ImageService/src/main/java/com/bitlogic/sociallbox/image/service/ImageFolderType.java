package com.bitlogic.sociallbox.image.service;

public enum ImageFolderType {
	EVENT ,
	MEETUP,
	USER,
	COMPANIES;
	
	public String getRootFolderPath(AmazonS3Config s3Config){
		if(this == EVENT){
			return s3Config.getEventsRootFolder();
		}else if (this == MEETUP){
			return s3Config.getMeetupsRootFolder();
		}else if (this == USER){
			return s3Config.getUserPicUrl();
		}else if (this == COMPANIES){
			return s3Config.getCompaniesRootFolder();
		}
		return null;
	}
}
