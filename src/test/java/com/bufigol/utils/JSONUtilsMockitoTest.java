package com.bufigol.utils;

import com.bufigol.expeciones.ConfigurationException;
import com.bufigol.modelo.auxiliares.JSONResponse;
import jakarta.json.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JSONUtilsMockitoTest {

    @Mock
    private LoggerUtil loggerUtil;

    private JSONUtils jsonUtils;

    @BeforeEach
    void setUp() {
        // Usar constructor con path específico para evitar problemas con el singleton
        jsonUtils = JSONUtils.getInstance("test-config.json");

        // Mock del LoggerUtil para evitar la recursión infinita
        try (MockedStatic<LoggerUtil> mockedStatic = mockStatic(LoggerUtil.class)) {
            mockedStatic.when(LoggerUtil::getInstance).thenReturn(loggerUtil);
        }
    }

    @Test
    void parseResponse_ValidJSON_Success() {
        // Arrange
        String jsonString = "{\"isok\":true,\"data\":{\"name\":\"test\",\"value\":123}}";

        // Act
        JSONResponse response = jsonUtils.parseResponse(jsonString);

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
        JSONResponse response = jsonUtils.parseResponse(jsonString);

        // Assert
        assertTrue(response.isIsok());
        assertNotNull(response.getData());
        assertTrue(response.getData().isEmpty());
    }

    @Test
    void parseResponse_InvalidJSON_ThrowsException() {
        // Arrange
        String invalidJson = "{invalid json}";

        // Act & Assert
        assertThrows(ConfigurationException.class, () -> jsonUtils.parseResponse(invalidJson));
    }

    @Test
    void convertJsonValueToMap_ComplexObject_Success() {
        // Arrange
        String jsonString = "{\"name\":\"test\",\"number\":123,\"boolean\":true,\"array\":[1,2,3]}";
        JsonValue jsonValue = Json.createReader(new StringReader(jsonString)).readValue();

        // Act
        Map<String, Object> result = jsonUtils.convertJsonValueToMap(jsonValue);

        // Assert
        assertEquals("test", result.get("name"));
        assertEquals(123.0, result.get("number"));
        assertEquals(true, result.get("boolean"));
        assertTrue(result.get("array") instanceof List);
        List<?> array = (List<?>) result.get("array");
        assertEquals(3, array.size());
    }

    @Test
    void convertJsonValueToMap_NullValue_ReturnsEmptyMap() {
        // Act
        Map<String, Object> result = jsonUtils.convertJsonValueToMap(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void convertJsonValueToJavaObject_String_Success() {
        // Arrange
        JsonString jsonString = Json.createValue("test");

        // Act
        Object result = jsonUtils.convertJsonValueToJavaObject(jsonString);

        // Assert
        assertEquals("test", result);
    }

    @Test
    void convertJsonValueToJavaObject_Number_Success() {
        // Arrange
        JsonNumber jsonNumber = Json.createValue(123.45);

        // Act
        Object result = jsonUtils.convertJsonValueToJavaObject(jsonNumber);

        // Assert
        assertEquals(123.45, result);
    }

    @Test
    void convertJsonValueToJavaObject_Boolean_Success() {
        // Arrange
        JsonValue jsonTrue = JsonValue.TRUE;
        JsonValue jsonFalse = JsonValue.FALSE;

        // Act & Assert
        assertEquals(true, jsonUtils.convertJsonValueToJavaObject(jsonTrue));
        assertEquals(false, jsonUtils.convertJsonValueToJavaObject(jsonFalse));
    }

    @Test
    void convertJsonValueToJavaObject_Null_ReturnsNull() {
        // Act & Assert
        assertNull(jsonUtils.convertJsonValueToJavaObject(JsonValue.NULL));
    }

    @Test
    void convertJsonValueToJavaObject_Array_Success() {
        // Arrange
        JsonArray jsonArray = Json.createArrayBuilder()
                .add("test")
                .add(123)
                .add(true)
                .build();

        // Act
        Object result = jsonUtils.convertJsonValueToJavaObject(jsonArray);

        // Assert
        assertTrue(result instanceof List);
        List<?> list = (List<?>) result;
        assertEquals(3, list.size());
        assertEquals("test", list.get(0));
        assertEquals(123.0, list.get(1));
        assertEquals(true, list.get(2));
    }
}