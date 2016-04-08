package com.bitlogic.sociallbox.service.business;

import java.util.List;

import com.bitlogic.sociallbox.data.model.UserSocialActivity;
import com.bitlogic.sociallbox.data.model.UserSocialActivity.ActivityTime;

public interface MySociallBoxService {

	public List<UserSocialActivity<?>> getMySociallBox(ActivityTime timeline,String deviceId,String userLocation);
}
