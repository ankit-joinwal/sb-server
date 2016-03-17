package com.bitlogic.sociallbox.test.util;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bitlogic.sociallbox.data.model.SourceSystemForPlaces;
import com.bitlogic.sociallbox.data.model.requests.NearbySearchRequestZomato;
import com.bitlogic.sociallbox.data.model.requests.SortOrderOption;
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
    	System.out.println(SourceSystemForPlaces.valueOf("GOOGLE1"));
    }
}
