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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WardEntity {
	
	@Id
	private Integer wardId;
	
	@Column(name = "ward_name", columnDefinition = "nvarchar(30)")
	private String wardName;
	
	@ManyToOne
	@JoinColumn(name = "district_id")
	@EqualsAndHashCode.Exclude
	private DistrictEntity district;
	
	@OneToMany(mappedBy = "ward")
	@EqualsAndHashCode.Exclude
	private List<VillageEntity> villages;
}
