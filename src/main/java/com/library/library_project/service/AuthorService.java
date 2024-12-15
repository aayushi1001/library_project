package com.library.library_project.service;

import com.library.library_project.model.Author;
import com.library.library_project.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    public Author getAuthorData(String email) {
        return authorRepository.getAuthorByEmail(email);
    }
}
