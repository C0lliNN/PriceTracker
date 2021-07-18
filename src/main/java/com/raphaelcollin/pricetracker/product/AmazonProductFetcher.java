package com.raphaelcollin.pricetracker.product;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class AmazonProductFetcher implements ProductFetcher {
    private static final String BASE_URL = "https://www.amazon.com.br";
    private static final String PRODUCTS_ROOT_SELECTOR = "s-main-slot s-result-list s-search-results sg-row";

    @Override
    public Collection<Product> fetchProducts(final String query) {
        final HttpClient client = createHttpClient();
        final HttpRequest request = createHttpRequest(query);

        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            final Document document = Jsoup.parse(response.body());
            final Element productsRootElement = document.body().getElementsByClass(PRODUCTS_ROOT_SELECTOR).first();

            return productsRootElement
                    .children()
                    .stream()
                    .map(this::createProductFromDomElement)
                    .filter(Objects::nonNull)
                    .limit(5)
                    .collect(Collectors.toUnmodifiableList());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private HttpClient createHttpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
    }

    private HttpRequest createHttpRequest(final String query) {
        final StringBuilder builder = new StringBuilder(BASE_URL);

        final String url = builder
                .append("/s?k=")
                .append(URLEncoder.encode(query, StandardCharsets.UTF_8))
                .append("&__mk_pt_BR=%C3%85M%C3%85%C5%BD%C3%95%C3%91&crid=130TT54MYLCKG&sprefix=")
                .append(URLEncoder.encode(query, StandardCharsets.UTF_8))
                .append("&ref=nb_sb_ss_ts-doa-p_2_11")
                .toString();

        return HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Mozilla/5.0")
                .build();
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
