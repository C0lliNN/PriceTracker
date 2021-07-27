package com.raphaelcollin.pricetracker.product;

import com.raphaelcollin.pricetracker.utils.CustomHttpClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toUnmodifiableList;

@AllArgsConstructor
@Slf4j
public class PichauProductFetcher implements ProductFetcher {
    private static final String BASE_URL = "https://www.pichau.com.br";
    private static final String PRODUCTS_ROOT_SELECTOR = "#__next > main > div:nth-child(2) > div > div > div:nth-child(3)";

    private final CustomHttpClient httpClient;

    @Override
    public CompletableFuture<List<Product>> fetchProducts(final String query) {
        try {
            return httpClient.executeGetRequest(createUrl(query))
                    .thenApply(Jsoup::parse)
                    .thenApply(document -> document.body().selectFirst(PRODUCTS_ROOT_SELECTOR))
                    .thenApply(productsRootElement -> Objects.requireNonNull(productsRootElement)
                            .children()
                            .stream()
                            .limit(5)
                            .map(this::createProductFromDomElement)
                            .filter(Objects::nonNull)
                            .collect(toUnmodifiableList())
                    ).thenApply(products -> {
                        log.info("Found products in Pichau: {}", products);
                        return products;
                    });
        } catch (Exception e) {
            log.error("An unexpected error happened when fetching products from Pichau: {}", e.getMessage(), e);
            return CompletableFuture.completedFuture(Collections.emptyList());
        }
    }

    private String createUrl(final String query) {
        return  BASE_URL + "/search?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
    }

    private Product createProductFromDomElement(final Element element) {
        try {
            final String title = element.getElementsByTag("h2").text();

            final Element priceContainer = element.selectFirst(".MuiCardContent-root > div > div > div");

            final String rawPrice = priceContainer.child(priceContainer.childrenSize() - 2).text();
            double doublePrice = Double.parseDouble(rawPrice.replace(",", ".").replace(".", "").replace("R$", ""));

            final BigDecimal price = BigDecimal.valueOf(doublePrice);
            final String productLink = BASE_URL + element.getElementsByTag("a").first().attr("href");

            return Product
                    .builder()
                    .title(title)
                    .price(price)
                    .link(productLink)
                    .build();
        } catch (Exception e) {
            return null;
        }
    }
}
