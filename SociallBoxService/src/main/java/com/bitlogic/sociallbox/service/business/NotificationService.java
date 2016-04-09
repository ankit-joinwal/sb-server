package com.bitlogic.sociallbox.service.business;

import java.util.List;

import com.bitlogic.sociallbox.data.model.Meetup;
import com.bitlogic.sociallbox.data.model.MeetupImage;
import com.bitlogic.sociallbox.data.model.User;

public interface NotificationService {

	public void notifyAboutNewFriend(User actor , List<User> friends);
	
	public void notifyAboutMeetupPhoto(User actor, List<MeetupImage> photos , Meetup meetup);
	
	public void notifyAboutMeetupInvite(User actor,Meetup meetup,List<Long> attendeeIds);
	
	public void notifyAboutMeetupModification(User actor,Meetup meetup);
	
	public void notifyAboutMeetupCancellation(User actor , Meetup meetup);
	
	public void notifyAboutMeetupMessage(User actor,Meetup meetup);
}
