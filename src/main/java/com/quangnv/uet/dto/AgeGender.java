package com.quangnv.uet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class AgeGender {
	private AgeLevel ageLevel;
	private int male;
	private int female;
}
