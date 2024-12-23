package com.library.library_project.service;

import com.library.library_project.dto.BookRequest;
import com.library.library_project.model.*;
import com.library.library_project.repository.AuthorRepository;
import com.library.library_project.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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


    public List<Book> filter(FilterType filterType, Operator operator, String value) {
        switch (filterType) {
            case BOOK_TITLE:
                return switch (operator) {
                    case EQUALS -> bookRepository.findByTitle(value);
                    case LIKE -> bookRepository.findByTitleContaining(value);
                    default -> new ArrayList<>();
                };
            case BOOK_TYPE:
                return switch (operator) {
                    case EQUALS -> bookRepository.findByBookType(BookType.valueOf(value));
                    case LIKE -> bookRepository.findByTitleContaining(value);
                    default -> new ArrayList<>();
                };
            case BOOK_NO:
                return switch (operator) {
                    case EQUALS -> bookRepository.findByBookNo(value);
                    default -> new ArrayList<>();
                };
            default: new ArrayList<>();
        }
        return new ArrayList<>();
    }

    public void updateBookData(Book book) {
        bookRepository.save(book);
    }
}
