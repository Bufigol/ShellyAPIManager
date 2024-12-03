package com.bufigol.config;

import com.bufigol.expeciones.ConfigurationException;
import com.bufigol.interfaces.INT_Configuracion;
import com.bufigol.utils.JSONUtils;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Implementación de la configuración para dispositivos Shelly.
 * Esta clase maneja la configuración específica para la conexión y gestión
 * de dispositivos Shelly, incluyendo claves de autenticación y IDs de dispositivos.
 */
public class ShellyConfig implements INT_Configuracion {
    private static final String CONFIG_FILE_NAME = "shelly.json";
    private static final String CONFIG_FOLDER = "config";
    private static final String AUTH_KEY = "auth_key";
    private static final String BASE_URL = "base_url";
    private static final String DEVICES = "devices";

    private final String configPath;
    private final JSONUtils jsonUtils;
    private final ReadWriteLock lock;
    private boolean loaded;
    private Map<String, Object> configuration;

    /**
     * Constructor que inicializa la configuración con la ruta por defecto.
     * La ruta se construye de forma relativa al classpath.
     */
    public ShellyConfig() {
        URL resourceUrl = ShellyConfig.class.getClassLoader().getResource(CONFIG_FOLDER + File.separator + CONFIG_FILE_NAME);
        if (resourceUrl == null) {
            throw new ConfigurationException("No se pudo encontrar el archivo de configuración en el classpath");
        }
        this.configPath = resourceUrl.getPath();
        this.jsonUtils = JSONUtils.getInstance();
        this.lock = new ReentrantReadWriteLock();
        this.configuration = new HashMap<>();
        this.loaded = false;
    }

    /**
     * Constructor que permite especificar un nombre de archivo personalizado dentro del directorio de configuración.
     * @param configFileName nombre del archivo de configuración
     */
    public ShellyConfig(String configFileName) {
        URL resourceUrl = ShellyConfig.class.getClassLoader().getResource(CONFIG_FOLDER + File.separator + configFileName);
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
            throw new ConfigurationException("Error al cargar la configuración de Shelly: " + e.getMessage(), e);
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
     * Obtiene la clave de autenticación para la API de Shelly.
     * @return String con la clave de autenticación
     * @throws ConfigurationException si la configuración no está cargada o la clave no existe
     */
    public String getAuthKey() throws ConfigurationException {
        lock.readLock().lock();
        try {
            return loadConfig(AUTH_KEY);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Obtiene la URL base para la API de Shelly.
     * @return String con la URL base
     * @throws ConfigurationException si la configuración no está cargada o la URL no existe
     */
    public String getBaseUrl() throws ConfigurationException {
        lock.readLock().lock();
        try {
            return loadConfig(BASE_URL);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Obtiene el ID de un dispositivo específico por su nombre.
     * @param deviceName nombre del dispositivo
     * @return String con el ID del dispositivo
     * @throws ConfigurationException si la configuración no está cargada o el dispositivo no existe
     */
    @SuppressWarnings("unchecked")
    public String getDeviceId(String deviceName) throws ConfigurationException {
        lock.readLock().lock();
        try {
            if (!loaded) {
                throw new ConfigurationException("La configuración no está cargada. Llame a loadConfig() primero.");
            }
            Map<String, String> devices = (Map<String, String>) configuration.get(DEVICES);
            if (devices == null || !devices.containsKey(deviceName)) {
                throw new ConfigurationException("Dispositivo no encontrado: " + deviceName);
            }
            return devices.get(deviceName);
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

        if (!configuration.containsKey(AUTH_KEY) || configuration.get(AUTH_KEY) == null) {
            throw new ConfigurationException("Falta el campo requerido: " + AUTH_KEY);
        }

        if (!configuration.containsKey(BASE_URL) || configuration.get(BASE_URL) == null) {
            throw new ConfigurationException("Falta el campo requerido: " + BASE_URL);
        }

        if (!configuration.containsKey(DEVICES) || !(configuration.get(DEVICES) instanceof Map)) {
            throw new ConfigurationException("Falta la configuración de dispositivos o es inválida");
        }
    }
}