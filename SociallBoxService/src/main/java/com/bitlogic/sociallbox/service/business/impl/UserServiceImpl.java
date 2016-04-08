package com.bitlogic.sociallbox.service.business.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.EventTag;
import com.bitlogic.sociallbox.data.model.EventType;
import com.bitlogic.sociallbox.data.model.Role;
import com.bitlogic.sociallbox.data.model.SmartDevice;
import com.bitlogic.sociallbox.data.model.SocialDetailType;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.UserRoleType;
import com.bitlogic.sociallbox.data.model.UserSetting;
import com.bitlogic.sociallbox.data.model.UserSocialDetail;
import com.bitlogic.sociallbox.data.model.UserTypeBasedOnDevice;
import com.bitlogic.sociallbox.data.model.response.UserEventInterest;
import com.bitlogic.sociallbox.data.model.response.UserFriend;
import com.bitlogic.sociallbox.data.model.response.UserRetailEventInterest;
import com.bitlogic.sociallbox.service.business.UserService;
import com.bitlogic.sociallbox.service.dao.EventTagDAO;
import com.bitlogic.sociallbox.service.dao.EventTypeDAO;
import com.bitlogic.sociallbox.service.dao.SmartDeviceDAO;
import com.bitlogic.sociallbox.service.dao.UserDAO;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.EntityNotFoundException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.exception.UnauthorizedException;
import com.bitlogic.sociallbox.service.transformers.Transformer;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory.TransformerTypes;
import com.bitlogic.sociallbox.service.utils.LoggingService;
import com.bitlogic.sociallbox.service.utils.LoginUtil;

@Service("userService")
@Transactional
public class UserServiceImpl extends LoggingService implements UserService, Constants {

	private static final Logger logger = LoggerFactory
			.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private EventTagDAO eventTagDAO;

	@Autowired
	private SmartDeviceDAO smartDeviceDAO;
	
	@Autowired
	private EventTypeDAO eventTypeDAO;

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public User signupOrSignin(User user,
			UserTypeBasedOnDevice userTypeBasedOnDevice)
			 {
		
		String LOG_PREFIX = "signupOrSignin";
		if (userTypeBasedOnDevice == UserTypeBasedOnDevice.MOBILE) {
			logInfo(LOG_PREFIX, "User is of Mobile type");
			return handleMobileUser(user);
		} else {
			logInfo(LOG_PREFIX, "User is of User type");
			return handleWebUser(user);
		}
	}

