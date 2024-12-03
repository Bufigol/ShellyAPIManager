package com.bufigol.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Temperature Converter Tests")
class ConversorTemperaturaTest {

    @Nested
    @DisplayName("Celsius to Fahrenheit Tests")
    class CelsiusToFahrenheitTests {
        @ParameterizedTest(name = "{0}°C should convert to {1}°F")
        @CsvSource({
                "0, 32",
                "100, 212",
                "-40, -40",
                "37, 98.6",
                "-17.78, 0"
        })
        void shouldConvertCelsiusToFahrenheit(double celsius, double expectedFahrenheit) {
            assertEquals(expectedFahrenheit, ConversorTemperatura.celsiusAFahrenheit(celsius), 0.1,
                    "Incorrect Celsius to Fahrenheit conversion");
        }
    }

    @Nested
    @DisplayName("Fahrenheit to Celsius Tests")
    class FahrenheitToCelsiusTests {
        @ParameterizedTest(name = "{0}°F should convert to {1}°C")
        @CsvSource({
                "32, 0",
                "212, 100",
                "-40, -40",
                "98.6, 37",
                "0, -17.78"
        })
        void shouldConvertFahrenheitToCelsius(double fahrenheit, double expectedCelsius) {
            assertEquals(expectedCelsius, ConversorTemperatura.fahrenheitACelsius(fahrenheit), 0.1,
                    "Incorrect Fahrenheit to Celsius conversion");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Special Values")
    class EdgeCasesTests {
        @Test
        @DisplayName("Should handle very high temperatures")
        void shouldHandleHighTemperatures() {
            assertEquals(1832, ConversorTemperatura.celsiusAFahrenheit(1000), 0.1);
            assertEquals(1000, ConversorTemperatura.fahrenheitACelsius(1832), 0.1);
        }

        @Test
        @DisplayName("Should handle very low temperatures")
        void shouldHandleLowTemperatures() {
            assertEquals(-459.67, ConversorTemperatura.celsiusAFahrenheit(-273.15), 0.1);
            assertEquals(-273.15, ConversorTemperatura.fahrenheitACelsius(-459.67), 0.1);
        }
    }
}