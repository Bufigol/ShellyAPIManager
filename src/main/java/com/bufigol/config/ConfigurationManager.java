package com.bufigol.config;

import com.bufigol.expeciones.ConfigurationException;
import com.bufigol.utils.LoggerUtil;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Gestor centralizado de configuraciones del sistema.
 * Implementa el patrón Singleton y proporciona acceso thread-safe a todas
 * las configuraciones del sistema.
 */
public class ConfigurationManager {

    private static ConfigurationManager instance;
    private static final ReadWriteLock instanceLock = new ReentrantReadWriteLock();

    private final ShellyConfig shellyConfig;
    private final BBDDConfig bbddConfig;
    private final OtherConfig otherConfig;
    private final ReadWriteLock configLock;
    private final AtomicBoolean initialized;

    /**
     * Constructor privado que inicializa las instancias de configuración.
     */
    private ConfigurationManager() {
        this.shellyConfig = new ShellyConfig();
        this.bbddConfig = new BBDDConfig();
        this.otherConfig = new OtherConfig();
        this.configLock = new ReentrantReadWriteLock();
        this.initialized = new AtomicBoolean(false);
    }

    /**
     * Obtiene la instancia única del ConfigurationManager.
     * @return instancia de ConfigurationManager
     */
    public static ConfigurationManager getInstance() {
        instanceLock.readLock().lock();
        try {
            if (instance == null) {
                instanceLock.readLock().unlock();
                instanceLock.writeLock().lock();
                try {
                    if (instance == null) {
                        instance = new ConfigurationManager();
                    }
                } finally {
                    instanceLock.writeLock().unlock();
                    instanceLock.readLock().lock();
                }
            }
            return instance;
        } finally {
            instanceLock.readLock().unlock();
        }
    }

    /**
     * Inicializa todas las configuraciones del sistema.
     * @throws ConfigurationException si hay algún error durante la inicialización
     */
    public void initialize() throws ConfigurationException {
        if (initialized.get()) {
            throw new ConfigurationException("El gestor de configuración ya está inicializado");
        }

        configLock.writeLock().lock();
        try {
            // Primero cargamos OtherConfig ya que puede contener configuraciones necesarias para los demás
            loadOtherConfig();

            // Luego BBDDConfig que puede ser necesaria para Shelly
            loadBBDDConfig();

            // Finalmente ShellyConfig
            loadShellyConfig();

            initialized.set(true);
            LoggerUtil.getInstance().logError(this.getClass().getName(),
                    "Inicialización exitosa del gestor de configuración", "");
        } catch (Exception e) {
            LoggerUtil.getInstance().logError(this.getClass().getName(),
                    "Error durante la inicialización del gestor de configuración", e.toString());
            throw new ConfigurationException("Error durante la inicialización: " + e.getMessage(), e);
        } finally {
            configLock.writeLock().unlock();
        }
    }

    /**
     * Recarga todas las configuraciones del sistema.
     * @throws ConfigurationException si hay algún error durante la recarga
     */
    public void reloadAll() throws ConfigurationException {
        checkInitialized();
        configLock.writeLock().lock();
        try {
            otherConfig.reloadConfig();
            bbddConfig.reloadConfig();
            shellyConfig.reloadConfig();
            LoggerUtil.getInstance().logError(this.getClass().getName(),
                    "Recarga exitosa de todas las configuraciones", "");
        } catch (Exception e) {
            LoggerUtil.getInstance().logError(this.getClass().getName(),
                    "Error durante la recarga de configuraciones", e.toString());
            throw new ConfigurationException("Error durante la recarga: " + e.getMessage(), e);
        } finally {
            configLock.writeLock().unlock();
        }
    }

    /**
     * Obtiene la configuración de Shelly.
     * @return instancia de ShellyConfig
     * @throws ConfigurationException si el gestor no está inicializado
     */
    public ShellyConfig getShellyConfig() throws ConfigurationException {
        checkInitialized();
        configLock.readLock().lock();
        try {
            return shellyConfig;
        } finally {
            configLock.readLock().unlock();
        }
    }

    /**
     * Obtiene la configuración de la base de datos.
     * @return instancia de BBDDConfig
     * @throws ConfigurationException si el gestor no está inicializado
     */
    public BBDDConfig getBBDDConfig() throws ConfigurationException {
        checkInitialized();
        configLock.readLock().lock();
        try {
            return bbddConfig;
        } finally {
            configLock.readLock().unlock();
        }
    }

    /**
     * Obtiene las otras configuraciones del sistema.
     * @return instancia de OtherConfig
     * @throws ConfigurationException si el gestor no está inicializado
     */
    public OtherConfig getOtherConfig() throws ConfigurationException {
        checkInitialized();
        configLock.readLock().lock();
        try {
            return otherConfig;
        } finally {
            configLock.readLock().unlock();
        }
    }

    /**
     * Verifica si todas las configuraciones están cargadas.
     * @return true si todas las configuraciones están cargadas
     */
    public boolean isAllConfigurationsLoaded() {
        configLock.readLock().lock();
        try {
            return initialized.get() &&
                    shellyConfig.isLoaded() &&
                    bbddConfig.isLoaded() &&
                    otherConfig.isLoaded();
        } finally {
            configLock.readLock().unlock();
        }
    }

    /**
     * Métod o para cerrar y limpiar recursos del gestor de configuración.
     */
    public void shutdown() {
        configLock.writeLock().lock();
        try {
            initialized.set(false);
            LoggerUtil.getInstance().logError(this.getClass().getName(),
                    "Shutdown exitoso del gestor de configuración", "");
        } finally {
            configLock.writeLock().unlock();
        }
    }

    // Métodos privados de ayuda

    private void checkInitialized() throws ConfigurationException {
        if (!initialized.get()) {
            throw new ConfigurationException("El gestor de configuración no está inicializado");
        }
    }

    private void loadOtherConfig() throws ConfigurationException {
        try {
            otherConfig.loadConfig();
        } catch (Exception e) {
            throw new ConfigurationException("Error cargando OtherConfig: " + e.getMessage(), e);
        }
    }

    private void loadBBDDConfig() throws ConfigurationException {
        try {
            bbddConfig.loadConfig();
        } catch (Exception e) {
            throw new ConfigurationException("Error cargando BBDDConfig: " + e.getMessage(), e);
        }
    }

    private void loadShellyConfig() throws ConfigurationException {
        try {
            shellyConfig.loadConfig();
        } catch (Exception e) {
            throw new ConfigurationException("Error cargando ShellyConfig: " + e.getMessage(), e);
        }
    }
}