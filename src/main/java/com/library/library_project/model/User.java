package com.library.library_project.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30)
    private String name;

    @Column(nullable = false, unique = true)
    private String phoneNo;

    @Column(unique = true, length = 50)
    private String email;

    private String address;

    @CreationTimestamp
    private Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;

    @Enumerated(value = EnumType.STRING)
    private UserType userType;

    @Enumerated
    private UserStatus userStatus;

    //Whenever you create a one to many relationship,
    // you need to create a many to one relationship in the other class also.
    @OneToMany(mappedBy = "user")
    private List<Book> bookList;

    @OneToMany(mappedBy = "user")
    private List<Txn> txnList;
}
