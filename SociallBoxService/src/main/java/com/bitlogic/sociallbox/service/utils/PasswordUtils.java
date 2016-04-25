package com.bitlogic.sociallbox.service.utils;

import java.security.MessageDigest;

public class PasswordUtils {

	
	
	public static String encryptPass(String password){
		 StringBuffer sb = new StringBuffer();
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
	        md.update(password.getBytes());
	        byte byteData[] = md.digest();
	        
	       
	        for (int i = 0; i < byteData.length; i++) {
	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
	     
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(encryptPass("p@ssword"));
	}
	
}
