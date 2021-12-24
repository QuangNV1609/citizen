package com.quangnv.uet.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
	private String username;
	private String password;
	private String userRole;
	private String location;
	private boolean enable;
	private String state;
	private Date start;
	private Date end;
	private String jwt;
}
