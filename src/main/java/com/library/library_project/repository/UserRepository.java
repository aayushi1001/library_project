package com.library.library_project.repository;

import com.library.library_project.model.User;
import com.library.library_project.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByPhoneNoAndUserType(String phoneNo, UserType type);
}
