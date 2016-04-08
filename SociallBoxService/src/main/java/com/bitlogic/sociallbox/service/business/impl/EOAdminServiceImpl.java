package com.bitlogic.sociallbox.service.business.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.EOAdminStatus;
import com.bitlogic.sociallbox.data.model.EventOrganizer;
import com.bitlogic.sociallbox.data.model.EventOrganizerAdmin;
import com.bitlogic.sociallbox.data.model.Role;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.UserRoleType;
import com.bitlogic.sociallbox.data.model.requests.AddCompanyToProfileRequest;
import com.bitlogic.sociallbox.data.model.requests.CreateEventOrganizerRequest;
import com.bitlogic.sociallbox.data.model.response.EOAdminProfile;
import com.bitlogic.sociallbox.data.model.response.EventOrganizerProfile;
import com.bitlogic.sociallbox.service.business.EOAdminService;
import com.bitlogic.sociallbox.service.business.EventOrganizerService;
import com.bitlogic.sociallbox.service.dao.UserDAO;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.transformers.EOToEOResponseTransformer;
import com.bitlogic.sociallbox.service.transformers.Transformer;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory.TransformerTypes;
import com.bitlogic.sociallbox.service.utils.LoggingService;
import com.bitlogic.sociallbox.service.utils.LoginUtil;
import com.bitlogic.sociallbox.service.utils.PasswordUtils;

@Service("eventOrganizerAdminService")
@Transactional
public class EOAdminServiceImpl extends LoggingService implements EOAdminService,Constants{

	private static final Logger LOGGER = LoggerFactory.getLogger(EOAdminServiceImpl.class);
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private EventOrganizerService eventOrganizerService;
	
	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	
	@Override
	public EOAdminProfile signup(User user) {
		String LOG_PREFIX = "EOAdminServiceImpl-signup";
		
		LoginUtil.validateOrganizerAdmin(user);
		logInfo(LOG_PREFIX, "Validated User successfully");
		logInfo(LOG_PREFIX, "Checking if user exists - {} ?",user.getEmailId());
		User userInDB = this.userDAO.getUserByEmailId(user.getEmailId(), false);
		User createdUser = null;
		EOAdminProfile adminProfile = null;
		if(userInDB==null){
			logInfo(LOG_PREFIX, "User not found. Signing up!");
			Date now = new Date();
			user.setCreateDt(now);
			user.setIsEnabled(Boolean.TRUE);
			Set<Role> userRoles = new HashSet<>();
			Role appUserRole = this.userDAO.getRoleType(UserRoleType.EVENT_ORGANIZER);
			userRoles.add(appUserRole);
			user.setUserroles(userRoles);
			user.setPassword(PasswordUtils.encryptPass(user.getPassword()));
			createdUser = this.userDAO.createNewWebUser(user);
			adminProfile = new EOAdminProfile(null, null, createdUser);
			logInfo(LOG_PREFIX, "User Signup Successful");
		}else{
			logError(LOG_PREFIX, "Organizer Admin already exists. Cannot signup again.{}", user.getEmailId());
			throw new ClientException(RestErrorCodes.ERR_002, ERROR_USER_ALREADY_EXISTS);
		}
		return adminProfile;
	}
	
	@Override
	public EOAdminProfile getProfile(Long id) {
		String LOG_PREFIX = "EOAdminServiceImpl-getProfile";
		EventOrganizerAdmin eventOrganizerAdmin = this.eventOrganizerService.getEOAdminById(id);
		
		Transformer<EventOrganizerProfile, EventOrganizer> eoProfileTransformer = 
				(Transformer<EventOrganizerProfile, EventOrganizer>) TransformerFactory.getTransformer(TransformerTypes.EO_TO_EO_RESPONSE_TRANSFORMER);
		EventOrganizerProfile eventOrganizerProfile = eoProfileTransformer.transform(eventOrganizerAdmin.getOrganizer());
		
		EOAdminProfile adminProfile = new EOAdminProfile(eventOrganizerProfile, eventOrganizerAdmin, eventOrganizerAdmin.getUser());
		
		return adminProfile;
	}
	
	@Override
	public EOAdminProfile addCompany(AddCompanyToProfileRequest addCompanyRequest,
			Long userId) {
		String LOG_PREFIX = "EOAdminServiceImpl-addCompany";
		validateAddCompanyRequest(addCompanyRequest);
		Boolean isExistingCompany = addCompanyRequest.getIsExistingCompany();
		if(isExistingCompany){
			logInfo(LOG_PREFIX, "Existing Company Case");
			return handleExistingCompanyCase(addCompanyRequest);
		}else{
			logInfo(LOG_PREFIX, "New Company Case");
			return handleNewCompanyCase(addCompanyRequest);
		}
	}
	
