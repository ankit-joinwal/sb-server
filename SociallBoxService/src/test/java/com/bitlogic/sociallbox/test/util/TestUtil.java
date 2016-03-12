package com.bitlogic.sociallbox.test.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.Address;
import com.bitlogic.sociallbox.data.model.EventOrganizer;
import com.bitlogic.sociallbox.data.model.requests.CreateEventOrganizerRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TestUtil {

static final Logger LOGGER = LoggerFactory.getLogger(TestUtil.class);

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        System.out.println("Object :"+mapper.writeValueAsString(object));
        return mapper.writeValueAsBytes(object);
    }

    
    public static void main(String[] args) throws Exception{
    	for ( int i = 1; i <= 24; i++ ) {
    		LOGGER.info( "write log" );

    		try {
    			Thread.sleep( 10000L );
    		} catch ( final InterruptedException e ) {
    			LOGGER.error( "an error occurred", e );
    		}
    	}
	}
}
