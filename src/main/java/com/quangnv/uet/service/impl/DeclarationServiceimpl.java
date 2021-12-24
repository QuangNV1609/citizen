package com.quangnv.uet.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quangnv.uet.dto.DeclarationTimeDto;
import com.quangnv.uet.dto.ListDeclarationTime;
import com.quangnv.uet.entites.DeclarationTime;
import com.quangnv.uet.entites.UserEntity;
import com.quangnv.uet.entites.ids.DeclarationTimeId;
import com.quangnv.uet.repository.DeclarationRepository;
import com.quangnv.uet.repository.UserRepository;
import com.quangnv.uet.service.DeclarationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DeclarationServiceimpl implements DeclarationService {
	@Autowired
	private DeclarationRepository declarationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

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
			if (declarationTime.getStart().compareTo(declarationTimeDto.getStart()) < 0
					|| declarationTime.getEnd().compareTo(declarationTimeDto.getEnd()) > 0) {
				log.error("save " + declarationTimeDto.toString() + " failure!");
				return DeclarationTimeDto.builder().message("Kiểm tra lại ngày tháng!").build();
			}
		}

		DeclarationTimeId declarationTimeId = new DeclarationTimeId(declarationTimeDto.getUsername(), usercreate);
		DeclarationTime declarationTime = modelMapper.map(declarationTimeDto, DeclarationTime.class);
		declarationTime.setDeclarationTimeId(declarationTimeId);
		declarationTime.setUser(userEntity);

		declarationTime = declarationRepository.save(declarationTime);
		declarationTimeDto.setMessage("Thay đổi thời gian khai báo thành công!");

		log.info("save " + declarationTimeDto.toString() + " successful!");

		return modelMapper.map(declarationTime, DeclarationTimeDto.class);
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
			declarationRepository.save(declarationTime);

			log.error("save " + listDeclarationTime.toString() + " successful!");
		}
		listDeclarationTime.setMessage("Thay đổi thời gian khai báo thành công!");
		return listDeclarationTime;
	}

}
