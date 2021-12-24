package com.quangnv.uet.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.quangnv.uet.entites.PeopleEntity;
import com.quangnv.uet.entites.PeopleLocation;
import com.quangnv.uet.entites.ids.PeopleLocationId;

@Repository
public interface PeopleLocationRepository extends JpaRepository<PeopleLocation, PeopleLocationId> {

	@Query(value = "Select Distinct pl.people From PeopleLocation pl Where pl.village.villageId = :locationId "
			+ "Or pl.village.ward.wardId = :locationId " + "Or pl.village.ward.district.districtId = :locationId "
			+ "Or pl.village.ward.district.city.cityId = :locationId ")
	public List<PeopleEntity> findPeopleByLocationId(@Param("locationId") String locationId, Pageable pageable);

	@Query(value = "Select pl.people From PeopleLocation pl Where (pl.village.villageId = :locationId "
			+ "Or pl.village.ward.wardId = :locationId " + "Or pl.village.ward.district.districtId = :locationId "
			+ "Or pl.village.ward.district.city.cityId = :locationId) And pl.peopleLocationId.locationType = :locationType")
	public List<PeopleEntity> findPeopleByLocationType(@Param("locationId") String locationId,
			@Param("locationType") String locationType, Pageable pageable);

	@Query(value = "Select pl.people From PeopleLocation pl Where pl.peopleLocationId.locationType = :locationType")
	public List<PeopleEntity> findAllByLocationType(@Param("locationType") String locationType, Pageable pageable);

	@Query(value = "Select Distinct pl.people From PeopleLocation pl Where pl.village.villageId In :locationIds "
			+ "Or pl.village.ward.wardId In :locationIds " + "Or pl.village.ward.district.districtId In :locationIds "
			+ "Or pl.village.ward.district.city.cityId In :locationIds")
	public List<PeopleEntity> findPeopleByLocationIds(@Param("locationIds") String[] locationIds, Pageable pageable);

	@Query(value = "Select pl.people From PeopleLocation pl Where (pl.village.villageId In :locationIds "
			+ "Or pl.village.ward.wardId In :locationIds " + "Or pl.village.ward.district.districtId In :locationIds "
			+ "Or pl.village.ward.district.city.cityId In :locationIds) And pl.peopleLocationId.locationType = :locationType")
	public List<PeopleEntity> findPeopleByLocationTypeAndLocationIds(@Param("locationIds") String[] locationIds,
			@Param("locationType") String locationType, Pageable pageable);

	@Query(value = "Select Count(*) From PeopleLocation pl Where (pl.village.villageId = :locationId "
			+ "Or pl.village.ward.wardId = :locationId " + "Or pl.village.ward.district.districtId = :locationId "
			+ "Or pl.village.ward.district.city.cityId = :locationId) "
			+ "And  pl.peopleLocationId.locationType = :locationType")
	public long countPeopleByLocationType(@Param("locationId") String locationId,
			@Param("locationType") String locationType);

	@Query(value = "Select Count(*) From PeopleLocation pl Where pl.peopleLocationId.locationType = :locationType")
	public long countAllByLocationType(@Param("locationType") String locationType);

	@Query(value = "Select Count(Distinct pl.people) From PeopleLocation pl Where pl.village.villageId In :locationIds "
			+ "Or pl.village.ward.wardId In :locationIds " + "Or pl.village.ward.district.districtId In :locationIds "
			+ "Or pl.village.ward.district.city.cityId In :locationIds")
	public long countPeopleByLocationIds(@Param("locationIds") String[] locationIds);

	@Query(value = "Select Count(*) From PeopleLocation pl Where (pl.village.villageId In :locationIds "
			+ "Or pl.village.ward.wardId In :locationIds " + "Or pl.village.ward.district.districtId In :locationIds "
			+ "Or pl.village.ward.district.city.cityId In :locationIds) And pl.peopleLocationId.locationType = :locationType")
	public long countPeopleByLocationTypeAndLocationIds(@Param("locationIds") String[] locationIds,
			@Param("locationType") String locationType);

	@Query(value = "Select Count(Distinct pl.people) From PeopleLocation pl Where pl.village.villageId = :locationId "
			+ "Or pl.village.ward.wardId = :locationId " + "Or pl.village.ward.district.districtId = :locationId "
			+ "Or pl.village.ward.district.city.cityId = :locationId ")
	public long countPeopleByLocationId(@Param("locationId") String locationId);
}
