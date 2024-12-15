package com.library.library_project.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30)
    private String title;

    //We can have n no. of books with same title
    // primary key
    @Column(length = 20, unique = true)
    private String bookNo;

    @Enumerated(value = EnumType.STRING)
    private BookType bookType;

    @CreationTimestamp(source = SourceType.DB)
    private Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;

    //Will create a new column inside book table and will store user id
    @ManyToOne
    @JoinColumn // it will create a foreign key and will store the @Id marked value
    private User user;

    @ManyToOne
    @JoinColumn
    private Author author;

    @OneToMany(mappedBy = "book")
    private List<Txn> txnList;

    private Integer securityAmount;
}
