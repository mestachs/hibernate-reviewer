package com.mestach.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class BadSoldier {
	@Id
	@GeneratedValue
	private long id;
	
	private BadTroop troop;
    @ManyToOne(targetEntity=BadTroop.class)
    @JoinColumn(name="troop_fk")
    public BadTroop getTroop() {
    	return troop;
    }
} 
