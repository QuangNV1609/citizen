package com.quangnv.uet.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quangnv.uet.dto.UserDto;
import com.quangnv.uet.entites.CityEntity;
import com.quangnv.uet.entites.DeclarationTime;
import com.quangnv.uet.entites.DistrictEntity;
import com.quangnv.uet.entites.UserEntity;
import com.quangnv.uet.entites.VillageEntity;
import com.quangnv.uet.entites.WardEntity;
import com.quangnv.uet.repository.CityRepository;
import com.quangnv.uet.repository.DistrictRepository;
import com.quangnv.uet.repository.UserRepository;
import com.quangnv.uet.repository.VillageRepository;
import com.quangnv.uet.repository.WardRepository;
import com.quangnv.uet.service.UserSevice;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserSevice {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private VillageRepository villageRepository;

	@Autowired
	private WardRepository wardRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserEntity> optional = userRepository.findById(username);

		if (!optional.isPresent()) {
			throw new UsernameNotFoundException(username + " not found!");
		} else if (!optional.get().isEnable()) {
			throw new UsernameNotFoundException(username + " is disable");
		}

		UserEntity userEntity = optional.get();
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		authorities.add(new SimpleGrantedAuthority(userEntity.getUserRole()));

		return new User(userEntity.getUsername(), userEntity.getPassword(), authorities);
	}

	@Override
	public UserDto saveUser(UserDto userDto, String username) {
		UserEntity userEntityAdmin = userRepository.findById(username).get();
		UserEntity newUserEntity = modelMapper.map(userDto, UserEntity.class);

		if (userEntityAdmin.getUserRole().equals("ROLE_ADMIN")) {
			newUserEntity.setUserRole("ROLE_A1");
		} else if (userEntityAdmin.getUserRole().equals("ROLE_A1")) {
			CityEntity cityEntity = CityEntity.builder().cityId(userDto.getUsername()).cityName(userDto.getLocation())
					.build();
			cityRepository.save(cityEntity);
			newUserEntity.setUserRole("ROLE_A2");
		} else if (userEntityAdmin.getUserRole().equals("ROLE_A2")) {
			DistrictEntity districtEntity = DistrictEntity.builder().districtId(userDto.getUsername())
					.districtName(userDto.getLocation()).build();
			districtEntity.setCity(cityRepository.findById(username).get());
			districtRepository.save(districtEntity);
			newUserEntity.setUserRole("ROLE_A3");
		} else if (userEntityAdmin.getUserRole().equals("ROLE_A3")) {
			WardEntity wardEntity = WardEntity.builder().wardId(userDto.getUsername()).wardName(userDto.getLocation())
					.build();
			wardEntity.setDistrict(districtRepository.findById(username).get());
			wardRepository.save(wardEntity);
			newUserEntity.setUserRole("ROLE_B1");
		} else if (userEntityAdmin.getUserRole().equals("ROLE_B1")) {
			VillageEntity villageEntity = VillageEntity.builder().villageName(userDto.getLocation())
					.villageId(userDto.getUsername()).build();
			villageEntity.setWard(wardRepository.findById(username).get());
			villageRepository.save(villageEntity);
			newUserEntity.setUserRole("ROLE_B2");
		}
		if (userDto.getState().equals("Active")) {
			newUserEntity.setEnable(true);
		} else if (userDto.getState().equals("InActive")) {
			newUserEntity.setEnable(false);
		}

		log.info(newUserEntity.toString());

		newUserEntity.setPassword(passwordEncoder.encode(newUserEntity.getPassword()));
		newUserEntity = userRepository.save(newUserEntity);

		return modelMapper.map(newUserEntity, UserDto.class);
	}

	@Override
	public UserDto saveAdmin(UserDto userDto) {
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
		userEntity.setUserRole("ROLE_A1");
		userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
		userRepository.save(userEntity);
		return modelMapper.map(userEntity, UserDto.class);
	}

	@Override
	@Transactional
	public String disable(String username, Boolean enable) {
		String message = null;

		if (enable) {
			userRepository.enable(username);
			message = "Kết nối thành công!";
		} else {
			userRepository.disable(username);
			message = "Khóa thành công!";
		}

		return message;
	}

	@Override
	public String changePassword(UserDto userDto) {
		Optional<UserEntity> optional = userRepository.findById(userDto.getUsername());
		if (optional.isPresent()) {
			UserEntity userEntity = optional.get();
			userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
			userRepository.save(userEntity);
			return "Đổi mật khẩu thành công!";
		} else {
			throw new UsernameNotFoundException(userDto.getUsername() + " not found!");
		}
	}

	@Override
	public List<UserDto> getUserByCreateBy(String userId) {
		List<UserDto> userDtos = new ArrayList<UserDto>();
		List<UserEntity> userEntities = userRepository.findUserByCreateBy(userId);
		for (UserEntity userEntity : userEntities) {
			UserDto userDto = modelMapper.map(userEntity, UserDto.class);
			DeclarationTime declarationTime = userEntity.getDeclarationTime();
			if (declarationTime != null) {
				userDto.setStart(declarationTime.getStart());
				userDto.setEnd(declarationTime.getEnd());
			}
			userDto.setLocation(getUserLocation(userEntity.getUsername()));
			userDto.setPassword(null);

			if (userDto.isEnable()) {
				userDto.setState("Active");
			} else {
				userDto.setState("InActive");
			}
			userDtos.add(userDto);
		}
		return userDtos;
	}

	@Override
	public UserDto getUserUserName(String username) {
		UserEntity userEntity = userRepository.findById(username).get();
		userEntity.setPassword(null);
		DeclarationTime declarationTime = userEntity.getDeclarationTime();
		UserDto userDto = modelMapper.map(userEntity, UserDto.class);
		if (declarationTime != null) {
			userDto.setStart(declarationTime.getStart());
			userDto.setEnd(declarationTime.getEnd());
		}
		userDto.setLocation(getUserLocationDetails(userEntity.getUsername()));
		userDto.setPassword(null);
		return userDto;
	}

	@Override
	@Transactional
	public String disableList(String createBy, List<String> usernames, String enable) {
		String message = null;
		for (String username : usernames) {
			if (userRepository.existsByCreateByUsername(createBy)) {
				if (enable.equalsIgnoreCase("Active")) {
					userRepository.enable(username);
					message = "Kết nối thành công!";
				} else {
					userRepository.disable(username);
					message = "Khóa thành công!";
				}
			}
		}
		return message;
	}

	private String getUserLocation(String userId) {
		String location = null;
		int length = userId.length();
		switch (length) {
		case 2:
			location = cityRepository.findById(userId).get().getCityName();
			break;
		case 4:
			location = districtRepository.findById(userId).get().getDistrictName();
			break;
		case 6:
			location = wardRepository.findById(userId).get().getWardName();
			break;
		case 8:
			location = villageRepository.findById(userId).get().getVillageName();
			break;
		default:
			break;
		}
		return location;
	}

	private String getUserLocationDetails(String userId) {
		String location = null;
		int length = userId.length();
		switch (length) {
		case 2:
			location = cityRepository.findById(userId).get().getCityName();
			break;
		case 4:
			DistrictEntity districtEntity = districtRepository.findById(userId).get();
			location = districtEntity.getDistrictName() + ", " + districtEntity.getCity().getCityName();
			break;
		case 6:
			WardEntity wardEntity = wardRepository.findById(userId).get();
			location = wardEntity.getWardName() + ", " + wardEntity.getDistrict().getDistrictName() + ", "
					+ wardEntity.getDistrict().getCity().getCityName();
			break;
		case 8:
			VillageEntity villageEntity = villageRepository.findById(userId).get();
			location = villageEntity.getVillageName() + ", " + villageEntity.getWard().getWardName() + ", "
					+ villageEntity.getWard().getDistrict().getDistrictName() + ", "
					+ villageEntity.getWard().getDistrict().getCity().getCityName();
			break;
		default:
			break;
		}
		return location;
	}

}
