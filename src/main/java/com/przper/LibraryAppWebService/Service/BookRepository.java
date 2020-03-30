package com.przper.LibraryAppWebService.Service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.przper.LibraryAppWebService.Model.Book;


public interface BookRepository extends JpaRepository<Book, Integer>{
	
}
