package com.bitlogic.sociallbox.service.security;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class RestToken extends UsernamePasswordAuthenticationToken {


	private static final long serialVersionUID = 1L;
	private Long timestamp;

	// this constructor creates a non-authenticated token (see super-class)
	public RestToken(String principal, RestCredentials credentials,
			Long timestamp) {
		super(principal, credentials);
		this.timestamp = timestamp;
	}

	// this constructor creates an authenticated token (see super-class)
	public RestToken(String principal, RestCredentials credentials,
			Long timestamp, Collection authorities) {
		super(principal, credentials, authorities);
		this.timestamp = timestamp;
	}

	@Override
	public String getPrincipal() {
		return (String) super.getPrincipal();
	}

	@Override
	public RestCredentials getCredentials() {
		return (RestCredentials) super.getCredentials();
	}

	public Long getTimestamp() {
		return timestamp;
	}
	
	@Override
	public String toString() {
		return "RestToken = [ timestamp ="+this.getTimestamp()+" , principal= "+this.getPrincipal()+ " ]";
	}
}

