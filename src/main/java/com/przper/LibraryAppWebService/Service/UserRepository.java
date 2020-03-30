package com.przper.LibraryAppWebService.Service;

import org.springframework.data.jpa.repository.JpaRepository;
import com.przper.LibraryAppWebService.Model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
