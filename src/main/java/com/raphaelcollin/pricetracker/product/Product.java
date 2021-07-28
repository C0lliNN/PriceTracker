package com.raphaelcollin.pricetracker.product;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Product {
    @NonNull
    String title;
    @NonNull
    String link;
    @NonNull
    BigDecimal price;

    public String getAbbreviatedTitle() {
        if (title.length() <= 20) {
            return title;
        }
        return title.substring(0, 20);
    }
}
