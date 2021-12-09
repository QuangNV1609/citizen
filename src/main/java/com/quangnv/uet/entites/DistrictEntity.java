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
@Builder
@Data
public class DistrictEntity {
	@Id
	private Integer districtId;
	
	@Column(name = "district_name", columnDefinition = "nvarchar(30)")
	private String districtName;
	
	@ManyToOne
	@JoinColumn(name = "city_id")
	@EqualsAndHashCode.Exclude
	private CityEntity city;
	
	@OneToMany(mappedBy = "city")
	@EqualsAndHashCode.Exclude
	private List<DistrictEntity> districts;
}
