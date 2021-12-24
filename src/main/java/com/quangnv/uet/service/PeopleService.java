package com.quangnv.uet.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.quangnv.uet.dto.AnalysisData;
import com.quangnv.uet.dto.ListPeopleDto;
import com.quangnv.uet.dto.PeopleDto;
import com.quangnv.uet.exception.ResourenotFoundException;

public interface PeopleService {
	public AnalysisData getAnalysisData(UserDetails userDetails, String locationType, String[] locationId);
	
	public ListPeopleDto findAllPeople(Integer size, Integer page, String locationType, String[] locationId);

	public ListPeopleDto findPeopleByLocationId(String locationId, Integer size, Integer page);

	public ListPeopleDto findPeopleByFilters(String userId, Integer size, Integer page, String locationType,
			String[] locationId);

	public String savePeople(PeopleDto peopleDto);
	
	public String editPeople(PeopleDto peopleDto);

	public String deletePeopleById(String peopleId);

	public PeopleDto findPeopleByPeopleId(String peopleId) throws ResourenotFoundException;
}
