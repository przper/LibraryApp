package com.przper.LibraryAppWebService.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.przper.LibraryAppWebService.Model.Book;
import com.przper.LibraryAppWebService.Model.User;
import com.przper.LibraryAppWebService.Exceptions.BookNotFoundException;

@RestController
public class BookController {
	@Autowired
	BookRepository bookRepository;

	@PostMapping("/books")
	public ResponseEntity<Object> createBook(@RequestBody Book book) {
		Book savedBook = bookRepository.save(book);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedBook.getBookId()).toUri();

		return ResponseEntity.created(location).build();
	}

	@GetMapping("/books")
	public CollectionModel<Book> allBooks() {
		List<Book> allBooks = bookRepository.findAll();
		allBooks.forEach(
				book -> book.add(linkTo(BookController.class).slash("books").slash(book.getBookId()).withSelfRel()));
		allBooks.forEach(
				book -> book.add(linkTo(BookController.class).slash("books").slash(book.getBookId()).slash("user").withRel("current_holder")));

		Link link = linkTo(UserController.class).slash("books").withSelfRel();
		CollectionModel<Book> model = new CollectionModel<>(allBooks, link);

		return model;
	}

	@GetMapping("/books/{id}")
	public Optional<Book> oneBookById(@PathVariable Integer id) {
		Optional<Book> book = bookRepository.findById(id);
		if (!book.isPresent()) {
			throw new BookNotFoundException("id: " + id);
		}
		book.get().add(linkTo(BookController.class).slash("books").slash(book.get().getBookId()).withSelfRel());
		book.get().add(linkTo(BookController.class).slash("books").slash(book.get().getBookId()).slash("user").withRel("current_holder"));
		
		return book;
	}

	@PutMapping("/books/{id}")
	public void updateBook(@PathVariable Integer id, @RequestBody Book book) {
		Optional<Book> bookToBeUpdated = bookRepository.findById(id);
		if (!bookToBeUpdated.isPresent()) {
			throw new BookNotFoundException("id: " + id);
		}
		bookToBeUpdated.get().setAuthorFirstName(book.getAuthorFirstName());
		bookToBeUpdated.get().setAuthorLastName(book.getAuthorLastName());
		bookToBeUpdated.get().setTitle(book.getTitle());
		bookRepository.save(bookToBeUpdated.get());
	}

	@DeleteMapping("/books/{id}")
	public void deleteUser(@PathVariable int id) {
		bookRepository.deleteById(id);
	}
}
