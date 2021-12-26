package com.quangnv.uet.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.quangnv.uet.dto.AgeEducation;
import com.quangnv.uet.dto.AgeGender;
import com.quangnv.uet.dto.AgeLevel;
import com.quangnv.uet.dto.AnalysisData;
import com.quangnv.uet.dto.ListPeopleDto;
import com.quangnv.uet.dto.LocationInfo;
import com.quangnv.uet.dto.PeopleDto;
import com.quangnv.uet.dto.PeopleLocationDto;
import com.quangnv.uet.dto.VillageDto;
import com.quangnv.uet.entites.CityEntity;
import com.quangnv.uet.entites.DeclarationTime;
import com.quangnv.uet.entites.DistrictEntity;
import com.quangnv.uet.entites.PeopleEntity;
import com.quangnv.uet.entites.PeopleLocation;
import com.quangnv.uet.entites.VillageEntity;
import com.quangnv.uet.entites.WardEntity;
import com.quangnv.uet.entites.ids.PeopleLocationId;
import com.quangnv.uet.exception.ResourenotFoundException;
import com.quangnv.uet.repository.CityRepository;
import com.quangnv.uet.repository.DeclarationRepository;
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

	@Autowired
	private DeclarationRepository declarationRepository;

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
		if (locationIds == null) {
			locationIds = userRepository.getUserIdByCreateBy(userDetails.getUsername());
		}
		AnalysisData analysisData = null;
		List<PeopleEntity> peopleEntities1 = null;
		if (locationType != null) {
			peopleEntities1 = peopleLocationRepository.findPeopleByLocationTypeAndLocationIds(locationIds, locationType,
					null);
		} else {
			peopleEntities1 = peopleLocationRepository.findPeopleByLocationIds(locationIds, null);
		}
		
		analysisData = getAnalysisFromListPeople(peopleEntities1);
		
		List<LocationInfo> locationInfos = new ArrayList<LocationInfo>();

		for (String locationId : locationIds) {
			List<PeopleEntity> peopleEntities = null;
			if (locationType != null) {
				peopleEntities = peopleLocationRepository.findPeopleByLocationType(locationId, locationType, null);
			} else {
				peopleEntities = peopleLocationRepository.findPeopleByLocationId(locationId, null);
			}

			LocationInfo locationInfo = new LocationInfo();
			locationInfo.setTotalPeople(peopleEntities.size());
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
			locationInfos.add(locationInfo);

		}

		analysisData.setLocationInfos(locationInfos);
		return analysisData;
	}

	private AnalysisData getAnalysisFromListPeople(List<PeopleEntity> peopleEntities) {
		int male = 0, female = 0;
		List<AgeGender> ageGenders = new ArrayList<AgeGender>();
		Map<String, Integer> educationMap = new HashMap<String, Integer>();
		Map<AgeEducation, Integer> educationAgeMap = new HashMap<AgeEducation, Integer>();

		AgeGender ageGender1 = new AgeGender(AgeLevel.LEVEL1, 0, 0);
		AgeGender ageGender2 = new AgeGender(AgeLevel.LEVEL2, 0, 0);
		AgeGender ageGender3 = new AgeGender(AgeLevel.LEVEL3, 0, 0);
		AgeGender ageGender4 = new AgeGender(AgeLevel.LEVEL4, 0, 0);
		AgeGender ageGender5 = new AgeGender(AgeLevel.LEVEL5, 0, 0);
		AgeGender ageGender6 = new AgeGender(AgeLevel.LEVEL6, 0, 0);
		AgeGender ageGender7 = new AgeGender(AgeLevel.LEVEL7, 0, 0);
		AgeGender ageGender8 = new AgeGender(AgeLevel.LEVEL8, 0, 0);

		educationMap.put("0/12", 0);
		educationMap.put("1/12", 0);
		educationMap.put("2/12", 0);
		educationMap.put("3/12", 0);
		educationMap.put("4/12", 0);
		educationMap.put("5/12", 0);
		educationMap.put("6/12", 0);
		educationMap.put("7/12", 0);
		educationMap.put("8/12", 0);
		educationMap.put("9/12", 0);
		educationMap.put("10/12", 0);
		educationMap.put("11/12", 0);
		educationMap.put("12/12", 0);
		educationMap.put("Trung cấp", 0);
		educationMap.put("Cao đẳng", 0);
		educationMap.put("Đại học", 0);
		educationMap.put("Sau đại học", 0);

		for (PeopleEntity peopleEntity : peopleEntities) {
			String educationLevel = peopleEntity.getEducationLevel();
			int age = (new Date()).getYear() - peopleEntity.getDateOfBirth().getYear();

			educationMap.put(educationLevel, educationMap.get(educationLevel) + 1);

			String genderName = peopleEntity.getGender();

			if (genderName.equalsIgnoreCase("nam")) {
				male++;
			} else {
				female++;
			}
			if (age >= 0 && age <= 5) {
				if (genderName.equalsIgnoreCase("nam")) {
					ageGender1.setMale(ageGender1.getMale() + 1);
				} else {
					ageGender1.setFemale(ageGender1.getFemale() + 1);
				}
				AgeEducation ageEducation = new AgeEducation(AgeLevel.LEVEL1, educationLevel);
				if (educationAgeMap.containsKey(ageEducation)) {
					educationAgeMap.put(ageEducation, educationAgeMap.get(ageEducation) + 1);
				} else {
					educationAgeMap.put(ageEducation, 1);
				}
			} else if (age >= 6 && age <= 10) {
				if (genderName.equalsIgnoreCase("nam")) {
					ageGender2.setMale(ageGender1.getMale() + 1);
				} else {
					ageGender2.setFemale(ageGender1.getFemale() + 1);
				}
				AgeEducation ageEducation = new AgeEducation(AgeLevel.LEVEL2, educationLevel);
				if (educationAgeMap.containsKey(ageEducation)) {
					educationAgeMap.put(ageEducation, educationAgeMap.get(ageEducation) + 1);
				} else {
					educationAgeMap.put(ageEducation, 1);
				}
			} else if (age >= 11 && age <= 14) {
				if (genderName.equalsIgnoreCase("nam")) {
					ageGender3.setMale(ageGender1.getMale() + 1);
				} else {
					ageGender3.setFemale(ageGender1.getFemale() + 1);
				}
				AgeEducation ageEducation = new AgeEducation(AgeLevel.LEVEL3, educationLevel);
				if (educationAgeMap.containsKey(ageEducation)) {
					educationAgeMap.put(ageEducation, educationAgeMap.get(ageEducation) + 1);
				} else {
					educationAgeMap.put(ageEducation, 1);
				}
			} else if (age >= 15 && age <= 17) {
				if (genderName.equalsIgnoreCase("nam")) {
					ageGender4.setMale(ageGender1.getMale() + 1);
				} else {
					ageGender4.setFemale(ageGender1.getFemale() + 1);
				}
				AgeEducation ageEducation = new AgeEducation(AgeLevel.LEVEL4, educationLevel);
				if (educationAgeMap.containsKey(ageEducation)) {
					educationAgeMap.put(ageEducation, educationAgeMap.get(ageEducation) + 1);
				} else {
					educationAgeMap.put(ageEducation, 1);
				}
			} else if (age >= 18 && age <= 21) {
				if (genderName.equalsIgnoreCase("nam")) {
					ageGender5.setMale(ageGender1.getMale() + 1);
				} else {
					ageGender5.setFemale(ageGender1.getFemale() + 1);
				}
				AgeEducation ageEducation = new AgeEducation(AgeLevel.LEVEL5, educationLevel);
				if (educationAgeMap.containsKey(ageEducation)) {
					educationAgeMap.put(ageEducation, educationAgeMap.get(ageEducation) + 1);
				} else {
					educationAgeMap.put(ageEducation, 1);
				}
			} else if (age >= 22 && age <= 30) {
				if (genderName.equalsIgnoreCase("nam")) {
					ageGender6.setMale(ageGender1.getMale() + 1);
				} else {
					ageGender6.setFemale(ageGender1.getFemale() + 1);
				}
				AgeEducation ageEducation = new AgeEducation(AgeLevel.LEVEL6, educationLevel);
				if (educationAgeMap.containsKey(ageEducation)) {
					educationAgeMap.put(ageEducation, educationAgeMap.get(ageEducation) + 1);
				} else {
					educationAgeMap.put(ageEducation, 1);
				}
			} else if (age >= 31 && age <= 60) {
				if (genderName.equalsIgnoreCase("nam")) {
					ageGender7.setMale(ageGender1.getMale() + 1);
				} else {
					ageGender7.setFemale(ageGender1.getFemale() + 1);
				}
				AgeEducation ageEducation = new AgeEducation(AgeLevel.LEVEL7, educationLevel);
				if (educationAgeMap.containsKey(ageEducation)) {
					educationAgeMap.put(ageEducation, educationAgeMap.get(ageEducation) + 1);
				} else {
					educationAgeMap.put(ageEducation, 1);
				}
			} else if (age > 60) {
				if (genderName.equalsIgnoreCase("nam")) {
					ageGender8.setMale(ageGender1.getMale() + 1);
				} else {
					ageGender8.setFemale(ageGender1.getFemale() + 1);
				}
				AgeEducation ageEducation = new AgeEducation(AgeLevel.LEVEL8, educationLevel);
				if (educationAgeMap.containsKey(ageEducation)) {
					educationAgeMap.put(ageEducation, educationAgeMap.get(ageEducation) + 1);
				} else {
					educationAgeMap.put(ageEducation, 1);
				}
			}
		}

		ageGenders.add(ageGender1);
		ageGenders.add(ageGender2);
		ageGenders.add(ageGender3);
		ageGenders.add(ageGender4);
		ageGenders.add(ageGender5);
		ageGenders.add(ageGender6);
		ageGenders.add(ageGender7);
		ageGenders.add(ageGender8);

		AnalysisData analysisData = AnalysisData.builder().maleNumber(male).femaleNumber(female).ageGenders(ageGenders)
				.educationMap(educationMap).educationAge(educationAgeMap).build();
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

	@Override
	public List<LocationInfo> getLocationInfoCurrent(UserDetails userDetails, String locationType,
			String[] locationIds) {
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

			LocationInfo locationInfo = new LocationInfo();

			switch (locationId.length()) {
			case 2:
				CityEntity cityEntity = cityRepository.getById(locationId);
				locationInfo = LocationInfo.builder().locationId(cityEntity.getCityId())
						.locationName(cityEntity.getCityName()).build();
				break;
			case 4:
				DistrictEntity districtEntity = districtRepository.getById(locationId);
				locationInfo = LocationInfo.builder().locationId(districtEntity.getDistrictId())
						.locationName(districtEntity.getDistrictName()).build();
				break;
			case 6:
				WardEntity wardEntity = wardRepository.getById(locationId);
				locationInfo = LocationInfo.builder().locationId(wardEntity.getWardId())
						.locationName(wardEntity.getWardName()).build();
				break;
			case 8:
				VillageEntity villageEntity = villageRepository.getById(locationId);
				locationInfo = LocationInfo.builder().locationId(villageEntity.getVillageId())
						.locationName(villageEntity.getVillageName()).build();
				break;
			default:
				break;
			}

			int male = 0, female = 0;
			for (PeopleEntity peopleEntity : peopleEntities) {
				if (peopleEntity.getGender().equalsIgnoreCase("nam")) {
					male++;
				} else {
					female++;
				}
			}

			locationInfo.setTotalPeople(peopleEntities.size());

			Optional<DeclarationTime> optional = declarationRepository.findByUserUsername(locationId);
			if (optional.isPresent()) {
				locationInfo.setState(optional.get().isState());
			} else {
				locationInfo.setState(true);
			}

			locationInfo.setFemale(female);
			locationInfo.setMale(male);
			locationInfos.add(locationInfo);

		}
		return locationInfos;
	}
}
