package com.quangnv.uet.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quangnv.uet.dto.DeclarationTimeDto;
import com.quangnv.uet.dto.ListDeclarationTime;
import com.quangnv.uet.service.DeclarationService;

@RestController
@RequestMapping(value = "/declarationtime")
@CrossOrigin(value = "http://localhost:3000/")
public class DeclarationTimeApi {

	@Autowired
	private DeclarationService declarationService;

	@PostMapping("/save")
	@PreAuthorize("@authorizationServiceImpl.checkAuthorization(#declarationTimeDto.username, authentication.principal)")
	public ResponseEntity<DeclarationTimeDto> addDeclarationTime(@RequestBody DeclarationTimeDto declarationTimeDto) {
		String usercreate = SecurityContextHolder.getContext().getAuthentication().getName();
		declarationTimeDto = declarationService.createNewDeclaration(usercreate, declarationTimeDto);
		return new ResponseEntity<DeclarationTimeDto>(declarationTimeDto, HttpStatus.OK);
	}

	@PostMapping("/save/list")
	public ResponseEntity<ListDeclarationTime> addDeclarationTime(
			@RequestBody ListDeclarationTime listDeclarationTime) {
		String usercreate = SecurityContextHolder.getContext().getAuthentication().getName();
		listDeclarationTime = declarationService.createNewDeclarations(usercreate, listDeclarationTime);
		return new ResponseEntity<ListDeclarationTime>(listDeclarationTime, HttpStatus.OK);
	}

}
