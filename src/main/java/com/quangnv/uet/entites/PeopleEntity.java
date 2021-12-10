package com.quangnv.uet.entites;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeopleEntity {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String peopleId;

	@Column(name = "citizen_id")
	private String citizen_id;

	@Column(name = "name", columnDefinition = "nvarchar(30)")
	private String name;

	@Column(name = "gender")
	private boolean gender;

	@Column(name = "religion", columnDefinition = "nvarchar(15)")
	private String religion;

	@Column(name = "educational_level")
	private int educationalLevel;

	@Column(name = "job", columnDefinition = "nvarchar(30)")
	private String job;

	@ManyToOne
	@JoinColumn(name = "home_town")
	@EqualsAndHashCode.Exclude
	private VillageEntity homeTown;

	@ManyToOne
	@JoinColumn(name = "permanent_address")
	@EqualsAndHashCode.Exclude
	private VillageEntity permanentAddress;

	@ManyToOne
	@EqualsAndHashCode.Exclude
	@JoinColumn(name = "staying_address")
	private VillageEntity stayingAddress;

	@Column(name = "create_at")
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date create_at;

	@CreatedBy
	@ManyToOne
	@JoinColumn(name = "create_by")
	private UserEntity create_by;

	@Column(name = "last_modified_at")
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date lastModifiedAt;

	@LastModifiedBy
	@ManyToOne
	@JoinColumn(name = "last_modified_by")
	private UserEntity lastModifiedBy;
}
