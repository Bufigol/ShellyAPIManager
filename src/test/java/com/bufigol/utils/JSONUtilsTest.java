package com.bufigol.utils;

import com.bufigol.modelo.auxiliares.JSONResponse;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JSONUtilsTest {
    @Test
    void parseResponse_ValidJSON_Success() {
        // Arrange
        String jsonString = "{\"isok\":true,\"data\":{\"name\":\"test\",\"value\":123}}";

        // Act
        JSONResponse response = JSONUtils.parseResponse(jsonString);

        // Assert
        assertTrue(response.isIsok());
        assertNotNull(response.getData());
        assertEquals("test", response.getData().get("name"));
        assertEquals(123.0, response.getData().get("value"));
    }

    @Test
    void parseResponse_InvalidJSON_ReturnsErrorResponse() {
        // Arrange
        String invalidJson = "{invalid json}";

        // Act
        JSONResponse response = JSONUtils.parseResponse(invalidJson);

        // Assert
        assertFalse(response.isIsok());
        assertTrue(response.getData().containsKey("error"));
        assertTrue(((String)response.getData().get("error")).startsWith("Error parsing JSON response"));
    }

    @Test
    void parseResponse_EmptyData_ReturnsEmptyMap() {
        // Arrange
        String jsonString = "{\"isok\":true}";

        // Act
        JSONResponse response = JSONUtils.parseResponse(jsonString);

        // Assert
        assertTrue(response.isIsok());
        assertNotNull(response.getData());
        assertTrue(response.getData().isEmpty());
    }

    @Test
    void convertJsonValueToMap_ComplexObject_Success() {
        // Arrange
        String jsonString = "{\"name\":\"test\",\"number\":123,\"boolean\":true,\"array\":[1,2,3]}";
        JsonValue jsonValue = Json.createReader(new java.io.StringReader(jsonString)).readValue();

        // Act
        Map<String, Object> result = JSONUtils.convertJsonValueToMap(jsonValue);

        // Assert
        assertEquals("test", result.get("name"));
        assertEquals(123.0, result.get("number"));
        assertEquals(true, result.get("boolean"));
        assertInstanceOf(List.class, result.get("array"));
        List<?> array = (List<?>) result.get("array");
        assertEquals(3, array.size());
    }

    @Test
    void convertJsonValueToMap_NullValue_ReturnsEmptyMap() {
        // Act
        Map<String, Object> result = JSONUtils.convertJsonValueToMap(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void convertJsonValueToMap_EmptyObject_ReturnsEmptyMap() {
        // Arrange
        String jsonString = "{}";
        JsonValue jsonValue = Json.createReader(new java.io.StringReader(jsonString)).readValue();

        // Act
        Map<String, Object> result = JSONUtils.convertJsonValueToMap(jsonValue);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void convertJsonValueToJavaObject_AllTypes_Success() {
        // Test String
        JsonValue stringValue = Json.createValue("test");
        assertEquals("test", JSONUtils.convertJsonValueToJavaObject(stringValue));

        // Test Integer
        JsonValue intValue = Json.createValue(123);
        assertEquals(123.0, JSONUtils.convertJsonValueToJavaObject(intValue));

        // Test Double
        JsonValue doubleValue = Json.createValue(123.45);
        assertEquals(123.45, JSONUtils.convertJsonValueToJavaObject(doubleValue));

        // Test Boolean
        JsonValue boolValue = JsonValue.TRUE;
        assertEquals(true, JSONUtils.convertJsonValueToJavaObject(boolValue));

        // Test Null
        assertNull(JSONUtils.convertJsonValueToJavaObject(JsonValue.NULL));

        // Test Array
        JsonValue arrayValue = Json.createArrayBuilder().add(1).add(2).add(3).build();
        Object arrayResult = JSONUtils.convertJsonValueToJavaObject(arrayValue);
        assertInstanceOf(List.class, arrayResult);
        assertEquals(3, ((List<?>) arrayResult).size());

        // Test Object
        JsonValue objectValue = Json.createObjectBuilder()
                .add("key", "value")
                .build();
        Object objectResult = JSONUtils.convertJsonValueToJavaObject(objectValue);
        assertInstanceOf(Map.class, objectResult);
        assertEquals("value", ((Map<?,?>) objectResult).get("key"));
    }

    @Test
    void convertJsonValueToJavaObject_NestedStructures_Success() {
        // Arrange
        String complexJson = "{\"name\":\"test\",\"nested\":{\"array\":[1,2,{\"key\":\"value\"}]}}";
        JsonValue jsonValue = Json.createReader(new java.io.StringReader(complexJson)).readValue();

        // Act
        Object result = JSONUtils.convertJsonValueToJavaObject(jsonValue);

        // Assert
        assertInstanceOf(Map.class, result);
        Map<?, ?> resultMap = (Map<?, ?>) result;
        assertEquals("test", resultMap.get("name"));
        assertInstanceOf(Map.class, resultMap.get("nested"));

        Map<?, ?> nestedMap = (Map<?, ?>) resultMap.get("nested");
        assertInstanceOf(List.class, nestedMap.get("array"));

        List<?> array = (List<?>) nestedMap.get("array");
        assertEquals(3, array.size());
        assertInstanceOf(Map.class, array.get(2));
        assertEquals("value", ((Map<?, ?>) array.get(2)).get("key"));
    }
}
