package core.basesyntax.bookstore;

import core.basesyntax.bookstore.controller.BookController;
import core.basesyntax.bookstore.model.Book;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookstoreApplication {
    @Autowired
    private BookController bookController;

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book shining = new Book(
                        "The Shining",
                        "Stephen King",
                        "9781444720723",
                        BigDecimal.valueOf(645),
                        "spooky book",
                        "https://m.media-amazon.com/images/I/81zqohMOk-L.jpg"
                );
                Book bot = new Book(
                        "Bot",
                        "Max Kidruk",
                        "9789661446532",
                        BigDecimal.valueOf(300),
                        "techno thriller",
                        "https://i.ebayimg.com/images/g/jRwAAOSwP~tW5EgU/s-l1200.jpg"
                );
                System.out.println(bookController.createBook(shining));
                System.out.println(bookController.createBook(bot));
                System.out.println(bookController.getAllBooks());
            }
        };
    }
}
