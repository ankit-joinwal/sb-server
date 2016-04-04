package com.bitlogic.sociallbox.notification.service.business.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import com.bitlogic.sociallbox.data.model.EventType;
import com.bitlogic.sociallbox.notification.service.business.TestService;
import com.bitlogic.sociallbox.notification.service.dao.TestDAO;
import com.bitlogic.sociallbox.notification.service.util.LoggingService;

@Transactional
@Service("testService")
public class TestServiceImpl extends LoggingService implements TestService{
	private static final Logger LOGGER = LoggerFactory.getLogger(TestServiceImpl.class);
	
	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	
	@Autowired
	private TestDAO testDAO;
	
	@Override
	@Async
	public ListenableFuture<List<EventType>> getallTypes() {
		LOGGER.info("Inside TestServiceImpl.getallTypes");
		LOGGER.info("Current Time before sleep : {}",System.currentTimeMillis());
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info("Current Time after sleep : {}",System.currentTimeMillis());
		List<EventType> types =  this.testDAO.getAllTypes();
		LOGGER.info("Found {} entries"+types.size());
		return new AsyncResult<List<EventType>>(types);
		
	}
}
