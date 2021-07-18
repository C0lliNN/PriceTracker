package com.raphaelcollin.pricetracker.fetchers;

import com.raphaelcollin.pricetracker.Product;

import java.math.BigDecimal;

public class AmazonPriceFetcher implements  PriceFetcher {

    @Override
    public Product fetchProduct(final String query) {
        return new Product("1", BigDecimal.valueOf(23d));
    }
}
