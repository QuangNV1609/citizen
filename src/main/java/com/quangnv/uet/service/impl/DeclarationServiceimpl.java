package com.quangnv.uet.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quangnv.uet.dto.DeclarationTimeDto;
import com.quangnv.uet.dto.ListDeclarationTime;
import com.quangnv.uet.entites.DeclarationTime;
import com.quangnv.uet.entites.UserEntity;
import com.quangnv.uet.entites.ids.DeclarationTimeId;
import com.quangnv.uet.repository.CityRepository;
import com.quangnv.uet.repository.DeclarationRepository;
import com.quangnv.uet.repository.DistrictRepository;
import com.quangnv.uet.repository.UserRepository;
import com.quangnv.uet.repository.VillageRepository;
import com.quangnv.uet.repository.WardRepository;
import com.quangnv.uet.service.DeclarationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DeclarationServiceimpl implements DeclarationService {
	@Autowired
	private DeclarationRepository declarationRepository;

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
	public DeclarationTimeDto createNewDeclaration(String usercreate, DeclarationTimeDto declarationTimeDto) {
		if (declarationTimeDto.getStart().compareTo(declarationTimeDto.getEnd()) > 0) {
			log.error("save " + declarationTimeDto.toString() + " failure!");
			return DeclarationTimeDto.builder().message("Kiểm tra lại ngày tháng!").build();
		}
		UserEntity userEntity = userRepository.findById(declarationTimeDto.getUsername()).get();
		UserEntity createBy = userEntity.getCreateBy();

		if (createBy.getDeclarationTime() != null) {
			DeclarationTime declarationTime = createBy.getDeclarationTime();
			if (declarationTime.getStart().compareTo(declarationTimeDto.getStart()) > 0
					|| declarationTime.getEnd().compareTo(declarationTimeDto.getEnd()) < 0) {
				log.error("save " + declarationTimeDto.toString() + " failure!");
				return DeclarationTimeDto.builder().message("Kiểm tra lại ngày tháng!").build();
			}
		}

		DeclarationTimeId declarationTimeId = new DeclarationTimeId(declarationTimeDto.getUsername(), usercreate);
		DeclarationTime declarationTime = modelMapper.map(declarationTimeDto, DeclarationTime.class);
		declarationTime.setDeclarationTimeId(declarationTimeId);
		declarationTime.setUser(userEntity);
		declarationTime.setState(false);

		declarationTime = declarationRepository.save(declarationTime);
		declarationTimeDto.setMessage("Thay đổi thời gian khai báo thành công!");

		log.info("save " + declarationTimeDto.toString() + " successful!");

		return declarationTimeDto;
	}

	@Override
	public ListDeclarationTime createNewDeclarations(String usercreate, ListDeclarationTime listDeclarationTime) {
		if (listDeclarationTime.getStart().compareTo(listDeclarationTime.getEnd()) > 0) {
			log.error("save " + listDeclarationTime.toString() + " failure!");
			return ListDeclarationTime.builder().message("Kiểm tra lại ngày tháng!").build();
		}
		UserEntity userEntity = userRepository.findById(usercreate).get();

		if (userEntity.getDeclarationTime() != null) {
			DeclarationTime declarationTime = userEntity.getDeclarationTime();
			if (declarationTime.getStart().compareTo(listDeclarationTime.getStart()) > 0
					|| declarationTime.getEnd().compareTo(listDeclarationTime.getEnd()) < 0) {
				log.error("save " + listDeclarationTime.toString() + " failure!");
				return ListDeclarationTime.builder().message("Kiểm tra lại ngày tháng!").build();
			}
		}
		for (String username : listDeclarationTime.getUsernames()) {
			DeclarationTimeId declarationTimeId = new DeclarationTimeId(username, usercreate);

			DeclarationTime declarationTime = DeclarationTime.builder().start(listDeclarationTime.getStart())
					.end(listDeclarationTime.getEnd()).build();
			declarationTime.setDeclarationTimeId(declarationTimeId);
			declarationTime.setUser(userRepository.findById(username).get());
			declarationTime.setState(false);
			declarationRepository.save(declarationTime);

			log.error("save " + listDeclarationTime.toString() + " successful!");
		}
		listDeclarationTime.setMessage("Thay đổi thời gian khai báo thành công!");
		return listDeclarationTime;
	}

	@Override
	@Transactional
	public DeclarationTimeDto completeDeclaration(String username, boolean state) {
		declarationRepository.changeState(username, state);
		return DeclarationTimeDto.builder().state(state).message("Thay đổi trạng thái thành công!").build();
	}

	@Override
	public List<DeclarationTimeDto> getListDeclarationTimeDtos(String createBy) {
		List<DeclarationTimeDto> declarationTimeDtos = new ArrayList<DeclarationTimeDto>();
		for (DeclarationTime declarationTime : declarationRepository.findByCreateByUsername(createBy)) {
			String locationName = null;
			String username = declarationTime.getUser().getUsername();
			switch (username.length()) {
			case 2:
				locationName = cityRepository.getById(username).getCityName();
				break;
			case 4:
				locationName = districtRepository.getById(username).getDistrictName();
				break;
			case 6:
				locationName = wardRepository.getById(username).getWardName();
				break;
			case 8:
				locationName = villageRepository.getById(username).getVillageName();
				break;
			default:
				break;
			}
			DeclarationTimeDto declarationTimeDto = DeclarationTimeDto.builder().state(declarationTime.isState())
					.locationName(locationName).build();
			declarationTimeDtos.add(declarationTimeDto);
		}
		return declarationTimeDtos;
	}

	@Override
	public Boolean getState(String username) {
		boolean state = false;
		Optional<DeclarationTime> optional = declarationRepository.findByUserUsername(username);
		if (optional.isPresent()) {
			state = optional.get().isState();
		} else {
			state = true;
		}
		return state;
	}

}
