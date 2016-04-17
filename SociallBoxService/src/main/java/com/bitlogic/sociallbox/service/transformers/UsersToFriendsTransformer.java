package com.bitlogic.sociallbox.service.transformers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.bitlogic.sociallbox.data.model.SocialDetailType;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.UserSocialDetail;
import com.bitlogic.sociallbox.data.model.response.UserFriend;

public class UsersToFriendsTransformer implements Transformer<List<UserFriend>, List<User>>{

	private static volatile UsersToFriendsTransformer instance = null;
	
	private UsersToFriendsTransformer(){
		
	}
	
	public static UsersToFriendsTransformer getInstance(){
		if(instance==null){
			synchronized (UsersToFriendsTransformer.class) {
				if(instance==null){
					instance = new UsersToFriendsTransformer();
				}
			}
		}
		return instance;
	}
	
	public List<UserFriend> transform(List<User> users) throws com.bitlogic.sociallbox.service.exception.ServiceException {
		List<UserFriend> userFriends = new ArrayList<>();
		if(users!=null && !users.isEmpty()){
			for(User user : users){
				UserFriend userFriend = new UserFriend();
				userFriend.setEmailId(user.getEmailId());
				userFriend.setName(user.getName());
				userFriend.setId(user.getId());
				Set<UserSocialDetail> details = user.getSocialDetails();
				if(details!=null && !details.isEmpty()){
					for(UserSocialDetail socialDetail : details){
						if(socialDetail.getSocialDetailType() == SocialDetailType.USER_PROFILE_PIC){
							userFriend.setProfilePic(socialDetail.getUserSocialDetail());
							break;
						}
					}
				}
				
				userFriends.add(userFriend);
			}
		}
		return userFriends;
		
	};
}
