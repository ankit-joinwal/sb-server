package com.bitlogic.sociallbox.test.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.Address;
import com.bitlogic.sociallbox.data.model.EventOrganizer;
import com.bitlogic.sociallbox.data.model.requests.CreateEventOrganizerRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TestUtil {



    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        System.out.println("Object :"+mapper.writeValueAsString(object));
        return mapper.writeValueAsBytes(object);
    }

    
    public static void main(String[] args) throws Exception{
    	CreateEventOrganizerRequest eventOrganizer = new CreateEventOrganizerRequest();
    	eventOrganizer.setName("Remix Entertainment");
    	eventOrganizer.setEmailId("harsh.singh@remixentertainments.com");
    	eventOrganizer.setPhone1("+91 7838250407");
    	Address address = new Address();
    	address.setStreet("Mandakini Enclave");
    	address.setCity("New Delhi");
    	address.setCountry("India");
    	address.setZipcode("110019");
    	eventOrganizer.setAddress(address);
    	ObjectMapper objectMapper = new ObjectMapper();
    	System.out.println(objectMapper.writeValueAsString(eventOrganizer));
	}
}
