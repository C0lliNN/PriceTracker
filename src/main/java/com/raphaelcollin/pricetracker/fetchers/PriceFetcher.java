package com.raphaelcollin.pricetracker.fetchers;

import com.raphaelcollin.pricetracker.Product;

public interface PriceFetcher {
    Product fetchProduct(String query);
}
