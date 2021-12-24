package com.quangnv.uet.entites;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PeopleEntity {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String peopleId;

	@Column(name = "citizen_id")
	private String citizenId;

	@Column(name = "name", columnDefinition = "nvarchar(30)")
	private String name;

	@Column(name = "gender", columnDefinition = "nvarchar(10)")
	private String gender;

	@Column(name = "religion", columnDefinition = "nvarchar(15)")
	private String religion;

	@Column(name = "education_level", columnDefinition = "nvarchar(30)")
	private String educationLevel;

	@Column(name = "job", columnDefinition = "nvarchar(30)")
	private String job;

	@Column(name = "date_of_birth")
	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;
	
	@Column(name = "phone", length = 10)
	private String phone;

	@OneToMany(mappedBy = "people")
	@Cascade(value = {CascadeType.DELETE})
	private List<PeopleLocation> peopleLocations;

	@Column(name = "create_at", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createAt;

	@CreatedBy
	@ManyToOne
	@JoinColumn(name = "create_by", updatable = false)
	private UserEntity createBy;

	@Column(name = "last_modified_at")
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date lastModifiedAt;

	@LastModifiedBy
	@ManyToOne
	@JoinColumn(name = "last_modified_by")
	private UserEntity lastModifiedBy;
}
