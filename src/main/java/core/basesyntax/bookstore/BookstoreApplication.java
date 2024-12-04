package core.basesyntax.bookstore;

import core.basesyntax.bookstore.dto.book.CreateBookRequestDto;
import core.basesyntax.bookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookstoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            CreateBookRequestDto shining = new CreateBookRequestDto(
                    "The Shining",
                    "Stephen King",
                    "9781444720723",
                    BigDecimal.valueOf(645),
                    "spooky book",
                    "https://m.media-amazon.com/images/I/81zqohMOk-L.jpg"
            );

            CreateBookRequestDto bot = new CreateBookRequestDto(
                    "Bot",
                    "Max Kidruk",
                    "9789661446532",
                    BigDecimal.valueOf(300),
                    "techno thriller",
                    "https://i.ebayimg.com/images/g/jRwAAOSwP~tW5EgU/s-l1200.jpg"
            );

            System.out.println(bookService.findAll());
        };
    }
}
