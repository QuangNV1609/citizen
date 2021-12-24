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
	@Column(length = 8)
	private String villageId;

	@Column(name = "village_name", columnDefinition = "nvarchar(30)")
	private String villageName;

	@OneToMany(mappedBy = "village")
	private List<PeopleLocation> peopleLocations;

	@ManyToOne
	@JoinColumn(name = "ward_id")
	private WardEntity ward;
}
