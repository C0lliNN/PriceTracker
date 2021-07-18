package com.raphaelcollin.pricetracker;

import com.raphaelcollin.pricetracker.fetchers.PriceFetcher;

import java.util.Collection;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        Collection<PriceFetcher> priceFetchers = new LinkedList<>();

        System.out.println(priceFetchers);
    }
}
