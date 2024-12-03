package com.bufigol.API;

import com.bufigol.modelo.auxiliares.JSONResponse;
import jakarta.json.*;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ShellyAPIService {
    private final HttpClient httpClient;
    private final String baseUrl;

    public ShellyAPIService(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
    }

    public JSONResponse getDeviceStatus() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/status"))
                .GET()
                .timeout(Duration.ofSeconds(30))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Error al obtener el estado del dispositivo. Código: " + response.statusCode());
        }

        return parseResponse(response.body());
    }

    private JSONResponse parseResponse(String jsonString) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonString))) {
            JsonObject jsonObject = jsonReader.readObject();
            JSONResponse jsonResponse = new JSONResponse();

            // Establecer el estado de la respuesta
            jsonResponse.setIsok(jsonObject.getBoolean("isok", false));

            // Si existe la propiedad data, procesarla
            if (jsonObject.containsKey("data")) {
                JsonValue dataValue = jsonObject.get("data");
                Map<String, Object> dataMap = convertJsonValueToMap(dataValue);
                jsonResponse.setData(dataMap);
            } else {
                jsonResponse.setData(new HashMap<>()); // Mapa vacío si no hay data
            }

            return jsonResponse;
        } catch (Exception e) {
            // En caso de error en el parsing, devolver una respuesta con error
            JSONResponse errorResponse = new JSONResponse();
            errorResponse.setIsok(false);
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Error parsing JSON response: " + e.getMessage());
            errorResponse.setData(errorMap);
            return errorResponse;
        }
    }

    private Map<String, Object> convertJsonValueToMap(JsonValue value) {
        Map<String, Object> result = new HashMap<>();

        if (value == null || value.getValueType() == JsonValue.ValueType.NULL) {
            return result;
        }

        switch (value.getValueType()) {
            case OBJECT -> {
                JsonObject obj = value.asJsonObject();
                obj.forEach((key, val) -> result.put(key, convertJsonValueToJavaObject(val)));
            }
            case ARRAY -> {
                // Si el valor es un array, convertirlo a una lista
                JsonArray array = value.asJsonArray();
                result.put("array", array.stream()
                        .map(this::convertJsonValueToJavaObject)
                        .collect(Collectors.toList()));
            }
            default -> result.put("value", convertJsonValueToJavaObject(value));
        }

        return result;
    }

    private Object convertJsonValueToJavaObject(JsonValue value) {
        if (value == null || value.getValueType() == JsonValue.ValueType.NULL) {
            return null;
        }

        return switch (value.getValueType()) {
            case STRING -> ((JsonString) value).getString();
            case NUMBER -> {
                JsonNumber num = (JsonNumber) value;
                yield num.isIntegral() ? num.longValue() : num.doubleValue();
            }
            case TRUE -> true;
            case FALSE -> false;
            case OBJECT -> convertJsonValueToMap(value);
            case ARRAY -> ((JsonArray) value).stream()
                    .map(this::convertJsonValueToJavaObject)
                    .collect(Collectors.toList());
            default -> null;
        };
    }
}