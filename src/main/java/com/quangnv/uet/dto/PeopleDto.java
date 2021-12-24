package com.quangnv.uet.dto;

import java.util.Date;
import java.util.List;

import com.quangnv.uet.entites.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PeopleDto {

	private String peopleId;
	private String citizenId;
	private String name;
	private String gender;
	private String religion;
	private String educationLevel;
	private String job;
	private String regularyCode;
	private String phone;
	private Date dateOfBirth;
	private List<PeopleLocationDto> peopleLocations;
	private Date createAt;
	private UserEntity createBy;
	private Date lastModifiedAt;
	private UserEntity lastModifiedBy;
}
