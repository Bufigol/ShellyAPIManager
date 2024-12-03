package com.bufigol.config;

import com.bufigol.expeciones.ConfigurationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileWriter;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class BBDDConfigTest {
    private BBDDConfig bbddConfig;
    @TempDir
    Path tempDir;
    private Path configPath;

    private static final String VALID_CONFIG = """
        {
            "url": "jdbc:mysql://localhost:3306/testdb",
            "username": "testuser",
            "password": "testpass",
            "driver": "com.mysql.cj.jdbc.Driver",
            "pool": {
                "max_size": 10,
                "timeout": 30
            }
        }
        """;

    private static final String INVALID_CONFIG = """
            {
                "url": "jdbc:mysql://localhost:3306/testdb",
                "username": "testuser"
                // Missing required fields
            }
            """;

    @BeforeEach
    void setUp() throws Exception {
        // Crear un archivo de configuración temporal válido
        configPath = tempDir.resolve("database.json");
        try (FileWriter writer = new FileWriter(configPath.toFile())) {
            writer.write(VALID_CONFIG);
        }
        bbddConfig = new BBDDConfig(configPath);
    }

    @AfterEach
    void tearDown() {
        bbddConfig = null;
    }

    @Test
    void loadConfig_ValidConfiguration_Success() {
        assertDoesNotThrow(() -> bbddConfig.loadConfig());
        assertTrue(bbddConfig.isLoaded());
    }

    @Test
    void loadConfig_InvalidConfiguration_ThrowsException() {
        // Crear archivo con configuración inválida
        assertThrows(ConfigurationException.class, () -> {
            try (FileWriter writer = new FileWriter(configPath.toFile())) {
                writer.write(INVALID_CONFIG);
            }
            bbddConfig.loadConfig();
        });
    }

    @Test
    void loadConfig_SpecificProperty_ReturnsCorrectValue() {
        assertDoesNotThrow(() -> {
            bbddConfig.loadConfig();
            String url = bbddConfig.loadConfig("url");
            assertEquals("jdbc:mysql://localhost:3306/testdb", url);
        });
    }

    @Test
    void loadConfig_NonExistentProperty_ThrowsException() {
        assertThrows(ConfigurationException.class, () -> {
            bbddConfig.loadConfig();
            bbddConfig.loadConfig("nonexistent");
        });
    }

    @Test
    void reloadConfig_UpdatedConfiguration_ReflectsChanges() throws Exception {
        bbddConfig.loadConfig();
        String originalUrl = bbddConfig.getDatabaseUrl();

        // Modificar el archivo de configuración
        String updatedConfig = VALID_CONFIG.replace("testdb", "newdb");
        try (FileWriter writer = new FileWriter(configPath.toFile())) {
            writer.write(updatedConfig);
        }

        bbddConfig.reloadConfig();
        String newUrl = bbddConfig.getDatabaseUrl();
        assertNotEquals(originalUrl, newUrl);
        assertTrue(newUrl.contains("newdb"));
    }

    @Test
    void getConfigPath_ReturnsCorrectPath() {
        String configPath = bbddConfig.getConfigPath();
        assertNotNull(configPath);
        assertTrue(configPath.endsWith("database.json"));
    }

    @Test
    void getDatabaseUrl_ReturnsCorrectUrl() throws Exception {
        bbddConfig.loadConfig();
        String url = bbddConfig.getDatabaseUrl();
        assertEquals("jdbc:mysql://localhost:3306/testdb", url);
    }

    @Test
    void getUsername_ReturnsCorrectUsername() throws Exception {
        bbddConfig.loadConfig();
        String username = bbddConfig.getUsername();
        assertEquals("testuser", username);
    }

    @Test
    void getPassword_ReturnsCorrectPassword() throws Exception {
        bbddConfig.loadConfig();
        String password = bbddConfig.getPassword();
        assertEquals("testpass", password);
    }

    @Test
    void getDriver_ReturnsCorrectDriver() throws Exception {
        bbddConfig.loadConfig();
        String driver = bbddConfig.getDriver();
        assertEquals("com.mysql.cj.jdbc.Driver", driver);
    }

    @Test
    void getPoolMaxSize_ReturnsCorrectSize() throws Exception {
        bbddConfig.loadConfig();
        int maxSize = bbddConfig.getPoolMaxSize();
        assertEquals(10, maxSize);
    }

    @Test
    void getPoolTimeout_ReturnsCorrectTimeout() throws Exception {
        bbddConfig.loadConfig();
        int timeout = bbddConfig.getPoolTimeout();
        assertEquals(30, timeout);
    }

    @Test
    void saveConfig_ValidConfiguration_SavesSuccessfully() {
        assertDoesNotThrow(() -> {
            bbddConfig.loadConfig();
            bbddConfig.saveConfig();
        });
    }

    @Test
    void saveConfig_SpecificProperty_SavesSuccessfully() {
        assertDoesNotThrow(() -> {
            bbddConfig.loadConfig();
            bbddConfig.saveConfig("url");
        });
    }
}