package com.quangnv.uet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationInfo {
	private String locationId;
	private String locationName;
	private int male;
	private int female;
	private long totalPeople;
	private boolean state;
	
}
