package com.quangnv.uet.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import com.quangnv.uet.entites.UserEntity;
import com.quangnv.uet.repository.UserRepository;

public class AuditorAwareImpl implements AuditorAware<UserEntity> {

	@Autowired
	private UserRepository userRepository;

	@Override
	public Optional<UserEntity> getCurrentAuditor() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepository.findById(username);
	}
}