	private User handleMobileUser(User user) {
		String LOG_PREFIX = "handleMobileUser";

		logInfo(LOG_PREFIX, "Validating mobile user");
		
		LoginUtil.validateMobileUser(user);

		logInfo(LOG_PREFIX, "Mobile user validation success.Checking if user exists or not?");
		User userInDB = this.userDAO.getUserByEmailId(user.getEmailId(), false);
		if (userInDB == null) {
			logInfo(LOG_PREFIX, "Mobile user does not exist. Registering user : {}", user.getEmailId());
			logUserObject(user, LOG_PREFIX,"New User Details");
			Date now = new Date();
			user.setCreateDt(now);
			Set<SmartDevice> userDevices = new HashSet<>(user.getSmartDevices());
			user.setSmartDevices(null);

			Set<Role> userRoles = new HashSet<>();
			Role appUserRole = this.userDAO.getRoleType(UserRoleType.APP_USER);

			userRoles.add(appUserRole);
			user.setUserroles(userRoles);
			user.setIsEnabled(Boolean.TRUE);
			User createdUser = this.userDAO.createNewMobileUser(user);

			String privateKeyForDevice = UUID.randomUUID().toString();

			SmartDevice newDevice = null;
			for (SmartDevice smartDevice : userDevices) {
				newDevice = smartDevice;
				newDevice.setPrivateKey(privateKeyForDevice);
				newDevice.setCreateDt(now);
				newDevice.setUser(createdUser);
				newDevice.setIsEnabled(Boolean.TRUE);
				break;
			}
			createdUser = this.userDAO.setupFirstDeviceForUser(createdUser,
					newDevice);
			Long id = createdUser.getId();
			for (UserSocialDetail socialDetail : user.getSocialDetails()) {
				socialDetail.setUser(createdUser);
				this.userDAO.saveUserSocialData(socialDetail);
			}
			List<EventType> userInterests = this.eventTypeDAO.getAllEventTypes();
			this.eventTypeDAO.saveUserEventInterests(userInterests, id);
			return createdUser;
		} else {
			logInfo(LOG_PREFIX, "Mobile user exists. Checking if case of new device or login case.");
			Set<SmartDevice> existingDevices = userInDB.getSmartDevices();
			String deviceIdInRequest = null;
			for (SmartDevice smartDevice : user.getSmartDevices()) {
				deviceIdInRequest = smartDevice.getUniqueId();
				break;
			}
			boolean newDeviceCase = true;
			boolean devicesExistForUser = false;
			// Case when web user is trying to setup phone. There will be no
			// devices existing for him.
			if (existingDevices != null && !existingDevices.isEmpty()) {
				devicesExistForUser = true;
				for (SmartDevice smartDevice : existingDevices) {
					if (deviceIdInRequest.equals(smartDevice.getUniqueId())) {
						newDeviceCase = false;
						break;
					}
				}
			}

			if (newDeviceCase && !devicesExistForUser) {
				logInfo(LOG_PREFIX, "First time mobile setup for user : {}", user.getEmailId());
				
				Date now = new Date();
				String privateKeyForDevice = UUID.randomUUID().toString();
				SmartDevice newDevice = null;
				for (SmartDevice smartDevice : user.getSmartDevices()) {
					newDevice = smartDevice;
					break;
				}
				newDevice.setPrivateKey(privateKeyForDevice);
				newDevice.setUser(userInDB);
				newDevice.setCreateDt(now);
				user.getSmartDevices().add(newDevice);
				return this.userDAO
						.setupFirstDeviceForUser(userInDB, newDevice);

			} else if (newDeviceCase && devicesExistForUser) {
				
				logInfo(LOG_PREFIX, "New Device setup for user having existing devices {} ", user.getEmailId());
				Date now = new Date();
				String privateKeyForDevice = UUID.randomUUID().toString();

				SmartDevice newDevice = null;
				for (SmartDevice smartDevice : user.getSmartDevices()) {
					newDevice = smartDevice;
					break;
				}
				newDevice.setPrivateKey(privateKeyForDevice);
				newDevice.setCreateDt(now);
				newDevice.setUser(userInDB);
				User userWithAllDevices = this.userDAO
						.addDeviceToExistingUserDevices(userInDB, newDevice);
				User userObjectToReturn = null;
				try {
					logInfo(LOG_PREFIX, "Cloning user object to return only newly added device");
					userObjectToReturn = (User) userWithAllDevices.clone();
				} catch (CloneNotSupportedException cloneNotSupportedException) {
					
					logError(LOG_PREFIX, "Error while cloning user object", cloneNotSupportedException);
					userObjectToReturn = userWithAllDevices;
				}
				Set<SmartDevice> newDevices = new HashSet<>(1);
				newDevices.add(newDevice);
				userObjectToReturn.setSmartDevices(newDevices);
				return userObjectToReturn;
			} else {
				logInfo(LOG_PREFIX, "No new device added. Simple User login case");
				User userObjectToReturn = null;
				/*try {
					logger.info("Cloning user object to return only newly added device");
					//userObjectToReturn = (User) userInDB.clone();
					
				} catch (CloneNotSupportedException cloneNotSupportedException) {
					logger.error("Error while cloning user object",
							cloneNotSupportedException);
					// TODO:Add custom clone code here
					userObjectToReturn = userInDB;
				}
				*/
				//Update Profile Pic if changed in request
				if(user.getSocialDetails()!=null){
					Set<UserSocialDetail> socialDetails = user.getSocialDetails();
					Iterator<UserSocialDetail> iter = socialDetails.iterator();
					Iterator<UserSocialDetail> existingDetailsIter = userInDB.getSocialDetails().iterator();
					while(iter.hasNext()){
						UserSocialDetail socialDetail = iter.next();
						if(socialDetail.getSocialDetailType().equals(SocialDetailType.USER_PROFILE_PIC)){
							String profilePic = socialDetail.getUserSocialDetail();
							
							while(existingDetailsIter.hasNext()){
								UserSocialDetail existingSocialDetail = existingDetailsIter.next();
								if(existingSocialDetail.getSocialDetailType().equals(SocialDetailType.USER_PROFILE_PIC)){
									if(!existingSocialDetail.getUserSocialDetail().equals(BLANK)&&!profilePic.equals(existingSocialDetail.getUserSocialDetail())){
										logInfo(LOG_PREFIX, "Social Details change identified for user : {} , updating details", user.getEmailId());
										existingSocialDetail.setUserSocialDetail(profilePic);
									}
								}
							}
							
						}
					}
				}
				Set<SmartDevice> deviceInRequest = user.getSmartDevices();
				for(SmartDevice device : deviceInRequest){
					for(SmartDevice existingDevice : existingDevices){
						if(device.equals(existingDevice)){
							if(!device.getGcmId().equals(existingDevice.getGcmId())){
								logInfo(LOG_PREFIX, "Updating GCM ID for Device {}", existingDevice.getId());
								existingDevice.setGcmId(device.getGcmId());
							}
						}
					}
				}
				userObjectToReturn = userInDB;
				//Set<SmartDevice> newDevices = new HashSet<>(1);
				//userObjectToReturn.setSmartDevices(newDevices);
				return userObjectToReturn;
			}

		}

	}

