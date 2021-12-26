package com.quangnv.uet.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthorizationService {
	public boolean checkAuthorization(String username, UserDetails userDetails);
	
	public boolean checkAuthorization(String[] usernames, UserDetails userDetails);

	public boolean isEnabel(UserDetails userDetails);
}
