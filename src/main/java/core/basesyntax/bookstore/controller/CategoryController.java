package core.basesyntax.bookstore.controller;

import core.basesyntax.bookstore.dto.book.BookDto;
import core.basesyntax.bookstore.dto.book.BookWithoutCategoryDto;
import core.basesyntax.bookstore.dto.category.CategoryDto;
import core.basesyntax.bookstore.dto.category.CreateCategoryRequestDto;
import core.basesyntax.bookstore.dto.category.UpdateCategoryRequestDto;
import core.basesyntax.bookstore.dto.exception.ExceptionDto;
import core.basesyntax.bookstore.dto.exception.ValidationExceptionDto;
import core.basesyntax.bookstore.service.BookService;
import core.basesyntax.bookstore.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
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
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Category management", description = "Endpoints for managing categories")
public class CategoryController {
    private static final char PATH_SEPARATOR = '/';
    private final CategoryService categoryService;
    private final BookService bookService;

    @Operation(summary = "Get all categories")
    @ApiResponse(
            responseCode = "200",
            description = "Return list of categories"
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<CategoryDto> getAllCategories() {
        return categoryService.findAll();
    }

    @Operation(summary = "Get category by id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the category",
                    content = @Content(schema = @Schema(implementation = CategoryDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Didn't find the category",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))
            )
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @Operation(summary = "Create category")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Created new category",
                    content = @Content(schema = @Schema(
                            implementation = CategoryDto.class)
                    )
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
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody
                                                      CreateCategoryRequestDto requestDto,
                                                      HttpServletRequest request) {
        CategoryDto responseDto = categoryService.save(requestDto);
        return ResponseEntity.created(
                URI.create(request.getRequestURL()
                        .append(PATH_SEPARATOR)
                        .append(responseDto.id())
                        .toString())
                )
                .body(responseDto);
    }

    @Operation(summary = "Update category by id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated category",
                    content = @Content(schema =
                    @Schema(implementation = BookDto.class))
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
                    description = "Didn't find the category",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable Long id,
                                      @RequestBody @Valid UpdateCategoryRequestDto requestDto) {
        return categoryService.update(id, requestDto);
    }

    @Operation(summary = "Delete category")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Deleted category by id"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Didn't find the category",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @Operation(summary = "Get books by category id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Found books by category by id",
                    content = @Content(array = @ArraySchema(
                            schema = @Schema (implementation = BookWithoutCategoryDto.class)
                    ))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Didn't find the category",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))
            )
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/books")
    public Page<BookWithoutCategoryDto> getBooksByCategoryId(@PathVariable Long id,
                                                             @ParameterObject Pageable pageable) {
        return bookService.findAllByCategoryId(id, pageable);
    }
}
