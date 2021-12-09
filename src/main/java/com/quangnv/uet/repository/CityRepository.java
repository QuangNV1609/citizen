package com.quangnv.uet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quangnv.uet.entites.CityEntity;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Integer>{

}
