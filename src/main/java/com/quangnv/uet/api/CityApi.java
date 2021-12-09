package com.quangnv.uet.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quangnv.uet.dto.CityDto;
import com.quangnv.uet.service.impl.CitySevice;

@RestController
@RequestMapping(value = "/city")
public class CityApi {
	@Autowired
	private CitySevice citySevice;

	@PostMapping
	public void saveCity(@RequestBody List<CityDto> cityDtos) {
		citySevice.saveListCity(cityDtos);
	}
}
