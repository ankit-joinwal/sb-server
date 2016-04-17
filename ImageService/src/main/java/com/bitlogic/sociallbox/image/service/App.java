package com.bitlogic.sociallbox.image.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;


public class App 
{
	private static final String SUFFIX = "/";
	static String EVENTS_ROOT_FOLDER = "public/events/";
	static String BUCKET_NAME = "sociallbox";
	
    public static void main( String[] args )
    {
    	String folderName = "test";
    	//Step 1 : Get client
    	AmazonS3 client = getClient();
    	//Check if folder exists
    	Boolean isfolderExist = checkIfFolderExists(folderName, client);
    	System.out.println(isfolderExist);
    	//Create folder
    	if(!isfolderExist){
    		createFolder(folderName, client);
    	}
    	//Upload File
    	String imageUrl = uploadFileToFolder(folderName, client);
    	System.out.println("Image URL : "+imageUrl);
    }
    
    public static AmazonS3 getClient(){
    	AWSCredentials credentials = new ProfileCredentialsProvider().getCredentials();
    	AmazonS3 s3client = new AmazonS3Client(credentials);
    	return s3client;
    }
    
    public static void createFolder(String folderName, AmazonS3 client) {
    	// create meta-data for your folder and set content-length to 0
    	ObjectMetadata metadata = new ObjectMetadata();
    	metadata.setContentLength(0);
    	// create empty content
    	InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
    	// create a PutObjectRequest passing the folder name suffixed by /
    	PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME,
    			EVENTS_ROOT_FOLDER+folderName + SUFFIX, emptyContent, metadata);
    	// send request to S3 to create folder
    	client.putObject(putObjectRequest);
    }
    
    public static boolean checkIfFolderExists(String folderName,AmazonS3 client){
    	String folderKey = EVENTS_ROOT_FOLDER+folderName+SUFFIX;
    	GetObjectRequest getObjectRequest = new GetObjectRequest(BUCKET_NAME, folderKey);
    	try{
	    	S3Object object = client.getObject(getObjectRequest);
	    	if(object!=null){
	    		return true;
	    	}
    	}catch(AmazonS3Exception exception){
    		System.out.println("Folder Does not exist");
    	}
    	return false;
    }
    
    public static String uploadFileToFolder(String folderName,AmazonS3 client){
    	String folderKey = EVENTS_ROOT_FOLDER+folderName;
    	String fileName = folderKey + SUFFIX + "auto_expo1.jpg";
    	PutObjectResult putObjectResult = client.putObject(new PutObjectRequest(BUCKET_NAME, fileName, 
    			new File("C:\\Users\\ajoinw\\Downloads\\auto_expo1.jpg"))
    			.withCannedAcl(CannedAccessControlList.PublicRead));
    	String baseUrl = "https://sociallbox.s3-ap-southeast-1.amazonaws.com/";
    	String imageUrl = baseUrl+fileName;
    	
    	return imageUrl;
    }
}
