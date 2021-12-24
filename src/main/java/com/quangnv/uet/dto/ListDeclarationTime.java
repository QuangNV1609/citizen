package com.quangnv.uet.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ListDeclarationTime {
	private List<String> usernames;
	private Date start;
	private Date end;
	private String message;
}
