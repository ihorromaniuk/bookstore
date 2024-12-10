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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(name = "Books management",
        description = "Endpoints for managing")
@RequiredArgsConstructor
public class BookController {
    private static final char PATH_SEPARATOR = '/';
    private final BookService bookService;

    @Operation(summary = "Get all books with pagination")
    @ApiResponse(
            responseCode = "200",
            description = "Found books with pagination"
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public Page<BookDto> getAllBooks(@ParameterObject Pageable pageable) {
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
    @PreAuthorize("hasRole('USER')")
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
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/search")
    public Page<BookDto> getBooksBySpec(@RequestBody BookParamsDto params,
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
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody @Valid CreateBookRequestDto requestDto,
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }
}
