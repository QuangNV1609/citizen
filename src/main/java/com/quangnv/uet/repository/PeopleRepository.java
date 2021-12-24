package com.quangnv.uet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quangnv.uet.entites.PeopleEntity;

@Repository
public interface PeopleRepository extends JpaRepository<PeopleEntity, String> {
	
}
