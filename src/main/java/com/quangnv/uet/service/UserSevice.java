package com.quangnv.uet.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.quangnv.uet.dto.UserDto;

public interface UserSevice extends UserDetailsService {

	public UserDto saveUser(UserDto userDto, String username);

	public UserDto saveAdmin(UserDto userDto);

	public String disable(String username, Boolean disable);

	public String disableList(String createBy, List<String> usernames, String enable);

	public UserDto getUserUserName(String username);

	public String changePassword(UserDto userDto);

	public List<UserDto> getUserByCreateBy(String userId);

}
