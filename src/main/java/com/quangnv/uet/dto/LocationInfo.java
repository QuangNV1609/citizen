package com.quangnv.uet.dto;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationInfo {
	private String locationName;
	private long totalPeople;
}
