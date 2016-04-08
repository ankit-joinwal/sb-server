package com.bitlogic.sociallbox.service.transformers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.bitlogic.sociallbox.data.model.MeetupAttendee;
import com.bitlogic.sociallbox.data.model.MeetupAttendeeEntity;
import com.bitlogic.sociallbox.data.model.SocialDetailType;
import com.bitlogic.sociallbox.data.model.UserSocialDetail;
import com.bitlogic.sociallbox.service.exception.ServiceException;

public class MeetupAttendeeTransformer implements Transformer<List<MeetupAttendee>, List<MeetupAttendeeEntity>>{

	private static volatile MeetupAttendeeTransformer instance = null;
	
	private MeetupAttendeeTransformer(){
		
	}
	public static MeetupAttendeeTransformer getInstance(){
		if(instance == null){
			synchronized (MeetupAttendeeTransformer.class) {
				if(instance==null){
					instance = new MeetupAttendeeTransformer();
				}
			}
		}
		return instance;
	}
	@Override
	public List<MeetupAttendee> transform(List<MeetupAttendeeEntity> entities)
			throws ServiceException {
		List<MeetupAttendee> meetupAttendees = new ArrayList<MeetupAttendee>();
		if(entities!=null){
			for(MeetupAttendeeEntity attendeeEntity : entities){
				MeetupAttendee attendee = new MeetupAttendee();
				attendee.setId(attendeeEntity.getAttendeeId());
				attendee.setAttendeeResponse(attendeeEntity.getAttendeeResponse());
				attendee.setIsAdmin(attendeeEntity.getIsAdmin());
				attendee.setName(attendeeEntity.getUser().getName());
				attendee.setUserId(attendeeEntity.getUser().getId());
				Set<UserSocialDetail> socialDetails = attendeeEntity.getUser().getSocialDetails();
				for(UserSocialDetail socialDetail : socialDetails ){
					if(socialDetail.getSocialDetailType()== SocialDetailType.USER_PROFILE_PIC){
						attendee.setProfilePic(socialDetail.getUserSocialDetail());
						break;
					}
				}
				
				meetupAttendees.add(attendee);
			}
		}
		
		
		return meetupAttendees;
	}
}
