package com.bitlogic;

import java.io.File;

public interface Constants {

	String BLANK = "";
	String SUCCESS_STATUS = "Success";
	String LOCATION_HEADER = "Location";
	String JDBC_DRIVER_PROPERTY = "jdbc.driverClassName";
	String JDBC_URL_PROPERTY = "jdbc.url";
	String JDBC_USERNAME_PROPERTY = "jdbc.username";
	String JDBC_PASSWORD_PROPERTY = "jdbc.password";
	String HIBERNATE_DIALECT_PROPERTY = "hibernate.dialect";
	String HIBERNATE_SHOW_SQL_PROPERTY = "hibernate.show_sql";
	String HIBERNATE_FORMAT_SQL_PROPERTY = "hibernate.format_sql";
	String HIBERNATE_HBM_DDL_PROPERTY = "hibernate.hbm2ddl.auto";
	String DEFAULT_USER_DAILY_QUOTA_PROPERTY = "ggenie.default.user.quota";
	Integer DEFAULT_USER_DAILY_QUOTA = 100;
	String G_NEARBY_PLACES_URL = "gplaces.nearby.url";
	String G_TSEARCH_URL = "gplaces.tsearch.url";
	String G_PLACE_DETAIL_URL = "gplaces.place.details.url";
	String DEFAULT_GAPI_DATA_EXCHANGE_FMT = "gapi.data.format";
	String GAPI_KEY = "gapi.key";
	String USER_SERVICE_URL = "user.svc.url";
	String USER_VALIDATE_URL = "user.validate.url";
	String AUTHORIZATION_HEADER = "Authorization";
	String USER_TYPE_HEADER = "type";
	String ACCEPT_HEADER = "Accept";
	String QUESTIONMARK = "?";
	String AMP = "&";
	String EQUAL = "=";
	String PLUS = "+";
	String UNAME_DELIM = "~";
	String DEVICE_PREFIX = "SD";
	String WEB_USER_PREFIX = "W";
	//Default Radius for Nearby Search
	String DEFAULT_RADIUS = "1000";
	Integer RECORDS_PER_PAGE = 5;
	String MEETUP_DATE_FORMAT = "dd/MM/yyyy hh:mm aa";
	String EVENT_RESPONSE_DATE_FORMAT = "d MMM yyyy";
	String MEETUP_RESPONSE_DATE_FORMAT = "d MMM yyyy";
	String TRUE = "true";
	String GEO_SERVICE_NAME = "GeoService";
	String LATTITUDE_KEY = "LATTITUDE";
	String LONGITUDE_KEY = "LONGITUDE";
	String KILOMETRES = " Kms";
	String IS_ENABLED_TRUE = "true";
	String ROLE_TYPE_ADMIN = "ADMIN";
	String ROLE_TYPE_APP_USER = "APP_USER";
	String ROLE_ORGANIZER = "EVENT_ORGANIZER";
	String ONE_WHITESPACE = " ";
	String COLON = ":";
	String DOUBLE_COLON = "::";
	String SECURED_REQUEST_START_LOG_MESSAGE = "Request Recieved | {} ";
	//Pass Request Name
	String PUBLIC_REQUEST_START_LOG = "Request Recieved | {} ";
	
	String EVENT_IMAGE_STORE_PATH = System.getProperty("catalina.home")+File.separator+"images"+File.separator+"events";
	String MEETUP_IMAGE_STORE_PATH = System.getProperty("catalina.home")+File.separator+"images"+File.separator+"meetups";
	
	//Error Message Keys
	String ERROR_GAPI_CLIENT_REQUEST = "error.gapi.client.request";
	String ERROR_GAPI_WEBSERVICE_ERROR = "error.gapi.webservice.error";
	String ERROR_LOGIN_SOCIAL_DETAILS_MISSING = "error.login.social.details.missing";
	String ERROR_LOGIN_INVALID_DEVICES_IN_REQ = "error.login.invalid.devices.in.req";
	String ERROR_LOGIN_DEVICE_MISSING = "error.login.device.missing";
	String ERROR_LOGIN_SD_ID_MISSING = "error.login.sd.id.missing";
	String ERROR_LOGIN_INVALID_CREDENTIALS = "error.login.invalid.credentials";
	String ERROR_LOGIN_USER_UNAUTHORIZED = "error.login.user.unauthorized";
	String ERROR_INVALID_EVENT_IN_REQUEST = "error.invalid.event.in.request";
	String ERROR_INVALID_MEETUP_IN_REQUEST = "error.invalid.meetup.in.request";
	String ERROR_INVALID_CATEGORY = "error.invalid.category";
	String ERROR_USER_INVALID = "error.user.invalid";
	String ERROR_INVALID_DEVICE = "error.invalid.device";
	String ERROR_USER_TYPE_INVALID = "error.user.type.invalid";
	String ERROR_MEETUP_NOT_FOUND = "error.meetup.not.found";
	String ERROR_SOCIAL_DETAILS_NOT_FOUND = "error.social.details.not.found";
	String ERROR_EVENT_TYPE_INVALID = "error.event.type.invalid";
	String ERROR_IMAGE_NOT_FOUND = "error.image.not.found";
	String ERROR_FEATURE_AVAILABLE_TO_MOBILE_ONLY = "error.feature.available.to.mobile.only";
	String ERROR_LOCATION_INVALID_FORMAT = "error.location.invalid.format";
	String ERROR_ADDRESS_MANDATORY = "error.address.mandatory";
	String ERROR_ORGANIZER_EXISTS = "error.organizer.exists";
	String ERROR_DATE_INVALID_FORMAT = "error.date.invalid.format";
	String ERROR_ORGANIZER_NOT_FOUND = "error.organizer.not.found";
	String ERROR_FEATURE_AVAILABLE_TO_WEB_ONLY = "error.feature.available.to.web.only";
	String ERROR_USER_ALREADY_EXISTS = "error.user.already.exists";
}
