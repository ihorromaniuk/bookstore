package core.basesyntax.bookstore.controller;

import core.basesyntax.bookstore.dto.book.CreateBookRequestDto;
import core.basesyntax.bookstore.service.BookService;
import java.math.BigDecimal;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dev")
@RequiredArgsConstructor
public class DevController {
    private final BookService bookService;

    @GetMapping("/fill")
    public ResponseEntity<?> fillDb() {
        CreateBookRequestDto shining = new CreateBookRequestDto(
                "The Shining",
                "Stephen King",
                "97814443220723",
                BigDecimal.valueOf(645),
                "spooky book",
                "https://m.media-amazon.com/images/I/81zqohMOk-L.jpg"
        );

        CreateBookRequestDto bot = new CreateBookRequestDto(
                "Bot",
                "Max Kidruk",
                "97896614433532",
                BigDecimal.valueOf(300),
                "techno thriller",
                "https://i.ebayimg.com/images/g/jRwAAOSwP~tW5EgU/s-l1200.jpg"
        );

        bookService.save(shining);
        bookService.save(bot);
        return ResponseEntity.created(URI.create("/")).build();
    }
}
