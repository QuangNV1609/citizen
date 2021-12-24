package com.quangnv.uet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quangnv.uet.entites.DeclarationTime;
import com.quangnv.uet.entites.ids.DeclarationTimeId;

@Repository
public interface DeclarationRepository extends JpaRepository<DeclarationTime, DeclarationTimeId> {
	
	public DeclarationTime findByUserUsername(String username);
}
