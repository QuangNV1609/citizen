package com.quangnv.uet.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.quangnv.uet.service.AuthorizationService;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

	@Override
	public boolean checkAuthorization(String username, UserDetails userDetails) {
		if (username == null) {
			return true;
		}
		if (userDetails.getAuthorities().toString().contains("ROLE_A1")
				|| userDetails.getAuthorities().toString().contains("ROLE_ADMIN")) {
			return true;
		}
		if (username.substring(0, userDetails.getUsername().length()).equals(userDetails.getUsername())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean checkAuthorization(String[] usernames, UserDetails userDetails) {
		String authoiation = userDetails.getAuthorities().toString();
		boolean test = userDetails.getAuthorities().toString().contains("ROEL_A1");
		if (userDetails.getAuthorities().toString().contains("ROLE_A1")
				|| userDetails.getAuthorities().toString().contains("ROLE_ADMIN") || usernames == null) {
			return true;
		}
		if (userDetails.getAuthorities().toString().contains("ROLE_B2")) {
			return false;
		}
		for (String username : usernames) {
			if (username.length() < userDetails.getUsername().length()) {
				return false;
			}
			if (!username.substring(0, userDetails.getUsername().length()).equals(userDetails.getUsername())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isEnabel(UserDetails userDetails) {
		// TODO Auto-generated method stub
		return false;
	}
}
