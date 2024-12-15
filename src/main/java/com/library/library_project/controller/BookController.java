package com.library.library_project.controller;

import com.library.library_project.dto.BookRequest;
import com.library.library_project.model.Book;
import com.library.library_project.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
public class BookController {

    //Responsibilities of controller
    //1. validations before business logic
    //2. to call business logic (should be present inside your service)
    //3. return the expected data - the return type from service can be different
    //   from your controller's return type

    @Autowired
    private BookService bookService;

    //Need to add @Valid for validation (added through library) to work
    @PostMapping("/addBook")
    public Book addBook(@RequestBody @Valid BookRequest bookRequest) {
        Book book = bookService.addBook(bookRequest);
        return book;
    }
}

//1. create a row in book table
//2. insert information related to author who has written this book
//   If author is already present because he wrote some other book also
//   Don't insert it again