	private User handleWebUser(User user) {
		String LOG_PREFIX = "handleWebUser";
		logInfo(LOG_PREFIX, "Validating Web User");
		LoginUtil.validateWebUser(user);
		logInfo(LOG_PREFIX, "User validation successful. \n Checking if user existing or not.");
		User userInDB = this.userDAO.getUserByEmailId(user.getEmailId(), false);
		if (userInDB == null) {
			logInfo(LOG_PREFIX, "User does not exist. Signup Case");
			Date now = new Date();
			user.setCreateDt(now);
			Set<Role> userRoles = new HashSet<>();
			Role appUserRole = this.userDAO.getRoleType(UserRoleType.APP_USER);
			userRoles.add(appUserRole);
			user.setUserroles(userRoles);
			User createdUser = this.userDAO.createNewWebUser(user);
			for (UserSocialDetail socialDetail : user.getSocialDetails()) {
				socialDetail.setUser(createdUser);
				this.userDAO.saveUserSocialData(socialDetail);
			}
			List<EventType> allTags = this.eventTypeDAO.getAllEventTypes();
			this.eventTypeDAO.saveUserEventInterests(allTags,
					createdUser.getId());
			return createdUser;
		} else {
			logger.info("User exists.Returning user details");
			logInfo(LOG_PREFIX, "User exists.Returning user details");
			User userToReturn = null;
			try {
				userToReturn = (User) userInDB.clone();
			} catch (CloneNotSupportedException e) {
				logger.error("Error while cloning user object", e);
				// TODO:Add custom clone code here
				userToReturn = userInDB;
			}
			userToReturn.setSmartDevices(new HashSet<SmartDevice>());
			return userToReturn;
		}

	}

	@Override
	public User getUser(long id) {
		User user = this.userDAO.getUserById(id);
		if(user == null){
			throw new EntityNotFoundException(id, RestErrorCodes.ERR_020, ERROR_USER_INVALID);
		}
		return user;
	}

	@Override
	public List<User> getAllUsers() {
		return this.userDAO.getAllUsers();
	}

	@Override
	public User loadUserByUsername(String username) {

		logger.info("### Inside loadUserByUsername. Username :{}  ###",
				username);

		User user = this.userDAO.getUserByEmailIdWithRoles(username, false);
		if (user == null) {
			throw new UnauthorizedException(RestErrorCodes.ERR_003,
					ERROR_USER_INVALID);
		}
		return user;
	}

