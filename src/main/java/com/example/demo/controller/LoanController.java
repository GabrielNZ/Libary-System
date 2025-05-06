package com.example.demo.controller;

import com.example.demo.entity.Loan;
import com.example.demo.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/loans")
public class LoanController {
    @Autowired
    LoanService loanService;

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE') or hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<Loan>> findAll() {
        List<Loan> list = loanService.findAll();
        if (!list.isEmpty()) {
            for (Loan loan : list) {
                loan.add(linkTo(methodOn(UserController.class).findById(loan.getId())).withSelfRel());
            }
        }
        return ResponseEntity.ok().body(list);
    }

    @PreAuthorize("#id == authentication.principal.id or hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EMPLOYEE')")  // para o cliente so poder ver seu proprio id, ou entao o adm ver tudo.
    @GetMapping("/{loanId}")
    public ResponseEntity<Loan> findById(@PathVariable Long loanId) {
        Loan loan = loanService.findById(loanId);
        loan.add(linkTo(methodOn(UserController.class).findAll()).withSelfRel());
        return ResponseEntity.ok().body(loan);
    }

    @PreAuthorize("#id == authentication.principal.id or hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EMPLOYEE')")  // para o cliente so poder ver seu proprio id, ou entao o adm ver tudo.
    @GetMapping("/{userId}/{bookId}")
    public ResponseEntity<Loan> findById(@PathVariable Long userId, @PathVariable Long bookId) {
        Loan loan = loanService.findById(bookId,userId);
        loan.add(linkTo(methodOn(UserController.class).findAll()).withSelfRel());
        return ResponseEntity.ok().body(loan);
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE') or hasAuthority('ROLE_ADMIN')")
    @PostMapping("/create/{userId}/{bookId}")
    public ResponseEntity<Loan> insert(@PathVariable Long userId, @PathVariable Long bookId) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userId,bookId).toUri();
        return ResponseEntity.created(uri).body(loanService.insert(userId,bookId));
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE') or hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{userId}/{bookId}")
    public ResponseEntity<Loan> update(@PathVariable Long userId, @PathVariable Long bookId) {
        return ResponseEntity.ok().body(loanService.update(userId,bookId));
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE') or hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{userId}/{bookId}")
    public ResponseEntity<Loan> returnBookOrPayOwe(@PathVariable Long userId, @PathVariable Long bookId) {
        Loan loan = loanService.findById(bookId,userId);
        loanService.returnBookOrPayOwe(bookId,userId);
        return ResponseEntity.ok().body(loan);
    }
}
