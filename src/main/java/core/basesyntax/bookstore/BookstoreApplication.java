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
            CreateBookRequestDto shining = new CreateBookRequestDto();
            shining.setTitle("The Shining");
            shining.setAuthor("Stephen King");
            shining.setIsbn("9781444720723");
            shining.setPrice(BigDecimal.valueOf(645));
            shining.setDescription("spooky book");
            shining.setCoverImage("https://m.media-amazon.com/images/I/81zqohMOk-L.jpg");

            CreateBookRequestDto bot = new CreateBookRequestDto();
            bot.setTitle("Bot");
            bot.setAuthor("Max Kidruk");
            bot.setIsbn("9789661446532");
            bot.setPrice(BigDecimal.valueOf(300));
            bot.setDescription("techno thriller");
            bot.setCoverImage("https://i.ebayimg.com/images/g/jRwAAOSwP~tW5EgU/s-l1200.jpg");

            System.out.println(bookService.save(shining));
            System.out.println(bookService.save(bot));
            System.out.println(bookService.findAll());
        };
    }
}
