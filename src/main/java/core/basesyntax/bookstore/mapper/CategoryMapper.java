package core.basesyntax.bookstore.mapper;

import core.basesyntax.bookstore.config.MapperConfig;
import core.basesyntax.bookstore.dto.category.CategoryDto;
import core.basesyntax.bookstore.dto.category.CreateCategoryRequestDto;
import core.basesyntax.bookstore.dto.category.UpdateCategoryRequestDto;
import core.basesyntax.bookstore.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    Category toModel(CreateCategoryRequestDto requestDto);

    CategoryDto toDto(Category category);

    void updateBookFromDto(UpdateCategoryRequestDto requestDto, @MappingTarget Category category);
}
