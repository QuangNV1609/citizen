package com.quangnv.uet.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quangnv.uet.dto.CityDto;
import com.quangnv.uet.entites.CityEntity;
import com.quangnv.uet.repository.CityRepository;

@Service
public class CitySevice {
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CityRepository cityRepository;

	public void saveListCity(List<CityDto> cityDtos) {
		for (CityDto cityDto : cityDtos) {
			CityEntity cityEntity = modelMapper.map(cityDto, CityEntity.class);
			cityRepository.save(cityEntity);
		}
	}
}
