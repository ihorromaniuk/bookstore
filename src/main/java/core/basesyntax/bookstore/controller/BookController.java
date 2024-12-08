package core.basesyntax.bookstore.controller;

import core.basesyntax.bookstore.dto.book.BookDto;
import core.basesyntax.bookstore.dto.book.BookParamsDto;
import core.basesyntax.bookstore.dto.book.CreateBookRequestDto;
import core.basesyntax.bookstore.dto.book.ExceptionDto;
import core.basesyntax.bookstore.dto.book.UpdateBookRequestDto;
import core.basesyntax.bookstore.dto.book.ValidationExceptionDto;
import core.basesyntax.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private static final char PATH_SEPARATOR = '/';
    private final BookService bookService;

    @Operation(summary = "Get all books with pagination")
    @ApiResponse(
            responseCode = "200",
            description = "Found books with pagination"
    )
    @GetMapping
    public List<BookDto> getAllBooks(@ParameterObject Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @Operation(summary = "Get book by id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the book",
                    content = @Content(schema = @Schema(implementation = BookDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Didn't find the book",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))
            )
    })
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getById(id);
    }

    @Operation(summary = "Get books with filters and pagination")
    @ApiResponse(
            responseCode = "200",
            description = "Found books",
            content = @Content(mediaType = "application/json")
    )
    @PostMapping("/search")
    public List<BookDto> getBooksBySpec(@RequestBody BookParamsDto params,
                                        @ParameterObject Pageable pageable) {
        return bookService.findAll(params, pageable);
    }

    @Operation(summary = "Create book")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Created new book",
                    content = @Content(schema = @Schema(implementation = BookDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed",
                    content = @Content(schema = @Schema(
                            implementation = ValidationExceptionDto.class
                    ))
            )
    })
    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody @Valid CreateBookRequestDto requestDto,
                                        HttpServletRequest httpServletRequest) {
        BookDto responseBook = bookService.save(requestDto);
        return ResponseEntity
                .created(URI.create(httpServletRequest.getRequestURL()
                        .append(PATH_SEPARATOR)
                        .append(responseBook.id())
                        .toString()))
                .body(responseBook);
    }

    @Operation(summary = "Update book by id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated book",
                    content = @Content(schema = @Schema(implementation = BookDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed",
                    content = @Content(schema = @Schema(
                            implementation = ValidationExceptionDto.class
                    ))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Didn't find the book",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))
            )
    })
    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable Long id,
                              @RequestBody @Valid UpdateBookRequestDto requestDto) {
        return bookService.update(id, requestDto);
    }

    @Operation(summary = "Update book")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Deleted book by id"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Didn't find the book",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))
            )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }
}
