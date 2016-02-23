package com.bitlogic.sociallbox.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.bitlogic.sociallbox.service.business.ValidationService;

@RestController
public class BaseController {

	@Autowired
	private ValidationService validationService;
	
	public void setValidationService(ValidationService validationService) {
		this.validationService = validationService;
	}

	
}
