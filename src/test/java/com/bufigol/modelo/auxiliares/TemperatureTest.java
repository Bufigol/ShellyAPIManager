package com.bufigol.modelo.auxiliares;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Temperature Tests")
class TemperatureTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        @Test
        @DisplayName("Default constructor should initialize with zero values")
        void defaultConstructorShouldInitializeWithZero() {
            Temperature temp = new Temperature();
            assertEquals(0, temp.getId());
            assertEquals(0.0, temp.gettC());
            assertEquals(0.0, temp.gettF());
        }

        @Test
        @DisplayName("Constructor with ID should initialize temperatures to zero")
        void constructorWithIdShouldInitializeTempsToZero() {
            Temperature temp = new Temperature(1);
            assertEquals(1, temp.getId());
            assertEquals(0.0, temp.gettC());
            assertEquals(0.0, temp.gettF());
        }

        @ParameterizedTest(name = "Constructor with {0}°{1} should set both units correctly")
        @CsvSource({
                "25, C, 25.0, 77.0",
                "77, F, 25.0, 77.0",
                "0, C, 0.0, 32.0",
                "32, F, 0.0, 32.0"
        })
        void constructorWithTempAndUnitShouldSetBothUnits(double temp, char unit, double expectedC, double expectedF) {
            Temperature temperature = new Temperature(1, temp, unit);
            assertEquals(expectedC, temperature.gettC(), 0.1);
            assertEquals(expectedF, temperature.gettF(), 0.1);
        }
    }

    @Nested
    @DisplayName("Setter Tests")
    class SetterTests {
        @Test
        @DisplayName("Setting Celsius should update Fahrenheit")
        void settingCelsiusShouldUpdateFahrenheit() {
            Temperature temp = new Temperature();
            temp.settC(25);
            assertEquals(77.0, temp.gettF(), 0.1);
        }

        @Test
        @DisplayName("Setting Fahrenheit should update Celsius")
        void settingFahrenheitShouldUpdateCelsius() {
            Temperature temp = new Temperature();
            temp.settF(77);
            assertEquals(25.0, temp.gettC(), 0.1);
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {
        @Test
        @DisplayName("Equal objects should have same hashcode")
        void equalObjectsShouldHaveSameHashcode() {
            Temperature temp1 = new Temperature(1, 25.0, 77.0);
            Temperature temp2 = new Temperature(1, 25.0, 77.0);

            assertEquals(temp1, temp2);
            assertEquals(temp1.hashCode(), temp2.hashCode());
        }

        @Test
        @DisplayName("Objects with different IDs should not be equal")
        void objectsWithDifferentIdsShouldNotBeEqual() {
            Temperature temp1 = new Temperature(1, 25.0, 77.0);
            Temperature temp2 = new Temperature(2, 25.0, 77.0);

            assertNotEquals(temp1, temp2);
        }
    }

    @Nested
    @DisplayName("Invalid Input Tests")
    class InvalidInputTests {
        @Test
        @DisplayName("Constructor should handle invalid temperature unit")
        void shouldHandleInvalidTemperatureUnit() {
            Temperature temp = new Temperature(1, 25, 'X');
            assertEquals(0.0, temp.gettC());
            assertEquals(0.0, temp.gettF());
        }

        @ParameterizedTest
        @ValueSource(ints = {-1000, Integer.MIN_VALUE, Integer.MAX_VALUE})
        @DisplayName("Should handle extreme sensor IDs")
        void shouldHandleExtremeSensorIds(int id) {
            Temperature temp = new Temperature(id);
            assertEquals(id, temp.getId());
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {
        @Test
        @DisplayName("ToString should contain all fields")
        void toStringShouldContainAllFields() {
            Temperature temp = new Temperature(1, 25.0, 77.0);
            String str = temp.toString();

            assertTrue(str.contains("id=1"));
            assertTrue(str.contains("tC=25.0"));
            assertTrue(str.contains("tF=77.0"));
        }
    }
}