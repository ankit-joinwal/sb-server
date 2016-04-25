package com.bitlogic.sociallbox.service.business;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.EOAdminStatus;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.UserTypeBasedOnDevice;
import com.bitlogic.sociallbox.data.model.response.EOAdminProfile;

public interface AdminService {

	@PreAuthorize("hasRole('"+Constants.ROLE_TYPE_ADMIN+"')")
	public List<EOAdminProfile> getPendingProfiles();
	
	@PreAuthorize("hasRole('"+Constants.ROLE_TYPE_ADMIN+"')")
	public void approveOrRejectProfiles(List<Long> profileIds,EOAdminStatus status);
	@PreAuthorize("hasRole('"+Constants.ROLE_TYPE_ADMIN+"')")
	public User signupOrSignin(String emailId,UserTypeBasedOnDevice userTypeBasedOnDevice);
	
	@PreAuthorize("hasRole('"+Constants.ROLE_TYPE_ADMIN+"')")
	public List<EOAdminProfile> getAllOrganizers(String emailId);
}
