package com.bufigol.utils;

import com.bufigol.modelo.auxiliares.JSONResponse;
import jakarta.json.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONUtils {

    public static JSONResponse parseResponse(String jsonString) {
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
            JSONResponse errorResponse = new JSONResponse();
            errorResponse.setIsok(false);
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Error parsing JSON response: " + e.getMessage());
            errorResponse.setData(errorMap);
            return errorResponse;
        }
    }

    private static Map<String, Object> convertJsonValueToMap(JsonValue value) {
        Map<String, Object> result = new HashMap<>();

        if (value == null || value.getValueType() == JsonValue.ValueType.NULL) {
            return result;
        }

        switch (value.getValueType()) {
            case OBJECT -> {
                JsonObject obj = value.asJsonObject();
                for (Map.Entry<String, JsonValue> entry : obj.entrySet()) {
                    result.put(entry.getKey(), convertJsonValueToJavaObject(entry.getValue()));
                }
            }
            case ARRAY -> {
                // Si el valor es un array, convertirlo a una lista
                JsonArray array = value.asJsonArray();
                List<Object> list = new ArrayList<>();
                for (JsonValue item : array) {
                    list.add(convertJsonValueToJavaObject(item));
                }
                result.put("array", list);
            }
            default -> result.put("value", convertJsonValueToJavaObject(value));
        }

        return result;
    }

    private static Object convertJsonValueToJavaObject(JsonValue value) {
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
            case ARRAY -> {
                JsonArray array = value.asJsonArray();
                List<Object> list = new ArrayList<>();
                for (JsonValue item : array) {
                    list.add(convertJsonValueToJavaObject(item));
                }
                yield list;
            }
            default -> null;
        };
    }
}