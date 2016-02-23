package com.bitlogic.sociallbox.service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.bitlogic.sociallbox.service.security.AjaxAuthenticationSuccessHandler;
import com.bitlogic.sociallbox.service.security.RestAuthenticationEntryPoint;
import com.bitlogic.sociallbox.service.security.RestSecurityFilter;

/**
 * 
 * The Spring Security configuration for the application - its a form login
 * config with authentication via session cookie (once logged in), with fallback
 * to HTTP Basic for non-browser clients.
 * 
 * 
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AppSecurityConfig.class);

	@Autowired
	private AuthenticationProvider authenticationProvider;

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		 auth.authenticationProvider(authenticationProvider);
	}
	

   @Override
   @Bean
   public AuthenticationManager authenticationManagerBean() throws Exception {
       return super.authenticationManagerBean();
   }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		LOGGER.info("### Inside AppSecurityConfig.configure ###");
		//CsrfTokenResponseHeaderBindingFilter csrfTokenFilter = new CsrfTokenResponseHeaderBindingFilter();
		//http.addFilterAfter(csrfTokenFilter, CsrfFilter.class);
		
		http.authorizeRequests()
				.antMatchers("/resources/public/**")
				.permitAll()
				.antMatchers("/resources/js/**")
				.permitAll()
				.antMatchers("/resources/css/**")
				.permitAll()
				.antMatchers("/resources/font-awesome/**")
				.permitAll()
				.antMatchers("/resources/fonts/**")
				.permitAll()
				.antMatchers("/resources/less/**")
				.permitAll()
				.antMatchers("/resources/img/**")
				.permitAll()
				.antMatchers(HttpMethod.POST, "/api/secured/users")
				.permitAll()
				.antMatchers("/api/public/**")
				.permitAll()
				.anyRequest()
				.authenticated()
				.and()
				.formLogin()
				.defaultSuccessUrl("/resources/public/home.html")
				.loginProcessingUrl("/authenticate")
				.usernameParameter("username")
				.passwordParameter("password")
				.successHandler(
						new AjaxAuthenticationSuccessHandler(
								new SavedRequestAwareAuthenticationSuccessHandler()))
				.loginPage("/resources/public/index.html")
				.and().httpBasic()
				.and().logout().logoutUrl("/logout")
				.logoutSuccessUrl("/resources/public/index.html").permitAll()
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().csrf().disable()
				.authenticationProvider(authenticationProvider)
				.exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint());
		
		RestSecurityFilter restSecurityFilter = new RestSecurityFilter(authenticationManager());
		http.addFilterBefore(restSecurityFilter, BasicAuthenticationFilter.class);
		
		// if ("true".equals(System.getProperty("httpsOnly"))) {
	/*	LOGGER.info("launching the application in HTTPS-only mode");
		http.requiresChannel().anyRequest().requiresSecure();*/
		// }

		LOGGER.info("### Done with configuring security ###");
	}
}
