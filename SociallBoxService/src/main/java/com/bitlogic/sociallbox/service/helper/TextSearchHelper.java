package com.bitlogic.sociallbox.service.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.GAPIConfig;
import com.bitlogic.sociallbox.data.model.ext.google.GooglePlaces;
import com.bitlogic.sociallbox.data.model.requests.TextSearchRequest;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.exception.ServiceException;

public class TextSearchHelper implements Constants{

	private static final Logger logger = LoggerFactory
			.getLogger(TextSearchHelper.class);

	public static GooglePlaces executeSearch(RestTemplate restTemplate,
			TextSearchRequest textSearchRequest, GAPIConfig gapiConfig)
			throws ClientException,ServiceException {

		StringBuilder url = new StringBuilder(gapiConfig.getTextSearchURL());
		url.append(gapiConfig.getDataExchangeFormat() + Constants.QUESTIONMARK);
		// "query" param
		url.append(TextSearchRequest.TextSearchRequestParamNames.QUERY
				.getName()
				+ Constants.EQUAL
				+ textSearchRequest.getQuery().replaceAll(" ", Constants.PLUS));

		// "location" param
		if (textSearchRequest.getLocation() != null) {
			url.append(Constants.AMP
					+ TextSearchRequest.TextSearchRequestParamNames.LOCATION
							.getName() + Constants.EQUAL
					+ textSearchRequest.getLocation());
		}

		// "radius" param
		if (textSearchRequest.getRadius() != null) {
			url.append(Constants.AMP
					+ TextSearchRequest.TextSearchRequestParamNames.RADIUS
							.getName() + Constants.EQUAL
					+ textSearchRequest.getRadius());
		}

		// "types" param
		if (textSearchRequest.getTypes() != null) {
			url.append(Constants.AMP
					+ TextSearchRequest.TextSearchRequestParamNames.TYPES
							.getName() + Constants.EQUAL
					+ textSearchRequest.getTypes());
		}

		// "name" param
		if (textSearchRequest.getName() != null) {
			url.append(Constants.AMP
					+ TextSearchRequest.TextSearchRequestParamNames.NAME
							.getName()
					+ Constants.EQUAL
					+ textSearchRequest.getName().replaceAll(" ",
							Constants.PLUS));
		}

		// "rankby" param
		if (textSearchRequest.getRankBy() != null) {
			url.append(Constants.AMP
					+ TextSearchRequest.TextSearchRequestParamNames.RANKBY
							.getName() + Constants.EQUAL
					+ textSearchRequest.getRankBy());
		}

		// "key" param
		url.append(Constants.AMP
				+ TextSearchRequest.TextSearchRequestParamNames.KEY.getName()
				+ Constants.EQUAL + gapiConfig.getGapiKey());

		logger.info("### Inside TextSearchHelper.executeSearch | URL : {} "
				, url.toString());

		logger.info("### Executing Search ###");

		ResponseEntity<GooglePlaces> placesResponse = restTemplate.exchange(
				url.toString(), HttpMethod.GET, null,
				new ParameterizedTypeReference<GooglePlaces>() {
				});

		HttpStatus returnStatus = placesResponse.getStatusCode();
		boolean isSuccess = returnStatus.is2xxSuccessful();
		if (isSuccess) {
			logger.info("### Search successful for url : {}" , url.toString());

		} else {
			if(returnStatus.is4xxClientError()){
				throw new ClientException(RestErrorCodes.ERR_010,Constants.ERROR_GAPI_CLIENT_REQUEST);
			}else if (returnStatus.is5xxServerError()){
				throw new ServiceException("GAPI",RestErrorCodes.ERR_010,Constants.ERROR_GAPI_WEBSERVICE_ERROR);
			}
		}
		GooglePlaces searchedPlaces = placesResponse.getBody();
		logger.info("Searched Places {} ",searchedPlaces.getResults());
		
		return searchedPlaces;
	}
}
