package com.raphaelcollin.pricetracker.product;

import com.raphaelcollin.pricetracker.utils.CustomHttpClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toUnmodifiableList;


@AllArgsConstructor
@Slf4j
public abstract class ProductFetcher {
    private final CustomHttpClient httpClient;
    private final String productsRootSelector;

    public CompletableFuture<List<Product>> fetchProducts(final String query) {
        try {
            return httpClient
                    .executeGetRequest(createUrl(query))
                    .thenApply(Jsoup::parse)
                    .thenApply(document -> document.body().selectFirst(productsRootSelector))
                    .thenApply(productsRootElement -> Objects.requireNonNull(productsRootElement)
                            .children()
                            .stream()
                            .limit(5)
                            .map(this::createProductFromDomElement)
                            .filter(Objects::nonNull)
                            .collect(toUnmodifiableList())
                    ).thenApply(products -> {
                        log.info("Found products: {}", products);
                        return products;
                    });
        } catch (Exception e) {
            log.error("An unexpected error happened when fetching products : {}", e.getMessage(), e);
            return CompletableFuture.completedFuture(Collections.emptyList());
        }
    }

    protected abstract String createUrl(final String query);

    protected abstract Product createProductFromDomElement(final Element element);

}
