package com.bufigol.expeciones;

/**
 * Excepción personalizada para errores de configuración.
 * Esta clase extiende RuntimeException y proporciona funcionalidad para
 * capturar y gestionar errores relacionados con la configuración.
 */
public class ConfigurationException extends RuntimeException {
    private final String sourceClass;
    private final String errorDetails;

    /**
     * Constructor para mensaje de error simple.
     *
     * @param message Mensaje de error
     */
    public ConfigurationException(String message) {
        super(message);
        this.sourceClass = getSourceClassName();
        this.errorDetails = "";
    }

    /**
     * Constructor para mensaje de error con causa.
     *
     * @param message Mensaje de error
     * @param cause Causa del error
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
        this.sourceClass = getSourceClassName();
        this.errorDetails = cause != null ? cause.toString() : "";
    }

    /**
     * Constructor para mensaje de error con detalles específicos.
     *
     * @param message Mensaje de error
     * @param sourceClass Clase donde se originó el error
     * @param errorDetails Detalles adicionales del error
     */
    public ConfigurationException(String message, String sourceClass, String errorDetails) {
        super(message);
        this.sourceClass = sourceClass;
        this.errorDetails = errorDetails;
    }

    /**
     * Obtiene la clase donde se originó el error.
     * @return Nombre de la clase origen
     */
    public String getSourceClass() {
        return sourceClass;
    }

    /**
     * Obtiene los detalles adicionales del error.
     * @return Detalles del error
     */
    public String getErrorDetails() {
        return errorDetails;
    }

    /**
     * Obtiene un mensaje formateado con toda la información del error.
     * @return Mensaje formateado
     */
    public String getFormattedMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Configuration Error in ").append(sourceClass).append("\n");
        sb.append("Message: ").append(getMessage()).append("\n");

        if (!errorDetails.isEmpty()) {
            sb.append("Details: ").append(errorDetails).append("\n");
        }

        if (getCause() != null) {
            sb.append("Cause: ").append(getCause().getMessage());
        }

        return sb.toString();
    }

    private String getSourceClassName() {
        StackTraceElement[] stackTrace = getStackTrace();
        // Buscamos la primera clase que no sea ConfigurationException
        for (StackTraceElement element : stackTrace) {
            if (!element.getClassName().contains("ConfigurationException")) {
                return element.getClassName();
            }
        }
        return "Unknown Class";
    }

    /**
     * Determina si este error es crítico basado en su causa y mensaje.
     * @return true si el error es crítico
     */
    public boolean isCriticalError() {
        // Consideramos críticos los errores que:
        // - Son causados por errores de IO
        // - Contienen ciertos mensajes clave
        if (getCause() != null && getCause() instanceof java.io.IOException) {
            return true;
        }

        String message = getMessage().toLowerCase();
        return message.contains("could not create") ||
                message.contains("permission denied") ||
                message.contains("file not found") ||
                message.contains("invalid configuration");
    }
}