package com.bitlogic.sociallbox.notification.service.config;

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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.GAPIConfig;
import com.bitlogic.sociallbox.notification.service.util.LoggingService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebMvc
@ComponentScan("com.bitlogic.sociallbox.notification.service")
@PropertySource(value = { "file:${catalina.home}/conf/application.properties" })
public class SpringConfiguration  extends LoggingService{
	private static final Logger LOGGER = LoggerFactory.getLogger(SpringConfiguration.class);
	
	
	@Override
	public Logger getLogger() {
		return LOGGER;
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
		GAPIConfig gConfig = new GAPIConfig();
		String gapiKey = environment.getRequiredProperty(Constants.GAPI_KEY);
		String gcmServerURL = environment.getRequiredProperty(Constants.GAPI_GCM_SERVER_URL);
		LOGGER.info("###           Google Config For GCM      ###");
		logInfo(LOG_PREFIX, "GAPI_GCM_SERVER_URL : {} "+gcmServerURL);
		LOGGER.info("###########################################");
		gConfig.setGapiKey(gapiKey);
		gConfig.setGcmServerURL(gcmServerURL);
		logInfo(LOG_PREFIX, "Successfully loaded google api config");
		return gConfig;
	}
	
	@Bean(name="objectMapper")
	public ObjectMapper getObjectMapper(){
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper;
	}
	
}
