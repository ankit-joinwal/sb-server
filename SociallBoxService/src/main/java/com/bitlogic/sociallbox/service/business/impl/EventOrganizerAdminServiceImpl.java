package com.bitlogic.sociallbox.service.business.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.Role;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.UserRoleType;
import com.bitlogic.sociallbox.data.model.response.UserPublicProfile;
import com.bitlogic.sociallbox.service.business.EventOrganizerAdminService;
import com.bitlogic.sociallbox.service.dao.UserDAO;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.utils.LoginUtil;

@Service("eventOrganizerAdminService")
@Transactional
public class EventOrganizerAdminServiceImpl implements EventOrganizerAdminService,Constants{

	private static final Logger LOGGER = LoggerFactory.getLogger(EventOrganizerAdminServiceImpl.class);
	@Autowired
	private UserDAO userDAO;
	
	@Override
	public UserPublicProfile signup(User user) {

		LoginUtil.validateOrganizerAdmin(user);
		UserPublicProfile userProfile = null;
		User userInDB = this.userDAO.getUserByEmailId(user.getEmailId(), false);
		if(userInDB==null){
			Date now = new Date();
			user.setCreateDt(now);
			Set<Role> userRoles = new HashSet<>();
			Role appUserRole = this.userDAO.getRoleType(UserRoleType.EVENT_ORGANIZER);
			userRoles.add(appUserRole);
			user.setUserroles(userRoles);
			User createdUser = this.userDAO.createNewWebUser(user);
			userProfile = new UserPublicProfile(createdUser);
		}else{
			LOGGER.error("Organizer Admin already exists. Cannot signup again.{}"+user.getEmailId());
			throw new ClientException(RestErrorCodes.ERR_002, ERROR_USER_ALREADY_EXISTS);
		}
		return userProfile;
	}
	
}
