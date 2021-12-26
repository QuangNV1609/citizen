package com.quangnv.uet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.quangnv.uet.entites.DeclarationTime;
import com.quangnv.uet.entites.ids.DeclarationTimeId;

@Repository
public interface DeclarationRepository extends JpaRepository<DeclarationTime, DeclarationTimeId> {

	public List<DeclarationTime> findByCreateByUsername(String createBy);

	public Optional<DeclarationTime> findByUserUsername(String username);

	@Modifying
	@Query(value = "UPDATE declaration_time " + "SET declaration_time.state = :state "
			+ "WHERE declaration_time.user = :username "
			+ "OR (declaration_time.create_by = :username AND Length(declaration_time.create_by) = 6)", nativeQuery = true)
	public void changeState(@Param("username") String username, @Param("state") Boolean state);
}
