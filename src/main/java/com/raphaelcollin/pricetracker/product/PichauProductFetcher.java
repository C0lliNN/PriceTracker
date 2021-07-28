package com.raphaelcollin.pricetracker.product;

import com.raphaelcollin.pricetracker.utils.CustomHttpClient;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class PichauProductFetcher extends ProductFetcher {
    private static final String BASE_URL = "https://www.pichau.com.br";
    private static final String PRODUCTS_ROOT_SELECTOR = "#__next > main > div:nth-child(2) > div > div > div:nth-child(3)";

    public PichauProductFetcher(final CustomHttpClient httpClient) {
        super(httpClient, PRODUCTS_ROOT_SELECTOR);
    }

    protected String createUrl(final String query) {
        return  BASE_URL + "/search?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
    }

    protected Product createProductFromDomElement(final Element element) {
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
