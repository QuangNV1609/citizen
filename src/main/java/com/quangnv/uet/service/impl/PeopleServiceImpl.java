package com.quangnv.uet.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.quangnv.uet.dto.AnalysisData;
import com.quangnv.uet.dto.ListPeopleDto;
import com.quangnv.uet.dto.LocationInfo;
import com.quangnv.uet.dto.PeopleDto;
import com.quangnv.uet.dto.PeopleLocationDto;
import com.quangnv.uet.dto.VillageDto;
import com.quangnv.uet.entites.PeopleEntity;
import com.quangnv.uet.entites.PeopleLocation;
import com.quangnv.uet.entites.VillageEntity;
import com.quangnv.uet.entites.ids.PeopleLocationId;
import com.quangnv.uet.exception.ResourenotFoundException;
import com.quangnv.uet.repository.CityRepository;
import com.quangnv.uet.repository.DistrictRepository;
import com.quangnv.uet.repository.PeopleLocationRepository;
import com.quangnv.uet.repository.PeopleRepository;
import com.quangnv.uet.repository.UserRepository;
import com.quangnv.uet.repository.VillageRepository;
import com.quangnv.uet.repository.WardRepository;
import com.quangnv.uet.service.PeopleService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PeopleServiceImpl implements PeopleService {
	@Autowired
	private PeopleLocationRepository peopleLocationRepository;

	@Autowired
	private PeopleRepository peopleRepository;

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

	@Autowired
	private UserRepository userRepository;

	@Override
	public ListPeopleDto findPeopleByLocationId(String locationId, Integer size, Integer page) {
		Pageable pageable = PageRequest.of(page - 1, size);
		List<PeopleEntity> peopleEntities = peopleLocationRepository.findPeopleByLocationId(locationId, pageable);
		List<PeopleDto> peopleDtos = new ArrayList<PeopleDto>();
		for (PeopleEntity peopleEntity : peopleEntities) {
			PeopleDto peopleDto = PeopleDto.builder().peopleId(peopleEntity.getPeopleId())
					.citizenId(peopleEntity.getCitizenId()).name(peopleEntity.getName())
					.gender(peopleEntity.getGender()).religion(peopleEntity.getReligion())
					.dateOfBirth(peopleEntity.getDateOfBirth())
					.regularyCode(peopleEntity.getPeopleLocations().get(1).getVillage().getVillageId()).build();
			peopleDtos.add(peopleDto);
		}

		ListPeopleDto listPeopleDto = new ListPeopleDto();
		listPeopleDto.setPeopleDtos(peopleDtos);
		listPeopleDto.setTotalPage(
				(int) (Math.ceil((double) peopleLocationRepository.countPeopleByLocationId(locationId) / size)));
		return listPeopleDto;
	}

	@Override
	public ListPeopleDto findPeopleByFilters(String userId, Integer size, Integer page, String locationType,
			String[] locationIds) {
		Pageable pageable = PageRequest.of(page - 1, size);
		List<PeopleEntity> peopleEntities = null;
		ListPeopleDto listPeopleDto = new ListPeopleDto();

		if (locationType != null && locationIds == null) {
			peopleEntities = peopleLocationRepository.findPeopleByLocationType(userId, locationType, pageable);
			listPeopleDto.setTotalPage((int) (Math
					.ceil((double) peopleLocationRepository.countPeopleByLocationType(userId, locationType) / size)));
		} else if (locationType == null && locationIds != null) {
			peopleEntities = peopleLocationRepository.findPeopleByLocationIds(locationIds, pageable);
			listPeopleDto.setTotalPage(
					(int) (Math.ceil((double) peopleLocationRepository.countPeopleByLocationIds(locationIds) / size)));
		} else if (locationType != null && locationIds != null) {
			peopleEntities = peopleLocationRepository.findPeopleByLocationTypeAndLocationIds(locationIds, locationType,
					pageable);
			listPeopleDto.setTotalPage((int) (Math.ceil(
					(double) peopleLocationRepository.countPeopleByLocationTypeAndLocationIds(locationIds, locationType)
							/ size)));
		} else {
			peopleEntities = peopleLocationRepository.findPeopleByLocationId(userId, pageable);
			listPeopleDto.setTotalPage(
					(int) (Math.ceil((double) peopleLocationRepository.countPeopleByLocationId(userId) / size)));
		}

		List<PeopleDto> peopleDtos = new ArrayList<PeopleDto>();
		for (PeopleEntity peopleEntity : peopleEntities) {
			PeopleDto peopleDto = PeopleDto.builder().peopleId(peopleEntity.getPeopleId())
					.citizenId(peopleEntity.getCitizenId()).name(peopleEntity.getName())
					.gender(peopleEntity.getGender()).religion(peopleEntity.getReligion())
					.dateOfBirth(peopleEntity.getDateOfBirth())
					.regularyCode(peopleEntity.getPeopleLocations().get(1).getVillage().getVillageId()).build();
			peopleDtos.add(peopleDto);
		}

		listPeopleDto.setPeopleDtos(peopleDtos);

		return listPeopleDto;
	}

	@Override
	public String savePeople(PeopleDto peopleDto) {
		PeopleEntity peopleEntity = modelMapper.map(peopleDto, PeopleEntity.class);
		peopleEntity = peopleRepository.save(peopleEntity);
		log.info(peopleDto.toString());

		for (PeopleLocationDto locationDto : peopleDto.getPeopleLocations()) {
			if (locationDto != null) {
				VillageEntity villageEntity = villageRepository.findById(locationDto.getVillage().getVillageId()).get();
				PeopleLocationId peopleLocationId = new PeopleLocationId(peopleEntity.getPeopleId(),
						villageEntity.getVillageId(), locationDto.getLocationType());
				PeopleLocation peopleLocation = new PeopleLocation(peopleLocationId, peopleEntity, villageEntity);
				peopleLocationRepository.save(peopleLocation);
			}
		}

		return "Khai báo thông tin thành công!";
	}

	@Override
	public PeopleDto findPeopleByPeopleId(String peopleId) throws ResourenotFoundException {
		Optional<PeopleEntity> optional = peopleRepository.findById(peopleId);

		if (!optional.isPresent()) {
			throw new ResourenotFoundException(peopleId + " not found!");
		}

		PeopleEntity peopleEntity = optional.get();
		PeopleDto peopleDto = PeopleDto.builder().peopleId(peopleEntity.getPeopleId())
				.citizenId(peopleEntity.getCitizenId()).name(peopleEntity.getName()).gender(peopleEntity.getGender())
				.religion(peopleEntity.getReligion()).dateOfBirth(peopleEntity.getDateOfBirth())
				.job(peopleEntity.getJob()).educationLevel(peopleEntity.getEducationLevel())
				.phone(peopleEntity.getPhone()).build();

		List<PeopleLocationDto> peopleLocationDtos = new ArrayList<PeopleLocationDto>();
		for (PeopleLocation peopleLocation : peopleEntity.getPeopleLocations()) {
			PeopleLocationDto peopleLocationDto = PeopleLocationDto.builder()
					.village(modelMapper.map(peopleLocation.getVillage(), VillageDto.class))
					.locationType(peopleLocation.getPeopleLocationId().getLocationType())
					.locationName(peopleLocation.getVillage().getVillageName() + ", "
							+ peopleLocation.getVillage().getWard().getWardName() + ", "
							+ peopleLocation.getVillage().getWard().getDistrict().getDistrictName() + ", "
							+ peopleLocation.getVillage().getWard().getDistrict().getCity().getCityName())
					.build();
			peopleLocationDtos.add(peopleLocationDto);
		}

		peopleDto.setPeopleLocations(peopleLocationDtos);

		return peopleDto;
	}

	@Override
	public ListPeopleDto findAllPeople(Integer size, Integer page, String locationType, String[] locationIds) {
		Pageable pageable = PageRequest.of(page - 1, size);
		List<PeopleEntity> peopleEntities = null;
		ListPeopleDto listPeopleDto = new ListPeopleDto();

		if (locationType != null && locationIds == null) {
			peopleEntities = peopleLocationRepository.findAllByLocationType(locationType, pageable);
			listPeopleDto.setTotalPage(
					(int) (Math.ceil((double) peopleLocationRepository.countAllByLocationType(locationType) / size)));
		} else if (locationType == null && locationIds != null) {
			peopleEntities = peopleLocationRepository.findPeopleByLocationIds(locationIds, pageable);
			listPeopleDto.setTotalPage(
					(int) (Math.ceil((double) peopleLocationRepository.countPeopleByLocationIds(locationIds) / size)));
		} else if (locationType != null && locationIds != null) {
			peopleEntities = peopleLocationRepository.findPeopleByLocationTypeAndLocationIds(locationIds, locationType,
					pageable);
			listPeopleDto.setTotalPage((int) (Math.ceil(
					(double) peopleLocationRepository.countPeopleByLocationTypeAndLocationIds(locationIds, locationType)
							/ size)));
		} else {
			peopleEntities = peopleRepository.findAll(pageable).toList();
			listPeopleDto.setTotalPage((int) (Math.ceil((double) peopleLocationRepository.count() / size)));
		}

		List<PeopleDto> peopleDtos = new ArrayList<PeopleDto>();
		for (PeopleEntity peopleEntity : peopleEntities) {
			PeopleDto peopleDto = PeopleDto.builder().peopleId(peopleEntity.getPeopleId())
					.citizenId(peopleEntity.getCitizenId()).name(peopleEntity.getName())
					.gender(peopleEntity.getGender()).religion(peopleEntity.getReligion())
					.dateOfBirth(peopleEntity.getDateOfBirth())
					.regularyCode(peopleEntity.getPeopleLocations().get(1).getVillage().getVillageId()).build();
			peopleDtos.add(peopleDto);
		}

		listPeopleDto.setPeopleDtos(peopleDtos);

		return listPeopleDto;
	}

	@Override
	public String deletePeopleById(String peopleId) {
		peopleRepository.deleteById(peopleId);
		return "Xóa thông tin thành công!";
	}

	@Override
	public AnalysisData getAnalysisData(UserDetails userDetails, String locationType, String[] locationIds) {
		int male = 0;
		int female = 0;

		if (locationIds == null) {
			locationIds = userRepository.getUserIdByCreateBy(userDetails.getUsername());
		}

		List<LocationInfo> locationInfos = new ArrayList<LocationInfo>();

		for (String locationId : locationIds) {
			List<PeopleEntity> peopleEntities = null;
			if (locationType != null) {
				peopleEntities = peopleLocationRepository.findPeopleByLocationType(locationId, locationType, null);
			} else {
				peopleEntities = peopleLocationRepository.findPeopleByLocationId(locationId, null);
			}
			LocationInfo locationInfo = LocationInfo.builder().totalPeople(peopleEntities.size()).build();
			if (peopleEntities.size() == 0) {
				switch (locationId.length()) {
				case 2:
					locationInfo.setLocationName(cityRepository.getById(locationId).getCityName());
					break;
				case 4:
					locationInfo.setLocationName(districtRepository.getById(locationId).getDistrictName());
					break;
				case 6:
					locationInfo.setLocationName(wardRepository.getById(locationId).getWardName());
					break;
				case 8:
					locationInfo.setLocationName(villageRepository.getById(locationId).getVillageName());
					break;
				default:
					break;
				}

			}
			locationInfos.add(locationInfo);
			for (PeopleEntity peopleEntity : peopleEntities) {
				if (peopleEntity.getGender().equalsIgnoreCase("Nam")) {
					male++;
				} else if (peopleEntity.getGender().equalsIgnoreCase("Nữ")) {
					female++;
				}
			}

		}

		AnalysisData analysisData = new AnalysisData(locationInfos, male, female);
		return analysisData;
	}

	@Override
	@Transactional
	public String editPeople(PeopleDto peopleDto) {

		peopleRepository.deleteById(peopleDto.getPeopleId());

		PeopleEntity peopleEntity = modelMapper.map(peopleDto, PeopleEntity.class);
		peopleEntity = peopleRepository.save(peopleEntity);
		log.info(peopleDto.toString());

		for (PeopleLocationDto locationDto : peopleDto.getPeopleLocations()) {
			if (locationDto != null) {
				VillageEntity villageEntity = villageRepository.findById(locationDto.getVillage().getVillageId()).get();
				PeopleLocationId peopleLocationId = new PeopleLocationId(peopleEntity.getPeopleId(),
						villageEntity.getVillageId(), locationDto.getLocationType());
				PeopleLocation peopleLocation = new PeopleLocation(peopleLocationId, peopleEntity, villageEntity);
				peopleLocationRepository.save(peopleLocation);
			}
		}
		return "Cập nhật thành công!";
	}
}
