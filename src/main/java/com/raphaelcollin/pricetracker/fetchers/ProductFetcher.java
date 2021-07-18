package com.raphaelcollin.pricetracker.fetchers;

import com.raphaelcollin.pricetracker.Product;

import java.util.Collection;

public interface ProductFetcher {
    Collection<Product> fetchProducts(String query);
}
