package com.quangnv.uet.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalysisData {
	private List<LocationInfo> locationInfos;
	private int maleNumber;
	private int femaleNumber;
	private List<AgeGender> ageGenders;
	private Map<String, Integer> educationMap;
	private Map<AgeEducation, Integer> educationAge;

}
