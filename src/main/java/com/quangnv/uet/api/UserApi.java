package com.quangnv.uet.api;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quangnv.uet.dto.UserDto;
import com.quangnv.uet.jwt.JwtTokenProvider;
import com.quangnv.uet.service.UserSevice;

@RestController
@RequestMapping(value = "/user")
//@CrossOrigin(value = "http://localhost:3000/")
public class UserApi {
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserSevice userSevice;

	@PostMapping(value = "/register")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
		String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
		userDto = userSevice.saveUser(userDto, username);
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
		userDto.setUsername(user.getUsername());
		userDto.setUserRole(user.getAuthorities().toString());
		userDto.setPassword(null);
		String jwt = jwtTokenProvider.generateToken(userDto);
		userDto.setJwt(jwt);

		return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
	}

	@GetMapping(value = "/test")
	@PreAuthorize("hasRole('ADMIN')")
	public String test() {
		System.out.println(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).toString());
		return "test";
	}

	@GetMapping(value = "/heroku")
	public String heroku() {
		return "test";
	}
}
