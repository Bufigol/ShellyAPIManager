package com.bufigol.config;

import com.bufigol.expeciones.ConfigurationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class ConfigurationManagerTest {

    private ConfigurationManager configManager;

    @TempDir
    Path tempDir;

    private Path configDir;
    private Path otherConfigPath;
    private Path shellyConfigPath;
    private Path dbConfigPath;

    @BeforeEach
    void setUp() throws IOException {
        // Crear directorio de configuración
        configDir = tempDir.resolve("config");
        Files.createDirectories(configDir);

        // Crear archivos de configuración con contenido mínimo válido
        otherConfigPath = configDir.resolve("other.json");
        shellyConfigPath = configDir.resolve("shelly.json");
        dbConfigPath = configDir.resolve("database.json");

        // Escribir configuraciones mínimas válidas
        String otherConfig = """
            {
                "log": {
                    "level": "INFO",
                    "file_path": "logs/app.log",
                    "max_size": 10,
                    "retention_days": 7
                },
                "update": {
                    "interval": 300,
                    "retry_count": 3,
                    "retry_delay": 60
                },
                "system": {
                    "timezone": "UTC",
                    "locale": "es_ES",
                    "date_format": "yyyy-MM-dd"
                }
            }""";
        Files.writeString(otherConfigPath, otherConfig);

        String shellyConfig = """
            {
                "auth_key": "test_key",
                "base_url": "https://test.shelly.cloud",
                "devices": {
                    "test_device": "test_id"
                }
            }""";
        Files.writeString(shellyConfigPath, shellyConfig);

        String dbConfig = """
            {
                "url": "jdbc:h2:mem:testdb",
                "username": "test",
                "password": "test",
                "driver": "org.h2.Driver",
                "pool": {
                    "max_size": 5,
                    "timeout": 30
                }
            }""";
        Files.writeString(dbConfigPath, dbConfig);

        // Set system property para que las clases de configuración encuentren los archivos
        System.setProperty("config.dir", configDir.toString());

        // Obtener instancia limpia del ConfigurationManager
        configManager = ConfigurationManager.getInstance();
        configManager.shutdown();
    }

    @AfterEach
    void tearDown() {
        if (configManager != null) {
            configManager.shutdown();
        }
        // Limpiar system property
        System.clearProperty("config.dir");
    }

    @Test
    void getInstance_ReturnsSingleInstance() {
        assertConfigFilesExist();
        // Test singleton pattern
        ConfigurationManager instance1 = ConfigurationManager.getInstance();
        ConfigurationManager instance2 = ConfigurationManager.getInstance();

        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2, "getInstance should return the same instance");
    }

    @Test
    void initialize_LoadsAllConfigurations() {
        assertConfigFilesExist();
        // Test successful initialization
        assertDoesNotThrow(() -> {
            configManager.initialize();
        }, "Initialize should not throw exceptions with valid configurations");

        assertTrue(configManager.isAllConfigurationsLoaded(),
                "All configurations should be loaded after initialization");
    }

    @Test
    void initialize_ThrowsException_WhenCalledTwice() {
        assertConfigFilesExist();
        // Test double initialization protection
        configManager.initialize();

        assertThrows(ConfigurationException.class, () -> {
            configManager.initialize();
        }, "Second initialization should throw ConfigurationException");
    }

    @Test
    void reloadAll_SuccessfullyReloadsConfigurations() {
        // First initialize
        configManager.initialize();

        // Test reload
        assertDoesNotThrow(() -> {
            configManager.reloadAll();
        }, "reloadAll should not throw exceptions with valid configurations");

        assertTrue(configManager.isAllConfigurationsLoaded(),
                "All configurations should remain loaded after reload");
    }

    @Test
    void reloadAll_ThrowsException_WhenNotInitialized() {
        assertThrows(ConfigurationException.class, () -> {
            configManager.reloadAll();
        }, "reloadAll should throw ConfigurationException when not initialized");
    }

    @Test
    void getShellyConfig_ReturnsValidConfiguration() {
        configManager.initialize();

        assertDoesNotThrow(() -> {
            ShellyConfig shellyConfig = configManager.getShellyConfig();
            assertNotNull(shellyConfig, "ShellyConfig should not be null");
            assertTrue(shellyConfig.isLoaded(), "ShellyConfig should be loaded");
        });
    }

    @Test
    void getBBDDConfig_ReturnsValidConfiguration() {
        configManager.initialize();

        assertDoesNotThrow(() -> {
            BBDDConfig bbddConfig = configManager.getBBDDConfig();
            assertNotNull(bbddConfig, "BBDDConfig should not be null");
            assertTrue(bbddConfig.isLoaded(), "BBDDConfig should be loaded");
        });
    }

    @Test
    void getOtherConfig_ReturnsValidConfiguration() {
        configManager.initialize();

        assertDoesNotThrow(() -> {
            OtherConfig otherConfig = configManager.getOtherConfig();
            assertNotNull(otherConfig, "OtherConfig should not be null");
            assertTrue(otherConfig.isLoaded(), "OtherConfig should be loaded");
        });
    }

    @Test
    void isAllConfigurationsLoaded_ReturnsFalse_BeforeInitialization() {
        assertFalse(configManager.isAllConfigurationsLoaded(),
                "Should return false before initialization");
    }

    @Test
    void isAllConfigurationsLoaded_ReturnsTrue_AfterInitialization() {
        configManager.initialize();
        assertTrue(configManager.isAllConfigurationsLoaded(),
                "Should return true after successful initialization");
    }

    @Test
    void shutdown_CleansUpResources() {
        configManager.initialize();
        assertTrue(configManager.isAllConfigurationsLoaded());

        configManager.shutdown();
        assertFalse(configManager.isAllConfigurationsLoaded(),
                "Configurations should not be loaded after shutdown");
    }

    @Test
    void getConfigs_ThrowException_WhenNotInitialized() {
        // Test all getter methods throw exceptions when not initialized
        assertThrows(ConfigurationException.class, () -> {
            configManager.getShellyConfig();
        }, "getShellyConfig should throw exception when not initialized");

        assertThrows(ConfigurationException.class, () -> {
            configManager.getBBDDConfig();
        }, "getBBDDConfig should throw exception when not initialized");

        assertThrows(ConfigurationException.class, () -> {
            configManager.getOtherConfig();
        }, "getOtherConfig should throw exception when not initialized");
    }

    // Métod o auxiliar para verificar que los archivos de configuración existen
    private void assertConfigFilesExist() {
        assertTrue(Files.exists(otherConfigPath), "other.json should exist");
        assertTrue(Files.exists(shellyConfigPath), "shelly.json should exist");
        assertTrue(Files.exists(dbConfigPath), "database.json should exist");
    }
}