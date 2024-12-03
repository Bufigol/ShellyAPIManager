package com.bufigol.config;

import com.bufigol.expeciones.ConfigurationException;
import com.bufigol.interfaces.INT_Configuracion;
import com.bufigol.utils.JSONUtils;

import java.net.URL;
import java.io.File;
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

    private final String configPath;
    private final JSONUtils jsonUtils;
    private final ReadWriteLock lock;
    private boolean loaded;
    private Map<String, Object> configuration;

    /**
     * Constructor por defecto que inicializa la configuración con la ruta estándar.
     */
    public BBDDConfig() {
        URL resourceUrl = BBDDConfig.class.getClassLoader().getResource(CONFIG_FOLDER + File.separator + CONFIG_FILE_NAME);
        if (resourceUrl == null) {
            throw new ConfigurationException("No se pudo encontrar el archivo de configuración de base de datos en el classpath");
        }
        this.configPath = resourceUrl.getPath();
        this.jsonUtils = JSONUtils.getInstance();
        this.lock = new ReentrantReadWriteLock();
        this.configuration = new HashMap<>();
        this.loaded = false;
    }

    /**
     * Constructor que permite especificar un nombre de archivo personalizado.
     * @param configFileName nombre del archivo de configuración
     */
    public BBDDConfig(String configFileName) {
        URL resourceUrl = BBDDConfig.class.getClassLoader().getResource(CONFIG_FOLDER + File.separator + configFileName);
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
     * Obtiene la URL de conexión a la base de datos.
     * @return String con la URL de conexión
     * @throws ConfigurationException si la configuración no está cargada o el valor no existe
     */
    public String getDatabaseUrl() throws ConfigurationException {
        lock.readLock().lock();
        try {
            return loadConfig(DB_URL);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Obtiene el nombre de usuario para la conexión.
     * @return String con el nombre de usuario
     * @throws ConfigurationException si la configuración no está cargada o el valor no existe
     */
    public String getUsername() throws ConfigurationException {
        lock.readLock().lock();
        try {
            return loadConfig(DB_USERNAME);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Obtiene la contraseña para la conexión.
     * @return String con la contraseña
     * @throws ConfigurationException si la configuración no está cargada o el valor no existe
     */
    public String getPassword() throws ConfigurationException {
        lock.readLock().lock();
        try {
            return loadConfig(DB_PASSWORD);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Obtiene el driver de la base de datos.
     * @return String con el nombre del driver
     * @throws ConfigurationException si la configuración no está cargada o el valor no existe
     */
    public String getDriver() throws ConfigurationException {
        lock.readLock().lock();
        try {
            return loadConfig(DB_DRIVER);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Obtiene el tamaño máximo del pool de conexiones.
     * @return int con el tamaño máximo del pool
     * @throws ConfigurationException si la configuración no está cargada o el valor no existe
     */
    @SuppressWarnings("unchecked")
    public int getPoolMaxSize() throws ConfigurationException {
        lock.readLock().lock();
        try {
            Map<String, Object> poolConfig = (Map<String, Object>) configuration.get(POOL_CONFIG);
            if (poolConfig == null || !poolConfig.containsKey(POOL_MAX_SIZE)) {
                throw new ConfigurationException("Configuración del pool no encontrada");
            }
            return Integer.parseInt(poolConfig.get(POOL_MAX_SIZE).toString());
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Obtiene el timeout del pool de conexiones en segundos.
     * @return int con el timeout en segundos
     * @throws ConfigurationException si la configuración no está cargada o el valor no existe
     */
    @SuppressWarnings("unchecked")
    public int getPoolTimeout() throws ConfigurationException {
        lock.readLock().lock();
        try {
            Map<String, Object> poolConfig = (Map<String, Object>) configuration.get(POOL_CONFIG);
            if (poolConfig == null || !poolConfig.containsKey(POOL_TIMEOUT)) {
                throw new ConfigurationException("Configuración del pool no encontrada");
            }
            return Integer.parseInt(poolConfig.get(POOL_TIMEOUT).toString());
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

        // Validar campos obligatorios
        validateField(DB_URL, "URL de la base de datos");
        validateField(DB_USERNAME, "Nombre de usuario");
        validateField(DB_PASSWORD, "Contraseña");
        validateField(DB_DRIVER, "Driver de la base de datos");
        validateField(POOL_CONFIG, "Configuración del pool");

        // Validar configuración del pool
        @SuppressWarnings("unchecked")
        Map<String, Object> poolConfig = (Map<String, Object>) configuration.get(POOL_CONFIG);
        if (poolConfig == null || !poolConfig.containsKey(POOL_MAX_SIZE) || !poolConfig.containsKey(POOL_TIMEOUT)) {
            throw new ConfigurationException("Configuración del pool incompleta o inválida");
        }
    }

    /**
     * Valida un campo específico de la configuración.
     * @param fieldName nombre del campo a validar
     * @param fieldDescription descripción del campo para el mensaje de error
     * @throws ConfigurationException si el campo no existe o es nulo
     */
    private void validateField(String fieldName, String fieldDescription) throws ConfigurationException {
        if (!configuration.containsKey(fieldName) || configuration.get(fieldName) == null) {
            throw new ConfigurationException("Falta el campo requerido: " + fieldDescription);
        }
    }
}