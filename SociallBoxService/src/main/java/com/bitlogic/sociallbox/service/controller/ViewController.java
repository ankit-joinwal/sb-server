package com.bitlogic.sociallbox.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class ViewController extends BaseController{

	private static final Logger LOGGER = LoggerFactory.getLogger(ViewController.class);
	
	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	
	 @RequestMapping(method = RequestMethod.GET)
     public ModelAndView getIndexPage() {
		 
         return new ModelAndView("index");
     }
    
	 
	 @RequestMapping(value="/eo/login",method = RequestMethod.GET)
     public ModelAndView getLoginPage() {
		
         return new ModelAndView("login");
     }
	 
	 @RequestMapping(value="/eo/dashboard",method = RequestMethod.GET)
     public ModelAndView getDashboardPage() {
		
         return new ModelAndView("dashboard");
     }
}
