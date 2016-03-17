package com.bitlogic.sociallbox.service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.GAPIConfig;
import com.bitlogic.sociallbox.data.model.ZomatoAPIConfig;
import com.bitlogic.sociallbox.service.utils.LoggingService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebMvc
@ComponentScan("com.bitlogic.sociallbox.service")
@PropertySource(value = { "file:${catalina.home}/conf/application.properties" })
public class SpringConfiguration extends LoggingService {
	private static final Logger logger = LoggerFactory.getLogger(SpringConfiguration.class);

	@Override
	public Logger getLogger() {
		return logger;
	}
	
	@Autowired
	private Environment environment;
	
	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
	  ReloadableResourceBundleMessageSource messageBundle = new ReloadableResourceBundleMessageSource();
	  messageBundle.setBasename("classpath:messages/messages");
	  messageBundle.setDefaultEncoding("UTF-8");
	  return messageBundle;
	}
	
	@Bean(name="restTemplate")
	public RestTemplate getRestTemplate(){
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
        return restTemplate;
	}
	
	@Bean(name="gapiConfig")
	public GAPIConfig getGConfig(){
		String LOG_PREFIX = "getGConfig";
		logInfo(LOG_PREFIX,"Inside SpringConfiguration to load gConfig");
		GAPIConfig gConfig = new GAPIConfig();
		String nearbySearchURL =  environment.getRequiredProperty(Constants.G_NEARBY_PLACES_URL);
		String gapiDataFormat = environment.getRequiredProperty(Constants.DEFAULT_GAPI_DATA_EXCHANGE_FMT);
		String gapiKey = environment.getRequiredProperty(Constants.GAPI_KEY);
		String textSearchURL = environment.getRequiredProperty(Constants.G_TSEARCH_URL);
		String placeDetailsURL= environment.getRequiredProperty(Constants.G_PLACE_DETAIL_URL);
		String placePhotoGetAPI = environment.getRequiredProperty(Constants.PLACES_PHOTOS_GET_API_BASE_PATH_KEY);
		String placePhotoGoogleAPI = environment.getRequiredProperty(Constants.G_PLACE_PHOTOS_URL_KEY);
		
		logInfo(LOG_PREFIX, "G_NEARBY_PLACES_URL : {} "+nearbySearchURL);
		logInfo(LOG_PREFIX,"G_TSEARCH_URL : {} "+textSearchURL);
		logInfo(LOG_PREFIX,"G_PLACE_DETAIL_URL : {}"+placeDetailsURL);
		logInfo(LOG_PREFIX,"DEFAULT_GAPI_DATA_EXCHANGE_FMT : {}"+gapiDataFormat);
		logInfo(LOG_PREFIX, "PLACES_PHOTOS_GET_API_BASE_PATH : {}", placePhotoGetAPI);
		logInfo(LOG_PREFIX, "G_PLACE_PHOTOS_URL : {}", placePhotoGoogleAPI);
		
		gConfig.setNearBySearchURL(nearbySearchURL);
		gConfig.setDataExchangeFormat(gapiDataFormat);
		gConfig.setGapiKey(gapiKey);
		gConfig.setTextSearchURL(textSearchURL);
		gConfig.setPlaceDetailsURL(placeDetailsURL);
		gConfig.setPlacePhotoGetAPI(placePhotoGetAPI);
		gConfig.setPlacePhotoGoogleAPI(placePhotoGoogleAPI);
		logInfo(LOG_PREFIX, "Successfully loaded google api config");
		return gConfig;
	}
	
	@Bean(name="zomatoApiConfig")
	public ZomatoAPIConfig getZomatoAPIConfig(){
		String LOG_PREFIX = "getZomatoAPIConfig";
		logInfo(LOG_PREFIX,"Inside SpringConfiguration to load ZomatoAPIConfig");
		
		ZomatoAPIConfig apiConfig = new ZomatoAPIConfig();
		String nearbySearchURL =  environment.getRequiredProperty(Constants.ZOMATO_NEARBY_PLACES_URL);
		String zapiDataFormat = environment.getRequiredProperty(Constants.ZOMATO_DEFAULT_GAPI_DATA_EXCHANGE_FMT);
		String zapiKey = environment.getRequiredProperty(Constants.ZOMATO_API_KEY);
		String placeDetailsURL= environment.getRequiredProperty(Constants.ZOMATO_PLACE_DETAIL_URL);
		
		logInfo(LOG_PREFIX, "Z_NEARBY_PLACES_URL : {} "+nearbySearchURL);
		logInfo(LOG_PREFIX,"Z_PLACE_DETAIL_URL : {}"+placeDetailsURL);
		logInfo(LOG_PREFIX,"DEFAULT_ZAPI_DATA_EXCHANGE_FMT : {}"+zapiDataFormat);
		
		apiConfig.setApiKey(zapiKey);
		apiConfig.setDataExchangeFormat(zapiDataFormat);
		apiConfig.setNearBySearchURL(nearbySearchURL);
		apiConfig.setPlaceDetailsURL(placeDetailsURL);
		
		return apiConfig;
	}
	
	@Bean(name="objectMapper")
	public ObjectMapper getObjectMapper(){
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper;
	}
	
	@Bean(name="multipartResolver")
	public CommonsMultipartResolver getMultiPartResolver(){
		return new CommonsMultipartResolver();
	}
  
	
}
