package com.bitlogic.sociallbox.service.transformers;

import org.springframework.web.multipart.MultipartFile;

import com.bitlogic.sociallbox.data.model.MeetupImage;
import com.bitlogic.sociallbox.service.exception.ServiceException;

public class MultipartToMeetupImageTransformer implements Transformer<MeetupImage, MultipartFile>{

	@Override
	public MeetupImage transform(MultipartFile v) throws ServiceException {
		MeetupImage meetupImage = new MeetupImage();
		meetupImage.setName(v.getOriginalFilename());
		return meetupImage;
	}
}
