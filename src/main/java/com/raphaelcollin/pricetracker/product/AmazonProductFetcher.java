package com.raphaelcollin.pricetracker.product;

import com.raphaelcollin.pricetracker.utils.CustomHttpClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import static java.util.stream.Collectors.toUnmodifiableList;

@Slf4j
@AllArgsConstructor
public class AmazonProductFetcher implements ProductFetcher {
    private static final String BASE_URL = "https://www.amazon.com.br";
    private static final String PRODUCTS_ROOT_SELECTOR = "s-main-slot s-result-list s-search-results sg-row";

    private final CustomHttpClient httpClient;

    @Override
    public Collection<Product> fetchProducts(final String query) {
        try {
            final String response = httpClient.executeGetRequest(createUrl(query));

            final Document document = Jsoup.parse(response);
            final Element productsRootElement = document.body().getElementsByClass(PRODUCTS_ROOT_SELECTOR).first();

            final Collection<Product> products = Objects.requireNonNull(productsRootElement)
                    .children()
                    .stream()
                    .map(element -> createProductFromDomElementAndQuery(element, query))
                    .filter(Objects::nonNull)
                    .limit(5)
                    .collect(toUnmodifiableList());

            log.info("Found products in Amazon: {}", products);
            return products;
        } catch (Exception e) {
            log.error("An unexpected error happened when fetching products from Amazon: {}", e.getMessage(), e);
            return Collections.emptyList();
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

    private Product createProductFromDomElementAndQuery(final Element element, final String query) {
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
                    .query(query)
                    .build();
        } catch (Exception e) {
            return null;
        }
    }
}
