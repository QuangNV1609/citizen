package com.quangnv.uet.entites;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.quangnv.uet.entites.ids.PeopleLocationId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PeopleLocation {

	@EmbeddedId
	private PeopleLocationId peopleLocationId;

	@ManyToOne
	@MapsId("peopleId")
	@JoinColumn(name = "people_id")
	@EqualsAndHashCode.Exclude
	private PeopleEntity people;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("locationId")
	@JoinColumn(name = "location_id")
	@EqualsAndHashCode.Exclude
	private VillageEntity village;

}
