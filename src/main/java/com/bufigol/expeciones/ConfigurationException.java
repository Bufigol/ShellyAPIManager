package com.bufigol.expeciones;

import com.bufigol.utils.LoggerUtil;
import java.io.PrintWriter;
import java.io.StringWriter;



/**
 * Excepción personalizada para errores de configuración.
 * Incluye logging automático de errores.
 */
public class ConfigurationException extends RuntimeException {


    /**
     * Constructor para mensaje de error simple.
     *
     * @param message Mensaje de error
     */
    public ConfigurationException(String message) {
        super(message);
        logException(message);
    }


    /**
     * Constructor para mensaje de error con causa.
     *
     * @param message Mensaje de error
     * @param cause Causa del error
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
        logException(message);
    }

    /**
     * Constructor para error con causa pero sin mensaje.
     *
     * @param cause Causa del error
     */
    public ConfigurationException(Throwable cause) {
        super(cause);
        logException(cause.getMessage());
    }

    private void logException(String message) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        this.printStackTrace(pw);

        LoggerUtil.getInstance().logError(
                getSourceClassName(),
                message,
                sw.toString()
        );
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
}
