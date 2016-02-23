package com.bitlogic.sociallbox.test.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.bitlogic.sociallbox.data.model.SocialDetailType;
import com.bitlogic.sociallbox.data.model.SocialSystem;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.UserSocialDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TestUtil {



    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        System.out.println("Object :"+mapper.writeValueAsString(object));
        return mapper.writeValueAsBytes(object);
    }

    
    public static void main(String[] args) throws Exception{
    	User user = new User();
		user.setEmailId("test-user@testdomain.com");
		user.setIsEnabled("true");
		user.setName("Test User");
		user.setPassword("password");
		
		UserSocialDetail detail = new UserSocialDetail();
		detail.setSocialDetailType(SocialDetailType.USER_EXTERNAL_ID);
		detail.setSocialSystem(SocialSystem.FACEBOOK);
		detail.setUserSocialDetail("1234");
		Set<UserSocialDetail> socialDetails = new HashSet<>();
		socialDetails.add(detail);
		
		user.setSocialDetails(socialDetails);
		
		convertObjectToJsonBytes(user);
	}
}
