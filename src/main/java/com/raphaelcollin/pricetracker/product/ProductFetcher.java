package com.raphaelcollin.pricetracker.product;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ProductFetcher {

    CompletableFuture<List<Product>> fetchProducts(String query);

}
