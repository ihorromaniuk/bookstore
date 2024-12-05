package core.basesyntax.bookstore.repository.book.spec;

import core.basesyntax.bookstore.model.Book;
import core.basesyntax.bookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorsSpecificationProvider implements SpecificationProvider<Book> {
    private static final String KEY = "author";

    @Override
    public Specification<Book> getSpecification(String... params) {
        return (root, query, criteriaBuilder) ->
                root.get("author").in(params);
    }

    @Override
    public String getKey() {
        return KEY;
    }
}
