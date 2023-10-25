package com.example.login.database;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface UserDAO extends CrudRepository<User, Integer> {

    List<User> findAllByEmail(String email);
}
