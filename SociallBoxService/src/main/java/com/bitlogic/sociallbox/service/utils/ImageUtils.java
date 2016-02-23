package com.bitlogic.sociallbox.service.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class ImageUtils {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ImageUtils.class);

	public static File storeImageOnServer(String basePath,
			MultipartFile multipartFile) throws Exception {
		InputStream inputStream = null;
		BufferedOutputStream stream = null;
		File eventImagesFolder = new File(basePath);
		LOGGER.info("### Inside ImageUtils.storeImageOnServer ###");
		LOGGER.info("Base Directory : {}", basePath);
		if (!eventImagesFolder.exists()) {
			LOGGER.info("Base directory doesnt exist. Creating..");
			eventImagesFolder.mkdir();
		}

		String filePath = basePath + File.separator
				+ multipartFile.getOriginalFilename();
		LOGGER.info("File Path : {} ", filePath);
		File image = new File(filePath);
		inputStream = multipartFile.getInputStream();
		if (!image.exists()) {
			image.createNewFile();
		}
		try {
			byte[] bytes = multipartFile.getBytes();
			stream = new BufferedOutputStream(new FileOutputStream(image));
			stream.write(bytes);
			LOGGER.info("File {} uploaded successfully",multipartFile.getOriginalFilename());
			return image;
		} finally {
			stream.close();

		}

	}
}
