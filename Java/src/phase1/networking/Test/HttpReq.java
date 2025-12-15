package Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

class HttpReq {

  // 📦 Production-ready GET request
  public String callExternalApi(String url) throws IOException {
    HttpURLConnection connection = null;
    try {
      // 1️⃣ Create URL
      URL apiUrl = new URL(url);

      // 2️⃣ Open connection
      connection = (HttpURLConnection) apiUrl.openConnection();

      // 3️⃣ CRITICAL: Set timeouts (ALWAYS DO THIS)
      connection.setConnectTimeout(30000); // 30 seconds to connect
      connection.setReadTimeout(60000); // 60 seconds to read response

      // 4️⃣ Set method
      connection.setRequestMethod("GET");

      // 5️⃣ Set headers (Always set these)
      connection.setRequestProperty("Accept", "application/json");
      connection.setRequestProperty("User-Agent", "YourApp/1.0");

      // 6️⃣ Check response code FIRST
      int status = connection.getResponseCode();

      if (status >= 200 && status < 300) {
        // Success - read response
        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(connection.getInputStream(), "utf-8"))) {
          StringBuilder response = new StringBuilder();
          String responseLine;
          while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
          }
          return response.toString();
        }
      } else {
        // Error - read error stream
        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
          String errorResponse = br.lines().collect(Collectors.joining());
          throw new IOException("HTTP Error: " + status + " - " + errorResponse);
        }
      }
    } finally {
      // 7️⃣ ALWAYS disconnect
      if (connection != null) {
        connection.disconnect();
      }
    }
  }


  // 📦 Production-ready POST with JSON
  public String postJson(String url, String jsonPayload) throws IOException {
    HttpURLConnection connection = null;
    try {
      URL apiUrl = new URL(url);
      connection = (HttpURLConnection) apiUrl.openConnection();

      // 🚨 CRITICAL SETTINGS
      connection.setConnectTimeout(30000);
      connection.setReadTimeout(60000);
      connection.setRequestMethod("POST");
      connection.setDoOutput(true); // 🎯 MUST SET FOR POST

      // 🏷️ MUST-HAVE HEADERS FOR JSON
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("Accept", "application/json");
      connection.setRequestProperty("User-Agent", "YourApp/1.0");

      // 📤 Write JSON payload
      try (OutputStream os = connection.getOutputStream()) {
        byte[] input = jsonPayload.getBytes("utf-8");
        os.write(input, 0, input.length);
      }

      // 📥 Get response
      int status = connection.getResponseCode();

      if (status >= 200 && status < 300) {
        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(connection.getInputStream(), "utf-8"))) {
          return br.lines().collect(Collectors.joining());
        }
      } else {
        // Handle error with details
        String errorDetails = "";
        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
          errorDetails = br.lines().collect(Collectors.joining());
        }
        throw new IOException("POST failed: " + status + " - " + errorDetails);
      }
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  

  public static void main(String[] args) throws IOException {
    HttpReq urlSet = new HttpReq();
    urlSet.callExternalApi("https://www.google.com");
  }
}