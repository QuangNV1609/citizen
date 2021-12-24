package com.quangnv.uet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.quangnv.uet.entites.CityEntity;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, String> {
	
	@Query("Select c From CityEntity c Where c.cityId In :cityIds")
	public List<CityEntity> findCityByCityIds(@Param("cityIds") String[] cityIds);
}