	@Override
	public List<UserEventInterest> getUserEventInterests(Long id) {
		String LOG_PREFIX = "getUserEventInterests";
		logInfo(LOG_PREFIX, "Getting user event interests");
		List<EventType> allEventTypesExceptShop = this.eventTypeDAO.getAllEventTypesExceptShop();
		List<UserEventInterest> userEventInterests = new ArrayList<UserEventInterest>();
		
		List<EventType> userInterests = this.eventTypeDAO.getUserInterests(id);
		if(userInterests!=null){
			for(EventType eventType : allEventTypesExceptShop){
				UserEventInterest eventInterest = new UserEventInterest();
				if(userInterests.contains(eventType)){
					
					eventInterest.setEventType(eventType);
					eventInterest.setIsUserInterest(Boolean.TRUE);
				}else{
					eventInterest.setEventType(eventType);
				}
				userEventInterests.add(eventInterest);
			}
		}
		return userEventInterests;
	}

	@Override
	public List<UserEventInterest> saveUserEventInterests(Long id, List<UserEventInterest> interests) {
		String LOG_PREFIX = "saveUserEventInterests";
		List<String> typeNames = new ArrayList<>();
		for (UserEventInterest interest : interests) {
			if(interest.getIsUserInterest()){
				typeNames.add(interest.getEventType().getName());
			}
		}
		logInfo(LOG_PREFIX, "Saving user interests for user {} | {} ", id,typeNames);
		List<EventType> typesInDB = this.eventTypeDAO.getEventTypesByNames(typeNames);
		
		this.eventTypeDAO.saveUserEventInterests(typesInDB, id);
		return interests;
	}
	
	@Override
	public List<UserRetailEventInterest> getUserRetailEventInterests(Long id) {
		String LOG_PREFIX = "UserServiceImpl-getUserRetailEventInterests";
		logInfo(LOG_PREFIX, "Getting user retail event interests");
		List<EventTag> allRetailTags = this.eventTagDAO.getAllRetailTag();
		List<UserRetailEventInterest> userEventInterests = new ArrayList<UserRetailEventInterest>();
		
		List<EventTag> userRetailInterests = this.eventTagDAO.getRetailTagsForUser(id);
		if(userRetailInterests!=null){
			for(EventTag tag : allRetailTags){
				UserRetailEventInterest interest = new UserRetailEventInterest();
				if(userRetailInterests.contains(tag)){
					interest.setEventTag(tag);
					interest.setIsUserInterest(Boolean.FALSE);
				}else{
					interest.setEventTag(tag);
				}
				userEventInterests.add(interest);
			}
		}
		return userEventInterests;
	}
	
	@Override
	public List<UserRetailEventInterest> saveUserRetailEventInterests(Long id,
			List<UserRetailEventInterest> interests) {
		String LOG_PREFIX = "saveUserRetailEventInterests";
		List<String> typeNames = new ArrayList<>();
		for (UserRetailEventInterest interest : interests) {
			if(interest.getIsUserInterest()){
				typeNames.add(interest.getEventTag().getName());
			}
		}
		logInfo(LOG_PREFIX, "Saving user interests for user {} | {} ", id,typeNames);
		List<EventTag> typesInDB = this.eventTagDAO.getTagsByNames(typeNames);
		
		
		return interests;
	}

	@Override
	public SmartDevice getSmartDeviceDetails(String uniqueId)
			{
		String LOG_PREFIX = "getSmartDeviceDetails";
		logInfo(LOG_PREFIX, "Getting device details for {} ", uniqueId);
		SmartDevice smartDevice = this.smartDeviceDAO
				.getSmartDeviceByDeviceId(uniqueId);
		if (smartDevice == null) {
			logError(LOG_PREFIX, "Device details not found for id {}", uniqueId);
			throw new ClientException(RestErrorCodes.ERR_003,
					ERROR_INVALID_DEVICE);
		}
		return smartDevice;
	}

	@Override
	public List<Role> getUserRolesByDevice(String deviceId)
			 {
		String LOG_PREFIX = "getUserRolesByDevice";
		logInfo(LOG_PREFIX, "Getting user roles for user with device {}", deviceId);
		List<Role> userRoles = this.smartDeviceDAO
				.getUserRolesByDevice(deviceId);
		if (userRoles == null) {
			logError(LOG_PREFIX, "No user roles found for user with device {}", deviceId);
			throw new UnauthorizedException(RestErrorCodes.ERR_002,
					ERROR_LOGIN_USER_UNAUTHORIZED);
		}

		return userRoles;
	}

