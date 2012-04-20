package com.mestach.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Child {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int id;

	@Column(name = "code")
	public String code;
}
