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
	
	private static final String LANDING_PAGE = "LandingPage View";
	private static final String DASHBOARD_PAGE = "DashboardPage View";
	private static final String REQUEST_START_MESSAGE = "Request Recieved | {}";
	private static final String REQUEST_END_MESSAGE = "Request Served | {}";
	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	
	 @RequestMapping(method = RequestMethod.GET)
     public ModelAndView getIndexPage() {
		 logRequestStart(LANDING_PAGE, REQUEST_START_MESSAGE, LANDING_PAGE);
		 logRequestEnd(LANDING_PAGE, REQUEST_END_MESSAGE, LANDING_PAGE);
         return new ModelAndView("index");
     }
    
	 
	 @RequestMapping(value="/eo/login",method = RequestMethod.GET)
     public ModelAndView getLoginPage() {
		 logRequestStart(LANDING_PAGE, REQUEST_START_MESSAGE, LANDING_PAGE);
		 logRequestEnd(LANDING_PAGE, REQUEST_END_MESSAGE, LANDING_PAGE);
         return new ModelAndView("login");
     }
	 
	 @RequestMapping(value="/eo/dashboard",method = RequestMethod.GET)
     public ModelAndView getDashboardPage() {
		 logRequestStart(DASHBOARD_PAGE, REQUEST_START_MESSAGE, DASHBOARD_PAGE);
		 logRequestEnd(DASHBOARD_PAGE, REQUEST_END_MESSAGE, DASHBOARD_PAGE);
         return new ModelAndView("dashboard");
     }
}
