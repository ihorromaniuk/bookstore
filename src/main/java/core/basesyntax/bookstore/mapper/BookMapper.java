package core.basesyntax.bookstore.mapper;

import core.basesyntax.bookstore.config.MapperConfig;
import core.basesyntax.bookstore.dto.book.BookDto;
import core.basesyntax.bookstore.dto.book.BookWithoutCategoryDto;
import core.basesyntax.bookstore.dto.book.CreateBookRequestDto;
import core.basesyntax.bookstore.dto.book.UpdateBookRequestDto;
import core.basesyntax.bookstore.model.Book;
import core.basesyntax.bookstore.model.Category;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categories", source = "categoryIds", qualifiedByName = "setCategories")
    Book createDtoToModel(CreateBookRequestDto dto);

    @Mapping(target = "categories", source = "categoryIds", qualifiedByName = "setCategories")
    void updateBookFromDto(UpdateBookRequestDto dto, @MappingTarget Book book);

    BookWithoutCategoryDto toDtoWithoutCategory(Book book);

    @Mapping(target = "categoryIds", source = "categories", qualifiedByName = "setCategoryIds")
    BookDto toDto(Book book);

    @Named("setCategoryIds")
    default Set<Long> setCategoryIds(Set<Category> categories) {
        return categories.stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
    }

    @Named("setCategories")
    default Set<Category> setCategories(List<Long> categoryIds) {
        return categoryIds == null
                ? new HashSet<>()
                : categoryIds.stream()
                        .map(Category::new)
                        .collect(Collectors.toSet());
    }
}
