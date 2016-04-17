package com.bitlogic.sociallbox.service.business;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.requests.AddCompanyToProfileRequest;
import com.bitlogic.sociallbox.data.model.requests.UpdateEOAdminProfileRequest;
import com.bitlogic.sociallbox.data.model.response.EOAdminProfile;

public interface EOAdminService {

	public EOAdminProfile signup(User user);
	
	public EOAdminProfile signin(String emailId);
	
	@PreAuthorize("hasAnyRole('"+Constants.ROLE_TYPE_ADMIN+"','"+Constants.ROLE_ORGANIZER+"')")
	public EOAdminProfile addCompany(AddCompanyToProfileRequest addCompanyRequest,Long userId);
	
	public EOAdminProfile getProfile(Long id);
	
	public EOAdminProfile updateProfile(UpdateEOAdminProfileRequest updateProfileRequest);
	
	public String updateProfilePic(Long userId,List<MultipartFile> images);
	
	public String updateCompanyPic(Long userId,String orgId,List<MultipartFile> images,String type);
}
