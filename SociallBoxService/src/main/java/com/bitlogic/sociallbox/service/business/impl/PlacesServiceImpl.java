package com.bitlogic.sociallbox.service.business.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.Category;
import com.bitlogic.sociallbox.data.model.SourceSystemForPlaces;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.UserAndPlaceMapping;
import com.bitlogic.sociallbox.data.model.ext.Place;
import com.bitlogic.sociallbox.data.model.ext.Places;
import com.bitlogic.sociallbox.data.model.ext.google.GooglePlace;
import com.bitlogic.sociallbox.data.model.ext.zomato.ZomatoPlaces;
import com.bitlogic.sociallbox.data.model.ext.zomato.ZomatoPlaces.Restaurant;
import com.bitlogic.sociallbox.data.model.requests.NearbySearchRequest;
import com.bitlogic.sociallbox.data.model.requests.PlaceDetailsRequest;
import com.bitlogic.sociallbox.data.model.response.UserFriend;
import com.bitlogic.sociallbox.service.business.EventService;
import com.bitlogic.sociallbox.service.business.PlacesService;
import com.bitlogic.sociallbox.service.dao.CategoryDAO;
import com.bitlogic.sociallbox.service.dao.PlaceDAO;
import com.bitlogic.sociallbox.service.dao.SmartDeviceDAO;
import com.bitlogic.sociallbox.service.dao.UserDAO;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.exception.UnauthorizedException;
import com.bitlogic.sociallbox.service.transformers.Transformer;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory.TransformerTypes;
import com.bitlogic.sociallbox.service.transformers.UsersToFriendsTransformer;
import com.bitlogic.sociallbox.service.utils.GeoUtils;
import com.bitlogic.sociallbox.service.utils.LoggingService;

/**
 * Service for searching nearby places based on category.
 * Note that this is not used for searching event category.
 * For events, 
 * @see EventService
 * @author ajoinwal
 *
 */
