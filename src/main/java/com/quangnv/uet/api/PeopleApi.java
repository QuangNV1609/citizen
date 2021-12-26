package com.quangnv.uet.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quangnv.uet.dto.AnalysisData;
import com.quangnv.uet.dto.ListPeopleDto;
import com.quangnv.uet.dto.LocationInfo;
import com.quangnv.uet.dto.PeopleDto;
import com.quangnv.uet.exception.ResourenotFoundException;
import com.quangnv.uet.service.PeopleService;

@RestController
@RequestMapping(value = "/people")
@CrossOrigin(value = "http://localhost:3000/")
public class PeopleApi {
	@Autowired
	private PeopleService peopleService;

	@GetMapping(value = "/{peopleId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<PeopleDto> getPeopleByPeopleId(@PathVariable("peopleId") String peopleId)
			throws ResourenotFoundException {
		PeopleDto peopleDto = peopleService.findPeopleByPeopleId(peopleId);
		return new ResponseEntity<PeopleDto>(peopleDto, HttpStatus.OK);
	}

	@GetMapping(value = "/location")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ListPeopleDto> findPeopleByLocation(
			@RequestParam(name = "size", defaultValue = "100", required = false) Integer size,
			@RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
			@RequestParam(name = "locationType", required = false) String locationType,
			@RequestParam(name = "locationIds", required = false) String[] locationIds) {
		UserDetails user = (UserDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		ListPeopleDto listPeopleDto = null;

		if (user.getAuthorities().toString().equals("[ROLE_ADMIN]")
				|| user.getAuthorities().toString().equals("[ROLE_A1]")) {
			listPeopleDto = peopleService.findAllPeople(size, page, locationType, locationIds);
		} else {
			listPeopleDto = peopleService.findPeopleByFilters(user.getUsername(), size, page, locationType,
					locationIds);
		}

		return new ResponseEntity<ListPeopleDto>(listPeopleDto, HttpStatus.OK);
	}

	@GetMapping(value = "/analysis")
	@PreAuthorize("@authorizationServiceImpl.checkAuthorization(#locationIds, authentication.principal)")
	public ResponseEntity<AnalysisData> analysisData(
			@RequestParam(name = "locationType", required = false) String locationType,
			@RequestParam(name = "locationIds", required = false) String[] locationIds) {
		UserDetails user = (UserDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		AnalysisData analysisData = peopleService.getAnalysisData(user, locationType, locationIds);

		return new ResponseEntity<AnalysisData>(analysisData, HttpStatus.OK);
	}

	@GetMapping(value = "/current")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<LocationInfo>> getLocationInfoCurrent(
			@RequestParam(name = "locationType", required = false) String locationType,
			@RequestParam(name = "locationIds", required = false) String[] locationIds) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<LocationInfo> locationInfos = peopleService.getLocationInfoCurrent(userDetails, locationType, locationIds);
		return new ResponseEntity<List<LocationInfo>>(locationInfos, HttpStatus.OK);
	}

	@GetMapping(value = "/location/filters")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ListPeopleDto> findPeopleByFilters(
			@RequestParam(name = "size", defaultValue = "100", required = false) Integer size,
			@RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
			@RequestParam(name = "locationType", required = false) String locationType,
			@RequestParam(name = "locationIds", required = false) String[] locationIds) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		ListPeopleDto listPeopleDto = peopleService.findPeopleByFilters(userId, size, page, locationType, locationIds);
		return new ResponseEntity<ListPeopleDto>(listPeopleDto, HttpStatus.OK);
	}

	@PostMapping(value = "/save")
	@PreAuthorize("hasRole('B1')")
	public ResponseEntity<String> savePeople(@RequestBody PeopleDto peopleDto) {
		String message = peopleService.savePeople(peopleDto);
		return new ResponseEntity<String>(message, HttpStatus.CREATED);
	}

	@PutMapping(value = "/edit")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> editPeopleByPeopleId(@RequestBody PeopleDto peopleDto)
			throws ResourenotFoundException {
		String message = peopleService.editPeople(peopleDto);
		return new ResponseEntity<String>(message, HttpStatus.OK);
	}

	@DeleteMapping(value = "/{peopleId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> deletePeopleById(@PathVariable("peopleId") String peopleId) {
		String message = peopleService.deletePeopleById(peopleId);
		return new ResponseEntity<String>(message, HttpStatus.OK);
	}
}
