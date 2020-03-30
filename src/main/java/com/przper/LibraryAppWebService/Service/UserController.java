package com.przper.LibraryAppWebService.Service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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

import com.przper.LibraryAppWebService.Exceptions.UserNotFoundException;
import com.przper.LibraryAppWebService.Model.User;

@RestController
public class UserController {
	@Autowired
	UserRepository userRepository;

	@PostMapping("/users")
	public ResponseEntity<Object> createUser(@RequestBody User user) {
		User savedUser = userRepository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedUser.getUserId()).toUri();

		return ResponseEntity.created(location).build();
	}

	@GetMapping("/users")
	public CollectionModel<User> allUsers() {
		List<User> allUsers = userRepository.findAll();
		allUsers.forEach(
				user -> user.add(linkTo(UserController.class).slash("users").slash(user.getUserId()).withSelfRel()));
		allUsers.forEach(user -> user.add(linkTo(UserController.class).slash("users").slash(user.getUserId())
				.slash("books").withRel("user's books")));

		Link link = linkTo(UserController.class).slash("users").withSelfRel();
		CollectionModel<User> model = new CollectionModel<>(allUsers, link);

		return model;
	}

	@GetMapping("/users/{id}")
	public Optional<User> oneUserById(@PathVariable Integer id) {
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {
			throw new UserNotFoundException("id: " + id);
		}
		
		user.get().add(linkTo(UserController.class).slash("users").slash(id).withSelfRel());
		user.get().add(linkTo(UserBookController.class).slash("users").slash(user.get().getUserId()).slash("books").withRel("user's books"));
		//Link link = linkTo(UserController.class).slash("users").slash(id).withSelfRel();
		//EntityModel<User> model = new EntityModel<>(user.get(), link);
		return user;
	}

	@PutMapping("/users/{id}")
	public void updateUser(@PathVariable Integer id, @RequestBody User user) {
		Optional<User> userToBeUpdated = userRepository.findById(id);
		if (!userToBeUpdated.isPresent()) {
			throw new UserNotFoundException("id: " + id);
		}
		userToBeUpdated.get().setFirstName(user.getFirstName());
		userToBeUpdated.get().setLastName(user.getLastName());
		userRepository.save(userToBeUpdated.get());
	}

	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable int id) {
		userRepository.deleteById(id);
	}
}
