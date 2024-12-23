package com.library.library_project.service;

import com.library.library_project.dto.TxnRequest;
import com.library.library_project.exception.TxnException;
import com.library.library_project.model.*;
import com.library.library_project.repository.TxnRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TxnService {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private TxnRepository txnRepository;

    @Value("${book.valid.upto}")
    private int validDays;

    @Value("${book.fine.amount.per.day}")
    private int finePerDay;

    //We don't need transactional till line no. 47
    //So, we can create a different method for that
//    @Transactional(rollbackOn = {TxnException.class})
//    public String create(TxnRequest txnRequest) throws TxnException {
//        // For a transaction to occur,
//        // 1. it should come from a valid user
//        User userFromDb = userService.getStudentByPhoneNo(txnRequest.getUserPhoneNo());
//        if (userFromDb == null) {
//            throw new TxnException("Student doesn't belong to my library");
//        }
//
//        // 2. Book exists or not
//        List<Book> books = bookService.filter(FilterType.BOOK_NO, Operator.EQUALS, txnRequest.getBookNo());
//        if(books.isEmpty()) {
//            throw new TxnException("Book doesn't belong to my library");
//        }
//
//        // 3. Book should not be taken by other student
//        Book bookFromDb = books.get(0);
//        if (bookFromDb.getUser() != null) {
//            throw new TxnException("This book is taken by other user");
//        }
//
//        String txnId = UUID.randomUUID().toString();
//        Txn txn = Txn
//                .builder()
//                .txnId(txnId)
//                .user(userFromDb)
//                .book(bookFromDb)
//                .txnStatus(TxnStatus.ISSUED)
//                .build();
//
//        txnRepository.save(txn);
//        bookFromDb.setUser(userFromDb);
//        bookService.updateBookData(bookFromDb);
//
//        return txnId;
//    }

    public User getUserFromDb(TxnRequest txnRequest) throws TxnException{
        User userFromDb = userService.getStudentByPhoneNo(txnRequest.getUserPhoneNo());
        if (userFromDb == null) {
            throw new TxnException("Student doesn't belong to my library");
        }
        return userFromDb;
    }

    public Book getBookFromDb(TxnRequest txnRequest) throws TxnException {
        List<Book> books = bookService.filter(FilterType.BOOK_NO, Operator.EQUALS, txnRequest.getBookNo());
        if(books.isEmpty()) {
            throw new TxnException("Book doesn't belong to my library");
        }
        return books.get(0);
    }

    // If txn got saved but exception thrown from user, it should roll back everything
    // Otherwise, book is taken, but it won't show up in book table
    @Transactional(rollbackOn = {TxnException.class})
    public String createTransaction(User userFromDb, Book bookFromDb) {
        String txnId = UUID.randomUUID().toString();
        Txn txn = Txn
                .builder()
                .txnId(txnId)
                .user(userFromDb)
                .book(bookFromDb)
                .txnStatus(TxnStatus.ISSUED)
                .build();

        txnRepository.save(txn);
        bookFromDb.setUser(userFromDb);
        bookService.updateBookData(bookFromDb);

        return txnId;
    }

    public String create(TxnRequest txnRequest) throws TxnException {
        User userFromDb = getUserFromDb(txnRequest);
        Book bookFromDb = getBookFromDb(txnRequest);
        if (bookFromDb.getUser() != null) {
            throw new TxnException("This book is taken by other user");
        }
        return createTransaction(userFromDb, bookFromDb);
    }

    private int calculateFine(Txn txn, int securityAmount) {
        long issueDate = txn.getCreatedOn().getTime();
        long returnDate = System.currentTimeMillis();
        long timeDiff = returnDate - issueDate;
        int daysPassed = (int) TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
        if(daysPassed > validDays) {
            int fineAmount = (daysPassed - validDays)*finePerDay;
            return securityAmount - fineAmount;
        }
        return securityAmount;
    }

    @Transactional(rollbackOn = {TxnException.class})
    public int returnBook(TxnRequest txnRequest) throws TxnException {
        User userFromDb = getUserFromDb(txnRequest);
        Book bookFromDb = getBookFromDb(txnRequest);
        if(bookFromDb.getUser() != userFromDb) {
            throw new TxnException("Book is not assigned to this user");
        }
        Txn txn = txnRepository.findByUserPhoneNoAndBookBookNoAndTxnStatus(txnRequest.getUserPhoneNo(), txnRequest.getBookNo(), TxnStatus.ISSUED);
        int fine = calculateFine(txn, bookFromDb.getSecurityAmount());
        if(fine == bookFromDb.getSecurityAmount()) {
            txn.setTxnStatus(TxnStatus.RETURNED);
        } else {
            txn.setTxnStatus(TxnStatus.FINED);
        }
        txn.setSettlementAmount(fine);
        bookFromDb.setUser(null);
        bookService.updateBookData(bookFromDb);
        return fine;
    }
}
