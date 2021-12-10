package com.quangnv.uet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quangnv.uet.entites.WardEntity;

@Repository
public interface WardRepository extends JpaRepository<WardEntity, String> {

}
