package com.library.library_project.dto;

import com.library.library_project.model.Author;
import com.library.library_project.model.Book;
import com.library.library_project.model.BookType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BookRequest {
    @NotBlank(message = "Book title cannot be blank")
    private String bookTitle;

    @NotBlank(message = "Book Number cannot be blank")
    private String bookNo;

    @NotBlank(message = "Author Name cannot be blank")
    private String authorName;

    @NotBlank(message = "Author Email cannot be blank")
    private String authorEmail;

    @NotNull(message = "Book type cannot be blank")
    private BookType type;

    @Positive(message = "security Amount should be positive")
    private int securityAmount;

    public Author toAuthor() {
        return Author
                .builder()
                .email(this.authorEmail)
                .name(this.authorName)
                .build();
    }

    public Book toBook() {
        return Book
                .builder()
                .bookNo(this.bookNo)
                .title(this.bookTitle)
                .bookType(this.type)
                .securityAmount(this.securityAmount)
                .build();
    }
}
