package com.bitlogic.sociallbox.service.helper;

import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.crypto.codec.Base64;

public class AuthHeaderGenerator {
	public static void main(String[] args)throws Exception {
		
		generateAuthorization("8343e1aff9e9e440", "11542765-e3fd-45a4-b274-362abb418f21");
		//generateHeaderForWeb("test.eo@gmail.com", "098f6bcd4621d373cade4e832627b4f6");
	}
	
	public static void generateHeaderForWeb(String userId,String password){
		Long timeStamp = System.currentTimeMillis();
		System.out.println("X-Auth-Date Header :"+timeStamp);
		String passAndTime = password+"~"+timeStamp;
		
		String encryptedKey = new String(Base64.encode(passAndTime.getBytes()));
		String username = "W~"+userId;
		String token = new String(Base64.encode((username+":"+encryptedKey).getBytes()));
		System.out.println("Token : "+"Basic "+token);
		
	}
	
	public static void generateAuthorization(String deviceId,String privateKey){
		Long timeStamp = System.currentTimeMillis();
		String signature = calculateSignature(privateKey, timeStamp);
		String username = "SD~"+deviceId;
		System.out.println("X-Auth-Date Header :"+timeStamp);
		String token = new String(Base64.encode((username+":"+signature).getBytes()));
		System.out.println("Token : "+"Basic "+token);
		
	}
	
	private static String calculateSignature(String secret, Long timeStamp) {
		try {
			SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(),
					"HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(signingKey);
			String timeStampStr = timeStamp + "";
			byte[] rawHmac = mac.doFinal(timeStampStr.getBytes());
			String result = new String(Base64.encode(rawHmac));
			return result;
		} catch (GeneralSecurityException e) {
			throw new IllegalArgumentException();
		}
	}
}
