package com.mestach.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class BadTroop {

	@Id
	@GeneratedValue
	private long id;
	
	@OneToMany(targetEntity=BadSoldier.class,mappedBy = "troop")
	Set<BadSoldier> soldiers;

	
	public Set<BadSoldier> getSoldiers() {
		return soldiers;
	}

}
