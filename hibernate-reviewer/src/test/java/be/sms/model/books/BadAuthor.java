package be.sms.model.books;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Version;

@Entity
public class BadAuthor {
	@Id
	@GeneratedValue
	private long id;

	private String name;

	@ManyToMany(mappedBy = "authors")
	private Set<BadBook> books;

	@Version
	private int version;

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<BadBook> getBooks() {
		return books;
	}

	public void addBook(BadBook book) {
		if (books == null)
			books = new HashSet<BadBook>();
		books.add(book);
	}

	public int getVersion() {
		return version;
	}
}
