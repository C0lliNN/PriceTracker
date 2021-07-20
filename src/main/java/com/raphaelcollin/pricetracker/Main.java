package com.raphaelcollin.pricetracker;

import com.raphaelcollin.pricetracker.product.AmazonProductFetcher;
import com.raphaelcollin.pricetracker.product.Product;
import com.raphaelcollin.pricetracker.product.ProductFetcher;
import com.raphaelcollin.pricetracker.utils.CustomHttpClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;

@Slf4j
public class Main {
    public static void main(String[] args) {
        final CustomHttpClient httpClient = new CustomHttpClient();

        final Collection<ProductFetcher> productFetchers = new LinkedList<>();
        productFetchers.add(new AmazonProductFetcher(httpClient));

        final Product cheapestProduct = productFetchers
                .stream()
                .map(fetcher -> fetcher.fetchProducts("SSD Kingston"))
                .flatMap(Collection::stream)
                .min(Comparator.comparing(Product::getPrice))
                .orElseThrow(() -> new RuntimeException("No products was fetched"));

        log.info("Cheapest Product: {}", cheapestProduct);
    }
}
