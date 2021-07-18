package com.raphaelcollin.pricetracker;

import com.raphaelcollin.pricetracker.product.AmazonProductFetcher;
import com.raphaelcollin.pricetracker.product.ProductFetcher;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        Collection<ProductFetcher> priceFetchers = new LinkedList<>();

        priceFetchers.add(new AmazonProductFetcher());

        priceFetchers
                .stream()
                .map(fetcher -> fetcher.fetchProducts("SSD Kingston"))
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .forEach(System.out::println);
    }
}
