package com.quangnv.uet.entites;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class VillageEntity {
	@Id
	private Integer villageId;
	
	@Column(name = "village_name", columnDefinition = "nvarchar(30)")
	private String villageName;
	
	@OneToMany(mappedBy = "homeTown")
	private List<PeopleEntity> homeTown;
	
	@OneToMany(mappedBy = "permanentAddress")
	private List<PeopleEntity> permanentAddress;

	@OneToMany(mappedBy = "stayingAddress")
	private List<PeopleEntity> stayingAddress;
	
	@ManyToOne
	@JoinColumn(name = "ward_id")
	private WardEntity ward;
}
