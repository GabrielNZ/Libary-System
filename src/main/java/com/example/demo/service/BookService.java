package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.exception.BookException;
import com.example.demo.service.exception.DataBaseException;
import com.example.demo.service.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    @Transactional
    public Book create(String isbn, Book book) {
        return bookRepository.save(creatingBook(isbn, book));
    }

    @Transactional
    public Book update(String isbn, Book book) {
        Book bookReferencedById = creatingBook(isbn, book);
        return bookRepository.save(bookReferencedById);
    }

    @Transactional
    public void delete(Long id) {
        try {
            if (!bookRepository.existsById(id)) {
                throw new ResourceNotFoundException("Book does not exist");
            }
            bookRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Book deletion failed");
        }
    }

    @Transactional
    public Book creatingBook(String isbn, Book book) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());
            if (json.has("items")) {
                JSONObject itemsVolumeInfo = json.getJSONArray("items").optJSONObject(0).optJSONObject("volumeInfo");
                JSONObject itemsAcecessInfo = json.getJSONArray("items").optJSONObject(0).optJSONObject("accessInfo");

                String titulo = itemsVolumeInfo.optString("title", "Unknow title");
                String author = itemsVolumeInfo.getJSONArray("authors").optString(0, "Unknow author");
                String description = itemsVolumeInfo.optString("description", "Unknow description");
                String editora = itemsVolumeInfo.optString("publisher", "Unknow publisher");
                Integer pages = itemsVolumeInfo.optInt("pageCount", 0);
                String webReaderLink = itemsAcecessInfo.optString("webReaderLink", "Book without digital version");
                Integer rating = itemsVolumeInfo.optInt("averageRating", 0);
                Integer year = itemsVolumeInfo.optInt("publishedDate", 0);
                String genre = itemsVolumeInfo.optJSONArray("categories").optString(0, "Not specified");

                book.setIsbn(isbn);
                book.setTitle(titulo);
                book.setAuthor(author);
                book.setDescription(description);
                book.setPages(pages);
                book.setWebReaderLink(webReaderLink);
                book.setPublisher(editora);
                book.setRating(rating);
                book.setPublishingYear(year);
                book.setGenre(genre);
            } else {
                throw new ResourceNotFoundException("ISBN not found");
            }
            return book;
        } catch (IOException | InterruptedException e) {
            throw new BookException("Book creation failed");
        } catch (DataIntegrityViolationException e) {
            throw new BookException("ISBN causing exception, may try another one.");
        }
    }
}
