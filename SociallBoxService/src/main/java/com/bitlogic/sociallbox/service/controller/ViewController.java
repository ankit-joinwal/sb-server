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
	 
	 @RequestMapping(value="/nimda/login",method = RequestMethod.GET)
     public ModelAndView getAdminLoginPage() {
		
         return new ModelAndView("adminLogin");
     }
	 
	 @RequestMapping(value="/eo/home",method = RequestMethod.GET)
     public ModelAndView getHomePage() {
         return new ModelAndView("home");
     }
	 
	 @RequestMapping(value="/nimda/home",method = RequestMethod.GET)
     public ModelAndView getAdminHomePage() {
         return new ModelAndView("adminHome");
     }
	 
	 @RequestMapping(value="/eo/dashboard",method = RequestMethod.GET)
     public ModelAndView getDashboardPage() {
         return new ModelAndView("dashboard");
     }
	 
	 @RequestMapping(value="/nimda/organizers",method = RequestMethod.GET)
     public ModelAndView getAdminDashboardPage() {
         return new ModelAndView("organizers");
     }
	 
	 @RequestMapping(value="/eo/profile",method = RequestMethod.GET)
     public ModelAndView getProfilePage() {
		
         return new ModelAndView("profile");
     }
	 
	 @RequestMapping(value="/eo/company",method = RequestMethod.GET)
     public ModelAndView getCompanyPage() {
		
         return new ModelAndView("company");
     }
	 
	 @RequestMapping(value="/eo/company/new",method = RequestMethod.GET)
     public ModelAndView getNewCompanyPage() {
		
         return new ModelAndView("newCompany");
     }
	 
	 @RequestMapping(value="/eo/events/create",method = RequestMethod.GET)
     public ModelAndView getCreateEventPage() {
		
         return new ModelAndView("newEvent");
     }
	 
	 @RequestMapping(value="/eo/events/list",method = RequestMethod.GET)
     public ModelAndView getEventsListPage() {
		
         return new ModelAndView("eventsHome");
     }
	 
	 @RequestMapping(value="/eo/events/details",method = RequestMethod.GET)
     public ModelAndView getEventsDetailsPage() {
		
         return new ModelAndView("eventDetails");
     }
	 
}
