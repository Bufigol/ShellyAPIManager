package com.bufigol.interfaces;


import com.bufigol.modelo.auxiliares.JSONResponse;
import jakarta.json.JsonValue;
import java.util.Map;

/**
 * Interfaz que define los métodos necesarios para el manejo de JSON en la aplicación.
 * Incluye tanto el procesamiento de respuestas API como la gestión de archivos de configuración.
 */
public interface INT_JSONUtils {

    // Métodos existentes
    /**
     * Procesa una cadena JSON y la convierte en un objeto JSONResponse
     * @param jsonString cadena JSON a procesar
     * @return objeto JSONResponse con la información procesada
     */
    JSONResponse parseResponse(String jsonString);

    /**
     * Convierte un JsonValue en un Map de Java
     * @param value valor JSON a convertir
     * @return Map con los datos convertidos
     */
    Map<String, Object> convertJsonValueToMap(JsonValue value);

    /**
     * Convierte un JsonValue en un objeto Java del tipo apropiado
     * @param value valor JSON a convertir
     * @return Object resultado de la conversión
     */
    Object convertJsonValueToJavaObject(JsonValue value);

    // Nuevos métodos necesarios para configuración
    /**
     * Lee un archivo JSON de configuración completo
     * @param filePath ruta del archivo a leer
     * @return Map con toda la configuración
     */
    Map<String, Object> readConfigFile(String filePath);

    /**
     * Lee un valor específico del archivo de configuración
     * @param propertyName nombre de la propiedad a leer
     * @return valor de la propiedad
     */
    String readConfigValue(String propertyName);

    /**
     * Lee un valor específico del archivo de configuración con un valor por defecto
     * @param propertyName nombre de la propiedad a leer
     * @param defaultValue valor por defecto si no existe la propiedad
     * @return valor de la propiedad o valor por defecto
     */
    String readConfigValue(String propertyName, String defaultValue);

    /**
     * Guarda un valor en el archivo de configuración
     * @param propertyName nombre de la propiedad a guardar
     * @param value valor a guardar
     */
    void saveConfigValue(String propertyName, String value);

    /**
     * Guarda una configuración completa en el archivo
     * @param config Map con la configuración a guardar
     * @param filePath ruta del archivo donde guardar
     */
    void saveConfigFile(Map<String, Object> config, String filePath);

    /**
     * Verifica si existe una propiedad en el archivo de configuración
     * @param propertyName nombre de la propiedad a verificar
     * @return true si existe la propiedad, false en caso contrario
     */
    boolean hasProperty(String propertyName);

    /**
     * Elimina una propiedad del archivo de configuración
     * @param propertyName nombre de la propiedad a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    boolean removeProperty(String propertyName);

    /**
     * Valida la estructura de un archivo JSON de configuración
     * @param filePath ruta del archivo a validar
     * @return true si la estructura es válida, false en caso contrario
     */
    boolean validateConfigFile(String filePath);

    /**
     * Fusiona dos archivos de configuración
     * @param baseConfig configuración base
     * @param overrideConfig configuración que sobrescribe
     * @return Map con la configuración combinada
     */
    Map<String, Object> mergeConfigs(Map<String, Object> baseConfig, Map<String, Object> overrideConfig);
}