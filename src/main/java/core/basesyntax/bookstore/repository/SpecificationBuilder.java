package core.basesyntax.bookstore.repository;

import core.basesyntax.bookstore.dto.book.BookParamsDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookParamsDto params);
}
