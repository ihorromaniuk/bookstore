package core.basesyntax.bookstore.repository.book;

import core.basesyntax.bookstore.dto.book.BookParamsDto;
import core.basesyntax.bookstore.model.Book;
import core.basesyntax.bookstore.repository.SpecificationBuilder;
import core.basesyntax.bookstore.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder<T> implements SpecificationBuilder<Book> {
    private static final String TITLE_KEY = "title";
    private static final String AUTHOR_KEY = "author";
    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookParamsDto params) {
        Specification<Book> baseSpec = Specification.where(null);
        if (params.partTitle() != null && !params.partTitle().isEmpty()) {
            Specification<Book> titleSpec = specificationProviderManager
                    .getSpecificationProvider(TITLE_KEY).getSpecification(params.partTitle());
            baseSpec = baseSpec.and(titleSpec);
        }
        if (params.authors() != null && params.authors().length != 0) {
            Specification<Book> authorsSpec = specificationProviderManager
                    .getSpecificationProvider(AUTHOR_KEY).getSpecification(params.authors());
            baseSpec = baseSpec.and(authorsSpec);
        }
        return baseSpec;
    }
}
