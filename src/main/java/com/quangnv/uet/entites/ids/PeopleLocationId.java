package com.quangnv.uet.entites.ids;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeopleLocationId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "people_id")
	private String peopleId;

	@Column(name = "location_id")
	private String locationId;
	
	@Column(name = "location_type", columnDefinition = "nvarchar(30)")
	@EqualsAndHashCode.Exclude
	private String locationType;
}
