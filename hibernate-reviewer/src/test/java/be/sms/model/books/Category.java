package be.sms.model.books;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.*;

@Entity

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
}
