package com.bufigol.modelo.auxiliares;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("WifiStatus Tests")
class WifiStatusTest {

    @Nested
    @DisplayName("Distance Calculation Tests")
    class DistanceCalculationTests {

        @ParameterizedTest(name = "RSSI={0}, A={1}, N={2} should give distance ~{3}m")
        @CsvSource({
                "-50, -30, 2.7, 0.1816",  // Near distance
                "-70, -30, 2.7, 0.0330", // Medium distance
                "-90, -30, 2.7, 0.0060"  // Far distance
        })
        void calculateDistanceShouldMatchExpectedValues(double rssi, double a, double n, double expected) {
            double distance = WifiStatus.calculateDistance(rssi, a, n);
            assertEquals(expected, distance, 0.0001);
        }

        @Test
        @DisplayName("Should handle minimum RSSI value (-100 dBm)")
        void shouldHandleMinimumRSSI() {
            double distance = WifiStatus.calculateDistance(-100, -30, 2.7);
            assertTrue(distance > 0);
            assertTrue(distance < 0.01);
        }

        @Test
        @DisplayName("Should handle edge case RSSI values")
        void shouldHandleEdgeCaseRSSI() {
            double closeDistance = WifiStatus.calculateDistance(-20, -30, 2.7);
            assertTrue(closeDistance > 0.99);

            double farDistance = WifiStatus.calculateDistance(-110, -30, 2.7);
            assertTrue(farDistance < 0.01);
        }

        @Test
        @DisplayName("Should throw exception for invalid path loss exponent")
        void shouldThrowExceptionForInvalidN() {
            assertThrows(IllegalArgumentException.class, () ->
                    WifiStatus.calculateDistance(-70, -30, 0));
            assertThrows(IllegalArgumentException.class, () ->
                    WifiStatus.calculateDistance(-70, -30, -2.7));
        }
    }

    @Nested
    @DisplayName("Constructor and Basic Operations Tests")
    class BasicOperationsTests {
        private final String validIp = "192.168.1.1";
        private final String validStatus = "connected";
        private final String validSsid = "MyWifi";
        private final int validRssi = -70;

        @Test
        @DisplayName("Default constructor should initialize with null/zero values")
        void defaultConstructorShouldInitializeCorrectly() {
            WifiStatus status = new WifiStatus();
            assertNull(status.getStaIp());
            assertNull(status.getStatus());
            assertNull(status.getSsid());
            assertEquals(0, status.getRssi());
        }

        @Test
        @DisplayName("Parameterized constructor should set all fields correctly")
        void parameterizedConstructorShouldSetAllFields() {
            WifiStatus status = new WifiStatus(validIp, validStatus, validSsid, validRssi);
            assertEquals(validIp, status.getStaIp());
            assertEquals(validStatus, status.getStatus());
            assertEquals(validSsid, status.getSsid());
            assertEquals(validRssi, status.getRssi());
        }

        @Test
        @DisplayName("Setters should update fields correctly")
        void settersShouldUpdateFields() {
            WifiStatus status = new WifiStatus();

            status.setStaIp(validIp);
            assertEquals(validIp, status.getStaIp());

            status.setStatus(validStatus);
            assertEquals(validStatus, status.getStatus());

            status.setSsid(validSsid);
            assertEquals(validSsid, status.getSsid());

            status.setRssi(validRssi);
            assertEquals(validRssi, status.getRssi());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {
        @Test
        @DisplayName("Equal objects should have same hashcode")
        void equalObjectsShouldHaveSameHashcode() {
            WifiStatus status1 = new WifiStatus("192.168.1.1", "connected", "MyWifi", -70);
            WifiStatus status2 = new WifiStatus("192.168.1.1", "connected", "MyWifi", -70);

            assertEquals(status1, status2);
            assertEquals(status1.hashCode(), status2.hashCode());
        }

        @Test
        @DisplayName("Objects with different values should not be equal")
        void differentObjectsShouldNotBeEqual() {
            WifiStatus status1 = new WifiStatus("192.168.1.1", "connected", "MyWifi", -70);
            WifiStatus status2 = new WifiStatus("192.168.1.2", "connected", "MyWifi", -70);
            WifiStatus status3 = new WifiStatus("192.168.1.1", "disconnected", "MyWifi", -70);
            WifiStatus status4 = new WifiStatus("192.168.1.1", "connected", "OtherWifi", -70);
            WifiStatus status5 = new WifiStatus("192.168.1.1", "connected", "MyWifi", -80);

            assertNotEquals(status1, status2);
            assertNotEquals(status1, status3);
            assertNotEquals(status1, status4);
            assertNotEquals(status1, status5);
            assertNotEquals(status1, null);
            assertNotEquals(status1, new Object());
        }
    }

    @Nested
    @DisplayName("ToString Test")
    class ToStringTest {
        @Test
        @DisplayName("ToString should include all fields")
        void toStringShouldIncludeAllFields() {
            WifiStatus status = new WifiStatus("192.168.1.1", "connected", "MyWifi", -70);
            String result = status.toString();

            assertTrue(result.contains("staIp='192.168.1.1'"));
            assertTrue(result.contains("status='connected'"));
            assertTrue(result.contains("ssid='MyWifi'"));
            assertTrue(result.contains("rssi=-70"));
        }
    }
}