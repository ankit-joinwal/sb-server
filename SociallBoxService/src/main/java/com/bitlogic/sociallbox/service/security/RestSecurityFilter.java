package com.bitlogic.sociallbox.service.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.GenericFilterBean;

import com.bitlogic.sociallbox.service.exception.UnauthorizedException;

public class RestSecurityFilter extends GenericFilterBean{

    private static final Logger LOGGER = LoggerFactory.getLogger(RestSecurityFilter.class);
    
    private AuthenticationManager authenticationManager;
    private AuthenticationEntryPoint authenticationEntryPoint;
    

    public RestSecurityFilter(AuthenticationManager authenticationManager) {
        this(authenticationManager, new RestAuthenticationEntryPoint());
        ((RestAuthenticationEntryPoint)this.authenticationEntryPoint).setRealmName("Secure realm");
    }

    public RestSecurityFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationManager = authenticationManager;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
    	LOGGER.info("### Inside RestSecurityFilter ###");
    	
    	HttpServletResponse response = (HttpServletResponse) resp;
        HttpServletRequest request = (HttpServletRequest)req;
        // Get authorization header
        String authHeader = request.getHeader("Authorization");
        LOGGER.info("Authorization : {} ",authHeader);
        
        // If there's not credentials return...
        if (authHeader == null) {
            chain.doFilter(request, response);
            return;
        }

        String credentials = authHeader.replaceAll("Basic ", "");
        LOGGER.info("Credentials : {}",credentials);
        
        String base64Decoded = new String(Base64.decode(credentials.getBytes()));
        LOGGER.info("Decoded Credentials : {}",base64Decoded);
        
        
        // Authorization header is in the form <public_access_key>:<signature>
        String auth[] = base64Decoded.split(":");
        if(auth==null || auth.length!=2){
        	authenticationEntryPoint.commence(request, response, new AuthenticationException("Authorization Token not in proper format") {

				private static final long serialVersionUID = 1L;
			});
        }
        String username = auth[0];
        String signature = auth[1];
        LOGGER.info("Username : {}",username);
        LOGGER.info("Signature : {} ",signature);
        
        // get timestamp
        String dateHeader =  request.getHeader("X-Auth-Date");
        LOGGER.info("Date Header : {} ",dateHeader);
        
        Long timestamp = null;
        try{
        	timestamp = Long.parseLong(dateHeader);
        }catch(NumberFormatException exception){
        	 SecurityContextHolder.clearContext();
             authenticationEntryPoint.commence(request, response, new AuthenticationException("Date header not in proper format",exception) {

				private static final long serialVersionUID = 1L;
			});
        }

        // a rest credential is composed by and the signature
        RestCredentials restCredential = new RestCredentials(signature);

      
        // Create an authentication token
        Authentication authentication = new RestToken(username, restCredential, timestamp);

        try {
            // Request the authentication manager to authenticate the token (throws exception)
            Authentication successfulAuthentication = authenticationManager.authenticate(authentication);
            
            // Pass the successful token to the SecurityHolder where it can be
            // retrieved by this thread at any stage.
            SecurityContextHolder.getContext().setAuthentication(successfulAuthentication);
            // Continue with the Filters
            chain.doFilter(request, response);
        } catch (AuthenticationException authenticationException) {
            // If it fails clear this threads context and kick off the
            // authentication entry point process.
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, authenticationException);
        }catch(UnauthorizedException unauthorizedException){
        	 SecurityContextHolder.clearContext();
        	 PrintWriter writer = response.getWriter();
        	 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
 	        	writer.println("HTTP Status " + HttpServletResponse.SC_UNAUTHORIZED + " - " + unauthorizedException.getMessage());
        }
    }
}
