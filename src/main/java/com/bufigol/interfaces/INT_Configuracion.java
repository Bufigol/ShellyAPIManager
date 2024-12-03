package com.bufigol.interfaces;

import com.bufigol.expeciones.ConfigurationException;

/**
 * Interfaz que define los métodos necesarios para la gestión de configuraciones.
 * Todas las clases de configuración deben implementar esta interfaz para mantener
 * una estructura consistente en la aplicación.
 */
public interface INT_Configuracion {

    /**
     * Carga toda la configuración desde el archivo especificado.
     *
     * @throws ConfigurationException si hay algún error durante la carga
     */
    void loadConfig() throws ConfigurationException;

    /**
     * Carga una propiedad específica de la configuración.
     *
     * @param name nombre de la propiedad a cargar
     * @return valor de la propiedad cargada
     * @throws ConfigurationException si la propiedad no existe o hay un error durante la carga
     */
    String loadConfig(String name) throws ConfigurationException;

    /**
     * Recarga toda la configuración desde el archivo.
     * Útil cuando se han realizado cambios en el archivo de configuración
     * y se necesita actualizarla sin reiniciar la aplicación.
     *
     * @throws ConfigurationException si hay algún error durante la recarga
     */
    void reloadConfig() throws ConfigurationException;

    /**
     * Verifica si la configuración ha sido cargada correctamente.
     *
     * @return true si la configuración está cargada, false en caso contrario
     */
    boolean isLoaded();

    /**
     * Obtiene la ruta del archivo de configuración.
     *
     * @return String con la ruta completa del archivo de configuración
     */
    String getConfigPath();

    /**
     * Guarda todas las propiedades actuales en el archivo de configuración.
     * Sobrescribe el archivo existente con los valores actuales.
     *
     * @throws ConfigurationException si hay algún error durante el guardado
     */
    void saveConfig() throws ConfigurationException;

    /**
     * Guarda una propiedad específica en el archivo de configuración.
     * Si la propiedad no existe, la crea. Si existe, la actualiza.
     *
     * @param name nombre de la propiedad a guardar
     * @throws ConfigurationException si hay algún error durante el guardado
     */
    void saveConfig(String name) throws ConfigurationException;
}