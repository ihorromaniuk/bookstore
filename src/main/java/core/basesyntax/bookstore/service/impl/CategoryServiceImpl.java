package core.basesyntax.bookstore.service.impl;

import core.basesyntax.bookstore.dto.category.CategoryDto;
import core.basesyntax.bookstore.dto.category.CreateCategoryRequestDto;
import core.basesyntax.bookstore.dto.category.UpdateCategoryRequestDto;
import core.basesyntax.bookstore.exception.EntityNotFoundException;
import core.basesyntax.bookstore.mapper.CategoryMapper;
import core.basesyntax.bookstore.model.Category;
import core.basesyntax.bookstore.repository.category.CategoryRepository;
import core.basesyntax.bookstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.toDto(
                categoryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find category by id. Id: " + id))
        );
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto categoryDto) {
        return categoryMapper.toDto(categoryRepository
                        .save(categoryMapper.toModel(categoryDto)));
    }

    @Override
    public CategoryDto update(Long id, UpdateCategoryRequestDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find category by id. Id: " + id));
        categoryMapper.updateBookFromDto(categoryDto, category);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Can't find category by id. Id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
