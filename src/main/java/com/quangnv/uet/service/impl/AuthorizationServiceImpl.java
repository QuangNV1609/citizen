package com.quangnv.uet.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.quangnv.uet.service.AuthorizationService;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

	@Override
	public boolean checkAuthorization(String username, UserDetails userDetails) {
		if (userDetails.getAuthorities().toString().contains("ROEL_A1")
				|| userDetails.getAuthorities().toString().contains("ROEL_ADMIN")) {
			return true;
		}
		if (username.substring(0, userDetails.getUsername().length()).equals(userDetails.getUsername())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isEnabel(UserDetails userDetails) {
		// TODO Auto-generated method stub
		return false;
	}
}
