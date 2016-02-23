package com.bitlogic.sociallbox.service.business;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitlogic.sociallbox.data.model.EventTag;
import com.bitlogic.sociallbox.service.dao.EventTagDAO;

@Service
@Transactional
public class EventTagServiceImpl implements EventTagService {

	private static final Logger logger = LoggerFactory.getLogger(EventTagServiceImpl.class);
	
	
	@Autowired
	private EventTagDAO eventTagDAO;
	
	
	@Override
	public EventTag create(EventTag eventTag) {
		logger.info("### Inside {} to create event type ",EventTagServiceImpl.class);
		return this.eventTagDAO.create(eventTag);
	}

	
	@Override
	public List<EventTag> getAll() {
		logger.info("### Inside {} to get all event tags ",EventTagServiceImpl.class);
		return this.eventTagDAO.getAll();
	}
}
