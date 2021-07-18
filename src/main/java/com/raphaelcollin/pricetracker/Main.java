package com.raphaelcollin.pricetracker;

import com.raphaelcollin.pricetracker.product.AmazonProductFetcher;
import com.raphaelcollin.pricetracker.product.Product;
import com.raphaelcollin.pricetracker.product.ProductFetcher;
import com.raphaelcollin.pricetracker.utils.CustomHttpClient;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        final CustomHttpClient httpClient = new CustomHttpClient();

        final Collection<ProductFetcher> productFetchers = new LinkedList<>();
        productFetchers.add(new AmazonProductFetcher(httpClient));

        final Product cheapestProduct = productFetchers
                .stream()
                .map(fetcher -> fetcher.fetchProducts("SSD Kingston"))
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .peek(System.out::println)
                .min(Comparator.comparing(Product::getPrice))
                .orElseThrow(() -> new RuntimeException("No products was fetched"));

        System.out.println(cheapestProduct);
    }
}
