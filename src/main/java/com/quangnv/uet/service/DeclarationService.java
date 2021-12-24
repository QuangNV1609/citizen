package com.quangnv.uet.service;

import com.quangnv.uet.dto.DeclarationTimeDto;
import com.quangnv.uet.dto.ListDeclarationTime;

public interface DeclarationService {
	public DeclarationTimeDto createNewDeclaration(String usercreate, DeclarationTimeDto declarationTimeDto);

	public ListDeclarationTime createNewDeclarations(String usercreate, ListDeclarationTime listDeclarationTime);
}
