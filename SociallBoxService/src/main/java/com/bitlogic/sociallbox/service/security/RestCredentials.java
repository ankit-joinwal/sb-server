package com.bitlogic.sociallbox.service.security;

public final class RestCredentials {

	private String signature;

	public RestCredentials(String signature) {
		this.signature = signature;
	}
	public String getSignature() {
		return signature;
	}

}
