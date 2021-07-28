package com.raphaelcollin.pricetracker.product;

import com.raphaelcollin.pricetracker.utils.CustomHttpClient;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


public class AmazonProductFetcher extends ProductFetcher {
    private static final String BASE_URL = "https://www.amazon.com.br";
    private static final String PRODUCTS_ROOT_SELECTOR = "#search > div.s-desktop-width-max.s-desktop-content.s-opposite-dir.sg-row > div.s-matching-dir.sg-col-16-of-20.sg-col.sg-col-8-of-12.sg-col-12-of-16 > div > span:nth-child(4) > div.s-main-slot.s-result-list.s-search-results.sg-row";

    public AmazonProductFetcher(final CustomHttpClient httpClient) {
        super(httpClient, PRODUCTS_ROOT_SELECTOR);
    }

    @Override
    protected String createUrl(final String query) {
        final StringBuilder builder = new StringBuilder(BASE_URL);

        return builder
                .append("/s?k=")
                .append(URLEncoder.encode(query, StandardCharsets.UTF_8))
                .append("&__mk_pt_BR=%C3%85M%C3%85%C5%BD%C3%95%C3%91&crid=130TT54MYLCKG&sprefix=")
                .append(URLEncoder.encode(query, StandardCharsets.UTF_8))
                .append("&ref=nb_sb_ss_ts-doa-p_2_11")
                .toString();
    }

    @Override
    protected Product createProductFromDomElement(final Element element) {
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
