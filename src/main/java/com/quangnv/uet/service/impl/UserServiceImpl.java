package com.quangnv.uet.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quangnv.uet.dto.UserDto;
import com.quangnv.uet.entites.UserEntity;
import com.quangnv.uet.repository.UserRepository;
import com.quangnv.uet.service.UserSevice;

@Service
public class UserServiceImpl implements UserSevice {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserEntity> optional = userRepository.findById(username);

		if (!optional.isPresent()) {
			throw new UsernameNotFoundException(username + " not found!");
		}

		UserEntity userEntity = optional.get();
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		authorities.add(new SimpleGrantedAuthority(userEntity.getUserRole()));

		return new User(userEntity.getUsername(), userEntity.getPassword(), authorities);
	}

	@Override
	public UserDto saveUser(UserDto userDto) {
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
		userEntity.setUserRole("ROLE_ADMIN");
		userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
		userEntity = userRepository.save(userEntity);
		return modelMapper.map(userEntity, UserDto.class);
	}

}
