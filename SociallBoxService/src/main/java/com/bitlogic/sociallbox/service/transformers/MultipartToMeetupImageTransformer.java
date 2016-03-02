package com.bitlogic.sociallbox.service.transformers;

import org.springframework.web.multipart.MultipartFile;

import com.bitlogic.sociallbox.data.model.MeetupImage;
import com.bitlogic.sociallbox.service.exception.ServiceException;

public class MultipartToMeetupImageTransformer implements Transformer<MeetupImage, MultipartFile>{

	
	private static volatile MultipartToMeetupImageTransformer instance = null;
	
	private MultipartToMeetupImageTransformer(){
		
	}
	public static MultipartToMeetupImageTransformer getInstance(){
		if(instance==null){
			synchronized (MultipartToMeetupImageTransformer.class) {
				if(instance==null){
					instance = new MultipartToMeetupImageTransformer();
				}
			}
		}
		return instance;
	}
	@Override
	public MeetupImage transform(MultipartFile v) throws ServiceException {
		MeetupImage meetupImage = new MeetupImage();
		meetupImage.setName(v.getOriginalFilename());
		return meetupImage;
	}
}
