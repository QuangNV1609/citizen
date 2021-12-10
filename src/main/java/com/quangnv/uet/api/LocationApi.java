package com.quangnv.uet.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quangnv.uet.dto.CityDto;
import com.quangnv.uet.dto.DistrictDto;
import com.quangnv.uet.dto.WardDto;
import com.quangnv.uet.service.LocationService;

@RestController
@CrossOrigin(value = "http://localhost:3000/")
@RequestMapping(value = "/location")
public class LocationApi {
	@Autowired
	private LocationService locationSevice;

	@GetMapping(value = "/city")
	public ResponseEntity<List<CityDto>> getCity() {
		List<CityDto> cityDtos = locationSevice.getCity();
		return new ResponseEntity<List<CityDto>>(cityDtos, HttpStatus.OK);
	}

	@GetMapping(value = "/district/{cityId}")
	public ResponseEntity<List<DistrictDto>> getDistrict(@PathVariable("cityId") String cityId) {
		List<DistrictDto> districtDtos = locationSevice.getDistrictByCityId(cityId);

		return new ResponseEntity<List<DistrictDto>>(districtDtos, HttpStatus.OK);
	}

	@GetMapping(value = "/ward/{districtId}")
	public ResponseEntity<List<WardDto>> getWard(@PathVariable("districtId") String districtId) {
		List<WardDto> wardDtos = locationSevice.getWardByDistrictId(districtId);
		return new ResponseEntity<List<WardDto>>(wardDtos, HttpStatus.OK);
	}

	@PostMapping(value = "/city")
	public void saveCity(@RequestBody List<CityDto> cityDtos) {
		locationSevice.saveListCity(cityDtos);
	}

	@PostMapping(value = "/district/{cityId}")
	public void saveDistrict(@RequestBody List<CityDto> cityDtos, @PathVariable("cityId") Integer cityId) {
		locationSevice.saveListCity(cityDtos);
	}

	@PostMapping(value = "/ward/{districtId}")
	public void saveWard(@RequestBody List<CityDto> cityDtos, @PathVariable("districtId") Integer districtId) {
		locationSevice.saveListCity(cityDtos);
	}

	@PostMapping(value = "/village/{villageId}")
	public void saveVillage(@RequestBody List<CityDto> cityDtos, @PathVariable("villageId") Integer villageId) {
		locationSevice.saveListCity(cityDtos);
	}
}
