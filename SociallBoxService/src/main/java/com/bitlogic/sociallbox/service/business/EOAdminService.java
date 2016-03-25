package com.bitlogic.sociallbox.service.business;

import org.springframework.security.access.prepost.PreAuthorize;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.requests.AddCompanyToProfileRequest;
import com.bitlogic.sociallbox.data.model.response.EOAdminProfile;

public interface EOAdminService {

	public EOAdminProfile signup(User user);
	
	@PreAuthorize("hasAnyRole('"+Constants.ROLE_TYPE_ADMIN+"','"+Constants.ROLE_ORGANIZER+"')")
	public EOAdminProfile addCompany(AddCompanyToProfileRequest addCompanyRequest,Long userId);
	
	public EOAdminProfile getProfile(Long id);
}
