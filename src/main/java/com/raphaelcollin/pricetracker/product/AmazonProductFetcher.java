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

@Slf4j
@AllArgsConstructor
public class AmazonProductFetcher implements ProductFetcher {
    private static final String BASE_URL = "https://www.amazon.com.br";
    private static final String PRODUCTS_ROOT_SELECTOR = "s-main-slot s-result-list s-search-results sg-row";

    private final CustomHttpClient httpClient;

    @Override
    public CompletableFuture<List<Product>> fetchProducts(final String query) {
        try {
            return httpClient.executeGetRequest(createUrl(query))
                    .thenApply(Jsoup::parse)
                    .thenApply(document -> document.body().getElementsByClass(PRODUCTS_ROOT_SELECTOR).first())
                    .thenApply(productsRootElement -> Objects.requireNonNull(productsRootElement)
                            .children()
                            .stream()
                            .limit(5)
                            .map(this::createProductFromDomElement)
                            .filter(Objects::nonNull)
                            .collect(toUnmodifiableList())
                    ).thenApply(products -> {
                        log.info("Found products in Amazon: {}", products);
                        return products;
                    });
        } catch (Exception e) {
            log.error("An unexpected error happened when fetching products from Amazon: {}", e.getMessage(), e);
            return CompletableFuture.completedFuture(Collections.emptyList());
        }
    }

    private String createUrl(final String query) {
        final StringBuilder builder = new StringBuilder(BASE_URL);

        return builder
                .append("/s?k=")
                .append(URLEncoder.encode(query, StandardCharsets.UTF_8))
                .append("&__mk_pt_BR=%C3%85M%C3%85%C5%BD%C3%95%C3%91&crid=130TT54MYLCKG&sprefix=")
                .append(URLEncoder.encode(query, StandardCharsets.UTF_8))
                .append("&ref=nb_sb_ss_ts-doa-p_2_11")
                .toString();
    }

    private Product createProductFromDomElement(final Element element) {
        try {
            final String title = element.getElementsByTag("h2").text();

            final String rawPrice = element.getElementsByClass("a-price-whole").text();
            double doublePrice = Double.parseDouble(rawPrice.replace(",", ""));

            final BigDecimal price = BigDecimal.valueOf(doublePrice);
            final String productLink = BASE_URL + element.getElementsByTag("a").last().attr("href");

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
