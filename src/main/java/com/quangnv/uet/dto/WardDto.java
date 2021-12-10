package com.quangnv.uet.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WardDto {
	private String wardId;
	private String wardName;
	private String districtId;

	private List<VillageDto> villages;
}
