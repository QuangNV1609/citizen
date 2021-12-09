package com.quangnv.uet.entites;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
	
	@Id
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "user_role")
	private String userRole;
	
	@Column(name = "enable")
	private boolean enable;
	
	@Column(name = "create_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date create_at;

	@Column(name = "create_by")
	private String create_by;

	@Column(name = "last_modified_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedAt;

	@Column(name = "last_modified_by")
	private String lastModifiedBy;
}
