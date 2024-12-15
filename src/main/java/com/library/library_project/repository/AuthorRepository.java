package com.library.library_project.repository;

import com.library.library_project.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    //native query - simple SQL
    @Query(value = "Select * from author where email = :email", nativeQuery = true)
    Author getAuthorByEmail(String email);
}
