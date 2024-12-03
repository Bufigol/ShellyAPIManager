package com.bufigol.config;

import com.bufigol.expeciones.ConfigurationException;
import org.junit.jupiter.api.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
class BBDDConfigIntegrationTest {
    private BBDDConfig bbddConfig;
    private static final Path REAL_CONFIG_PATH = Paths.get("src", "main", "resources", "config", "database.json");

    @BeforeEach
    void setUp() {
        bbddConfig = new BBDDConfig(REAL_CONFIG_PATH);
    }

    @AfterEach
    void tearDown() {
        bbddConfig = null;
    }

    @Test
    @DisplayName("Debería cargar la configuración real exitosamente")
    void loadRealConfig_Success() {
        assertDoesNotThrow(() -> {
            bbddConfig.loadConfig();
            assertTrue(bbddConfig.isLoaded());
        });
    }

    @Test
    @DisplayName("Debería obtener la URL de la base de datos real")
    void getRealDatabaseUrl_Success() {
        assertDoesNotThrow(() -> {
            bbddConfig.loadConfig();
            String url = bbddConfig.getDatabaseUrl();
            assertNotNull(url);
            assertTrue(url.startsWith("jdbc:mysql://"));
        });
    }

    @Test
    @DisplayName("Debería obtener las credenciales de la base de datos real")
    void getRealDatabaseCredentials_Success() {
        assertDoesNotThrow(() -> {
            bbddConfig.loadConfig();
            String username = bbddConfig.getUsername();
            String password = bbddConfig.getPassword();
            assertNotNull(username);
            assertNotNull(password);
            assertFalse(username.isEmpty());
            assertFalse(password.isEmpty());
        });
    }

    @Test
    @DisplayName("Debería obtener la configuración del pool real")
    void getRealPoolConfiguration_Success() {
        assertDoesNotThrow(() -> {
            bbddConfig.loadConfig();
            int maxSize = bbddConfig.getPoolMaxSize();
            int timeout = bbddConfig.getPoolTimeout();
            assertTrue(maxSize > 0);
            assertTrue(timeout > 0);
        });
    }

    @Test
    @DisplayName("Debería obtener el driver de la base de datos real")
    void getRealDatabaseDriver_Success() {
        assertDoesNotThrow(() -> {
            bbddConfig.loadConfig();
            String driver = bbddConfig.getDriver();
            assertNotNull(driver);
            assertEquals("com.mysql.cj.jdbc.Driver", driver);
        });
    }

    @Test
    @DisplayName("Debería validar la estructura completa del archivo real")
    void validateRealConfigStructure_Success() {
        assertDoesNotThrow(() -> {
            bbddConfig.loadConfig();

            // Verificar que todos los campos necesarios existen
            assertNotNull(bbddConfig.getDatabaseUrl());
            assertNotNull(bbddConfig.getUsername());
            assertNotNull(bbddConfig.getPassword());
            assertNotNull(bbddConfig.getDriver());
            assertTrue(bbddConfig.getPoolMaxSize() > 0);
            assertTrue(bbddConfig.getPoolTimeout() > 0);

            // Verificar que la URL tiene el formato correcto
            String url = bbddConfig.getDatabaseUrl();
            assertTrue(url.matches("jdbc:mysql://[^/]+/\\w+"));
        });
    }

    @Test
    @DisplayName("Debería poder recargar la configuración real sin errores")
    void reloadRealConfig_Success() {
        assertDoesNotThrow(() -> {
            bbddConfig.loadConfig();
            bbddConfig.reloadConfig();
            assertTrue(bbddConfig.isLoaded());
        });
    }

    @Test
    @DisplayName("Debería mantener la consistencia en múltiples lecturas")
    void multipleReads_ConsistentValues() {
        assertDoesNotThrow(() -> {
            bbddConfig.loadConfig();
            String url1 = bbddConfig.getDatabaseUrl();
            String url2 = bbddConfig.getDatabaseUrl();
            assertEquals(url1, url2);

            int maxSize1 = bbddConfig.getPoolMaxSize();
            int maxSize2 = bbddConfig.getPoolMaxSize();
            assertEquals(maxSize1, maxSize2);
        });
    }
}