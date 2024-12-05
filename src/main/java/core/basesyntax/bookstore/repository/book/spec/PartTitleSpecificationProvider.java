package core.basesyntax.bookstore.repository.book.spec;

import core.basesyntax.bookstore.model.Book;
import core.basesyntax.bookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PartTitleSpecificationProvider implements SpecificationProvider<Book> {
    private static final String KEY = "title";
    private static final int FIRST_INDEX = 0;

    @Override
    public Specification<Book> getSpecification(String... params) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(KEY), "%" + params[FIRST_INDEX] + "%");
    }

    @Override
    public String getKey() {
        return KEY;
    }
}
