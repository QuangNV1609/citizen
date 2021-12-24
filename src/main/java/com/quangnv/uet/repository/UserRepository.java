package com.quangnv.uet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.quangnv.uet.entites.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
	public boolean existsByCreateByUsername(String username);

	@Query("Select u.userRole From UserEntity u Where u.username = :username")
	public String getUserRole(@Param("username") String username);

	@Query(value = "SELECT * FROM user_entity WHERE LENGTH( user_entity.username ) = 2", nativeQuery = true)
	public List<UserEntity> findUserCity();

	@Query("Select u From UserEntity u Where u.createBy.username = :createBy")
	public List<UserEntity> findUserLocation(@Param("createBy") String createBy);

	@Query("Select u From UserEntity u Where u.createBy.username = :username")
	public List<UserEntity> findUserByCreateBy(@Param("username") String username);

	@Modifying
	@Query("Update UserEntity u Set u.enable = false Where u.username Like :username%")
	public void disable(@Param("username") String username);

	@Modifying
	@Query("Update UserEntity u Set u.enable = true Where u.username Like :username%")
	public void enable(@Param("username") String username);

	@Query(value = "Select user_entity.username From user_entity Where user_entity.create_by = :createBy", nativeQuery = true)
	public String[] getUserIdByCreateBy(@Param("createBy") String createBy);

}
