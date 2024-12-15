package com.library.library_project.service;

import com.library.library_project.dto.BookRequest;
import com.library.library_project.model.Author;
import com.library.library_project.model.Book;
import com.library.library_project.repository.AuthorRepository;
import com.library.library_project.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    public Book addBook(BookRequest bookRequest) {
        Author authorFromDB = authorRepository.getAuthorByEmail(bookRequest.getAuthorEmail());
        if(authorFromDB == null) {
            authorFromDB = authorRepository.save(bookRequest.toAuthor());
        }
        Book book = bookRequest.toBook();
        book.setAuthor(authorFromDB);
        return bookRepository.save(book);
    }
}
