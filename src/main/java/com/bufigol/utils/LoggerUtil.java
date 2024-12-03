package com.bufigol.utils;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Utilidad para escribir logs de error en un archivo.
 * Implementa un patrón Singleton para asegurar una única instancia de escritura.
 */
public class LoggerUtil {
    private static LoggerUtil instance;
    private final String logFilePath;
    private final ReentrantLock lock;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String DEFAULT_LOG_PATH = "logs/config_errors.log";
    private static final String CONFIG_PROPERTY_NAME = "log.file.path";

    private LoggerUtil() {
        this.logFilePath = determineLogPath();
        this.lock = new ReentrantLock();
        createLogFileIfNotExists();
    }

    public static synchronized LoggerUtil getInstance() {
        if (instance == null) {
            instance = new LoggerUtil();
        }
        return instance;
    }

    /**
     * Determina la ruta del archivo de log.
     * Primero intenta obtenerla de la configuración, si no existe usa el valor por defecto
     * y lo guarda en la configuración.
     *
     * @return la ruta del archivo de log
     */
    private String determineLogPath() {
        try {
            // Aquí usaríamos la clase de configuración para obtener la ruta
            String configPath = JSONUtils.readConfigValue(CONFIG_PROPERTY_NAME);

            if (configPath != null && !configPath.isEmpty()) {
                return configPath;
            }

            // Si no existe la configuración, usar valor por defecto y guardarlo
            saveDefaultPath();
            return DEFAULT_LOG_PATH;
        } catch (Exception e) {
            // Si hay algún error, usar valor por defecto
            System.err.println("Error al leer la configuración del log. Usando ruta por defecto: " + e.getMessage());
            return DEFAULT_LOG_PATH;
        }
    }

    /**
     * Guarda la ruta por defecto en la configuración
     */
    private void saveDefaultPath() {
        try {
            // Aquí usaríamos la clase de configuración para guardar la ruta
            JSONUtils.saveConfigValue(CONFIG_PROPERTY_NAME, DEFAULT_LOG_PATH);
        } catch (Exception e) {
            System.err.println("No se pudo guardar la ruta por defecto en la configuración: " + e.getMessage());
        }
    }

    private void createLogFileIfNotExists() {
        File logFile = new File(logFilePath);

        // Crear directorio si no existe
        File logDir = logFile.getParentFile();
        if (logDir != null && !logDir.exists()) {
            if (!logDir.mkdirs()) {
                throw new RuntimeException("No se pudo crear el directorio para los logs: " + logDir.getAbsolutePath());
            }
        }

        // Crear archivo si no existe
        if (!logFile.exists()) {
            try {
                if (!logFile.createNewFile()) {
                    throw new RuntimeException("No se pudo crear el archivo de log: " + logFilePath);
                }
            } catch (IOException e) {
                throw new RuntimeException("Error al crear el archivo de log: " + e.getMessage());
            }
        }
    }

    /**
     * Escribe un mensaje de error en el archivo de log.
     *
     * @param errorClass Clase donde se produjo el error
     * @param errorMessage Mensaje de error
     * @param stackTrace Stack trace del error
     */
    public void logError(String errorClass, String errorMessage, String stackTrace) {
        lock.lock();
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFilePath, true))) {
            StringBuilder logEntry = new StringBuilder();
            logEntry.append("\n=== Error Log Entry ===\n");
            logEntry.append("Timestamp: ").append(LocalDateTime.now().format(formatter)).append("\n");
            logEntry.append("Class: ").append(errorClass).append("\n");
            logEntry.append("Error: ").append(errorMessage).append("\n");
            logEntry.append("Stack Trace:\n").append(stackTrace).append("\n");
            logEntry.append("=====================\n");

            writer.write(logEntry.toString());
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error escribiendo en el archivo de log: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    /**
     * Limpia el contenido del archivo de log.
     * Útil para mantenimiento o cuando el archivo crece demasiado.
     */
    public void clearLog() {
        lock.lock();
        try (PrintWriter writer = new PrintWriter(logFilePath)) {
            writer.print("");
        } catch (IOException e) {
            System.err.println("Error limpiando el archivo de log: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    /**
     * Obtiene la ruta actual del archivo de log
     * @return la ruta del archivo de log
     */
    public String getLogFilePath() {
        return logFilePath;
    }
}