@Service
@Transactional
public class PlacesServiceImpl extends LoggingService implements PlacesService,Constants {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PlacesServiceImpl.class);
	
	@Autowired
	private CategoryDAO categoryDAO;
	
	@Autowired
	private NearbySearchServiceZomato nearbySearchServiceZomato;
	
	@Autowired
	private NearbySearchServiceGoogle nearbySearchServiceGoogle;
	
	@Autowired
	private PlaceDetailsServiceGoogle placeDetailsServiceGoogle;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private PlaceDAO placeDAO;
	
	@Autowired
	private SmartDeviceDAO smartDeviceDAO;
	
	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	
	@Override
	public Places searchNearby(NearbySearchRequest nearbySearchRequest) {
		String LOG_PREFIX = "PlacesServiceImpl-searchNearby"; 
		/*
		 * 	To determine source system for places search,
		 *  search for input category and check SourceSystem in Category object
		 */
		Long categoryId = nearbySearchRequest.getCategoryId();
		
		logInfo(LOG_PREFIX, "Category To Search ={}", categoryId);
		
		Category category = this.categoryDAO.getCategoryById(categoryId);
		
		if(category==null){
			logError(LOG_PREFIX, "Invalid input category ={}", categoryId);
			throw new ClientException(RestErrorCodes.ERR_003, ERROR_INVALID_CATEGORY);
		}
		
		SourceSystemForPlaces sourceSystem = category.getSystemForPlaces();
		
		//Depending upon source system , route the request to appropriate search service

		if(sourceSystem == SourceSystemForPlaces.ZOMATO){
			logInfo(LOG_PREFIX, "Source system for search is {}",sourceSystem);
			nearbySearchRequest.setExternalId(category.getExtId());
			return searchPlacesFromZomato(nearbySearchRequest);
		}else if(sourceSystem == SourceSystemForPlaces.GOOGLE){
			logInfo(LOG_PREFIX, "Source system for search is {}",sourceSystem);
			nearbySearchRequest.setExternalId(category.getExtId());
			return searchPlacesFromGoogle(nearbySearchRequest);
		}else{
			throw new ClientException(RestErrorCodes.ERR_003,ERROR_INVALID_SOURCE_SYSTEM_PLACES);
		}
	}
	
	/**
	 * Utility method for searching places using zomato api
	 * @param nearbySearchRequest
	 * @return List of places from zomato
	 */
	private Places searchPlacesFromZomato(NearbySearchRequest nearbySearchRequest){
		String LOG_PREFIX = "PlacesServiceImpl-searchPlacesFromZomato";
		Places places = this.nearbySearchServiceZomato.search(nearbySearchRequest);
		if(places instanceof ZomatoPlaces){
			places.setSourceSystem(SourceSystemForPlaces.ZOMATO.toString());
			ZomatoPlaces zomatoPlaces = (ZomatoPlaces) places;
			
			logInfo(LOG_PREFIX, "Found Total Results = {} , showing from {} to {}",
					zomatoPlaces.getResultsFound(),zomatoPlaces.getResultsStart(),zomatoPlaces.getResultsShown());
			if(zomatoPlaces.getRestaurants()!=null && !zomatoPlaces.getRestaurants().isEmpty()){
				Double userLat = Double.parseDouble(nearbySearchRequest.getLatitude());
				Double userLon = Double.parseDouble(nearbySearchRequest.getLongitude());
				
				for(Restaurant restaurant : zomatoPlaces.getRestaurants()){
					String distanceFromUser = null;
					try {
						Double placeLat = Double.parseDouble(restaurant
								.getRestaurantData().getLocation()
								.getLatitude());
						Double placeLong = Double.parseDouble(restaurant
								.getRestaurantData().getLocation()
								.getLongitude());
						distanceFromUser = GeoUtils.calculateDistance(userLat,
								userLon, placeLat, placeLong);
					} catch (Exception ex) {
						logError(LOG_PREFIX,
								"Error while calculating distance from user for restaurant "
										+ restaurant.getRestaurantData()
												.getName(), ex);
						logWarning(LOG_PREFIX,
								"Skipping this for distance calculation and continue for next restaurant");
					}
					Integer costForTwo = restaurant.getRestaurantData().getAverageCostForTwo();
					if(costForTwo!=null){
						restaurant.getRestaurantData().setCostPerPerson(costForTwo/2);
					}
					
					restaurant.getRestaurantData().setDistanceFromYou(distanceFromUser);
				}
			}
			
		}else{
			logError(LOG_PREFIX, "Return data is not of Type {}",ZomatoPlaces.class.getName());
		}
		
		return places;
	}
	
	/**
	 * Utility method for searching places using google api
	 * @param nearbySearchRequest
	 * @return List of places from google
	 */
	private Places searchPlacesFromGoogle(NearbySearchRequest nearbySearchRequest){
		Places places = this.nearbySearchServiceGoogle.search(nearbySearchRequest);
		places.setSourceSystem(SourceSystemForPlaces.GOOGLE.toString());
		return places;
	}
	
	@Override
	public Place getPlaceDetails(PlaceDetailsRequest placeDetailsRequest) {
		String LOG_PREFIX = "PlacesServiceImpl-getPlaceDetails";
		logInfo(LOG_PREFIX, "PlaceDetailsRequest = {}", placeDetailsRequest);
		String sourceSystem = placeDetailsRequest.getSourceSystem();
		SourceSystemForPlaces systemForPlaces = null;
		try{
			systemForPlaces = SourceSystemForPlaces.valueOf(sourceSystem);
		}catch(Exception ex){
			throw new ClientException(RestErrorCodes.ERR_001, ERROR_INVALID_SOURCE_SYSTEM);
		}
		if(systemForPlaces != null && systemForPlaces == SourceSystemForPlaces.GOOGLE){
			Place place = placeDetailsServiceGoogle.getPlaceDetails(placeDetailsRequest);
			
			if(placeDetailsRequest.getUserId()!=null){
				UserAndPlaceMapping mapping = new UserAndPlaceMapping();
				mapping.setUserId(placeDetailsRequest.getUserId());
				mapping.setPlaceId(placeDetailsRequest.getPlaceId());
				Boolean isLikedByUser = placeDAO.checkIfUserLikesPlace(mapping);
				GooglePlace googlePlace = (GooglePlace) place;
				googlePlace.getResult().setIsLikedByuser(isLikedByUser);
			}
			
			return place;
		}else{
			throw new ClientException(RestErrorCodes.ERR_001, ERROR_INVALID_SOURCE_SYSTEM);
		}
		
	}
	
	@Override
	public List<UserFriend> getFriendsWhoLikePlace(String deviceId,String placeId) {
		
		List<UserFriend> userFriends = new ArrayList<UserFriend>();
		String LOG_PREFIX = "PlacesServiceImpl-getFriendsWhoLikePlace";
		logInfo(LOG_PREFIX, "Getting user info from device id : {}",deviceId);
		User user = this.smartDeviceDAO.getUserInfoFromDeviceId(deviceId);
		if(user == null){
			logError(LOG_PREFIX, "No user exists fro given device Id {}", deviceId);
			throw new UnauthorizedException(RestErrorCodes.ERR_002, ERROR_LOGIN_USER_UNAUTHORIZED);
		}
		logInfo(LOG_PREFIX, "Getting friends list of user = {} who like place = {}", user.getId(),placeId);
		List<Long> usersIds = this.placeDAO.getUsersWhoLikePlace(placeId);
		if(usersIds!=null){
			List<User> friendsWhoLikePlace = this.userDAO.getUserFriendsByIds(user, usersIds);
			if(friendsWhoLikePlace!=null){
				UsersToFriendsTransformer transformer = (UsersToFriendsTransformer) TransformerFactory
					.getTransformer(TransformerTypes.USER_TO_FRIEND_TRANSFORMER);
				userFriends = transformer.transform(friendsWhoLikePlace);
			}
		}else{
			logInfo(LOG_PREFIX, "No users like this place = {}", placeId);
		}
		
		
		return userFriends;
	}
	
	@Override
	public void saveUserPlaceLike(String deviceId, String placeId) {

		String LOG_PREFIX = "PlacesServiceImpl-saveUserPlaceLike";
		logInfo(LOG_PREFIX, "Getting user info from device id : {}",deviceId);
		User user = this.smartDeviceDAO.getUserInfoFromDeviceId(deviceId);
		if(user == null){
			logError(LOG_PREFIX, "No user exists fro given device Id {}", deviceId);
			throw new UnauthorizedException(RestErrorCodes.ERR_002, ERROR_LOGIN_USER_UNAUTHORIZED);
		}
		
		UserAndPlaceMapping mapping = new UserAndPlaceMapping();
		mapping.setUserId(user.getId());
		mapping.setPlaceId(placeId);
		
		this.placeDAO.saveUserLikeForPlace(mapping);
		logInfo(LOG_PREFIX, "User Like for place saved successfully");
	}
	
}
