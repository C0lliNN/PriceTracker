package com.raphaelcollin.pricetracker.product;

import java.util.Collection;

public interface ProductFetcher {
    Collection<Product> fetchProducts(String query);
}
