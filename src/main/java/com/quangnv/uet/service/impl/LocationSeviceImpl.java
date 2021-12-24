package com.quangnv.uet.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quangnv.uet.dto.CityDto;
import com.quangnv.uet.dto.DistrictDto;
import com.quangnv.uet.dto.VillageDto;
import com.quangnv.uet.dto.WardDto;
import com.quangnv.uet.entites.CityEntity;
import com.quangnv.uet.entites.DistrictEntity;
import com.quangnv.uet.entites.VillageEntity;
import com.quangnv.uet.entites.WardEntity;
import com.quangnv.uet.repository.CityRepository;
import com.quangnv.uet.repository.DistrictRepository;
import com.quangnv.uet.repository.VillageRepository;
import com.quangnv.uet.repository.WardRepository;
import com.quangnv.uet.service.LocationService;

@Service
public class LocationSeviceImpl implements LocationService {
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private WardRepository wardRepository;

	@Autowired
	private VillageRepository villageRepository;

	@Override
	public void saveListDistrict(List<DistrictDto> districtDtos, String cityId) {
		CityEntity cityEntity = cityRepository.findById(cityId).get();

		for (DistrictDto districtDto : districtDtos) {
			DistrictEntity districtEntity = modelMapper.map(districtDto, DistrictEntity.class);
			if (Integer.parseInt(districtEntity.getDistrictId()) < 1000) {
				districtEntity.setDistrictId("0" + districtEntity.getDistrictId());
			}
			districtEntity.setCity(cityEntity);
			districtRepository.save(districtEntity);
		}

	}

	@Override
	public void saveListWard(List<WardDto> wardDtos, String districtId) {
		DistrictEntity districtEntity = districtRepository.findById(districtId).get();

		for (WardDto wardDto : wardDtos) {
			WardEntity wardEntity = modelMapper.map(wardDto, WardEntity.class);
			if (Integer.parseInt(wardEntity.getWardId()) < 100000) {
				wardEntity.setWardId("0" + wardEntity.getWardId());
			}
			wardEntity.setDistrict(districtEntity);
			wardRepository.save(wardEntity);
		}

	}

	@Override
	public void saveListVillage(List<VillageDto> villageDtos, String wardId) {
		WardEntity wardEntity = wardRepository.findById(wardId).get();

		for (VillageDto villageDto : villageDtos) {
			VillageEntity villageEntity = modelMapper.map(villageDto, VillageEntity.class);
			if (Integer.parseInt(villageEntity.getVillageId()) < 10000000) {
				villageEntity.setVillageId("0" + villageEntity.getVillageId());
			}
			villageEntity.setWard(wardEntity);
			villageRepository.save(villageEntity);
		}

	}

	@Override
	public List<CityDto> getCities() {
		List<CityEntity> cityEntities = cityRepository.findAll();
		List<CityDto> cityDtos = new ArrayList<CityDto>();

		for (CityEntity cityEntity : cityEntities) {
			CityDto cityDto = CityDto.builder().cityId(cityEntity.getCityId()).cityName(cityEntity.getCityName())
					.build();
			cityDtos.add(cityDto);
		}
		return cityDtos;
	}

	@Override
	public List<DistrictDto> getDistrictByCityId(String cityId) {
		CityEntity cityEntity = cityRepository.findById(cityId).get();

		List<DistrictDto> districtDtos = new ArrayList<DistrictDto>();
		for (DistrictEntity districtEntity : cityEntity.getDistrict()) {
			DistrictDto districtDto = DistrictDto.builder().districtId(districtEntity.getDistrictId())
					.districtName(districtEntity.getDistrictName()).build();
			districtDtos.add(districtDto);
		}
		return districtDtos;
	}

	@Override
	public List<WardDto> getWardByDistrictId(String districtId) {
		DistrictEntity districtEntity = districtRepository.findById(districtId).get();
		List<WardDto> wardDtos = new ArrayList<WardDto>();

		for (WardEntity wardEntity : districtEntity.getWardEntities()) {
			WardDto wardDto = WardDto.builder().wardId(wardEntity.getWardId()).wardName(wardEntity.getWardName())
					.build();
			wardDtos.add(wardDto);
		}
		return wardDtos;
	}

	@Override
	public List<VillageDto> getVillageByWardId(String wardId) {
		WardEntity wardEntity = wardRepository.findById(wardId).get();
		List<VillageDto> villageDtos = new ArrayList<VillageDto>();

		for (VillageEntity villageEntity : wardEntity.getVillages()) {
			VillageDto villageDto = VillageDto.builder().villageId(villageEntity.getVillageId())
					.villageName(villageEntity.getVillageName()).build();
			villageDtos.add(villageDto);
		}
		return villageDtos;
	}

	@Override
	public List<DistrictDto> getDistrictByCityId(String[] cityIds) {
		List<DistrictDto> districtDtos = new ArrayList<DistrictDto>();
		for (CityEntity cityEntity : cityRepository.findCityByCityIds(cityIds)) {
			for (DistrictEntity districtEntity : cityEntity.getDistrict()) {
				DistrictDto districtDto = DistrictDto.builder().districtId(districtEntity.getDistrictId())
						.districtName(districtEntity.getDistrictName()).build();
				districtDtos.add(districtDto);
			}
		}
		return districtDtos;
	}

	@Override
	public List<WardDto> getWardByDistrictId(String[] districtIds) {
		List<WardDto> wardDtos = new ArrayList<WardDto>();
		for (DistrictEntity districtEntity : districtRepository.findDistrictByDistrictIds(districtIds)) {
			for (WardEntity wardEntity : districtEntity.getWardEntities()) {
				WardDto wardDto = WardDto.builder().wardId(wardEntity.getWardId()).wardName(wardEntity.getWardName())
						.build();
				wardDtos.add(wardDto);
			}
		}
		return wardDtos;
	}

	@Override
	public List<VillageDto> getVillageByWardId(String[] wardIds) {
		List<VillageDto> villageDtos = new ArrayList<VillageDto>();

		for (WardEntity wardEntity : wardRepository.findWardByWardIds(wardIds)) {
			for (VillageEntity villageEntity : wardEntity.getVillages()) {
				VillageDto villageDto = VillageDto.builder().villageId(villageEntity.getVillageId())
						.villageName(villageEntity.getVillageName()).build();
				villageDtos.add(villageDto);
			}
		}
		return villageDtos;
	}

}
