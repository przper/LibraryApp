package com.przper.LibraryAppWebService.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class BookIsBorrowedException extends RuntimeException {
	public BookIsBorrowedException(String message) {
		super(message);
	}
}
