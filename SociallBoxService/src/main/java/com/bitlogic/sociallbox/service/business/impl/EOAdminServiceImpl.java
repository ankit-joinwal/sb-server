package com.bitlogic.sociallbox.service.business.impl;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.EOAdminStatus;
import com.bitlogic.sociallbox.data.model.EventOrganizer;
import com.bitlogic.sociallbox.data.model.EventOrganizerAdmin;
import com.bitlogic.sociallbox.data.model.Role;
import com.bitlogic.sociallbox.data.model.SocialDetailType;
import com.bitlogic.sociallbox.data.model.SocialSystem;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.UserMessage;
import com.bitlogic.sociallbox.data.model.UserRoleType;
import com.bitlogic.sociallbox.data.model.UserSocialDetail;
import com.bitlogic.sociallbox.data.model.requests.AddCompanyToProfileRequest;
import com.bitlogic.sociallbox.data.model.requests.CreateEventOrganizerRequest;
import com.bitlogic.sociallbox.data.model.requests.UpdateEOAdminProfileRequest;
import com.bitlogic.sociallbox.data.model.response.EOAdminProfile;
import com.bitlogic.sociallbox.data.model.response.EODashboardResponse;
import com.bitlogic.sociallbox.data.model.response.EODashboardResponse.AttendeesInMonth;
import com.bitlogic.sociallbox.data.model.response.EventOrganizerProfile;
import com.bitlogic.sociallbox.image.service.ImageService;
import com.bitlogic.sociallbox.service.business.EOAdminService;
import com.bitlogic.sociallbox.service.business.EventOrganizerService;
import com.bitlogic.sociallbox.service.dao.EventDAO;
import com.bitlogic.sociallbox.service.dao.UserDAO;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.exception.ServiceException;
import com.bitlogic.sociallbox.service.exception.UnauthorizedException;
import com.bitlogic.sociallbox.service.transformers.EOToEOResponseTransformer;
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
	
	@Autowired
	private MessageSource msgSource;
	
	@Autowired
	private EventDAO eventDAO;
	
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
			
			//Insert a new message for user
			String messageKey = WELCOME_MESSAGE_KEY;
			Locale currentLocale = LocaleContextHolder.getLocale();
			String messageDetails = msgSource.getMessage(messageKey, null,
					currentLocale);
			if(messageDetails!=null){
				String formattedMsg = String.format(messageDetails, user.getName());
				UserMessage message = new UserMessage();
				message.setCreateDt(new Date());
				message.setIsRead(Boolean.FALSE);
				message.setMessage(formattedMsg);
				message.setUser(createdUser);
				this.userDAO.addMessageForUser(message);
			}
			adminProfile = new EOAdminProfile(null, null, createdUser);
			logInfo(LOG_PREFIX, "User Signup Successful");
		}else{
			logError(LOG_PREFIX, "Organizer Admin already exists. Cannot signup again.{}", user.getEmailId());
			throw new ClientException(RestErrorCodes.ERR_002, ERROR_USER_ALREADY_EXISTS);
		}
		return adminProfile;
	}
	
	@Override
	public EOAdminProfile signin(String emailId) {
		String LOG_PREFIX = "EOAdminServiceImpl-signin";
		logInfo(LOG_PREFIX, "Checking if user exists - {} ?",emailId);
		User userInDB = this.userDAO.getUserByEmailId(emailId, false);
		EOAdminProfile adminProfile = null;
		if(userInDB!=null){
			logInfo(LOG_PREFIX, "User Found");
			EventOrganizerAdmin eventOrganizerAdmin = this.eventOrganizerService.getEOAdminByUserId(userInDB.getId());
			if(eventOrganizerAdmin==null){
				adminProfile = new EOAdminProfile(null, null, userInDB);
			}else{
				if(eventOrganizerAdmin.getOrganizer()==null){
					adminProfile = new EOAdminProfile(null, eventOrganizerAdmin, userInDB);
				}else{
					EOToEOResponseTransformer eoProfileTransformer = 
							(EOToEOResponseTransformer) TransformerFactory.getTransformer(TransformerTypes.EO_TO_EO_RESPONSE_TRANSFORMER);
					EventOrganizerProfile eventOrganizerProfile = eoProfileTransformer.transform(eventOrganizerAdmin.getOrganizer());
					adminProfile = new EOAdminProfile(eventOrganizerProfile, eventOrganizerAdmin, userInDB);
				}
			}
			
		}else{
			logError(LOG_PREFIX, "User not found for Email Id {}", emailId);
			throw new UnauthorizedException(RestErrorCodes.ERR_002, ERROR_USER_INVALID);
		}
		return adminProfile;
	}
	
	@Override
	public EOAdminProfile getProfile(Long id) {
		String LOG_PREFIX = "EOAdminServiceImpl-getProfile";
		EventOrganizerAdmin eventOrganizerAdmin = this.eventOrganizerService.getEOAdminById(id);
		
		EOToEOResponseTransformer eoProfileTransformer = 
				(EOToEOResponseTransformer) TransformerFactory.getTransformer(TransformerTypes.EO_TO_EO_RESPONSE_TRANSFORMER);
		EventOrganizerProfile eventOrganizerProfile = eoProfileTransformer.transform(eventOrganizerAdmin.getOrganizer());
		
		EOAdminProfile adminProfile = new EOAdminProfile(eventOrganizerProfile, eventOrganizerAdmin, eventOrganizerAdmin.getUser());
		
		return adminProfile;
	}
	
	@Override
	public EOAdminProfile updateProfile(
			UpdateEOAdminProfileRequest updateProfileRequest) {
		String LOG_PREFIX = "EOAdminServiceImpl-updateProfile";
		logInfo(LOG_PREFIX, "Checking if user exists - {} ?",updateProfileRequest.getUserId());
		User userInDB = this.userDAO.getUserById(updateProfileRequest.getUserId());
		EOAdminProfile adminProfile = null;
		if(userInDB!=null){
			logInfo(LOG_PREFIX, "User Found");
			EventOrganizerAdmin eventOrganizerAdmin = this.eventOrganizerService.getEOAdminByUserId(userInDB.getId());
			User adminUser = userInDB;
			if(updateProfileRequest.getNewPassword()!=null){
				logInfo(LOG_PREFIX, "Password updated for User with id {}", updateProfileRequest.getUserId());
				adminUser.setPassword(PasswordUtils.encryptPass(updateProfileRequest.getNewPassword()));
			}
			if(updateProfileRequest.getName()!=null){
				if(!adminUser.getName().equals(updateProfileRequest.getName())){
					logInfo(LOG_PREFIX, "Name updated for user with id {}",adminUser.getId());
					adminUser.setName(updateProfileRequest.getName());
				}
			}
			if(eventOrganizerAdmin==null){
				adminProfile = new EOAdminProfile(null, null, userInDB);
			}else{
				if(eventOrganizerAdmin.getOrganizer()==null){
					adminProfile = new EOAdminProfile(null, eventOrganizerAdmin, userInDB);
				}else{
					EOToEOResponseTransformer eoProfileTransformer = 
							(EOToEOResponseTransformer) TransformerFactory.getTransformer(TransformerTypes.EO_TO_EO_RESPONSE_TRANSFORMER);
					EventOrganizerProfile eventOrganizerProfile = eoProfileTransformer.transform(eventOrganizerAdmin.getOrganizer());
					adminProfile = new EOAdminProfile(eventOrganizerProfile, eventOrganizerAdmin, userInDB);
				}
			}
			
		}else{
			logError(LOG_PREFIX, "User not found for Email Id {}", updateProfileRequest.getUserId());
			throw new UnauthorizedException(RestErrorCodes.ERR_002, ERROR_USER_INVALID);
		}
		return adminProfile;
	}
	
	@Override
	public String updateProfilePic(Long userId, List<MultipartFile> images) {
		String LOG_PREFIX = "EOAdminServiceImpl-updateProfilePic";
		User user = this.userDAO.getUserById(userId);
		if (user == null) {
			throw new UnauthorizedException(RestErrorCodes.ERR_002,
					ERROR_LOGIN_USER_UNAUTHORIZED);
		}
		try {
			for (MultipartFile multipartFile : images) {
				String fileName = multipartFile.getOriginalFilename();
				logInfo(LOG_PREFIX, "File to process : {} ", fileName);
				logInfo(LOG_PREFIX, "File size : {} ", multipartFile.getSize());

				ByteArrayInputStream imageStream = new ByteArrayInputStream(
						multipartFile.getBytes());

				Map<String, ?> uploadedImageInfo = ImageService
						.uploadUserProfilePic(user.getId() + "", imageStream,
								multipartFile.getContentType(),
								multipartFile.getBytes().length, fileName);
				if (uploadedImageInfo == null
						|| !uploadedImageInfo
								.containsKey(Constants.IMAGE_URL_KEY)) {
					throw new ServiceException(IMAGE_SERVICE_NAME,
							RestErrorCodes.ERR_052,
							"Unable to upload image.Please try later");
				}
				String imageURL = (String) uploadedImageInfo
						.get(Constants.IMAGE_URL_KEY);

				UserSocialDetail socialDetail = new UserSocialDetail();
				socialDetail
						.setSocialDetailType(SocialDetailType.USER_PROFILE_PIC);
				socialDetail.setSocialSystem(SocialSystem.SOCIALLBOX);
				socialDetail.setUser(user);
				socialDetail.setUserSocialDetail(imageURL);

				this.userDAO.saveUserSocialData(socialDetail);
				return imageURL;

			}
		}catch(ServiceException serviceException){
    		   logError(LOG_PREFIX,"Error occurred while processing user profile pic image",serviceException);
    		   throw serviceException;
    	   }catch(Exception ex){
    		 logError(LOG_PREFIX,"Error occurred while processing user profile pic image",ex);
    		 throw new ServiceException(IMAGE_SERVICE_NAME, RestErrorCodes.ERR_052, ex.getMessage());
    	   }
		return user.getProfilePic();
	}
	
	@Override
	public String updateCompanyPic(Long userId,String orgId, List<MultipartFile> images,
			String type) {
		String LOG_PREFIX = "EOAdminServiceImpl-updateCompanyPic";
		User user = this.userDAO.getUserById(userId);
		if (user == null) {
			throw new UnauthorizedException(RestErrorCodes.ERR_002,
					ERROR_LOGIN_USER_UNAUTHORIZED);
		}
		EventOrganizer eventOrganizer = this.eventOrganizerService.getOrganizerDetails(orgId);
		if(eventOrganizer == null){
			throw new ClientException(RestErrorCodes.ERR_003, ERROR_INVALID_COMPANY_ID);
		}
		try {
			
			for (MultipartFile multipartFile : images) {
				String fileName = multipartFile.getOriginalFilename();
				logInfo(LOG_PREFIX, "File to process : {} ", fileName);
				logInfo(LOG_PREFIX, "File size : {} ", multipartFile.getSize());

				ByteArrayInputStream imageStream = new ByteArrayInputStream(
						multipartFile.getBytes());
				Map<String, ?> uploadedImageInfo = ImageService
						.uploadCompanyPic(eventOrganizer.getUuid(), imageStream,
								multipartFile.getContentType(),
								multipartFile.getBytes().length, fileName);
				if (uploadedImageInfo == null
						|| !uploadedImageInfo
								.containsKey(Constants.IMAGE_URL_KEY)) {
					throw new ServiceException(IMAGE_SERVICE_NAME,
							RestErrorCodes.ERR_052,
							"Unable to upload image.Please try later");
				}
				String imageURL = (String) uploadedImageInfo
						.get(Constants.IMAGE_URL_KEY);
				if(type.equals("profilePic")){
					eventOrganizer.setProfilePic(imageURL);
				}else{
					eventOrganizer.setCoverPic(imageURL);
				}
				return imageURL;
			}
		}catch(ServiceException serviceException){
 		   logError(LOG_PREFIX,"Error occurred while processing user profile pic image",serviceException);
 		   throw serviceException;
 	   }catch(Exception ex){
 		 logError(LOG_PREFIX,"Error occurred while processing user profile pic image",ex);
 		 throw new ServiceException(IMAGE_SERVICE_NAME, RestErrorCodes.ERR_052, ex.getMessage());
 	   }	
		return null;
	}
	
	@Override
	public EOAdminProfile addCompany(AddCompanyToProfileRequest addCompanyRequest,
			Long userId) {
		String LOG_PREFIX = "EOAdminServiceImpl-addCompany";
		validateAddCompanyRequest(addCompanyRequest);
		addCompanyRequest.setUserId(userId);
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
		//Insert a new message for user
		String messageKey = COMPANY_ADDED_MESSAGE;
		Locale currentLocale = LocaleContextHolder.getLocale();
		String messageDetails = msgSource.getMessage(messageKey, null,
				currentLocale);
		if(messageDetails!=null){
			String formattedMsg = String.format(messageDetails, organizer.getName());
			UserMessage message = new UserMessage();
			message.setCreateDt(new Date());
			message.setIsRead(Boolean.FALSE);
			message.setMessage(formattedMsg);
			message.setUser(eoAdminUser);
			this.userDAO.addMessageForUser(message);
		}
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
	
	@Override
	public EODashboardResponse getDashboardData(Long userId) {
		String LOG_PREFIX = "EOAdminServiceImpl-getDashboardData";
		EODashboardResponse dashboardResponse = new EODashboardResponse();
		EventOrganizerAdmin eventOrganizerAdmin = this.eventOrganizerService.getEOAdminByUserId(userId);
		if(eventOrganizerAdmin!=null && eventOrganizerAdmin.getOrganizer()!=null){
			//Find total events
			List<String> events = this.eventDAO.getEventCountPastSixMonth(eventOrganizerAdmin.getId());
			//Find total attendees
			Integer attendees = 0;
			Integer interestedUsers = 0;
			Integer meetups = 0;
			if(events!=null && !events.isEmpty()){
				attendees = this.eventDAO.getAttendeesCountForEvents(events);
				interestedUsers = this.eventDAO.getInterestedUsersCountForEvents(events);
				meetups = this.eventDAO.getMeetupsAtEvents(events);
				dashboardResponse.setEvents(events.size());
				dashboardResponse.setInterestedUsers(interestedUsers);
				dashboardResponse.setRegisteredUsers(attendees);
				dashboardResponse.setMeetups(meetups);
			}
			
		}
		
		/*List<UserMessage> messages = this.userDAO.getUnreadMessages(userId);
		dashboardResponse.setMessages(messages);*/
		
		return dashboardResponse;
	}
	
	@Override
	public EODashboardResponse getAttendeesByMonth(Long userId) {
		EODashboardResponse dashboardResponse = new EODashboardResponse();
		EventOrganizerAdmin eventOrganizerAdmin = this.eventOrganizerService.getEOAdminByUserId(userId);
		if(eventOrganizerAdmin!=null && eventOrganizerAdmin.getOrganizer()!=null){
			List<AttendeesInMonth> attendeesInMonths = this.eventDAO.getAttendeesByMonth(eventOrganizerAdmin.getId());
			dashboardResponse.setAttendeesInMonths(attendeesInMonths);
		}else{
			List<AttendeesInMonth> attendeesInMonths = new ArrayList<>();
			 for(int i = 5;i>=1;i--){
			        Calendar cal1 =  Calendar.getInstance();
			        cal1.add(Calendar.MONTH ,-i);
			        //format it to MMM-yyyy // January-2012
			        String previousMonthYear  = new SimpleDateFormat("MMM").format(cal1.getTime());
			        AttendeesInMonth attendeesInMonth = new AttendeesInMonth();
			        attendeesInMonth.setAttendees(0);
			        attendeesInMonth.setMonth(previousMonthYear);
			        attendeesInMonths.add(attendeesInMonth);
			        
		        }
			 
			 dashboardResponse.setAttendeesInMonths(attendeesInMonths);
		}
		return dashboardResponse;
	}
}
