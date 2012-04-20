package com.mestach.model.books;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.mestach.model.BadSoldier;


@Entity
public class BadBook {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToMany(fetch = FetchType.EAGER)
	@BatchSize(size = 40)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<BadAuthor> authors;
	private String title;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	Set<BadSoldier> soldiers;

	@OneToMany(targetEntity = BadSoldier.class, mappedBy = "troop")
	public Set<BadSoldier> getSoldiers() {
		return soldiers;
	}

	@Override
	public String toString() {
		return title;
	}
}