	private EOAdminProfile handleExistingCompanyCase(AddCompanyToProfileRequest addCompanyRequest){
		String LOG_PREFIX = "EOAdminServiceImpl-handleExistingCompanyCase";
		return null;
	}
	
	private EOAdminProfile handleNewCompanyCase(AddCompanyToProfileRequest addCompanyRequest){
		String LOG_PREFIX = "EOAdminServiceImpl-handleNewCompanyCase";
		Long userId = addCompanyRequest.getUserId();
		User eoAdminUser = this.userDAO.getUserById(userId);
		if(eoAdminUser==null){
			logError(LOG_PREFIX, "EOAdmin user not found");
			throw new ClientException(RestErrorCodes.ERR_002,ERROR_USER_INVALID);
		}
		
		EventOrganizer organizer = this.eventOrganizerService.create(addCompanyRequest.getCreateEventOrganizerRequest());
		logInfo(LOG_PREFIX, "Created Company {} ",organizer.getName());
		EventOrganizerAdmin eoAdmin = new EventOrganizerAdmin();
		eoAdmin.setUser(eoAdminUser);
		eoAdmin.setOrganizer(organizer);
		eoAdmin.setStatus(EOAdminStatus.PENDING);
		eoAdmin.setCreateDt(new Date());
		this.eventOrganizerService.createEOAdmin(eoAdmin);
		
		EOToEOResponseTransformer eoProfileTransformer = (EOToEOResponseTransformer) TransformerFactory.getTransformer(TransformerTypes.EO_TO_EO_RESPONSE_TRANSFORMER);
		EventOrganizerProfile eventOrganizerProfile = eoProfileTransformer.transform(organizer);
		
		EOAdminProfile adminProfile = new EOAdminProfile(eventOrganizerProfile, eoAdmin, eoAdminUser);
		logInfo(LOG_PREFIX, "Added Company to EOAdmin profile");
		return adminProfile;
	}
	
	private void validateAddCompanyRequest(AddCompanyToProfileRequest addCompanyRequest){
		String LOG_PREFIX = "addCompany-validateAddCompanyRequest";
		logInfo(LOG_PREFIX, "Validating Request = {}", addCompanyRequest);
		if(addCompanyRequest.getIsExistingCompany()){
			if(addCompanyRequest.getCreateEventOrganizerRequest().getUuid() ==null || 
					addCompanyRequest.getCreateEventOrganizerRequest().getUuid().isEmpty()){
				throw new ClientException(RestErrorCodes.ERR_001, ERROR_COMPANY_ID_MANDATORY);
			}
		}else{
			CreateEventOrganizerRequest company = addCompanyRequest.getCreateEventOrganizerRequest();
			if(StringUtils.isBlank(company.getName())){
				throw new ClientException(RestErrorCodes.ERR_001,ERROR_NAME_MANDATORY);
			}
			if(StringUtils.isBlank(company.getEmailId())){
				throw new ClientException(RestErrorCodes.ERR_001,ERROR_EMAIL_MANDATORY);
			}
			if(company.getAddress()==null){
				throw new ClientException(RestErrorCodes.ERR_001,ERROR_ADDRESS_MANDATORY);
			}
			if(StringUtils.isBlank(company.getAddress().getCity())){
				throw new ClientException(RestErrorCodes.ERR_001,ERROR_CITY_MANDATORY);
			}
			if(StringUtils.isBlank(company.getAddress().getState())){
				throw new ClientException(RestErrorCodes.ERR_001,ERROR_STATE_MANDATORY);
			}
			if(StringUtils.isBlank(company.getAddress().getStreet())){
				throw new ClientException(RestErrorCodes.ERR_001,ERROR_STREET_MANDATORY);
			}
			if(StringUtils.isBlank(company.getAddress().getCountry())){
				throw new ClientException(RestErrorCodes.ERR_001,ERROR_COUNTRY_MANDATORY);
			}
			if(StringUtils.isBlank(company.getAddress().getZipcode())){
				throw new ClientException(RestErrorCodes.ERR_001,ERROR_ZIPCODE_MANDATORY);
			}
			if(StringUtils.isBlank(company.getPhone1())){
				throw new ClientException(RestErrorCodes.ERR_001,ERROR_PHONE_MANDATORY);
			}
			
		}
		logInfo(LOG_PREFIX, "Validation completed successfully");
	}
}
