package com.quangnv.uet.service;

import java.util.List;

import com.quangnv.uet.dto.CityDto;
import com.quangnv.uet.dto.DistrictDto;
import com.quangnv.uet.dto.VillageDto;
import com.quangnv.uet.dto.WardDto;

public interface LocationService {
	public List<CityDto> getCity();

	public List<DistrictDto> getDistrictByCityId(String cityId);

	public List<WardDto> getWardByDistrictId(String districtId);

	public List<VillageDto> getVillageByWardId(String wardId);

	public void saveListCity(List<CityDto> cityDtos);

	public void saveListDistrict(List<DistrictDto> districtDtos, String cityId);

	public void saveListWard(List<WardDto> wardDtos, String districtId);

	public void saveListVillage(List<VillageDto> villageDtos, String wardId);
}
