package core.basesyntax.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
    private static final Long ID_SCI_FI = 1L;
    private static final String NAME_SCI_FI = "sci-fi";
    private static final Category SCI_FI = new Category(ID_SCI_FI);
    private static final Long ID_ACTION = 2L;
    private static final String NAME_ACTION = "action";
    private static final Category ACTION = new Category(ID_ACTION);

    private static final CategoryDto sciFiDto = new CategoryDto(ID_SCI_FI, NAME_SCI_FI, null);
    private static final CategoryDto actionDto = new CategoryDto(ID_ACTION, NAME_ACTION, null);

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeAll
    static void beforeAll() {
        SCI_FI.setName(NAME_SCI_FI);
        SCI_FI.setDeleted(false);

        ACTION.setName(NAME_ACTION);
        ACTION.setDeleted(false);
    }

    @BeforeEach
    void setUp() {

    }

    @Test
    void findAll_find2Categories_ok() {
        Pageable pageable = Pageable.unpaged();
        Page<Category> page = new PageImpl<>(List.of(SCI_FI, ACTION), pageable, 2);

        when(categoryMapper.toDto(SCI_FI)).thenReturn(sciFiDto);
        when(categoryMapper.toDto(ACTION)).thenReturn(actionDto);
        when(categoryRepository.findAll(pageable)).thenReturn(page);

        Page<CategoryDto> expected = new PageImpl<>(List.of(sciFiDto, actionDto), pageable, 2);
        Page<CategoryDto> actual = categoryService.findAll(pageable);

        assertEquals(expected, actual);
    }

    @Test
    void getById_getCategoryById_ok() {
        when(categoryRepository.findById(ID_ACTION)).thenReturn(Optional.of(ACTION));
        when(categoryMapper.toDto(ACTION)).thenReturn(actionDto);

        CategoryDto actual = categoryService.getById(ID_ACTION);

        assertEquals(actionDto, actual);
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
                new CreateCategoryRequestDto(NAME_SCI_FI, null);

        when(categoryMapper.toModel(requestDto)).thenReturn(SCI_FI);
        when(categoryRepository.save(SCI_FI)).thenReturn(SCI_FI);
        when(categoryMapper.toDto(SCI_FI)).thenReturn(sciFiDto);

        CategoryDto actual = categoryService.save(requestDto);
        CategoryDto expected = new CategoryDto(ID_SCI_FI, NAME_SCI_FI, null);

        assertEquals(expected, actual);
    }

    @Test
    void update_updateCategory_ok() {
        UpdateCategoryRequestDto requestDto = new UpdateCategoryRequestDto(NAME_SCI_FI, null);

        when(categoryRepository.findById(ID_SCI_FI)).thenReturn(Optional.of(SCI_FI));
        when(categoryRepository.save(SCI_FI)).thenReturn(SCI_FI);
        when(categoryMapper.toDto(SCI_FI)).thenReturn(sciFiDto);

        CategoryDto actual = categoryService.update(ID_SCI_FI, requestDto);

        assertEquals(sciFiDto, actual);
    }

    @Test
    void update_categoryNotFound_notOk() {
        UpdateCategoryRequestDto requestDto = new UpdateCategoryRequestDto(NAME_SCI_FI, null);

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
