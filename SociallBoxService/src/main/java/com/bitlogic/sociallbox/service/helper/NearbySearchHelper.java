package com.bitlogic.sociallbox.service.helper;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.APIConfig;
import com.bitlogic.sociallbox.data.model.GAPIConfig;
import com.bitlogic.sociallbox.data.model.ZomatoAPIConfig;
import com.bitlogic.sociallbox.data.model.ext.Places;
import com.bitlogic.sociallbox.data.model.ext.google.GooglePlaces;
import com.bitlogic.sociallbox.data.model.ext.zomato.ZomatoPlaces;
import com.bitlogic.sociallbox.data.model.requests.NearbySearchRequest;
import com.bitlogic.sociallbox.data.model.requests.NearbySearchRequestGoogle;
import com.bitlogic.sociallbox.data.model.requests.NearbySearchRequestZomato;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;
import com.bitlogic.sociallbox.service.exception.ServiceException;
import com.bitlogic.sociallbox.service.utils.LoggingService;

public class NearbySearchHelper extends LoggingService {

	private static final Logger logger = LoggerFactory
			.getLogger(NearbySearchHelper.class);

	@Override
	public Logger getLogger() {
		return logger;
	}

	public static Places execute(RestTemplate restTemplate,
			NearbySearchRequest nearbySearchRequest, APIConfig apiConfig) {

		if (nearbySearchRequest instanceof NearbySearchRequestZomato) {
			NearbySearchRequestZomato requestZomato = (NearbySearchRequestZomato) nearbySearchRequest;
			ZomatoAPIConfig zomatoAPIConfig = (ZomatoAPIConfig) apiConfig;
			return executeZomatoSearch(restTemplate, requestZomato,
					zomatoAPIConfig);
		}else if(nearbySearchRequest instanceof NearbySearchRequestGoogle){
			NearbySearchRequestGoogle requestGoogle = (NearbySearchRequestGoogle) nearbySearchRequest;
			GAPIConfig gapiConfig = (GAPIConfig) apiConfig;
			return executeGoogleSearch(restTemplate, requestGoogle, gapiConfig);
		}

		return null;
	}

	private static Places executeZomatoSearch(RestTemplate restTemplate,
			NearbySearchRequestZomato nearbySearchRequest,
			ZomatoAPIConfig apiConfig) {
		String LOG_PREFIX = "executeZomatoSearch";
		StringBuilder url = new StringBuilder(apiConfig.getNearBySearchURL()
				+ Constants.QUESTIONMARK);
		// ?start=:start
		append(url,
				NearbySearchRequestZomato.RequestParamNames.START.getName(),
				Constants.EQUAL, nearbySearchRequest.getStart());
		// &count=:count
		append(url, Constants.AMP,
				NearbySearchRequestZomato.RequestParamNames.COUNT.getName(),
				Constants.EQUAL, nearbySearchRequest.getCount());
		// &lat=:latitude
		append(url, Constants.AMP,
				NearbySearchRequestZomato.RequestParamNames.LATITUDE,
				Constants.EQUAL, nearbySearchRequest.getLatitude());
		// &lon=:longitude
		append(url, Constants.AMP,
				NearbySearchRequestZomato.RequestParamNames.LONGITUDE,
				Constants.EQUAL, nearbySearchRequest.getLongitude());
		// &radius=:radius
		append(url, Constants.AMP,
				NearbySearchRequestZomato.RequestParamNames.RADIUS,
				Constants.EQUAL, nearbySearchRequest.getRadius());
		// &category=:category
		append(url, Constants.AMP,
				NearbySearchRequestZomato.RequestParamNames.CATEGORY,
				Constants.EQUAL, nearbySearchRequest.getCategory());
		// &sort=:sort
		append(url, Constants.AMP,
				NearbySearchRequestZomato.RequestParamNames.SORT,
				Constants.EQUAL, nearbySearchRequest.getSort());
		// &order=:order
		append(url, Constants.AMP,
				NearbySearchRequestZomato.RequestParamNames.ORDER,
				Constants.EQUAL, nearbySearchRequest.getOrder());
		String message = LOG_PREFIX + " :: " + "Invoking request to URL = {}";
		logger.info(message, url.toString());
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add(NearbySearchRequestZomato.API_KEY_HEADER_NAME,
				apiConfig.getApiKey());
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<ZomatoPlaces> responseEntity = restTemplate.exchange(
				url.toString(), HttpMethod.GET, entity,
				new ParameterizedTypeReference<ZomatoPlaces>() {
				});

		HttpStatus returnStatus = responseEntity.getStatusCode();
		boolean isSuccess = returnStatus.is2xxSuccessful();
		if (isSuccess) {
			message = LOG_PREFIX + " :: " + "Search returned success response";
			logger.info(message);

		} else {
			if (returnStatus.is4xxClientError()) {
				throw new ClientException(RestErrorCodes.ERR_010,
						Constants.ERROR_ZAPI_CLIENT_REQUEST);
			} else if (returnStatus.is5xxServerError()) {
				throw new ServiceException("ZAPI", RestErrorCodes.ERR_010,
						Constants.ERROR_ZAPI_WEBSERVICE_ERROR);
			}
		}

		return responseEntity.getBody();
	}

