package com.bitlogic.sociallbox.image.service;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bitlogic.Constants;

public class ImageService {

	public static AmazonS3Config amazonS3Config;
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);
	
	public static void setConfigProperties(Properties properties){
		String eventsRootFolder = properties.getProperty(Constants.AWS_EVENTS_ROOT_FOLDER_KEY);
		String bucketName = properties.getProperty(Constants.AWS_BUCKET_NAME_KEY);
		String imageBaseUrl = properties.getProperty(Constants.AWS_IMAGES_BASE_URL_KEY);
		String meetupsRootFolder = properties.getProperty(Constants.AWS_MEETUPS_ROOT_FOLDER_KEY);
		amazonS3Config = new AmazonS3Config();
		amazonS3Config.setBucketName(bucketName);
		amazonS3Config.setEventsRootFolder(eventsRootFolder);
		amazonS3Config.setImageBaseUrl(imageBaseUrl);
		amazonS3Config.setMeetupsRootFolder(meetupsRootFolder);
		LOGGER.info("###       AWS S3 Config       ###");
		LOGGER.info("{}",amazonS3Config);
		LOGGER.info("#################################");
		
	}
	
	public static Map<String,?> uploadImageToEvent(String eventId, InputStream inputStream,
			String contentType,
			Integer contentLength,
			String fileName){
		LOGGER.info("Inside ImageService to upload image :{} for Event : {} ",fileName,eventId);
		LOGGER.info("S3Config {}",amazonS3Config);
		AmazonS3ImageService s3ImageService = new AmazonS3ImageService(amazonS3Config);
		Map<String,?> imageInfoMap = s3ImageService.uploadImage(ImageFolderType.EVENT,eventId, fileName, inputStream,contentType,contentLength);
		return imageInfoMap;
	}
	
	public static Map<String,?> uploadImageToMeetup(String meetupId, InputStream inputStream,
			String contentType,
			Integer contentLength,
			String fileName){
		LOGGER.info("Inside ImageService to upload image :{} for Meetup : {} ",fileName,meetupId);
		LOGGER.info("S3Config {}",amazonS3Config);
		AmazonS3ImageService s3ImageService = new AmazonS3ImageService(amazonS3Config);
		Map<String,?> imageInfoMap = s3ImageService.uploadImage(ImageFolderType.MEETUP,meetupId, fileName, inputStream,contentType,contentLength);
		return imageInfoMap;
	}
}
