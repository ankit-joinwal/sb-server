package com.bitlogic.sociallbox.image.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;

public class AmazonS3Client {

	private static final Logger LOGGER = LoggerFactory.getLogger(AmazonS3Client.class);
	
	private static volatile AmazonS3 s3client = null;
	
	public static AmazonS3 getClient(){
		if(s3client==null){
			synchronized (AmazonS3Client.class) {
				if(s3client==null){
					LOGGER.info("Creating new instance of AmazonS3Client");
					AWSCredentials credentials = new ProfileCredentialsProvider().getCredentials();
			    	s3client = new com.amazonaws.services.s3.AmazonS3Client(credentials);
				}
			}
		}
		return s3client;
	}
}
