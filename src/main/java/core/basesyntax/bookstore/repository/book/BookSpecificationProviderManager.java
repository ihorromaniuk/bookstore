package core.basesyntax.bookstore.repository.book;

import core.basesyntax.bookstore.exception.BeanNotFoundException;
import core.basesyntax.bookstore.model.Book;
import core.basesyntax.bookstore.repository.SpecificationProvider;
import core.basesyntax.bookstore.repository.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> specificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return specificationProviders.stream()
                .filter(provider -> provider.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new BeanNotFoundException(
                        "Can't find specification bean for key. Key: " + key));
    }
}
