package com.quangnv.uet.entites;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.quangnv.uet.entites.ids.DeclarationTimeId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DeclarationTime {
	
	@EmbeddedId
	private DeclarationTimeId declarationTimeId;
	
	@Column(name = "start")
	private Date start;

	@Column(name = "end")
	private Date end;

	@Column(name = "create_at", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createAt;
	
	@OneToOne
	@JoinColumn(name = "user")
	@MapsId("username")
	private UserEntity user;
	
	@CreatedBy
	@ManyToOne
	@MapsId("create_by")
	@JoinColumn(name = "create_by", updatable = false)
	private UserEntity createBy;

	@Column(name = "last_modified_at")
	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedAt;

	@LastModifiedBy
	@ManyToOne
	@JoinColumn(name = "last_modified_by")
	private UserEntity lastModifiedBy;
}
