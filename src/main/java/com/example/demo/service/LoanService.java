package com.example.demo.service;

import com.example.demo.entity.Loan;
import com.example.demo.repository.LoanRepository;
import com.example.demo.service.exception.DataBaseException;
import com.example.demo.service.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;

    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    public Loan findById(Long bookId, Long userId) {
        return loanRepository.findByBookIdAndUserId(userId,bookId).orElseThrow(() -> new ResourceNotFoundException(userId));
    }

    public Loan findById(Long loanId) {
        return loanRepository.findById(loanId).orElseThrow(() -> new ResourceNotFoundException(loanId));
    }

    @Transactional
    public Loan insert(Long userId, Long bookId) {
        Loan loan = new Loan(userService.findById(userId), bookService.findById(bookId));
        loanRepository.save(loan);
        Instant deadLine = loan.getCreationTime().plus(15, ChronoUnit.DAYS);
        loan.setDeadline(deadLine);
        return loanRepository.save(loan);
    }

    @Transactional
    public Loan update(Long bookId, Long userId) {
        if (!loanRepository.existsById(userId)) {
            throw new ResourceNotFoundException(userId);
        }
        Loan loan = findById(bookId,userId);
        Instant now = Instant.now();
        if (now.isAfter(loan.getDeadline()) && !loan.isPayed()) {
            long daysLated = ChronoUnit.DAYS.between(loan.getDeadline(), now);
            loan.setOweAcumulated(loan.getOweAcumulated() + daysLated * loan.getBook().getStock().getPrice()/12);
        }
        return loanRepository.save(loan);
    }

    @Transactional
    public void delete(Long bookId, Long userId) {
        try {
            if (!loanRepository.existsById(userId)) {
                throw new ResourceNotFoundException(userId);
            }
            Loan loan = findById(userId, bookId);
            if (loan.isPayed()) {
                loanRepository.delete(loan);
            }else {
                throw new DataBaseException("You can't delete this loan, the book need to be returned or there are still owed payments to be made.");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException(e.getMessage());
        }
    }
    public void returnBookOrPayOwe(Long bookId, Long userId) {
        if(!loanRepository.existsById(userId)) {
            throw new ResourceNotFoundException(userId);
        }
        Loan loan = findById(userId, bookId);
        Instant now = Instant.now();
        if(now.isAfter(loan.getDeadline())) {
            update(loan.getBook().getId(),loan.getUser().getId());
            System.out.println("Owe payed, value of: R$"+loan.getOweAcumulated());
        }else{
            System.out.println("Book returned!");
        }
        loan.setPayed(true);
        delete(loan.getUser().getId(),loan.getBook().getId());
    }
}