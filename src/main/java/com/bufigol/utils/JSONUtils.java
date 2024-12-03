package com.bufigol.utils;

import com.bufigol.expeciones.ConfigurationException;
import com.bufigol.interfaces.INT_JSONUtils;
import com.bufigol.modelo.auxiliares.JSONResponse;
import jakarta.json.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONUtils implements INT_JSONUtils {

    private static JSONUtils instance;
    private final String configPath;
    private static final String DEFAULT_CONFIG_PATH = "config/config.json";

    private JSONUtils(String configPath) {
        this.configPath = configPath;
        createConfigDirectoryIfNotExists();
    }

    private JSONUtils() {
        this(DEFAULT_CONFIG_PATH);
    }

    public static synchronized JSONUtils getInstance() {
        if (instance == null) {
            instance = new JSONUtils();
        }
        return instance;
    }

    public static synchronized JSONUtils getInstance(String configPath) {
        if (instance == null || !instance.getConfigPath().equals(configPath)) {
            instance = new JSONUtils(configPath);
        }
        return instance;
    }

    public String getConfigPath() {
        return configPath;
    }

    @Override
    public JSONResponse parseResponse(String jsonString) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonString))) {
            JsonObject jsonObject = jsonReader.readObject();
            JSONResponse jsonResponse = new JSONResponse();

            jsonResponse.setIsok(jsonObject.getBoolean("isok", false));

            if (jsonObject.containsKey("data")) {
                JsonValue dataValue = jsonObject.get("data");
                Map<String, Object> dataMap = convertJsonValueToMap(dataValue);
                jsonResponse.setData(dataMap);
            } else {
                jsonResponse.setData(new HashMap<>());
            }

            return jsonResponse;
        } catch (Exception e) {
            throw new ConfigurationException("Error parsing JSON response: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> convertJsonValueToMap(JsonValue value) {
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

    @Override
    public Object convertJsonValueToJavaObject(JsonValue value) {
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

    @Override
    public Map<String, Object> readConfigFile(String filePath) {
        try {
            String content = Files.readString(Paths.get(filePath));
            try (JsonReader jsonReader = Json.createReader(new StringReader(content))) {
                JsonObject jsonObject = jsonReader.readObject();
                return convertJsonValueToMap(jsonObject);
            }
        } catch (IOException e) {
            throw new ConfigurationException("Error reading config file: " + e.getMessage(), e);
        }
    }

    @Override
    public String readConfigValue(String propertyName) {
        return readConfigValue(propertyName, null);
    }

    @Override
    public String readConfigValue(String propertyName, String defaultValue) {
        try {
            Map<String, Object> config = readConfigFile(this.configPath);
            Object value = config.get(propertyName);
            return value != null ? value.toString() : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public void saveConfigValue(String propertyName, String value) {
        Map<String, Object> config;
        try {
            config = readConfigFile(this.configPath);
        } catch (Exception e) {
            config = new HashMap<>();
        }

        config.put(propertyName, value);
        saveConfigFile(config, this.configPath);
    }

    @Override
    public void saveConfigFile(Map<String, Object> config, String filePath) {
        try (JsonWriter jsonWriter = Json.createWriter(Files.newBufferedWriter(Paths.get(filePath)))) {
            JsonObjectBuilder builder = Json.createObjectBuilder();

            for (Map.Entry<String, Object> entry : config.entrySet()) {
                addValueToBuilder(builder, entry.getKey(), entry.getValue());
            }

            jsonWriter.writeObject(builder.build());
        } catch (IOException e) {
            throw new ConfigurationException("Error saving config file: " + e.getMessage(), e);
        }
    }

    private void addValueToBuilder(JsonObjectBuilder builder, String key, Object value) {
        if (value == null) {
            builder.addNull(key);
        } else if (value instanceof String) {
            builder.add(key, (String) value);
        } else if (value instanceof Number) {
            if (value instanceof Integer || value instanceof Long) {
                builder.add(key, ((Number) value).longValue());
            } else {
                builder.add(key, ((Number) value).doubleValue());
            }
        } else if (value instanceof Boolean) {
            builder.add(key, (Boolean) value);
        } else if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) value;
            JsonObjectBuilder mapBuilder = Json.createObjectBuilder();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                addValueToBuilder(mapBuilder, entry.getKey(), entry.getValue());
            }
            builder.add(key, mapBuilder);
        } else if (value instanceof List) {
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (Object item : (List<?>) value) {
                addValueToArrayBuilder(arrayBuilder, item);
            }
            builder.add(key, arrayBuilder);
        }
    }

    private void addValueToArrayBuilder(JsonArrayBuilder builder, Object value) {
        if (value == null) {
            builder.addNull();
        } else if (value instanceof String) {
            builder.add((String) value);
        } else if (value instanceof Number) {
            if (value instanceof Integer || value instanceof Long) {
                builder.add(((Number) value).longValue());
            } else {
                builder.add(((Number) value).doubleValue());
            }
        } else if (value instanceof Boolean) {
            builder.add((Boolean) value);
        } else if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) value;
            JsonObjectBuilder mapBuilder = Json.createObjectBuilder();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                addValueToBuilder(mapBuilder, entry.getKey(), entry.getValue());
            }
            builder.add(mapBuilder);
        } else if (value instanceof List) {
            JsonArrayBuilder innerBuilder = Json.createArrayBuilder();
            for (Object item : (List<?>) value) {
                addValueToArrayBuilder(innerBuilder, item);
            }
            builder.add(innerBuilder);
        }
    }

    @Override
    public boolean hasProperty(String propertyName) {
        try {
            Map<String, Object> config = readConfigFile(this.configPath);
            return config.containsKey(propertyName);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean removeProperty(String propertyName) {
        try {
            Map<String, Object> config = readConfigFile(this.configPath);
            boolean removed = config.remove(propertyName) != null;
            if (removed) {
                saveConfigFile(config, this.configPath);
            }
            return removed;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean validateConfigFile(String filePath) {
        try {
            String content = Files.readString(Paths.get(filePath));
            try (JsonReader jsonReader = Json.createReader(new StringReader(content))) {
                jsonReader.readObject();
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Map<String, Object> mergeConfigs(Map<String, Object> baseConfig, Map<String, Object> overrideConfig) {
        Map<String, Object> result = new HashMap<>(baseConfig);

        for (Map.Entry<String, Object> entry : overrideConfig.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map && result.get(key) instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> baseMap = (Map<String, Object>) result.get(key);
                @SuppressWarnings("unchecked")
                Map<String, Object> overrideMap = (Map<String, Object>) value;
                result.put(key, mergeConfigs(baseMap, overrideMap));
            } else {
                result.put(key, value);
            }
        }

        return result;
    }

    private void createConfigDirectoryIfNotExists() {
        File configDir = new File(configPath).getParentFile();
        if (configDir != null && !configDir.exists()) {
            if (!configDir.mkdirs()) {
                throw new ConfigurationException("Could not create config directory: " + configDir.getAbsolutePath());
            }
        }
    }
}