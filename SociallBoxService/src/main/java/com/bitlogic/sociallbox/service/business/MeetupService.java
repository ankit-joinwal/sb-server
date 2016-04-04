package com.bitlogic.sociallbox.service.business;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bitlogic.sociallbox.data.model.Meetup;
import com.bitlogic.sociallbox.data.model.MeetupMessage;
import com.bitlogic.sociallbox.data.model.requests.AddMeetupAttendeesRequest;
import com.bitlogic.sociallbox.data.model.requests.CreateMeetupRequest;
import com.bitlogic.sociallbox.data.model.requests.SaveAttendeeResponse;

public interface MeetupService {

	public Meetup createMetup(CreateMeetupRequest createMeetupRequest);
	
	public Meetup getMeetup(String meetupId);
	
	public void addAttendees(AddMeetupAttendeesRequest editMeetupRequest);
	
	public void saveAttendeeResponse(SaveAttendeeResponse attendeeResponse);
	
	public void sendMessageInMeetup(MeetupMessage meetupMessage,String meetupId,String senderId);
	
	public void uploadImageToMeetup(String deviceId,String imagesURL,List<MultipartFile> images , String meetupId) ;
	
	public List<MeetupMessage> getMeetupMessages(String meetupId,Integer page);

}
