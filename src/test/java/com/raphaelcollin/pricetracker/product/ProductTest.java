package com.raphaelcollin.pricetracker.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Nested
    @DisplayName("method: getAbbreviatedTitle()")
    class GetAbbreviatedTitleMethod {

        @Test
        @DisplayName("when title has less than 20 chars, then it should not abbreviate")
        void whenTitleHasLessThan20Chars_shouldNotAbbreviate() {
            final Product product = new Product("Some title", "", BigDecimal.ONE);

            assertEquals("Some title", product.getAbbreviatedTitle());
        }

        @Test
        @DisplayName("when title has more than 20 chars, then it should return the first 20 chars")
        void whenTitleHasMoreThan20Chars_shouldReturnTheFirst20Chars() {
            final String title = UUID.randomUUID().toString();
            final Product product = new Product(title, "", BigDecimal.ONE);

            assertEquals(title.substring(0, 20), product.getAbbreviatedTitle());
        }
    }
}