package com.raphaelcollin.pricetracker;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class Product {
    String name;
    BigDecimal price;
}
