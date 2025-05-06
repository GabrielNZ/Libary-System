package com.example.demo.controller;

import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
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
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> findAll() {
        List<Book> book = bookService.findAll();
        for (Book b : book) {
            b.add(linkTo(methodOn(BookController.class).findById(b.getId())).withSelfRel());
        }
        return ResponseEntity.ok().body(book);
    }

    @GetMapping("/genre")
    public ResponseEntity<List<Book>> findAllByGenre(@RequestBody String genre) {
        List<Book> book = bookService.findAll();
        List<Book> bookFiltred = book.stream().filter(x-> x.getGenre().equals(genre)).toList();
        for (Book b : bookFiltred) {
            b.add(linkTo(methodOn(BookController.class).findById(b.getId())).withSelfRel());
        }
        return ResponseEntity.ok().body(bookFiltred);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findById(@PathVariable Long id) {
        Book book = bookService.findById(id);
        book.add(linkTo(methodOn(BookController.class).findAll()).withSelfRel());
        return ResponseEntity.ok().body(book);
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE') or hasAuthority('ROLE_ADMIN')")
    @PostMapping("/{isbn}")
    public ResponseEntity<Book> insert(@PathVariable String isbn) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{isbn}").buildAndExpand(isbn).toUri();
        return ResponseEntity.created(uri).body(bookService.create(isbn, new Book()));
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE') or hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}/{isbn}")
    public ResponseEntity<Book> update(@PathVariable(value = "id") Long id, @PathVariable(value = "isbn") String isbn) {
        return ResponseEntity.ok().body(bookService.update(isbn, bookService.findById(id)));
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE') or hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
