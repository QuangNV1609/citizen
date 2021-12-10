package com.quangnv.uet.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.quangnv.uet.dto.UserDto;

public interface UserSevice extends UserDetailsService {

	public UserDto saveUser(UserDto userDto, String username);
	
	public UserDto saveAdmin(UserDto userDto);
	
		
	
}