	@Override
	public List<UserFriend> setupUserFriendsForNewUser(Long userId,
			String[] friendSocialIds) {
		String LOG_PREFIX  = "setupUserFriendsForNewUser";
		logInfo(LOG_PREFIX, "Getting user details by id {}", userId);
		User user = this.userDAO.getUserById(userId);

		if (user == null) {
			logError(LOG_PREFIX,"User does not exist for id {}" , userId);
			throw new ClientException(RestErrorCodes.ERR_003,
					ERROR_USER_INVALID);
		}

		logInfo(LOG_PREFIX, "Finding friends with external ids {}", Arrays.toString(friendSocialIds));
		List<User> friendsInSystem = this.userDAO.setupFriendsUsingExternalIds(
				user, friendSocialIds);
		if (friendsInSystem == null) {
			logInfo(LOG_PREFIX, "No friends found for user {}", userId);
			friendsInSystem = new ArrayList<User>();
		}

		Transformer<List<UserFriend>, List<User>> transformer = (Transformer<List<UserFriend>, List<User>>) TransformerFactory
				.getTransformer(TransformerTypes.USER_TO_FRIEND_TRANSFORMER);
		List<UserFriend> userFriends = transformer.transform(friendsInSystem);
		return userFriends;
	}
	
	@Override
	public List<UserFriend> getUserFriends(Long userId) {
		String LOG_PREFIX = "getUserFriends";
		logInfo(LOG_PREFIX, "Getting user details by id {}", userId);
		User user = this.userDAO.getUserById(userId);

		if (user == null) {
			logError(LOG_PREFIX, "User not found with id {}", userId);
			throw new ClientException(RestErrorCodes.ERR_003,
					ERROR_USER_INVALID);
		}
		List<User> friendsInSystem = this.userDAO.getUserFriends(user);
		
		Transformer<List<UserFriend>, List<User>> transformer = (Transformer<List<UserFriend>, List<User>>) TransformerFactory
				.getTransformer(TransformerTypes.USER_TO_FRIEND_TRANSFORMER);
		List<UserFriend> userFriends = transformer.transform(friendsInSystem);
		return userFriends;
	}
	
	@Override
	public List<UserSetting> getUserSettings(Long id) {
		String LOG_PREFIX = "getUserSettings";
		logInfo(LOG_PREFIX, "Getting user details by id {}", id);
		User user = this.userDAO.getUserById(id);
		if(user==null){
			logError(LOG_PREFIX,"User does not exist for id {}" , id);
			throw new ClientException(RestErrorCodes.ERR_003,
					ERROR_USER_INVALID);
		}
		
		List<UserSetting> userSettings = this.userDAO.getUserSettings(user);
		return userSettings;
	}
	
	@Override
	public List<UserSetting> setUserSettings(Long userId, List<UserSetting> newSettings) {
		logger.info("### Inside setUserSettings ###");
		String LOG_PREFIX = "setUserSettings";
		logInfo(LOG_PREFIX, "Getting user details by id {}", userId);
		User user = this.userDAO.getUserById(userId);
		if(user==null){
			logError(LOG_PREFIX,"User does not exist for id {}" , userId);
			throw new ClientException(RestErrorCodes.ERR_003,
					ERROR_USER_INVALID);
		}
		List<UserSetting> oldSettings = this.userDAO.getUserSettings(user);
		logInfo(LOG_PREFIX, "Found Old User Settings {}", oldSettings);
		Map<String,UserSetting> settingsMap = new HashMap<String,UserSetting>();
		for(UserSetting userSetting : newSettings){
			settingsMap.put(userSetting.getName(), userSetting);
		}
		Date now = new Date();
		for(UserSetting userSetting : oldSettings){
			String name = userSetting.getName();
			String value = userSetting.getValue();
			UserSetting newSetting = settingsMap.get(name);
			if(newSetting!=null && !newSetting.getValue().equalsIgnoreCase(value)){
				logInfo(LOG_PREFIX, "Updating User Setting {} to {}", name,newSetting.getValue());
				userSetting.setValue(newSetting.getValue());
				userSetting.setUpdateDt(now);
			}
		}
		List<UserSetting> updatedSettings = this.userDAO.getUserSettings(user);
		return updatedSettings;
	}
	
	
}
