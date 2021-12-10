package com.quangnv.uet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quangnv.uet.entites.DistrictEntity;

@Repository
public interface DistrictRepository extends JpaRepository<DistrictEntity, String> {

}
