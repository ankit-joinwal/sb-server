package com.bitlogic.sociallbox.image.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.bitlogic.Constants;

public class AmazonS3ImageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AmazonS3ImageService.class);
	
	public static final String SUFFIX = "/";
	private AmazonS3Config amazonS3Config;
	private AmazonS3 s3Client;

	public AmazonS3ImageService(AmazonS3Config s3Config) {
		this.amazonS3Config = s3Config;
		this.s3Client = AmazonS3Client.getClient();
	}
	
	public Map<String,?> uploadImage(String folderName,String fileName, InputStream inputStream,
			String contentType,
			Integer contentLength){
		LOGGER.info("Inside AmazonS3ImageService to upload image {} to folder {} ",fileName,folderName);
		Boolean isfolderExist = checkIfFolderExists(folderName);

		//Create folder
    	if(!isfolderExist){
    		LOGGER.info("Folder does not exist. Creating new folder");
    		createFolder(folderName);
    	}
    	//Upload File
    	String imageUrl = uploadFileToFolder(folderName, fileName,inputStream,contentType,contentLength);
    	LOGGER.info("File Uploaded succesfully. URL for file {} ",imageUrl);
    	Map<String,String> imageInfoMap = new HashMap<String,String>();
    	imageInfoMap.put(Constants.IMAGE_URL_KEY, imageUrl);
		return imageInfoMap;
	}

	private void createFolder(String folderName) {
		// create meta-data for your folder and set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(
				amazonS3Config.getBucketName(),
				amazonS3Config.getEventsRootFolder() + folderName + SUFFIX,
				emptyContent, metadata);
		// send request to S3 to create folder
		s3Client.putObject(putObjectRequest);
	}

	private boolean checkIfFolderExists(String folderName) {
		String folderKey = amazonS3Config.getEventsRootFolder() + folderName
				+ SUFFIX;
		GetObjectRequest getObjectRequest = new GetObjectRequest(
				amazonS3Config.getBucketName(), folderKey);
		try {
			S3Object object = s3Client.getObject(getObjectRequest);
			if (object != null) {
				return true;
			}
		} catch (AmazonS3Exception exception) {
			System.out.println("Folder Does not exist");
		}
		return false;
	}

	public String uploadFileToFolder(String folderName, String imageName, InputStream inputStream,
			String contentType,
			Integer contentLength) {
		String folderKey = amazonS3Config.getEventsRootFolder() + folderName;
		String fileName = folderKey + SUFFIX + imageName;
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(contentType);
		metadata.setContentLength(contentLength);
		PutObjectRequest imageUploadRequest = new PutObjectRequest(amazonS3Config.getBucketName(),
				fileName,inputStream,metadata)
				.withCannedAcl(CannedAccessControlList.PublicRead);
		s3Client.putObject(imageUploadRequest);
		String imageUrl = amazonS3Config.getImageBaseUrl() + fileName;

		return imageUrl;
	}
}
