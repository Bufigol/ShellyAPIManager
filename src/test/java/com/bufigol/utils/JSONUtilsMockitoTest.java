package com.bufigol.utils;

import com.bufigol.modelo.auxiliares.JSONResponse;
import jakarta.json.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class JSONUtilsMockitoTest {

    @Mock
    private JsonReader jsonReader;

    @Mock
    private JsonObject jsonObject;

    @Mock
    private JsonString jsonString;

    @Mock
    private JsonNumber jsonNumber;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void parseResponse_ValidJSON_Success() {
        // Arrange
        String jsonString = "{\"isok\":true,\"data\":{\"name\":\"test\",\"value\":123}}";
        JsonReader actualReader = Json.createReader(new StringReader(jsonString));
        JsonObject actualObject = actualReader.readObject();

        when(jsonObject.getBoolean("isok", false)).thenReturn(true);
        when(jsonObject.containsKey("data")).thenReturn(true);
        when(jsonObject.get("data")).thenReturn(actualObject.get("data"));

        // Act
        JSONResponse response = JSONUtils.parseResponse(jsonString);

        // Assert
        assertTrue(response.isIsok());
        assertNotNull(response.getData());
        assertEquals("test", response.getData().get("name"));
        assertEquals(123.0, response.getData().get("value"));
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
        JsonValue jsonValue = Json.createReader(new StringReader(jsonString)).readValue();

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
    void convertJsonValueToJavaObject_AllTypes_Success() {
        // Test String
        when(jsonString.getValueType()).thenReturn(JsonValue.ValueType.STRING);
        when(jsonString.getString()).thenReturn("test");
        assertEquals("test", JSONUtils.convertJsonValueToJavaObject(jsonString));

        // Test Number
        when(jsonNumber.getValueType()).thenReturn(JsonValue.ValueType.NUMBER);
        when(jsonNumber.isIntegral()).thenReturn(false);
        when(jsonNumber.doubleValue()).thenReturn(123.45);
        assertEquals(123.45, JSONUtils.convertJsonValueToJavaObject(jsonNumber));

        // Test Boolean
        JsonValue mockBool = mock(JsonValue.class);
        when(mockBool.getValueType()).thenReturn(JsonValue.ValueType.TRUE);
        assertEquals(true, JSONUtils.convertJsonValueToJavaObject(mockBool));

        // Test Null
        JsonValue mockNull = mock(JsonValue.class);
        when(mockNull.getValueType()).thenReturn(JsonValue.ValueType.NULL);
        assertNull(JSONUtils.convertJsonValueToJavaObject(mockNull));
    }

    @Test
    void convertJsonValueToJavaObject_NestedStructures_Success() {
        // Arrange
        String complexJson = "{\"name\":\"test\",\"nested\":{\"array\":[1,2,{\"key\":\"value\"}]}}";
        JsonValue jsonValue = Json.createReader(new StringReader(complexJson)).readValue();

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
    void convertJsonValueToMap_EmptyObject_ReturnsEmptyMap() {
        // Arrange
        String jsonString = "{}";
        JsonValue jsonValue = Json.createReader(new StringReader(jsonString)).readValue();

        // Act
        Map<String, Object> result = JSONUtils.convertJsonValueToMap(jsonValue);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}