	private static StringBuilder append(StringBuilder builder,
			Object... objects) {
		for (Object object : objects) {
			builder.append(object);
		}
		return builder;
	}

	private static Places executeGoogleSearch(RestTemplate restTemplate,
			NearbySearchRequestGoogle nearbySearchRequest,
			GAPIConfig apiConfig) {
		String LOG_PREFIX = "executeZomatoSearch";
		StringBuilder url = new StringBuilder(apiConfig.getNearBySearchURL()
				+apiConfig.getDataExchangeFormat()
				+ Constants.QUESTIONMARK);
		
		// &location=:location
		append(url, 
				NearbySearchRequestGoogle.RequestParamNames.LOCATION,
				Constants.EQUAL, nearbySearchRequest.getLocation());
		// &radius=:radius
		append(url, Constants.AMP,
				NearbySearchRequestGoogle.RequestParamNames.RADIUS,
				Constants.EQUAL, nearbySearchRequest.getRadius());
		// &type=:type
		append(url, Constants.AMP,
				NearbySearchRequestGoogle.RequestParamNames.TYPES,
				Constants.EQUAL, nearbySearchRequest.getTypes());
		// &sort=:sort
		append(url, Constants.AMP,
				NearbySearchRequestGoogle.RequestParamNames.RANKBY,
				Constants.EQUAL, nearbySearchRequest.getRankBy());
		
		// &opennow=true
		//append(url, Constants.AMP,
		//		NearbySearchRequestGoogle.RequestParamNames.OPEN_NOW,
		//		Constants.EQUAL, "true");
		// &key=:key
		append(url, Constants.AMP,
				NearbySearchRequestGoogle.RequestParamNames.KEY,
				Constants.EQUAL, apiConfig.getGapiKey());
		//&pagetoken=:pagetoken
		if(nearbySearchRequest.getPageToken()!=null){
			append(url, Constants.AMP
					,NearbySearchRequestGoogle.RequestParamNames.PAGE_TOKEN
					,Constants.EQUAL,
					nearbySearchRequest.getPageToken());
		}
		String message = LOG_PREFIX + " :: " + "Invoking request to URL = {}";
		logger.info(message, url.toString());
		/*HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add(NearbySearchRequestZomato.API_KEY_HEADER_NAME,
				apiConfig.getApiKey());
		HttpEntity<String> entity = new HttpEntity<String>(headers);*/

		ResponseEntity<GooglePlaces> responseEntity = restTemplate.exchange(
				url.toString(), HttpMethod.GET, null,
				new ParameterizedTypeReference<GooglePlaces>() {
				});

		HttpStatus returnStatus = responseEntity.getStatusCode();
		boolean isSuccess = returnStatus.is2xxSuccessful();
		if (isSuccess) {
			message = LOG_PREFIX + " :: " + "Search returned success response";
			logger.info(message);

		} else {
			if (returnStatus.is4xxClientError()) {
				throw new ClientException(RestErrorCodes.ERR_010,
						Constants.ERROR_GAPI_CLIENT_REQUEST);
			} else if (returnStatus.is5xxServerError()) {
				throw new ServiceException("ZAPI", RestErrorCodes.ERR_010,
						Constants.ERROR_GAPI_WEBSERVICE_ERROR);
			}
		}

		return responseEntity.getBody();
	}

