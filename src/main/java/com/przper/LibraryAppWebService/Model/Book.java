package com.przper.LibraryAppWebService.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "books")
public class Book extends RepresentationModel<Book> {
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Integer bookId;
	private String authorFirstName;
	private String authorLastName;
	private String title;
	private @ManyToOne() @JsonIgnore User holder;

	public Book() {
	}

	public Book(String fName, String lName, String title) {
		this.authorFirstName = fName;
		this.authorLastName = lName;
		this.title = title;
	}

	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public String getAuthorFirstName() {
		return authorFirstName;
	}

	public void setAuthorFirstName(String authorFirstName) {
		this.authorFirstName = authorFirstName;
	}

	public String getAuthorLastName() {
		return authorLastName;
	}

	public void setAuthorLastName(String authorLastName) {
		this.authorLastName = authorLastName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getHolder() {
		return holder;
	}

	public void setHolder(User holder) {
		this.holder = holder;
	}

	@Override
	public String toString() {
		return "Book [authorFirstName=" + authorFirstName + ", authorLastName=" + authorLastName + ", title=" + title
				+ ", holder=" + holder + "]";
	}

}
