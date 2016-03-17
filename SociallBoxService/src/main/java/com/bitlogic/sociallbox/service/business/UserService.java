package com.bitlogic.sociallbox.service.business;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.EventType;
import com.bitlogic.sociallbox.data.model.Role;
import com.bitlogic.sociallbox.data.model.SmartDevice;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.UserSetting;
import com.bitlogic.sociallbox.data.model.UserTypeBasedOnDevice;
import com.bitlogic.sociallbox.data.model.response.UserFriend;

public interface UserService {

	public User getUser(long id);
	
	@PreAuthorize("hasRole('"+Constants.ROLE_TYPE_ADMIN+"')")
	public List<User> getAllUsers();
	
	public User signupOrSignin(User user,UserTypeBasedOnDevice userTypeBasedOnDevice) ;
	
	public List<EventType> getUserEventInterests(Long id);
	
	public List<EventType> saveUserEventInterests(Long id,List<EventType> types);
	
	public SmartDevice getSmartDeviceDetails(String uniqueId) ;
	
	public List<Role> getUserRolesByDevice(String deviceId) ;
	
	public User loadUserByUsername(String username) ;
	
	public List<UserFriend> setupUserFriendsForNewUser(Long userId, String[] friendSocialIds);
	
	public List<UserFriend> getUserFriends(Long userId);
	
	public List<UserSetting> getUserSettings(Long userId);

	public List<UserSetting> setUserSettings(Long userId,List<UserSetting> newSettings);
	
	
}
