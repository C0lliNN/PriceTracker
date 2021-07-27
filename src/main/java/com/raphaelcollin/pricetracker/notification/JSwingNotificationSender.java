package com.raphaelcollin.pricetracker.notification;

import com.raphaelcollin.pricetracker.product.Product;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;

@Slf4j
public class JSwingNotificationSender implements NotificationSender {

    @Override
    public void send(final Product product) {
        JFrame frame = new JFrame("Promotion Alert");

        Box box = Box.createVerticalBox();
        box.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(product.getAbbreviatedTitle() + " found costing R$ " + product.getPrice());
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton button = new JButton("Show product");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener((e) -> openWebPage(product.getLink()));

        box.add(Box.createVerticalStrut(50));
        box.add(label);
        box.add(Box.createVerticalStrut(10));
        box.add(button);

        frame.setContentPane(box);
        frame.setSize(400, 200);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void openWebPage(String url) {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(URI.create(url));
        } catch (IOException e) {
            log.error("It was not possible to open web page: {}", e.getMessage(), e);
        }
    }
}
