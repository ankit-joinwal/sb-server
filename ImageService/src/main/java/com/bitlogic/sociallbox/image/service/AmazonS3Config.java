package com.bitlogic.sociallbox.image.service;

public class AmazonS3Config {

	private String eventsRootFolder;
	private String meetupsRootFolder;
	private String bucketName;
	private String imageBaseUrl;
	private String userPicUrl;
	private String companiesRootFolder;
	
	
	
	public String getCompaniesRootFolder() {
		return companiesRootFolder;
	}
	public void setCompaniesRootFolder(String companiesRootFolder) {
		this.companiesRootFolder = companiesRootFolder;
	}
	public String getUserPicUrl() {
		return userPicUrl;
	}
	public void setUserPicUrl(String userPicUrl) {
		this.userPicUrl = userPicUrl;
	}
	public String getMeetupsRootFolder() {
		return meetupsRootFolder;
	}
	public void setMeetupsRootFolder(String meetupsRootFolder) {
		this.meetupsRootFolder = meetupsRootFolder;
	}
	public String getEventsRootFolder() {
		return eventsRootFolder;
	}
	public void setEventsRootFolder(String eventsRootFolder) {
		this.eventsRootFolder = eventsRootFolder;
	}
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	public String getImageBaseUrl() {
		return imageBaseUrl;
	}
	public void setImageBaseUrl(String imageBaseUrl) {
		this.imageBaseUrl = imageBaseUrl;
	}
	
	@Override
	public String toString() {
		return "AmazonS3Config [ eventsRootFolder="+this.eventsRootFolder+ " , bucketName="
				+ this.bucketName + " , imageBaseUrl="+this.imageBaseUrl+ " ]";
	}
}
