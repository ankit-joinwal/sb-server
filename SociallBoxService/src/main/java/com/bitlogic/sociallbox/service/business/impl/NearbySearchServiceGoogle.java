package com.bitlogic.sociallbox.service.business.impl;

import static com.bitlogic.Constants.COMMA;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.GAPIConfig;
import com.bitlogic.sociallbox.data.model.ext.Places;
import com.bitlogic.sociallbox.data.model.ext.google.GooglePlaces;
import com.bitlogic.sociallbox.data.model.ext.google.GooglePlaces.Result;
import com.bitlogic.sociallbox.data.model.ext.google.GooglePlaces.Result.Photo;
import com.bitlogic.sociallbox.data.model.requests.NearbySearchRequest;
import com.bitlogic.sociallbox.data.model.requests.NearbySearchRequestGoogle;
import com.bitlogic.sociallbox.service.business.NearbySearchService;
import com.bitlogic.sociallbox.service.helper.NearbySearchHelper;
import com.bitlogic.sociallbox.service.utils.GeoUtils;
import com.bitlogic.sociallbox.service.utils.LoggingService;

/**
 * Service for Performing "Near by" search. <a href=
 * "https://developers.google.com/places/web-service/search#PlaceSearchRequests"
 * >
 * 
 * @author ajoinwal
 * 
 */
@Component("nearbySearchServiceGoogle")
public class NearbySearchServiceGoogle extends LoggingService implements NearbySearchService ,Constants{

	private static final Logger logger = LoggerFactory
			.getLogger(NearbySearchServiceGoogle.class);

	@Override
	public Logger getLogger() {
		return logger;
	}
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private GAPIConfig gapiConfig;
	
	
	public GAPIConfig getGapiConfig() {
		return gapiConfig;
	}

	public void setGapiConfig(GAPIConfig gapiConfig) {
		this.gapiConfig = gapiConfig;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public Places search(NearbySearchRequest nearbySearchRequest) {
		String LOG_PREFIX = "search";
		logInfo(LOG_PREFIX, "NearbySearchRequest ={}", nearbySearchRequest);
		NearbySearchRequestGoogle requestZomato = constructGoogleRequest(nearbySearchRequest);
		Places places = NearbySearchHelper.execute(restTemplate, requestZomato, gapiConfig);
		enrichResponse(places, nearbySearchRequest);
		
		return places;
	}
	
	private void enrichResponse(Places places,NearbySearchRequest nearbySearchRequest){
		
		String LOG_PREFIX = "enrichResponse";
		if(places instanceof GooglePlaces){
			String placePhotoGetAPIPath = gapiConfig.getPlacePhotoGetAPI();
			
			GooglePlaces googlePlaces = (GooglePlaces) places;
			logInfo(LOG_PREFIX, "Results found {}",googlePlaces.getResults().size());
			if(googlePlaces.getResults()!=null && !googlePlaces.getResults().isEmpty()){
				Double userLat = Double.parseDouble(nearbySearchRequest.getLatitude());
				Double userLon = Double.parseDouble(nearbySearchRequest.getLongitude());
				
				for(Result result : googlePlaces.getResults()){
					String distanceFromUser = null;
					
					try {
						Double placeLat = result.getGeometry().getLocation().getLattitude();
						Double placeLong = result.getGeometry().getLocation().getLongitude();
						distanceFromUser = GeoUtils.calculateDistance(userLat,
								userLon, placeLat, placeLong);
						if(result.getPhotos()!=null && result.getPhotos().length>0){
							for(Photo photo : result.getPhotos()){
								if(photo.getPhotoReference()!=null){
									photo.setUrl(placePhotoGetAPIPath + 
											photo.getPhotoReference());
								}
							}
						}
					} catch (Exception ex) {
						logError(LOG_PREFIX,
								"Error while calculating distance from user for place "
										+ result.getName()
												, ex);
						logWarning(LOG_PREFIX,
								"Skipping this for distance calculation and continue for next place");
					}
					
					result.setDistanceFromYou(distanceFromUser);
				}
			}
		}else{
			logError(LOG_PREFIX, "Return data is not of Type {}",GooglePlaces.class.getName());
		}
		
	}

	private NearbySearchRequestGoogle constructGoogleRequest(NearbySearchRequest nearbySearchRequest){
		String LOG_PREFIX = "constructGoogleRequest";
		logInfo(LOG_PREFIX, "Constructing Google Request");
		NearbySearchRequestGoogle googleRequest = new NearbySearchRequestGoogle();
		
		googleRequest.setRadius(nearbySearchRequest.getRadius());
		/*
		googleRequest.setLatitude(nearbySearchRequest.getLatitude());
		googleRequest.setLongitude(nearbySearchRequest.getLongitude());
		*/
		googleRequest.setLocation(nearbySearchRequest.getLatitude()+COMMA+nearbySearchRequest.getLongitude());
		googleRequest.setTypes(nearbySearchRequest.getExternalId());

		if(nearbySearchRequest.getPageNumber()!=null &&
				!nearbySearchRequest.getPageNumber().isEmpty()&& !nearbySearchRequest.getPageNumber().equals("1")){
			googleRequest.setPageToken(nearbySearchRequest.getPageNumber());
		}

		String sortBy = nearbySearchRequest.getSort();
		googleRequest.setRankBy(NearbySearchRequestGoogle.getSortByUsingCode(sortBy).getValue());
		logInfo(LOG_PREFIX, "NearbySearchRequestZomato = {}", googleRequest);
		return googleRequest;
	}
}
