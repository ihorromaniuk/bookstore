package core.basesyntax.bookstore.service;

import static core.basesyntax.bookstore.util.TestUtil.DESCRIPTION_SCI_FI;
import static core.basesyntax.bookstore.util.TestUtil.ID_ACTION;
import static core.basesyntax.bookstore.util.TestUtil.ID_SCI_FI;
import static core.basesyntax.bookstore.util.TestUtil.NAME_SCI_FI;
import static core.basesyntax.bookstore.util.TestUtil.getActionCategory;
import static core.basesyntax.bookstore.util.TestUtil.getActionDto;
import static core.basesyntax.bookstore.util.TestUtil.getSciFiCategory;
import static core.basesyntax.bookstore.util.TestUtil.getSciFiDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import core.basesyntax.bookstore.dto.category.CategoryDto;
import core.basesyntax.bookstore.dto.category.CreateCategoryRequestDto;
import core.basesyntax.bookstore.dto.category.UpdateCategoryRequestDto;
import core.basesyntax.bookstore.exception.EntityNotFoundException;
import core.basesyntax.bookstore.mapper.CategoryMapper;
import core.basesyntax.bookstore.model.Category;
import core.basesyntax.bookstore.repository.category.CategoryRepository;
import core.basesyntax.bookstore.service.impl.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void findAll_find2Categories_ok() {
        Pageable pageable = Pageable.unpaged();
        Page<Category> page = new PageImpl<>(
                List.of(getActionCategory(), getSciFiCategory()), pageable, 2
        );
        when(categoryMapper.toDto(getSciFiCategory())).thenReturn(getSciFiDto());
        when(categoryMapper.toDto(getActionCategory())).thenReturn(getActionDto());
        when(categoryRepository.findAll(pageable)).thenReturn(page);

        List<CategoryDto> actual = categoryService.findAll(pageable).getContent();

        List<CategoryDto> expected = List.of(getActionDto(), getSciFiDto());
        assertIterableEquals(expected, actual);
    }

    @Test
    void getById_getCategoryById_ok() {
        when(categoryRepository.findById(ID_ACTION)).thenReturn(Optional.of(getActionCategory()));
        when(categoryMapper.toDto(getActionCategory())).thenReturn(getActionDto());

        CategoryDto actual = categoryService.getById(ID_ACTION);

        assertEquals(getActionDto(), actual);
    }

    @Test
    void getById_categoryNotFound_notOk() {
        when(categoryRepository.findById(ID_ACTION)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(ID_ACTION));

        String actualMessage = exception.getMessage();
        String expectedMessage = "Can't find category by id. Id: " + ID_ACTION;
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void save_saveCategory_ok() {
        CreateCategoryRequestDto requestDto =
                new CreateCategoryRequestDto(NAME_SCI_FI, DESCRIPTION_SCI_FI);
        when(categoryMapper.toModel(requestDto)).thenReturn(getSciFiCategory());
        when(categoryRepository.save(getSciFiCategory())).thenReturn(getSciFiCategory());
        when(categoryMapper.toDto(getSciFiCategory())).thenReturn(getSciFiDto());

        CategoryDto actual = categoryService.save(requestDto);

        CategoryDto expected = new CategoryDto(ID_SCI_FI, NAME_SCI_FI, DESCRIPTION_SCI_FI);
        assertEquals(expected, actual);
    }

    @Test
    void update_updateCategory_ok() {
        UpdateCategoryRequestDto requestDto = new UpdateCategoryRequestDto(
                NAME_SCI_FI, DESCRIPTION_SCI_FI
        );
        when(categoryRepository.findById(ID_SCI_FI)).thenReturn(Optional.of(getSciFiCategory()));
        when(categoryRepository.save(getSciFiCategory())).thenReturn(getSciFiCategory());
        when(categoryMapper.toDto(getSciFiCategory())).thenReturn(getSciFiDto());

        CategoryDto actual = categoryService.update(ID_SCI_FI, requestDto);

        assertEquals(getSciFiDto(), actual);
    }

    @Test
    void update_categoryNotFound_notOk() {
        UpdateCategoryRequestDto requestDto = new UpdateCategoryRequestDto(
                NAME_SCI_FI, DESCRIPTION_SCI_FI
        );
        when(categoryRepository.findById(ID_SCI_FI)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(ID_SCI_FI, requestDto));

        String actualMessage = exception.getMessage();
        String expectedMessage = "Can't find category by id. Id: " + ID_SCI_FI;
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deleteById_deleteCategory_ok() {
        when(categoryRepository.existsById(ID_SCI_FI)).thenReturn(true);

        categoryService.deleteById(ID_SCI_FI);

        verify(categoryRepository).deleteById(ID_SCI_FI);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void deleteById_categoryNotFound_notOk() {
        when(categoryRepository.existsById(ID_SCI_FI)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.deleteById(ID_SCI_FI));

        String actualMessage = exception.getMessage();
        String expectedMessage = "Can't find category by id. Id: " + ID_SCI_FI;
        assertEquals(expectedMessage, actualMessage);
    }
}
