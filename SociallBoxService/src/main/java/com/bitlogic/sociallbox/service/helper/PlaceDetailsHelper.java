package com.bitlogic.sociallbox.service.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.APIConfig;
import com.bitlogic.sociallbox.data.model.GAPIConfig;
import com.bitlogic.sociallbox.data.model.ext.Place;
import com.bitlogic.sociallbox.data.model.ext.google.GooglePlace;
import com.bitlogic.sociallbox.data.model.requests.NearbySearchRequestGoogle;
import com.bitlogic.sociallbox.data.model.requests.PlaceDetailsRequest;
import com.bitlogic.sociallbox.data.model.requests.PlaceDetailsRequestGoogle;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.exception.ServiceException;
import com.bitlogic.sociallbox.service.utils.LoggingService;

public class PlaceDetailsHelper extends LoggingService implements Constants {

	private static final Logger logger = LoggerFactory
			.getLogger(PlaceDetailsHelper.class);

	@Override
	public Logger getLogger() {
		return logger;
	}

	public static Place executeSearch(RestTemplate restTemplate,
			PlaceDetailsRequest placeDetailsRequest, APIConfig apiConfig)
			 {
		if(placeDetailsRequest instanceof PlaceDetailsRequestGoogle){
			PlaceDetailsRequestGoogle requestGoogle = (PlaceDetailsRequestGoogle) placeDetailsRequest;
			GAPIConfig gapiConfig = (GAPIConfig) apiConfig;
			return getPlaceDetailsFromGoogle(restTemplate, requestGoogle, gapiConfig);
		}else{
			throw new IllegalArgumentException("Place Details request is of invalid type");
		}
		
	}

	private static Place getPlaceDetailsFromGoogle(RestTemplate restTemplate,
			PlaceDetailsRequestGoogle placeDetailsRequest, GAPIConfig gapiConfig) {
		
		String LOG_PREFIX = "getPlaceDetailsFromGoogle";
		StringBuilder url = new StringBuilder(gapiConfig.getPlaceDetailsURL());
		url.append(gapiConfig.getDataExchangeFormat() + Constants.QUESTIONMARK);
		url.append(PlaceDetailsRequestGoogle.PlaceDetailsRequestParams.PLACEID
				.getName() + Constants.EQUAL + placeDetailsRequest.getPlaceId());
		url.append(Constants.AMP
				+ NearbySearchRequestGoogle.RequestParamNames.KEY.getName()
				+ Constants.EQUAL + gapiConfig.getGapiKey());
		String message = LOG_PREFIX + " :: " + "Invoking request to URL = {}";
		logger.info(message, url.toString());

		ResponseEntity<GooglePlace> placesResponse = restTemplate
				.exchange(url.toString(), HttpMethod.GET, null,
						new ParameterizedTypeReference<GooglePlace>() {
						});
		HttpStatus returnStatus = placesResponse.getStatusCode();
		boolean isSuccess = returnStatus.is2xxSuccessful();
		if (isSuccess) {
			message = LOG_PREFIX + " :: " + "Search returned success response";
			logger.info(message);

		} else {
			if (returnStatus.is4xxClientError()) {
				throw new ClientException(RestErrorCodes.ERR_010,
						ERROR_GAPI_CLIENT_REQUEST);
			} else if (returnStatus.is5xxServerError()) {
				throw new ServiceException("GAPI", RestErrorCodes.ERR_010,
						Constants.ERROR_GAPI_WEBSERVICE_ERROR);
			}
		}

		return placesResponse.getBody();
	}
}
