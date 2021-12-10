package com.quangnv.uet.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DistrictDto {
	private String districtId;
	private String districtName;
	private String cityId;
	
	private List<WardDto> wards;
}
