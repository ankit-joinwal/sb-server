package com.bitlogic.sociallbox.service.helper;

import java.io.File;
import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.crypto.codec.Base64;

public class Test {
	public static void main(String[] args)throws Exception {
		Long timeStamp = System.currentTimeMillis();
		System.out.println(timeStamp);
		String sign = calculateSignature("2fc9d17b-a4b1-4b75-b3e3-9b75353a3286", timeStamp);
		System.out.println(sign);

		String path = "D:\\softwares\\apache-tomcat-7.0.65\\apache-tomcat-7.0.65\\images\\events\\40289188530948ba0153094be7f30000";
		File eventImagesFolder = new File(path);
		if(!eventImagesFolder.exists()){
			eventImagesFolder.mkdir();
		}
		
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
