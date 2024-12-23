package com.library.library_project.repository;

import com.library.library_project.model.Book;
import com.library.library_project.model.BookType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByTitle(String title);
    List<Book> findByTitleContaining(String title);
    List<Book> findByBookType(BookType bookType);
}
