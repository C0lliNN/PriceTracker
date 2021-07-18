package com.raphaelcollin.pricetracker;

import com.raphaelcollin.pricetracker.product.AmazonProductFetcher;
import com.raphaelcollin.pricetracker.product.ProductFetcher;
import com.raphaelcollin.pricetracker.utils.CustomHttpClient;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        final Collection<ProductFetcher> priceFetchers = new LinkedList<>();

        final CustomHttpClient httpClient = new CustomHttpClient();

        priceFetchers.add(new AmazonProductFetcher(httpClient));

        priceFetchers
                .stream()
                .map(fetcher -> fetcher.fetchProducts("SSD Kingston"))
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .forEach(System.out::println);
    }
}