	/*
	 * public static GooglePlaces executeSearch(RestTemplate restTemplate,
	 * NearbySearchRequestGoogle nearbySearchRequest, GAPIConfig gapiConfig) {
	 * 
	 * StringBuilder url = new StringBuilder(gapiConfig.getNearBySearchURL());
	 * url.append(gapiConfig.getDataExchangeFormat() + Constants.QUESTIONMARK);
	 * 
	 * url.append(NearbySearchRequestGoogle.NearbySearchRequestParamNames.LOCATION
	 * .getName() + Constants.EQUAL + nearbySearchRequest.getLocation()); if
	 * (nearbySearchRequest.getRadius() != null) { url.append(Constants.AMP +
	 * NearbySearchRequestGoogle.NearbySearchRequestParamNames.RADIUS .getName()
	 * + Constants.EQUAL + nearbySearchRequest.getRadius()); } if
	 * (nearbySearchRequest.getTypes() != null) { url.append(Constants.AMP +
	 * NearbySearchRequestGoogle.NearbySearchRequestParamNames.TYPES .getName()
	 * + Constants.EQUAL + nearbySearchRequest.getTypes()); } if
	 * (nearbySearchRequest.getName() != null) { url.append(Constants.AMP +
	 * NearbySearchRequestGoogle.NearbySearchRequestParamNames.NAME .getName() +
	 * Constants.EQUAL + nearbySearchRequest.getName().replaceAll(" ",
	 * Constants.PLUS)); } if (nearbySearchRequest.getRankBy() != null) {
	 * url.append(Constants.AMP +
	 * NearbySearchRequestGoogle.NearbySearchRequestParamNames.RANKBY .getName()
	 * + Constants.EQUAL + nearbySearchRequest.getRankBy()); }
	 * url.append(Constants.AMP +
	 * NearbySearchRequestGoogle.NearbySearchRequestParamNames.KEY .getName() +
	 * Constants.EQUAL + gapiConfig.getGapiKey()); if
	 * (nearbySearchRequest.getPageToken() != null) { url.append(Constants.AMP +
	 * NearbySearchRequestGoogle.NearbySearchRequestParamNames.PAGE_TOKEN
	 * .getName() + Constants.EQUAL + nearbySearchRequest.getPageToken()); }
	 * 
	 * logger.info("### Inside NearbySearchHelper.executeSearch | URL : {} " +
	 * url.toString());
	 * 
	 * logger.info("### Executing Search ###"); ResponseEntity<GooglePlaces>
	 * placesResponse = restTemplate.exchange( url.toString(), HttpMethod.GET,
	 * null, new ParameterizedTypeReference<GooglePlaces>() { });
	 * 
	 * HttpStatus returnStatus = placesResponse.getStatusCode(); boolean
	 * isSuccess = returnStatus.is2xxSuccessful(); if (isSuccess) {
	 * logger.info("### Search successful for url : {}" + url.toString());
	 * 
	 * } else { if (returnStatus.is4xxClientError()) { throw new
	 * ClientException(RestErrorCodes.ERR_010,
	 * Constants.ERROR_GAPI_CLIENT_REQUEST); } else if
	 * (returnStatus.is5xxServerError()) { throw new ServiceException("GAPI",
	 * RestErrorCodes.ERR_010, Constants.ERROR_GAPI_WEBSERVICE_ERROR); } }
	 * 
	 * return placesResponse.getBody(); }
	 */
}
