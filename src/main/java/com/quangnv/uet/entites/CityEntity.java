package com.quangnv.uet.entites;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CityEntity {
	@Id
	@Column(length = 2)
	private String cityId;
	
	@Column(name = "city_name", columnDefinition = "nvarchar(30)")
	private String cityName;
	
	@OneToMany(mappedBy = "city")
	@EqualsAndHashCode.Exclude
	private List<DistrictEntity> district;
}
