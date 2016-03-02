package com.bitlogic.sociallbox.service.business;

import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.response.UserPublicProfile;

public interface EventOrganizerAdminService {

	public UserPublicProfile signup(User user);
}
