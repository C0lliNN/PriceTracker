package com.raphaelcollin.pricetracker;

import com.raphaelcollin.pricetracker.notification.JSwingNotificationSender;
import com.raphaelcollin.pricetracker.notification.NotificationSender;
import com.raphaelcollin.pricetracker.product.AmazonProductFetcher;
import com.raphaelcollin.pricetracker.product.PichauProductFetcher;
import com.raphaelcollin.pricetracker.product.Product;
import com.raphaelcollin.pricetracker.product.ProductFetcher;
import com.raphaelcollin.pricetracker.utils.ArgumentsParser;
import com.raphaelcollin.pricetracker.utils.CustomHttpClient;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Slf4j
public class Main {

    public static void main(String[] args) {
        final ArgumentsParser parser = new ArgumentsParser(args);

        final String productQuery = parser.getParameter("product");
        final BigDecimal targetPrice = BigDecimal.valueOf(Double.parseDouble(parser.getParameter("price")));

        final Product cheapestProduct = getCheapestProduct(productQuery);

        if (cheapestProduct.getPrice().compareTo(targetPrice) < 0) {
            NotificationSender notificationSender = new JSwingNotificationSender();
            notificationSender.send(cheapestProduct);
        }
    }

    private static Product getCheapestProduct(String productQuery) {
        final CustomHttpClient httpClient = new CustomHttpClient();

        final ProductFetcher amazonFetcher = new AmazonProductFetcher(httpClient);
        final ProductFetcher pichauFetcher = new PichauProductFetcher(httpClient);

        final CompletableFuture<List<Product>> amazonProductsFuture = amazonFetcher.fetchProducts(productQuery);
        final CompletableFuture<List<Product>> pichauProductsFtuture = pichauFetcher.fetchProducts(productQuery);

        CompletableFuture.allOf(amazonProductsFuture, pichauProductsFtuture).join();

        final Product cheapestProduct = Stream.of(amazonProductsFuture.join(), pichauProductsFtuture.join())
                .flatMap(Collection::stream)
                .min(Comparator.comparing(Product::getPrice))
                .orElseThrow(() -> new RuntimeException("No product was fetched"));

        log.info("Cheapest Product: {}", cheapestProduct);

        return cheapestProduct;
    }
}
