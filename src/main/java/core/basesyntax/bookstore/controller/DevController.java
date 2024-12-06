package core.basesyntax.bookstore.controller;

import core.basesyntax.bookstore.dto.book.CreateBookRequestDto;
import core.basesyntax.bookstore.service.BookService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dev")
@RequiredArgsConstructor
public class DevController {
    private final BookService bookService;

    @GetMapping("/fill")
    public void fillDatabase() {
        CreateBookRequestDto shining = new CreateBookRequestDto(
                "The Shining",
                "Stephen King",
                "9781444720723",
                BigDecimal.valueOf(645),
                "spooky book",
                "https://m.media-amazon.com/images/I/81zqohMOk-L.jpg"
        );

        CreateBookRequestDto carnival = new CreateBookRequestDto(
                "Carnival",
                "Stephen King",
                "9781444720725",
                BigDecimal.valueOf(500),
                "yet another spooky book",
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

        CreateBookRequestDto tverdynia = new CreateBookRequestDto(
                "Tverdynia",
                "Max Kidruk",
                "9789661446500",
                BigDecimal.valueOf(200),
                "thriller",
                "https://i.ebayimg.com/images/g/jRwAAOSwP~tW5EgU/s-l1200.jpg"
        );

        CreateBookRequestDto kobzar = new CreateBookRequestDto(
                "Kobzar",
                "Taras Shevchenko",
                "9789661446200",
                BigDecimal.valueOf(700),
                "poetry",
                "https://glagoslav.com/wp-content/uploads/2019/10/Shevchenko_Kobzar_website.jpg"
        );

        bookService.save(shining);
        bookService.save(carnival);
        bookService.save(bot);
        bookService.save(tverdynia);
        bookService.save(kobzar);
    }
}
