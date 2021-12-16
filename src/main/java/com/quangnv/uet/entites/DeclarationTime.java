package com.quangnv.uet.entites;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
	@Id
	private String declarationTimeId;

	@Column(name = "start")
	private Date start;

	@Column(name = "end")
	private Date end;

	@Column(name = "create_at", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date create_at;

	@CreatedBy
	@ManyToOne
	@JoinColumn(name = "create_by", updatable = false)
	private UserEntity create_by;

	@Column(name = "last_modified_at")
	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedAt;

	@LastModifiedBy
	@ManyToOne
	@JoinColumn(name = "last_modified_by")
	private UserEntity lastModifiedBy;
}
