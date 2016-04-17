package com.bitlogic.sociallbox.service.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.velocity.VelocityView;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

/**
 * 
 * Spring MVC config for the servlet context in the application.
 * 
 * The beans of this context are only visible inside the servlet context.
 * 
 */
@Configuration
@EnableWebMvc
public class ServletContextConfig extends WebMvcConfigurerAdapter {

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		VelocityViewResolver viewResolver = new VelocityViewResolver();

		viewResolver.setViewClass(VelocityView.class);
		viewResolver.setCache(true);
		viewResolver.setPrefix("");
		viewResolver.setSuffix(".html");
		viewResolver.setExposeSpringMacroHelpers(true);

		registry.viewResolver(viewResolver);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations(
				"/resources/");
	}
}
