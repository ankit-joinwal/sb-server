package com.bitlogic.sociallbox.service.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * Authentication success handler for integration with SPA applications that need to login using Ajax instead of
 * a form post.
 *
 * Detects if its a ajax login request, and if so sends a customized response in the body, otherwise defaults
 * to the existing behaviour for none-ajax login attempts.
 *
 */
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(AjaxAuthenticationSuccessHandler.class);
	
    private AuthenticationSuccessHandler defaultHandler;

    public AjaxAuthenticationSuccessHandler(AuthenticationSuccessHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

    	LOGGER.info("### Inside AjaxAuthenticationSuccessHandler ###");
       // if ("true".equals(request.getHeader("X-Login-Ajax-call"))) {
    	if(request.getRequestURI().contains("api")){
        	LOGGER.info("### Request is of Ajax type. ###");
            response.getWriter().print("ok");
            response.getWriter().flush();
    	}else{
    		LOGGER.info("### Request is of Non-ajax type ###");
            defaultHandler.onAuthenticationSuccess(request, response, authentication);
    	}
        /*}
        else {
        	LOGGER.info("### Request is of Non-ajax type ###");
            defaultHandler.onAuthenticationSuccess(request, response, authentication);
        }*/

    }
}