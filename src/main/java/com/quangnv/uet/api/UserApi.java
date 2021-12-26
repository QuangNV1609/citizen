package com.quangnv.uet.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quangnv.uet.dto.UserDto;
import com.quangnv.uet.handleException.HandleException;
import com.quangnv.uet.jwt.JwtTokenProvider;
import com.quangnv.uet.service.UserSevice;

@RestController
@RequestMapping(value = "/user")
@CrossOrigin(value = "http://localhost:3000/")
public class UserApi extends HandleException {
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserSevice userSevice;

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<UserDto> getUserLogined() {

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDto userDto = userSevice.getUserUserName(user.getUsername());

		return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
	}

	@GetMapping(value = "/account")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<UserDto>> getAccountCreateByUserLogined() {
		String userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
		List<UserDto> userDtos = userSevice.getUserByCreateBy(userId);
		return new ResponseEntity<List<UserDto>>(userDtos, HttpStatus.OK);
	}

	@PostMapping(value = "/register")
	@PreAuthorize("isAuthenticated() && !hasRole('B2')")
	public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
		String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

		userDto = userSevice.saveUser(userDto, username);
		userDto.setPassword(null);
		return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
	}
	
	@PostMapping(value = "/register/admin")
	public ResponseEntity<UserDto> registerAdmin(@RequestBody UserDto userDto) {
		userDto = userSevice.saveAdmin(userDto);
		return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
	}

	@PostMapping(value = "/login")
	public ResponseEntity<UserDto> login(@RequestBody UserDto userDto) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		User user = (User) authentication.getPrincipal();
		userDto = userSevice.getUserUserName(user.getUsername());
		String jwt = jwtTokenProvider.generateToken(userDto);
		userDto.setJwt(jwt);

		return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
	}

	@PutMapping(value = "/disable")
	@PreAuthorize("@authorizationServiceImpl.checkAuthorization(#userDto.username, authentication.principal)")
	public ResponseEntity<String> disableAccount(@RequestBody UserDto userDto) {
		String message = userSevice.disable(userDto.getUsername(), userDto.isEnable());
		return new ResponseEntity<String>(message, HttpStatus.OK);
	}

	@PutMapping(value = "/disable/list/{enable}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> disableListAccount(@PathVariable(value = "enable") String enable,
			@RequestBody List<String> usernames) {
		String createBy = SecurityContextHolder.getContext().getAuthentication().getName();
		String message = userSevice.disableList(createBy, usernames, enable);
		return new ResponseEntity<String>(message, HttpStatus.OK);
	}

	@PutMapping(value = "/edit/password")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> changePassword(@RequestBody UserDto userDto) {
		userDto.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		String message = userSevice.changePasswordUser(userDto);
		return new ResponseEntity<String>(message, HttpStatus.OK);
	}

	@PutMapping(value = "/edit")
	@PreAuthorize("@authorizationServiceImpl.checkAuthorization(#userDto.username, authentication.principal)")
	public ResponseEntity<String> editPassword(@RequestBody UserDto userDto) {
		String message = userSevice.changePassword(userDto);
		return new ResponseEntity<String>(message, HttpStatus.OK);
	}

}
