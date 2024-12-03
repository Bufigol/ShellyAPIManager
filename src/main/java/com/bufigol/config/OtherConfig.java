package com.bufigol.config;

import com.bufigol.expeciones.ConfigurationException;
import com.bufigol.interfaces.INT_Configuracion;
import com.bufigol.utils.JSONUtils;

import java.net.URL;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Implementación de la configuración para otras configuraciones del sistema.
 * Esta clase maneja configuraciones generales como:
 * - Configuración de logs
 * - Tiempos de actualización
 * - Configuraciones de sistema
 * - Configuraciones por defecto
 */
public class OtherConfig implements INT_Configuracion {
    private static final String CONFIG_FILE_NAME = "other.json";
    private static final String CONFIG_FOLDER = "config";

    // Campos de configuración
    private static final String LOG_CONFIG = "log";
    private static final String LOG_LEVEL = "level";
    private static final String LOG_FILE_PATH = "file_path";
    private static final String LOG_MAX_SIZE = "max_size";
    private static final String LOG_RETENTION = "retention_days";

    private static final String UPDATE_CONFIG = "update";
    private static final String UPDATE_INTERVAL = "interval";
    private static final String UPDATE_RETRY_COUNT = "retry_count";
    private static final String UPDATE_RETRY_DELAY = "retry_delay";

    private static final String SYSTEM_CONFIG = "system";
    private static final String SYSTEM_TIMEZONE = "timezone";
    private static final String SYSTEM_LOCALE = "locale";
    private static final String SYSTEM_DATE_FORMAT = "date_format";

    private final String configPath;
    private final JSONUtils jsonUtils;
    private final ReadWriteLock lock;
    private boolean loaded;
    private Map<String, Object> configuration;

    /**
     * Constructor por defecto que inicializa la configuración con la ruta estándar.
     */
    public OtherConfig() {
        String configDirPath = System.getProperty("config.dir", "config");
        Path configPath = Path.of(configDirPath, CONFIG_FILE_NAME);
        this.configPath = configPath.toString();
        this.jsonUtils = JSONUtils.getInstance();
        this.lock = new ReentrantReadWriteLock();
        this.configuration = new HashMap<>();
        this.loaded = false;
    }
    /**
     * Constructor que permite especificar un nombre de archivo personalizado.
     * @param configFileName nombre del archivo de configuración
     */
    public OtherConfig(String configFileName) {
        URL resourceUrl = OtherConfig.class.getClassLoader().getResource(CONFIG_FOLDER + File.separator + configFileName);
        if (resourceUrl == null) {
            throw new ConfigurationException("No se pudo encontrar el archivo " + configFileName + " en el classpath");
        }
        this.configPath = resourceUrl.getPath();
        this.jsonUtils = JSONUtils.getInstance();
        this.lock = new ReentrantReadWriteLock();
        this.configuration = new HashMap<>();
        this.loaded = false;
    }

