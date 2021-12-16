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

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
	public UserDto saveUser(UserDto userDto, String username) {
		UserEntity userEntityAdmin = userRepository.findById(username).get();
		UserEntity newUserEntity = modelMapper.map(userDto, UserEntity.class);

		if (userEntityAdmin.getUserRole().equals("ROLE_ADMIN")) {
			newUserEntity.setUserRole("ROLE_A1");
		} else if (userEntityAdmin.getUserRole().equals("ROLE_A1")) {
			newUserEntity.setUserRole("ROLE_A2");
		} else if (userEntityAdmin.getUserRole().equals("ROLE_A2")) {
			newUserEntity.setUserRole("ROLE_A3");
		} else if (userEntityAdmin.getUserRole().equals("ROLE_A3")) {
			newUserEntity.setUserRole("ROLE_B1");
		} else if (userEntityAdmin.getUserRole().equals("ROLE_B1")) {
			newUserEntity.setUserRole("ROLE_B2");
		}

		newUserEntity.setEnable(true);

		log.info(newUserEntity.toString());

		newUserEntity.setPassword(passwordEncoder.encode(newUserEntity.getPassword()));
		newUserEntity = userRepository.save(newUserEntity);
		
		return modelMapper.map(newUserEntity, UserDto.class);
	}

	@Override
	public UserDto saveAdmin(UserDto userDto) {
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
		userEntity.setUserRole("ADMIN");
		userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
		userRepository.save(userEntity);
		return modelMapper.map(userEntity, UserDto.class);
	}

}
