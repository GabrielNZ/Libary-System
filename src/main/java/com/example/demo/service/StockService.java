package com.example.demo.service;
import com.example.demo.entity.Stock;
import com.example.demo.repository.StockRepository;
import com.example.demo.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {
    @Autowired
    StockRepository stockRepository;

    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    public Stock findById(Long id) {
        return stockRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Stock not found"));
    }

    public Stock update(Long id, Stock stock) {
        Stock referencedStock = stockRepository.getReferenceById(id);
        referencedStock.setPrice(stock.getPrice());
        referencedStock.setQuantity(stock.getQuantity());
        return stockRepository.save(stock);
    }
}
