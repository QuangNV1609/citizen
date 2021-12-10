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

	public void saveListCity(List<CityDto> cityDtos) {
		for (CityDto cityDto : cityDtos) {
			CityEntity cityEntity = modelMapper.map(cityDto, CityEntity.class);
			if (Integer.parseInt(cityEntity.getCityId()) < 10) {
				cityEntity.setCityId("0" + cityEntity.getCityId());
			}
			cityEntity = cityRepository.save(cityEntity);

			for (DistrictDto districtDto : cityDto.getDistricts()) {
				DistrictEntity districtEntity = modelMapper.map(districtDto, DistrictEntity.class);
				if (Integer.parseInt(districtEntity.getDistrictId()) < 10) {
					districtEntity.setDistrictId(cityEntity.getCityId() + "0" + districtEntity.getDistrictId());
				}
				districtEntity.setCity(cityEntity);
				districtEntity = districtRepository.save(districtEntity);

				for (WardDto wardDto : districtDto.getWards()) {
					WardEntity wardEntity = modelMapper.map(wardDto, WardEntity.class);
					if (Integer.parseInt(wardEntity.getWardId()) < 10) {
						wardEntity.setWardId(districtEntity.getDistrictId() + "0" + wardEntity.getWardId());
					}
					wardEntity.setDistrict(districtEntity);
					wardRepository.save(wardEntity);
				}

			}
		}
	}

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
	public List<CityDto> getCity() {
		List<CityEntity> cityEntities = cityRepository.findAll();
		List<CityDto> cityDtos = new ArrayList<CityDto>();

		for (CityEntity cityEntity : cityEntities) {
			CityDto cityDto = modelMapper.map(cityEntity, CityDto.class);
			cityDtos.add(cityDto);
		}
		return cityDtos;
	}

	@Override
	public List<DistrictDto> getDistrictByCityId(String cityId) {
		CityEntity cityEntity = cityRepository.findById(cityId).get();

		List<DistrictDto> districtDtos = new ArrayList<DistrictDto>();
		for (DistrictEntity districtEntity : cityEntity.getDistrict()) {
			DistrictDto districtDto = modelMapper.map(districtEntity, DistrictDto.class);
			districtDtos.add(districtDto);
		}
		return districtDtos;
	}

	@Override
	public List<WardDto> getWardByDistrictId(String districtId) {
		DistrictEntity districtEntity = districtRepository.findById(districtId).get();
		List<WardDto> wardDtos = new ArrayList<WardDto>();

		for (WardEntity wardEntity : districtEntity.getWardEntities()) {
			WardDto wardDto = modelMapper.map(wardEntity, WardDto.class);
			wardDtos.add(wardDto);
		}
		return wardDtos;
	}

	@Override
	public List<VillageDto> getVillageByWardId(String wardId) {
		// TODO Auto-generated method stub
		return null;
	}
}