    @Override
    public void loadConfig() throws ConfigurationException {
        lock.writeLock().lock();
        try {
            configuration = jsonUtils.readConfigFile(configPath);
            validateConfiguration();
            loaded = true;
        } catch (Exception e) {
            loaded = false;
            throw new ConfigurationException("Error al cargar otras configuraciones: " + e.getMessage(), e);
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
        return configPath;
    }

    @Override
    public void saveConfig() throws ConfigurationException {
        lock.writeLock().lock();
        try {
            validateConfiguration();
            jsonUtils.saveConfigFile(configuration, configPath);
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

    /**
     * Métodos para obtener configuración de logs
     */
    @SuppressWarnings("unchecked")
    public String getLogLevel() throws ConfigurationException {
        lock.readLock().lock();
        try {
            Map<String, Object> logConfig = (Map<String, Object>) configuration.get(LOG_CONFIG);
            validateNestedConfig(logConfig, LOG_LEVEL, "nivel de log");
            return logConfig.get(LOG_LEVEL).toString();
        } finally {
            lock.readLock().unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public String getLogFilePath() throws ConfigurationException {
        lock.readLock().lock();
        try {
            Map<String, Object> logConfig = (Map<String, Object>) configuration.get(LOG_CONFIG);
            validateNestedConfig(logConfig, LOG_FILE_PATH, "ruta del archivo de log");
            return logConfig.get(LOG_FILE_PATH).toString();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Métodos para obtener configuración de actualizaciones
     */
    @SuppressWarnings("unchecked")
    public int getUpdateInterval() throws ConfigurationException {
        lock.readLock().lock();
        try {
            Map<String, Object> updateConfig = (Map<String, Object>) configuration.get(UPDATE_CONFIG);
            validateNestedConfig(updateConfig, UPDATE_INTERVAL, "intervalo de actualización");
            return Integer.parseInt(updateConfig.get(UPDATE_INTERVAL).toString());
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Métodos para obtener configuración del sistema
     */
    @SuppressWarnings("unchecked")
    public String getSystemTimezone() throws ConfigurationException {
        lock.readLock().lock();
        try {
            Map<String, Object> systemConfig = (Map<String, Object>) configuration.get(SYSTEM_CONFIG);
            validateNestedConfig(systemConfig, SYSTEM_TIMEZONE, "zona horaria");
            return systemConfig.get(SYSTEM_TIMEZONE).toString();
        } finally {
            lock.readLock().unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public String getSystemLocale() throws ConfigurationException {
        lock.readLock().lock();
        try {
            Map<String, Object> systemConfig = (Map<String, Object>) configuration.get(SYSTEM_CONFIG);
            validateNestedConfig(systemConfig, SYSTEM_LOCALE, "configuración regional");
            return systemConfig.get(SYSTEM_LOCALE).toString();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Valida que la configuración contenga todos los campos requeridos.
     * @throws ConfigurationException si falta algún campo requerido o el formato es inválido
     */
    private void validateConfiguration() throws ConfigurationException {
        if (configuration == null) {
            throw new ConfigurationException("La configuración es nula");
        }

        validateField(LOG_CONFIG, "Configuración de logs");
        validateField(UPDATE_CONFIG, "Configuración de actualizaciones");
        validateField(SYSTEM_CONFIG, "Configuración del sistema");

        validateLogConfig();
        validateUpdateConfig();
        validateSystemConfig();
    }

    /**
     * Valida la configuración de logs
     */
    @SuppressWarnings("unchecked")
    private void validateLogConfig() throws ConfigurationException {
        Map<String, Object> logConfig = (Map<String, Object>) configuration.get(LOG_CONFIG);
        validateNestedConfig(logConfig, LOG_LEVEL, "nivel de log");
        validateNestedConfig(logConfig, LOG_FILE_PATH, "ruta del archivo de log");
        validateNestedConfig(logConfig, LOG_MAX_SIZE, "tamaño máximo de log");
        validateNestedConfig(logConfig, LOG_RETENTION, "días de retención");
    }

    /**
     * Valida la configuración de actualizaciones
     */
    @SuppressWarnings("unchecked")
    private void validateUpdateConfig() throws ConfigurationException {
        Map<String, Object> updateConfig = (Map<String, Object>) configuration.get(UPDATE_CONFIG);
        validateNestedConfig(updateConfig, UPDATE_INTERVAL, "intervalo de actualización");
        validateNestedConfig(updateConfig, UPDATE_RETRY_COUNT, "número de reintentos");
        validateNestedConfig(updateConfig, UPDATE_RETRY_DELAY, "tiempo entre reintentos");
    }

    /**
     * Valida la configuración del sistema
     */
    @SuppressWarnings("unchecked")
    private void validateSystemConfig() throws ConfigurationException {
        Map<String, Object> systemConfig = (Map<String, Object>) configuration.get(SYSTEM_CONFIG);
        validateNestedConfig(systemConfig, SYSTEM_TIMEZONE, "zona horaria");
        validateNestedConfig(systemConfig, SYSTEM_LOCALE, "configuración regional");
        validateNestedConfig(systemConfig, SYSTEM_DATE_FORMAT, "formato de fecha");
    }

    /**
     * Valida un campo específico de la configuración.
     */
    private void validateField(String fieldName, String fieldDescription) throws ConfigurationException {
        if (!configuration.containsKey(fieldName) || configuration.get(fieldName) == null) {
            throw new ConfigurationException("Falta el campo requerido: " + fieldDescription);
        }
    }

    /**
     * Valida un campo en una configuración anidada.
     */
    private void validateNestedConfig(Map<String, Object> config, String fieldName, String fieldDescription)
            throws ConfigurationException {
        if (config == null || !config.containsKey(fieldName) || config.get(fieldName) == null) {
            throw new ConfigurationException("Falta el campo requerido en la configuración: " + fieldDescription);
        }
    }
}