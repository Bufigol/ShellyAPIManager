package com.bufigol.API;

import com.bufigol.modelo.auxiliares.JSONResponse;
import com.bufigol.utils.JSONUtils;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class ShellyAPIService {
    private final HttpClient httpClient;
    private final String baseUrl;
    private final String authKey;

    public ShellyAPIService(String authKey) {
        this.baseUrl = "https://shelly-141-eu.shelly.cloud";
        this.authKey = authKey;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
    }

    public JSONResponse getDeviceStatus(String deviceId) throws Exception {
        // Construir la URL con los parámetros requeridos
        String encodedAuthKey = URLEncoder.encode(authKey, StandardCharsets.UTF_8);
        String encodedId = URLEncoder.encode(deviceId, StandardCharsets.UTF_8);
        String url = String.format("%s/device/status?auth_key=%s&id=%s",
                baseUrl, encodedAuthKey, encodedId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofSeconds(30))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Error al obtener el estado del dispositivo. Código: " + response.statusCode());
        }

        return JSONUtils.parseResponse(response.body());
    }
}