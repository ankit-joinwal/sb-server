package com.bitlogic.sociallbox.service.business.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitlogic.sociallbox.data.model.EventType;
import com.bitlogic.sociallbox.service.business.EventTypeService;
import com.bitlogic.sociallbox.service.dao.EventTypeDAO;

@Service
@Transactional
public class EventTypeServiceImpl implements EventTypeService {

	private static final Logger logger = LoggerFactory.getLogger(EventTypeServiceImpl.class);
	
	@Autowired
	private EventTypeDAO eventTypeDao;
	
	@Override
	public EventType createEventType(EventType eventType) {

		logger.info("### Inside {} to create event type ",EventTypeServiceImpl.class);
		
		return this.eventTypeDao.createEventType(eventType);
	}

	@Override
	public List<EventType> getAllEventTypes() {
		logger.info("### Inside {} to create event type ",EventTypeServiceImpl.class);
		
		return this.eventTypeDao.getAllEventTypes();
	}
}
