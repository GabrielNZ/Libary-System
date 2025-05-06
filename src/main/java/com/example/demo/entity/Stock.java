package com.example.demo.entity;

import com.example.demo.service.exception.StockException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_stock")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Stock extends RepresentationModel<Stock> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;
    private Double price;

    @OneToOne
    @MapsId
    @JsonIgnore
    private Book book;

    public Stock(Book book) {
        if (book.getId() == null) {
            throw new StockException("Book must have an Id to associate with Stock");
        }
        this.id = book.getId();
        this.book = book;
    }

    public Stock() {

    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        if (book.getId() == null) {
            throw new StockException("Book must have an Id to associate with Stock");
        }
        this.book = book;
        this.id = this.book.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(id, stock.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
