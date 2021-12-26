package com.quangnv.uet.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quangnv.uet.dto.CityDto;
import com.quangnv.uet.dto.DistrictDto;
import com.quangnv.uet.dto.VillageDto;
import com.quangnv.uet.dto.WardDto;
import com.quangnv.uet.service.LocationService;

@RestController
@CrossOrigin(value = "http://localhost:3000/")
@RequestMapping(value = "/location")
public class LocationApi {
	@Autowired
	private LocationService locationSevice;

	@GetMapping(value = "/city")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<CityDto>> getCity() {
		List<CityDto> cityDtos = locationSevice.getCities();
		return new ResponseEntity<List<CityDto>>(cityDtos, HttpStatus.OK);
	}

	@GetMapping(value = "/district/{cityId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<DistrictDto>> getDistrict(@PathVariable("cityId") String cityId) {
		List<DistrictDto> districtDtos = locationSevice.getDistrictByCityId(cityId);

		return new ResponseEntity<List<DistrictDto>>(districtDtos, HttpStatus.OK);
	}

	@GetMapping(value = "/ward/{districtId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<WardDto>> getWard(@PathVariable("districtId") String districtId) {
		List<WardDto> wardDtos = locationSevice.getWardByDistrictId(districtId);
		return new ResponseEntity<List<WardDto>>(wardDtos, HttpStatus.OK);
	}

	@GetMapping(value = "/village/{wardId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<VillageDto>> getVillages(@PathVariable("wardId") String wardId) {
		List<VillageDto> villageDtos = locationSevice.getVillageByWardId(wardId);
		return new ResponseEntity<List<VillageDto>>(villageDtos, HttpStatus.OK);
	}

	@GetMapping(value = "/district")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<DistrictDto>> getDistrict(@RequestParam("cityIds") String[] cityIds) {
		List<DistrictDto> districtDtos = locationSevice.getDistrictByCityId(cityIds);

		return new ResponseEntity<List<DistrictDto>>(districtDtos, HttpStatus.OK);
	}

	@GetMapping(value = "/ward")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<WardDto>> getWard(@RequestParam("districtIds") String[] districtIds) {
		List<WardDto> wardDtos = locationSevice.getWardByDistrictId(districtIds);
		return new ResponseEntity<List<WardDto>>(wardDtos, HttpStatus.OK);
	}

	@GetMapping(value = "/village")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<VillageDto>> getVillages(@RequestParam("wardIds") String[] wardIds) {
		List<VillageDto> villageDtos = locationSevice.getVillageByWardId(wardIds);
		return new ResponseEntity<List<VillageDto>>(villageDtos, HttpStatus.OK);
	}

}
