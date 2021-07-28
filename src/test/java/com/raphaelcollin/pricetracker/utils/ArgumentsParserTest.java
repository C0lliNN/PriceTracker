package com.raphaelcollin.pricetracker.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentsParserTest {

    @Nested
    @DisplayName("method: ArgumentsParser(String[])")
    class ConstructorTest {

        @Test
        @DisplayName("when the args size is not even, then it should throw an exception")
        void whenTheArgsSizeIsNotEvent_shouldThrowAnException() {
            final String[] args = {"--price", "242", "--product"};

            assertThrows(IllegalArgumentException.class, () -> new ArgumentsParser(args));
        }

        @Test
        @DisplayName("when the args size is even, then it should not throw any exception")
        void whenTheArgsSizeIsEven_shouldNotThrowAnyException() {
            final String[] args = {"--price", "242", "--product", "SSD Kingston"};

            assertDoesNotThrow(() -> new ArgumentsParser(args));
        }
    }

    @Nested
    @DisplayName("method: getParameter(String)")
    class GetParameterMethod {

        @Test
        @DisplayName("when the parameter is found, then it should return it")
        void whenTheParameterIsFound_shouldReturnIt() {
            final String[] args = {"--price", "242", "--product", "SSD Kingston"};

            final ArgumentsParser argumentsParser = new ArgumentsParser(args);

            assertEquals("SSD Kingston", argumentsParser.getParameter("product"));
            assertEquals("242", argumentsParser.getParameter("price"));
        }

        @Test
        @DisplayName("when the parameter is not found, then it should return null")
        void whenTheParameterIsNotFound_shouldReturnNull() {
            final String[] args = {"--price", "242"};

            final ArgumentsParser argumentsParser = new ArgumentsParser(args);

            assertNull(argumentsParser.getParameter("product"));
        }
    }
}