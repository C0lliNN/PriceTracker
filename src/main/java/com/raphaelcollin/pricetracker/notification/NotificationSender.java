package com.raphaelcollin.pricetracker.notification;

import com.raphaelcollin.pricetracker.product.Product;

public interface NotificationSender {
    void send(Product product);
}
