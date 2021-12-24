package com.quangnv.uet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quangnv.uet.entites.VillageEntity;

@Repository
public interface VillageRepository extends JpaRepository<VillageEntity, String> {
	

}
