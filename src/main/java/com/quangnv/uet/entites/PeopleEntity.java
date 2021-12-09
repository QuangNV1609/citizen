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
	private Date create_at;

	@Column(name = "create_by")
	private String create_by;

	@Column(name = "last_modified_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedAt;

	@Column(name = "last_modified_by")
	private String lastModifiedBy;
}
