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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.przper.LibraryAppWebService.Model.Book;
import com.przper.LibraryAppWebService.Model.User;
import com.przper.LibraryAppWebService.Exceptions.BookIsBorrowedException;
import com.przper.LibraryAppWebService.Exceptions.UserNotFoundException;

@RestController
public class UserBookController {
	@Autowired
	UserRepository userRepository;
	@Autowired
	BookRepository bookRepository;

	@GetMapping("/users/{userId}/books")
	public CollectionModel<Book> allBooksOfUser(@PathVariable Integer userId) {
		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			throw new UserNotFoundException("id: " + userId);
		}
		List<Book> usersListOfBorrowedBooks = user.get().getBorrowedBooks();

		usersListOfBorrowedBooks.forEach(book -> book
				.add(linkTo(UserBookController.class).slash("books").slash(book.getBookId()).withSelfRel()));

		Link link = linkTo(UserBookController.class).slash("users").slash(user.get().getUserId()).slash("books")
				.withSelfRel();
		CollectionModel<Book> model = new CollectionModel<>(usersListOfBorrowedBooks, link);

		return model;
	}

	@PostMapping("books/{bookId}/user")
	public ResponseEntity<Object> borrowBook(@PathVariable Integer bookId, @RequestBody Integer userId) {
		Optional<Book> book = bookRepository.findById(bookId);
		if (!book.isPresent()) {
			throw new UserNotFoundException("id: " + bookId);
		}
		if (book.get().getHolder() != null) {
			throw new BookIsBorrowedException("Book is already borrowed.");
		}
		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			throw new UserNotFoundException("id: " + userId);
		}

		book.get().setHolder(user.get());
		bookRepository.save(book.get());

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();

		return ResponseEntity.created(location).build();
	}

	@GetMapping("books/{bookId}/user")
	public User showBookHolder(@PathVariable Integer bookId) {
		Optional<Book> book = bookRepository.findById(bookId);
		if (!book.isPresent()) {
			throw new UserNotFoundException("id: " + bookId);
		}
		User user = book.get().getHolder();

		return user;
	}

	@DeleteMapping("books/{bookId}/user")
	public void returnBook(@PathVariable Integer bookId) {
		Optional<Book> book = bookRepository.findById(bookId);
		if (!book.isPresent()) {
			throw new UserNotFoundException("id: " + bookId);
		}
		User user = book.get().getHolder();
		book.get().setHolder(null);
		bookRepository.save(book.get());
	}

}
