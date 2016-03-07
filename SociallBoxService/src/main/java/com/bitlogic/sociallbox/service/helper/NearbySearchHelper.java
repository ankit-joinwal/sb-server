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
import com.bitlogic.sociallbox.data.model.ext.Places;
import com.bitlogic.sociallbox.data.model.requests.NearbySearchRequest;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.exception.ServiceException;

public class NearbySearchHelper {

	private static final Logger logger = LoggerFactory
			.getLogger(NearbySearchHelper.class);

	public static Places executeSearch(RestTemplate restTemplate,
			NearbySearchRequest nearbySearchRequest, GAPIConfig gapiConfig)
			throws ClientException,ServiceException {

		StringBuilder url = new StringBuilder(gapiConfig.getNearBySearchURL());
		url.append(gapiConfig.getDataExchangeFormat() + Constants.QUESTIONMARK);
		
		url.append(NearbySearchRequest.NearbySearchRequestParamNames.LOCATION
				.getName()
				+ Constants.EQUAL
				+ nearbySearchRequest.getLocation());
		if (nearbySearchRequest.getRadius() != null) {
			url.append(Constants.AMP
					+ NearbySearchRequest.NearbySearchRequestParamNames.RADIUS
							.getName() + Constants.EQUAL
					+ nearbySearchRequest.getRadius());
		}
		if (nearbySearchRequest.getTypes() != null) {
			url.append(Constants.AMP
					+ NearbySearchRequest.NearbySearchRequestParamNames.TYPES
							.getName() + Constants.EQUAL
					+ nearbySearchRequest.getTypes());
		}
		if (nearbySearchRequest.getName() != null) {
			url.append(Constants.AMP
					+ NearbySearchRequest.NearbySearchRequestParamNames.NAME
							.getName()
					+ Constants.EQUAL
					+ nearbySearchRequest.getName().replaceAll(" ",
							Constants.PLUS));
		}
		if (nearbySearchRequest.getRankBy() != null) {
			url.append(Constants.AMP
					+ NearbySearchRequest.NearbySearchRequestParamNames.RANKBY
							.getName() + Constants.EQUAL
					+ nearbySearchRequest.getRankBy());
		}
		url.append(Constants.AMP
				+ NearbySearchRequest.NearbySearchRequestParamNames.KEY
						.getName() + Constants.EQUAL + gapiConfig.getGapiKey());
		if (nearbySearchRequest.getPageToken() != null) {
			url.append(Constants.AMP
					+ NearbySearchRequest.NearbySearchRequestParamNames.PAGE_TOKEN
							.getName() + Constants.EQUAL
					+ nearbySearchRequest.getPageToken());
		}

		logger.info("### Inside NearbySearchHelper.executeSearch | URL : {} "
				+ url.toString());

		logger.info("### Executing Search ###");
		ResponseEntity<Places> placesResponse = restTemplate.exchange(
				url.toString(), HttpMethod.GET, null,
				new ParameterizedTypeReference<Places>() {
				});

		HttpStatus returnStatus = placesResponse.getStatusCode();
		boolean isSuccess = returnStatus.is2xxSuccessful();
		if(isSuccess){
			logger.info("### Search successful for url : {}"+url.toString());
			
		}else{
			if(returnStatus.is4xxClientError()){
				throw new ClientException(RestErrorCodes.ERR_010,Constants.ERROR_GAPI_CLIENT_REQUEST);
			}else if (returnStatus.is5xxServerError()){
				throw new ServiceException("GAPI",RestErrorCodes.ERR_010,Constants.ERROR_GAPI_WEBSERVICE_ERROR);
			}
		}
		
		return placesResponse.getBody();
	}
}
