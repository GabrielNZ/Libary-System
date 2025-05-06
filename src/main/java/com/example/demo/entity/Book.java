package com.example.demo.entity;

import com.example.demo.controller.BookController;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Entity
@Table(name = "tb_book")
public class Book extends RepresentationModel<Book> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private Integer pages;
    private Integer publishingYear;
    private Integer rating;
    private String genre;
    private String webReaderLink;
    @Column(length = 5000)
    private String description;
    @OneToOne(cascade = CascadeType.ALL)
    private Stock stock;
    @OneToMany(mappedBy = "book")
    private List<Loan> loan;

    public Book() {

    }

    @PostPersist
    public void createStock() {
        if (this.stock == null) {
            this.stock = new Stock(this);
            stock.setPrice(0.0);
            stock.setQuantity(0);
        }
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebReaderLink() {
        return webReaderLink;
    }

    public void setWebReaderLink(String webReaderLink) {
        this.webReaderLink = webReaderLink;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getPublishingYear() {
        return publishingYear;
    }

    public void setPublishingYear(Integer publishingYear) {
        this.publishingYear = publishingYear;
    }

    public Long getId() {
        return id;
    }

    public Stock getStock() {
        stock.add(linkTo(methodOn(BookController.class).findById(this.id)).withSelfRel());
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    @JsonIgnore
    public List<Loan> getLoan() {
        return loan;
    }

    public void setLoan(List<Loan> loan) {
        this.loan = loan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
