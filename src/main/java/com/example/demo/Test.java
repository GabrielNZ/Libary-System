package com.example.demo;

import com.example.demo.entity.Book;
import com.example.demo.entity.User;
import com.example.demo.entity.enumtype.Role;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Configuration
@Profile("dev")
public class Test implements CommandLineRunner {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        String isbn1 = "9788535902778";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn1)).GET().build();
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
            String genre = "Not specified";

            Book book1 = new Book();
            book1.setIsbn(isbn1);
            book1.setTitle(titulo);
            book1.setAuthor(author);
            book1.setDescription(description);
            book1.setPages(pages);
            book1.setWebReaderLink(webReaderLink);
            book1.setPublisher(editora);
            book1.setRating(rating);
            book1.setPublishingYear(year);
            book1.setGenre(genre);

            bookRepository.save(book1);

            String isbn2 = "9780553103540";
            request = HttpRequest.newBuilder().uri(URI.create("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn2)).GET().build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            json = new JSONObject(response.body());
            if (json.has("items")) {
                itemsVolumeInfo = json.getJSONArray("items").optJSONObject(0).optJSONObject("volumeInfo");
                itemsAcecessInfo = json.getJSONArray("items").optJSONObject(0).optJSONObject("accessInfo");

                titulo = itemsVolumeInfo.optString("title", "Unknow title");
                author = itemsVolumeInfo.getJSONArray("authors").optString(0, "Unknow author");
                description = itemsVolumeInfo.optString("description", "Unknow description");
                editora = itemsVolumeInfo.optString("publisher", "Unknow publisher");
                pages = itemsVolumeInfo.optInt("pageCount", 0);
                webReaderLink = itemsAcecessInfo.optString("webReaderLink", "Book without digital version");
                rating = itemsVolumeInfo.optInt("averageRating", 0);
                year = itemsVolumeInfo.optInt("publishedDate", 0);
                genre = "Not specified";

                Book book2 = new Book();
                book2.setIsbn(isbn2);
                book2.setTitle(titulo);
                book2.setAuthor(author);
                book2.setDescription(description);
                book2.setPages(pages);
                book2.setWebReaderLink(webReaderLink);
                book2.setPublisher(editora);
                book2.setRating(rating);
                book2.setPublishingYear(year);
                book2.setGenre(genre);

                bookRepository.save(book2);

                String isbn3 = "9780547928227";
                request = HttpRequest.newBuilder().uri(URI.create("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn3)).GET().build();
                response = client.send(request, HttpResponse.BodyHandlers.ofString());

                json = new JSONObject(response.body());
                if (json.has("items")) {
                    itemsVolumeInfo = json.getJSONArray("items").optJSONObject(0).optJSONObject("volumeInfo");
                    itemsAcecessInfo = json.getJSONArray("items").optJSONObject(0).optJSONObject("accessInfo");

                    titulo = itemsVolumeInfo.optString("title", "Unknow title");
                    author = itemsVolumeInfo.getJSONArray("authors").optString(0, "Unknow author");
                    description = itemsVolumeInfo.optString("description", "Unknow description");
                    editora = itemsVolumeInfo.optString("publisher", "Unknow publisher");
                    pages = itemsVolumeInfo.optInt("pageCount", 0);
                    webReaderLink = itemsAcecessInfo.optString("webReaderLink", "Book without digital version");
                    rating = itemsVolumeInfo.optInt("averageRating", 0);
                    year = itemsVolumeInfo.optInt("publishedDate", 0);
                    genre = "Not specified";

                    Book book3 = new Book();
                    book3.setIsbn(isbn3);
                    book3.setTitle(titulo);
                    book3.setAuthor(author);
                    book3.setDescription(description);
                    book3.setPages(pages);
                    book3.setWebReaderLink(webReaderLink);
                    book3.setPublisher(editora);
                    book3.setRating(rating);
                    book3.setPublishingYear(year);
                    book3.setGenre(genre);

                    bookRepository.save(book3);

                    String isbn4 = "9780060934347";
                    request = HttpRequest.newBuilder().uri(URI.create("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn4)).GET().build();
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    json = new JSONObject(response.body());
                    if (json.has("items")) {
                        itemsVolumeInfo = json.getJSONArray("items").optJSONObject(0).optJSONObject("volumeInfo");
                        itemsAcecessInfo = json.getJSONArray("items").optJSONObject(0).optJSONObject("accessInfo");

                        titulo = itemsVolumeInfo.optString("title", "Unknow title");
                        author = itemsVolumeInfo.getJSONArray("authors").optString(0, "Unknow author");
                        description = itemsVolumeInfo.optString("description", "Unknow description");
                        editora = itemsVolumeInfo.optString("publisher", "Unknow publisher");
                        pages = itemsVolumeInfo.optInt("pageCount", 0);
                        webReaderLink = itemsAcecessInfo.optString("webReaderLink", "Book without digital version");
                        rating = itemsVolumeInfo.optInt("averageRating", 0);
                        year = itemsVolumeInfo.optInt("publishedDate", 0);
                        genre = "Not specified";

                        Book book4 = new Book();
                        book4.setIsbn(isbn4);
                        book4.setTitle(titulo);
                        book4.setAuthor(author);
                        book4.setDescription(description);
                        book4.setPages(pages);
                        book4.setWebReaderLink(webReaderLink);
                        book4.setPublisher(editora);
                        book4.setRating(rating);
                        book4.setPublishingYear(year);
                        book4.setGenre(genre);

                        bookRepository.save(book4);

                        User user1 = new User();
                        user1.setName("João Silva");
                        user1.setEmail("joao.silva@email.com");
                        user1.setPhone("11999999999");
                     //   user1.setPassword("senhaSegura123");
                      //  user1.setFines(0.0);
                      //  user1.setRole(Role.CLIENT);

                        userRepository.save(user1);
                        User user2 = new User();
                        user2.setName("Pedro Parizotto Aura");
                        user2.setEmail("jhonson@email.com");
                        user2.setPhone("11999459999");
                    //    user2.setPassword("senhaAura");
                     //   user2.setFines(0.0);
                      //  user2.setRole(Role.CLIENT);

                        userRepository.save(user2);
                        User user3 = new User();
                        user3.setName("Maria Claudia");
                        user3.setEmail("maria@email.com");
                        user3.setPhone("18932143124");
                    //    user3.setPassword("12345678");
                     //   user3.setFines(0.0);
                     //   user3.setRole(Role.CLIENT);

                        userRepository.save(user3);
                    }
                }
            }
        }
    }
}
