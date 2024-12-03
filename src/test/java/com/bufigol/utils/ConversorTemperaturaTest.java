package com.bufigol.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

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

        @ParameterizedTest(name = "Body temperature {0}°C should be around 98.6°F")
        @ValueSource(doubles = {36.5, 37.0, 37.5})
        void shouldHandleBodyTemperatures(double celsius) {
            double fahrenheit = ConversorTemperatura.celsiusAFahrenheit(celsius);
            assertTrue(fahrenheit >= 97.7 && fahrenheit <= 99.5,
                    "Body temperature conversion out of normal range");
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

        @Test
        @DisplayName("Should handle room temperature range")
        void shouldHandleRoomTemperatureRange() {
            double[] roomTempsF = {68, 72, 75};
            double[] expectedC = {20, 22.22, 23.89};

            for (int i = 0; i < roomTempsF.length; i++) {
                assertEquals(expectedC[i],
                        ConversorTemperatura.fahrenheitACelsius(roomTempsF[i]), 0.1,
                        "Room temperature conversion incorrect");
            }
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

        @Test
        @DisplayName("Should be reversible")
        void shouldBeReversible() {
            double originalCelsius = 25;
            double fahrenheit = ConversorTemperatura.celsiusAFahrenheit(originalCelsius);
            double backToCelsius = ConversorTemperatura.fahrenheitACelsius(fahrenheit);
            assertEquals(originalCelsius, backToCelsius, 0.1,
                    "Converting back and forth should return original value");
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {
        @Test
        @DisplayName("Should handle multiple conversions efficiently")
        void shouldHandleMultipleConversions() {
            long startTime = System.nanoTime();
            for (int i = 0; i < 1000000; i++) {
                ConversorTemperatura.celsiusAFahrenheit(i % 100);
                ConversorTemperatura.fahrenheitACelsius(i % 100);
            }
            long endTime = System.nanoTime();
            assertTrue((endTime - startTime) < 1000000000,
                    "Conversions took too long to complete");
        }
    }
}