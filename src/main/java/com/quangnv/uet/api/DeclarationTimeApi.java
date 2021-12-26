package com.quangnv.uet.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

	@GetMapping
	@PreAuthorize("!hasAnyRole('B1', 'B2')")
	public ResponseEntity<List<DeclarationTimeDto>> getListDeclaration() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		List<DeclarationTimeDto> declarationTimeDtos = declarationService.getListDeclarationTimeDtos(username);
		return new ResponseEntity<List<DeclarationTimeDto>>(declarationTimeDtos, HttpStatus.OK);
	}

	@GetMapping(value = "/state")
	@PreAuthorize("hasRole('B1')")
	public ResponseEntity<Boolean> getState() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Boolean state = declarationService.getState(username);
		return new ResponseEntity<Boolean>(state, HttpStatus.OK);
	}

	@PostMapping("/save")
	@PreAuthorize("@authorizationServiceImpl.checkAuthorization(#declarationTimeDto.username, authentication.principal)")
	public ResponseEntity<DeclarationTimeDto> addDeclarationTime(@RequestBody DeclarationTimeDto declarationTimeDto) {
		String usercreate = SecurityContextHolder.getContext().getAuthentication().getName();
		declarationTimeDto = declarationService.createNewDeclaration(usercreate, declarationTimeDto);
		return new ResponseEntity<DeclarationTimeDto>(declarationTimeDto, HttpStatus.OK);
	}

	@PostMapping("/save/list")
	@PreAuthorize("!hasRole('B2')")
	public ResponseEntity<ListDeclarationTime> addDeclarationTime(
			@RequestBody ListDeclarationTime listDeclarationTime) {
		String usercreate = SecurityContextHolder.getContext().getAuthentication().getName();
		listDeclarationTime = declarationService.createNewDeclarations(usercreate, listDeclarationTime);
		return new ResponseEntity<ListDeclarationTime>(listDeclarationTime, HttpStatus.OK);
	}

	@PutMapping(value = "/complete/{state}")
	@PreAuthorize("!hasRole('B2')")
	public ResponseEntity<DeclarationTimeDto> complete(@PathVariable("state") Boolean state) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		DeclarationTimeDto declarationTimeDto = declarationService.completeDeclaration(username, state);
		return new ResponseEntity<DeclarationTimeDto>(declarationTimeDto, HttpStatus.OK);
	}

}
