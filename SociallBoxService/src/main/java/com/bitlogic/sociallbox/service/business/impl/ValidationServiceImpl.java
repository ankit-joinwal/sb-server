package com.bitlogic.sociallbox.service.business.impl;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitlogic.sociallbox.service.business.ValidationService;
import com.bitlogic.sociallbox.service.dao.UserDAO;

@Service
@Transactional
public class ValidationServiceImpl implements ValidationService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ValidationServiceImpl.class);

	
	@Autowired
	private UserDAO userDAO;
	
	
	
	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}


}
