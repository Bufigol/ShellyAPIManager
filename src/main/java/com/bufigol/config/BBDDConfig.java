package com.bufigol.config;

import com.bufigol.expeciones.ConfigurationException;
import com.bufigol.interfaces.INT_Configuracion;
import com.bufigol.utils.JSONUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Implementación de la configuración para la base de datos.
 * Esta clase maneja la configuración específica para la conexión
 * y gestión de la base de datos, incluyendo URL, credenciales y
 * configuración del pool de conexiones.
 */
public class BBDDConfig implements INT_Configuracion {
    private static final String CONFIG_FILE_NAME = "database.json";
    private static final String CONFIG_FOLDER = "config";

    // Campos de configuración
    private static final String DB_URL = "url";
    private static final String DB_USERNAME = "username";
    private static final String DB_PASSWORD = "password";
    private static final String POOL_CONFIG = "pool";
    private static final String POOL_MAX_SIZE = "max_size";
    private static final String POOL_TIMEOUT = "timeout";
    private static final String DB_DRIVER = "driver";

    private final Path configPath;
    private final JSONUtils jsonUtils;
    private final ReadWriteLock lock;
    private boolean loaded;
    private Map<String, Object> configuration;

    /**
     * Constructor por defecto que inicializa la configuración con la ruta estándar.
     */
    public BBDDConfig() {
        this.configPath = resolveDefaultConfigPath();
        this.jsonUtils = JSONUtils.getInstance();
        this.lock = new ReentrantReadWriteLock();
        this.configuration = new HashMap<>();
        this.loaded = false;
        createConfigDirectoryIfNeeded();
    }

    /**
     * Constructor que permite especificar una ruta de archivo personalizada.
     * @param configPath Path al archivo de configuración
     */
    public BBDDConfig(Path configPath) {
        this.configPath = configPath;
        this.jsonUtils = JSONUtils.getInstance();
        this.lock = new ReentrantReadWriteLock();
        this.configuration = new HashMap<>();
        this.loaded = false;
        createConfigDirectoryIfNeeded();
    }

    private Path resolveDefaultConfigPath() {
        try {
            // Intenta obtener el recurso del classpath
            URL resourceUrl = BBDDConfig.class.getClassLoader()
                    .getResource(CONFIG_FOLDER + "/" + CONFIG_FILE_NAME);

            if (resourceUrl != null) {
                try {
                    return Paths.get(resourceUrl.toURI());
                } catch (URISyntaxException e) {
                    // Si falla la conversión URI, intentar con ruta relativa
                    return Paths.get(CONFIG_FOLDER, CONFIG_FILE_NAME);
                }
            }

            // Si no encuentra el recurso, usar ruta relativa
            return Paths.get(CONFIG_FOLDER, CONFIG_FILE_NAME);
        } catch (Exception e) {
            // En caso de cualquier error, usar ruta relativa
            return Paths.get(CONFIG_FOLDER, CONFIG_FILE_NAME);
        }
    }

    private void createConfigDirectoryIfNeeded() {
        try {
            Path parentDir = configPath.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
        } catch (IOException e) {
            throw new ConfigurationException("No se pudo crear el directorio de configuración", e);
        }
    }

    @Override
    public void loadConfig() throws ConfigurationException {
        lock.writeLock().lock();
        try {
            configuration = jsonUtils.readConfigFile(configPath.toString());
            validateConfiguration();
            loaded = true;
        } catch (Exception e) {
            loaded = false;
            throw new ConfigurationException("Error al cargar la configuración de base de datos: " + e.getMessage(), e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String loadConfig(String name) throws ConfigurationException {
        lock.readLock().lock();
        try {
            if (!loaded) {
                throw new ConfigurationException("La configuración no está cargada. Llame a loadConfig() primero.");
            }
            Object value = configuration.get(name);
            if (value == null) {
                throw new ConfigurationException("Propiedad no encontrada: " + name);
            }
            return value.toString();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void reloadConfig() throws ConfigurationException {
        loadConfig();
    }

    @Override
    public boolean isLoaded() {
        lock.readLock().lock();
        try {
            return loaded;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public String getConfigPath() {
        return configPath.toString();
    }

    @Override
    public void saveConfig() throws ConfigurationException {
        lock.writeLock().lock();
        try {
            validateConfiguration();
            jsonUtils.saveConfigFile(configuration, configPath.toString());
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void saveConfig(String name) throws ConfigurationException {
        lock.writeLock().lock();
        try {
            if (!configuration.containsKey(name)) {
                throw new ConfigurationException("Propiedad no encontrada: " + name);
            }
            jsonUtils.saveConfigValue(name, configuration.get(name).toString());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public String getDatabaseUrl() throws ConfigurationException {
        lock.readLock().lock();
        try {
            return loadConfig(DB_URL);
        } finally {
            lock.readLock().unlock();
        }
    }

    public String getUsername() throws ConfigurationException {
        lock.readLock().lock();
        try {
            return loadConfig(DB_USERNAME);
        } finally {
            lock.readLock().unlock();
        }
    }

    public String getPassword() throws ConfigurationException {
        lock.readLock().lock();
        try {
            return loadConfig(DB_PASSWORD);
        } finally {
            lock.readLock().unlock();
        }
    }

    public String getDriver() throws ConfigurationException {
        lock.readLock().lock();
        try {
            return loadConfig(DB_DRIVER);
        } finally {
            lock.readLock().unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public int getPoolMaxSize() throws ConfigurationException {
        lock.readLock().lock();
        try {
            Map<String, Object> poolConfig = (Map<String, Object>) configuration.get(POOL_CONFIG);
            if (poolConfig == null || !poolConfig.containsKey(POOL_MAX_SIZE)) {
                throw new ConfigurationException("Configuración del pool no encontrada");
            }
            // Convertir el número a double primero y luego a int
            double value = Double.parseDouble(poolConfig.get(POOL_MAX_SIZE).toString());
            return (int) value;
        } finally {
            lock.readLock().unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public int getPoolTimeout() throws ConfigurationException {
        lock.readLock().lock();
        try {
            Map<String, Object> poolConfig = (Map<String, Object>) configuration.get(POOL_CONFIG);
            if (poolConfig == null || !poolConfig.containsKey(POOL_TIMEOUT)) {
                throw new ConfigurationException("Configuración del pool no encontrada");
            }
            // Convertir el número a double primero y luego a int
            double value = Double.parseDouble(poolConfig.get(POOL_TIMEOUT).toString());
            return (int) value;
        } finally {
            lock.readLock().unlock();
        }
    }

    private void validateConfiguration() throws ConfigurationException {
        if (configuration == null) {
            throw new ConfigurationException("La configuración es nula");
        }

        validateField(DB_URL, "URL de la base de datos");
        validateField(DB_USERNAME, "Nombre de usuario");
        validateField(DB_PASSWORD, "Contraseña");
        validateField(DB_DRIVER, "Driver de la base de datos");
        validateField(POOL_CONFIG, "Configuración del pool");

        @SuppressWarnings("unchecked")
        Map<String, Object> poolConfig = (Map<String, Object>) configuration.get(POOL_CONFIG);
        if (poolConfig == null || !poolConfig.containsKey(POOL_MAX_SIZE) || !poolConfig.containsKey(POOL_TIMEOUT)) {
            throw new ConfigurationException("Configuración del pool incompleta o inválida");
        }
    }

    private void validateField(String fieldName, String fieldDescription) throws ConfigurationException {
        if (!configuration.containsKey(fieldName) || configuration.get(fieldName) == null) {
            throw new ConfigurationException("Falta el campo requerido: " + fieldDescription);
        }
    }
}