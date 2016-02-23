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
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.GAPIConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebMvc
@ComponentScan("com.bitlogic.sociallbox.service")
@PropertySource(value = { "file:${catalina.home}/conf/application.properties" })
public class SpringConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(SpringConfiguration.class);

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
        
        return restTemplate;
	}
	
	@Bean(name="gapiConfig")
	public GAPIConfig getGConfig(){
		logger.info("### Inside SpringConfiguration of GeoService to load gConfig ###");
		GAPIConfig gConfig = new GAPIConfig();
		String nearbySearchURL =  environment.getRequiredProperty(Constants.G_NEARBY_PLACES_URL);
		String gapiDataFormat = environment.getRequiredProperty(Constants.DEFAULT_GAPI_DATA_EXCHANGE_FMT);
		String gapiKey = environment.getRequiredProperty(Constants.GAPI_KEY);
		String textSearchURL = environment.getRequiredProperty(Constants.G_TSEARCH_URL);
		String placeDetailsURL= environment.getRequiredProperty(Constants.G_PLACE_DETAIL_URL);
		
		logger.info("### G_NEARBY_PLACES_URL : {} ###"+nearbySearchURL);
		logger.info("### G_TSEARCH_URL : {} ###"+textSearchURL);
		logger.info("### G_PLACE_DETAIL_URL : {} ###"+placeDetailsURL);
		logger.info("### DEFAULT_GAPI_DATA_EXCHANGE_FMT : {} ###"+gapiDataFormat);
		
		gConfig.setNearBySearchURL(nearbySearchURL);
		gConfig.setDataExchangeFormat(gapiDataFormat);
		gConfig.setGapiKey(gapiKey);
		gConfig.setTextSearchURL(textSearchURL);
		gConfig.setPlaceDetailsURL(placeDetailsURL);
		return gConfig;
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
