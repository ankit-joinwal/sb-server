package com.bitlogic.sociallbox.service.business.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.GAPIConfig;
import com.bitlogic.sociallbox.data.model.ext.Place;
import com.bitlogic.sociallbox.data.model.ext.google.GooglePlace;
import com.bitlogic.sociallbox.data.model.ext.google.GooglePlace.Result;
import com.bitlogic.sociallbox.data.model.ext.google.GooglePlace.Result.Photo;
import com.bitlogic.sociallbox.data.model.requests.PlaceDetailsRequest;
import com.bitlogic.sociallbox.data.model.requests.PlaceDetailsRequestGoogle;
import com.bitlogic.sociallbox.service.business.PlaceDetailService;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.exception.ServiceException;
import com.bitlogic.sociallbox.service.helper.PlaceDetailsHelper;
import com.bitlogic.sociallbox.service.utils.GeoUtils;
import com.bitlogic.sociallbox.service.utils.LoggingService;

@Component("placeDetailsServiceGoogle")
public class PlaceDetailsServiceGoogle extends LoggingService implements
		PlaceDetailService, Constants {
	private static final Logger logger = LoggerFactory
			.getLogger(PlaceDetailsServiceGoogle.class);

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private GAPIConfig gapiConfig;

	@Override
	public Place getPlaceDetails(PlaceDetailsRequest placeDetailsRequest) {
		Place placeDetails = null;
		try {
			PlaceDetailsRequestGoogle requestGoogle = new PlaceDetailsRequestGoogle();
			requestGoogle.setPlaceId(placeDetailsRequest.getPlaceId());
			placeDetails = PlaceDetailsHelper.executeSearch(restTemplate,
					requestGoogle, gapiConfig);
			if (placeDetails instanceof GooglePlace) {
				enrichResponse(placeDetails, placeDetailsRequest);
			}
		} catch (ClientException exception) {
			logger.error("Error occurred while retrieving place details ",
					exception);
			throw exception;
		} catch (ServiceException exception) {
			logger.error("Error occurred while retrieving place details ",
					exception);
			throw exception;
		} catch (Exception exception) {
			logger.error("Error occurred while retrieving place details",
					exception);
			throw new ServiceException(GEO_SERVICE_NAME,
					RestErrorCodes.ERR_050, exception.getMessage());
		}
		return placeDetails;
	}

	private void enrichResponse(Place place,
			PlaceDetailsRequest placeDetailsRequest) {
		String LOG_PREFIX = "enrichResponse";

		Double userLat = Double.parseDouble(placeDetailsRequest
				.getUserLatitude());
		Double userLon = Double.parseDouble(placeDetailsRequest
				.getUserLongitude());
		GooglePlace googlePlace = (GooglePlace) place;
		String placePhotoGetAPIPath = gapiConfig.getPlacePhotoGetAPI();
		String distanceFromUser = null;
		Result result = googlePlace.getResult();
		try {
			Double placeLat = googlePlace.getResult().getGeometry()
					.getLocation().getLattitude();
			Double placeLong = googlePlace.getResult().getGeometry()
					.getLocation().getLongitude();
			distanceFromUser = GeoUtils.calculateDistance(userLat, userLon,
					placeLat, placeLong);
			if (result.getPhotos() != null && result.getPhotos().length > 0) {
				for (Photo photo : result.getPhotos()) {
					if (photo.getPhotoReference() != null) {
						photo.setUrl(placePhotoGetAPIPath
								+ photo.getPhotoReference());
					}
				}
			}
		} catch (Exception ex) {
			logError(LOG_PREFIX,
					"Error while calculating distance from user for place "
							+ result.getName(), ex);
			logWarning(LOG_PREFIX,
					"Skipping this for distance calculation and continue for next place");
		}

		result.setDistanceFromYou(distanceFromUser);

	}
}
