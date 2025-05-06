package com.example.demo.controller;

import com.example.demo.entity.Stock;
import com.example.demo.service.StockService;
import com.example.demo.service.exception.StockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/books/stock")
public class StockController {
    @Autowired
    private StockService stockService;

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE') or hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<Stock>> findAll(@RequestParam(required = false) Double minPrice,
                                               @RequestParam(required = false) Double maxPrice,
                                               @RequestParam(required = false) Double minQuantity,
                                               @RequestParam(required = false) Double maxQuantity) {

        if ((minPrice != null && maxPrice == null) || (minPrice == null && maxPrice != null)) {
            throw new StockException("You need to complete the filter, its necessary to have both min & max values");
        }
        if ((minQuantity != null && maxQuantity == null) || (minQuantity == null && maxQuantity != null)) {
            throw new StockException("You need to complete the filter, its necessary to have both min & max values");
        }

        List<Stock> stock = stockService.findAll();

        if (minPrice != null && minQuantity == null) {
            List<Stock> stockFilteredPrice = stock.stream().filter(x -> x.getPrice() >= minPrice && x.getPrice() <= maxPrice).toList();
            for (Stock s : stockFilteredPrice) {
                s.add(linkTo(methodOn(BookController.class).findById(s.getId())).withSelfRel());
            }
            return ResponseEntity.ok().body(stockFilteredPrice);
        }

        if (minQuantity != null && minPrice == null) {
            List<Stock> stockFilteredQuantity = stock.stream().filter(x -> x.getQuantity() >= minQuantity && x.getQuantity() <= maxQuantity).toList();
            for (Stock s : stockFilteredQuantity) {
                s.add(linkTo(methodOn(BookController.class).findById(s.getId())).withSelfRel());
            }
            return ResponseEntity.ok().body(stockFilteredQuantity);
        }

        if (minPrice != null && minQuantity != null) {
            List<Stock> stockFilteredAll = stock.stream().filter(x -> x.getPrice() >= minPrice && x.getPrice() <= maxPrice && x.getQuantity() >= minQuantity && x.getQuantity() <= maxQuantity).toList();
            for (Stock s : stockFilteredAll) {
                s.add(linkTo(methodOn(BookController.class).findById(s.getId())).withSelfRel());
            }
            return ResponseEntity.ok().body(stockFilteredAll);
        }

        for (Stock s : stock) {
            s.add(linkTo(methodOn(BookController.class).findById(s.getId())).withSelfRel());
        }
        return ResponseEntity.ok().body(stock);
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE') or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Stock> findById(@PathVariable Long id) {
        Stock stock = stockService.findById(id);
        stock.add(linkTo(methodOn(BookController.class).findAll()).withSelfRel());
        return ResponseEntity.ok().body(stock);
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE') or hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Stock> update(@PathVariable Long id, @RequestBody Stock stock) {
        return ResponseEntity.ok().body(stockService.update(id,stock));
    }
}
