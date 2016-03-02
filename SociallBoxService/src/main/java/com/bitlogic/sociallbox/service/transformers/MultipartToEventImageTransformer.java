package com.bitlogic.sociallbox.service.transformers;

import org.springframework.web.multipart.MultipartFile;

import com.bitlogic.sociallbox.data.model.EventImage;
import com.bitlogic.sociallbox.service.exception.ServiceException;

public class MultipartToEventImageTransformer implements Transformer<EventImage,MultipartFile>{

	private static MultipartToEventImageTransformer instance = null;
	
	private MultipartToEventImageTransformer(){
		
	}
	
	public static MultipartToEventImageTransformer getInstance(){
		if(instance==null){
			synchronized (MultipartToEventImageTransformer.class) {
				if(instance==null){
					instance = new MultipartToEventImageTransformer();
				}
			}
		}
		return instance;
	}
	
	@Override
	public EventImage transform(MultipartFile file) throws ServiceException{
		EventImage eventImage = new EventImage();
		eventImage.setName(file.getOriginalFilename());
		
		return eventImage;
	}
}
