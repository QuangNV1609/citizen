package com.quangnv.uet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.quangnv.uet.entites.WardEntity;

@Repository
public interface WardRepository extends JpaRepository<WardEntity, String> {
	
	@Query("Select w From WardEntity w Where w.wardId In :wardIds")
	public List<WardEntity> findWardByWardIds(@Param("wardIds") String[] wardIds);
}
