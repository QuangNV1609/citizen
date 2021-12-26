package com.quangnv.uet.service;

import java.util.List;

import com.quangnv.uet.dto.DeclarationTimeDto;
import com.quangnv.uet.dto.ListDeclarationTime;

public interface DeclarationService {
	public Boolean getState(String username);
	
	public List<DeclarationTimeDto> getListDeclarationTimeDtos(String createBy);
	
	public DeclarationTimeDto createNewDeclaration(String usercreate, DeclarationTimeDto declarationTimeDto);
	
	public DeclarationTimeDto completeDeclaration(String username, boolean state);

	public ListDeclarationTime createNewDeclarations(String usercreate, ListDeclarationTime listDeclarationTime);
}
