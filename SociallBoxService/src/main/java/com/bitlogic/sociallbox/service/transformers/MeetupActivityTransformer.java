package com.bitlogic.sociallbox.service.transformers;

import java.util.ArrayList;
import java.util.List;

import com.bitlogic.sociallbox.data.model.Meetup;
import com.bitlogic.sociallbox.data.model.UserMeetupActivity;
import com.bitlogic.sociallbox.data.model.UserSocialActivity;
import com.bitlogic.sociallbox.data.model.UserSocialActivity.ActivityType;
import com.bitlogic.sociallbox.service.exception.ServiceException;

public class MeetupActivityTransformer implements Transformer<List<UserSocialActivity<UserMeetupActivity>>, List<Meetup>> {

	private static volatile MeetupActivityTransformer instance = null;
	
	private MeetupActivityTransformer(){
		
	}
	
	public static synchronized MeetupActivityTransformer getInstance(){
		if(instance == null){
			synchronized (MeetupActivityTransformer.class){
				if(instance==null){
					instance = new MeetupActivityTransformer();
				}
			}
		}
		return instance;
	}
	@Override
	public List<UserSocialActivity<UserMeetupActivity>> transform(List<Meetup> meetups)
			throws ServiceException {
		List<UserSocialActivity<UserMeetupActivity>> activities = new ArrayList<UserSocialActivity<UserMeetupActivity>>();
		
		
		return null;
	}
}
