/*
 * Copyright 2017 saftsau
 *
 * This file is part of xREL4J.
 *
 * xREL4J is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * xREL4J is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with xREL4J. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.github.saftsau.xrel4j.util;

import com.github.saftsau.xrel4j.Token;
import com.github.saftsau.xrel4j.XrelException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.net.ssl.HttpsURLConnection;

/**
 * Helper class used to handle all networking related operations, e.g. GET and POST requests on the
 * xREL API. This class also implements the handling of errors and throws corresponding exceptions.
 */
public abstract class NetworkingHelper {

  private static int xRateLimitLimit = -1;
  private static int xRateLimitRemaining = -1;
  private static int xRateLimitReset = -1;
  private static int responseCode = -1;

  /**
   * Builds the query parameters needed for POST requests. This implementation is using 2
   * {@link List} instead of a {@link Map} because to provide arrays as parameters we need the same
   * key twice or more times.
   *
   * @param keyList List of keys
   * @param valueList List of values
   * @return The built String to be used for the POST request
   * @throws UnsupportedEncodingException If UTF-8 Encoding is not available
   * @throws XrelException If there is an error returned by the xREL API
   */
  private static String buildQueryParameter(List<String> keyList, List<String> valueList)
      throws UnsupportedEncodingException {
    if (keyList == null || keyList.isEmpty() || valueList == null || valueList.isEmpty()) {
      return "";
    }

    StringBuilder result = new StringBuilder();
    boolean first = true;

    for (int i = 0; i < keyList.size(); i++) {
      if (first) {
        first = false;
      } else {
        result.append("&");
      }
      result.append(URLEncoder.encode(keyList.get(i), StandardCharsets.UTF_8.name()));
      result.append("=");
      result.append(URLEncoder.encode(valueList.get(i), StandardCharsets.UTF_8.name()));
    }

    return result.toString();
  }

  /**
   * Reads all input by a given {@link Reader} and converts it to a {@link String}.
   * 
   * @param reader The {@link Reader} to read the input from
   * @return The input as a {@link String}
   * @throws IOException If there is a networking problem
   */
  private static String readAll(Reader reader) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    int cp;
    while ((cp = reader.read()) != -1) {
      stringBuilder.append((char) cp);
    }
    return stringBuilder.toString();
  }

  /**
   * Extracts the HTTP response code and the RateLimit values from an xREL API response and saves
   * them.
   * 
   * @param httpsUrlConnection The connection to extract the response from
   * @throws IOException If there is a networking problem
   */
  private static void handleResponseHeaders(HttpsURLConnection httpsUrlConnection)
      throws IOException {
    // Handle the response code and header fields
    setResponseCode(httpsUrlConnection.getResponseCode());

    String xRateLimitLimit = httpsUrlConnection.getHeaderField("X-RateLimit-Limit");
    if (xRateLimitLimit != null) {
      setXRateLimitLimit(Integer.valueOf(xRateLimitLimit));
    } else {
      setXRateLimitLimit(-1);
    }

    String xRateLimitRemaining = httpsUrlConnection.getHeaderField("X-RateLimit-Remaining");
    if (xRateLimitRemaining != null) {
      setXRateLimitRemaining(Integer.valueOf(xRateLimitRemaining));
    } else {
      setXRateLimitRemaining(-1);
    }

    String xRateLimitReset = httpsUrlConnection.getHeaderField("X-RateLimit-Reset");
    if (xRateLimitReset != null) {
      setXRateLimitReset(Integer.valueOf(xRateLimitReset));
    } else {
      setXRateLimitReset(-1);
    }
  }

  /**
   * Connects to the xREL API using a GET request and retrieves the {@link InputStream}. This can be
   * the ErrorStream if there is a problem while processing your request.
   * 
   * @param url The URL
   * @param token The {@link Token} to be used for authentication
   * @return The retrieved {@link InputStream}
   * @throws MalformedURLException If the given URL is wrong
   * @throws IOException If there is a networking problem
   */
  private static InputStream connectToXrelGet(String url, Token token)
      throws MalformedURLException, IOException {
    HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) new URL(url).openConnection();
    httpsUrlConnection.setRequestMethod("GET");
    if (token != null) {
      httpsUrlConnection.addRequestProperty("Authorization", "Bearer " + token.getAccessToken());
    }
    httpsUrlConnection.setDoInput(true);
    httpsUrlConnection.setDoOutput(true);

    handleResponseHeaders(httpsUrlConnection);

    // Choose InputStream or ErrorStream depending on the response code
    InputStream inputStream;
    if (getResponseCode() == HttpsURLConnection.HTTP_OK) {
      inputStream = httpsUrlConnection.getInputStream();
    } else {
      inputStream = httpsUrlConnection.getErrorStream();
    }
    return inputStream;
  }

  /**
   * Connects to the xREL API using a POST request and retrieves the {@link InputStream}. This can
   * be the ErrorStream if there is a problem while processing your request.
   * 
   * @param url The URL.
   * @param params The POST parameters.
   * @param token The optional {@link Token} to be used for authentication
   * @return The retrieved {@link InputStream}
   * @throws IOException If there is a networking problem
   */
  private static InputStream connectToXrelPost(String url, String params, Token token)
      throws IOException {
    HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) new URL(url).openConnection();
    httpsUrlConnection.setRequestMethod("POST");
    httpsUrlConnection.setDoInput(true);
    httpsUrlConnection.setDoOutput(true);
    httpsUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    httpsUrlConnection.setFixedLengthStreamingMode(params.getBytes().length);
    if (token != null) {
      httpsUrlConnection.addRequestProperty("Authorization", "Bearer " + token.getAccessToken());
    }

    OutputStream outputStream = httpsUrlConnection.getOutputStream();
    BufferedWriter writer =
        new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
    writer.write(params);
    writer.close();

    handleResponseHeaders(httpsUrlConnection);

    // Choose InputStream or ErrorStream depending on the response code
    InputStream inputStream;
    if (getResponseCode() == HttpsURLConnection.HTTP_OK) {
      inputStream = httpsUrlConnection.getInputStream();
    } else {
      inputStream = httpsUrlConnection.getErrorStream();
    }
    return inputStream;
  }

  /**
   * Reads a String from a given URL. If you are performing a POST request, use the keyList and
   * valueList to provide parameters. This implementation is using 2 {@link List} instead of a
   * {@link Map} because to provide arrays as parameters we need the same key twice or more times.
   * 
   * @param url The URL you want to read the JSON String from.
   * @param token The optional {@link Token} to be used for authentication
   * @param keyList Optional List of keys
   * @param valueList Optional List of values
   * @return The read String
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   */
  private static String readStringFromUrl(String url, Token token, List<String> keyList,
      List<String> valueList) throws IOException, XrelException {
    Objects.requireNonNull(url, "url missing");

    InputStream inputStream;
    if (keyList != null && valueList != null) {
      String params = buildQueryParameter(keyList, valueList);
      inputStream = connectToXrelPost(url, params, token);
    } else {
      inputStream = connectToXrelGet(url, token);
    }
    if (inputStream == null) {
      throw XrelException.createXrelException(Optional.empty(), getResponseCode());
    }
    BufferedReader bufferedReader =
        new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    String result = readAll(bufferedReader);
    if (result.contains("\"error\":")) {
      throw XrelException.createXrelException(Optional.of(result), getResponseCode());
    }
    inputStream.close();

    return result;
  }

  /**
   * Reads a String from a given URL using POST. Use the keyList and valueList to provide
   * parameters. This implementation is using 2 {@link List} instead of a {@link Map} because to
   * provide arrays as parameters we need the same key twice or more.
   * 
   * @param url The URL you want to read the String from.
   * @param keyList Optional List of keys
   * @param valueList Optional List of values
   * @return The read String
   * @throws MalformedURLException If the given URL is wrong
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   */
  public static String readStringFromUrlPost(String url, List<String> keyList,
      List<String> valueList) throws IOException, XrelException {
    Objects.requireNonNull(url, "url missing");
    Objects.requireNonNull(keyList, "keyList missing");
    Objects.requireNonNull(valueList, "valueList missing");

    return readStringFromUrl(url, null, keyList, valueList);
  }

  /**
   * Reads a String from a given URL using POST. Use the keyList and valueList to provide
   * parameters. This implementation is using 2 {@link List} instead of a {@link Map} because to
   * provide arrays as parameters we need the same key twice or more.
   * 
   * @param url The URL you want to read the String from.
   * @param token The {@link Token} to be used for authentication
   * @param keyList List of keys
   * @param valueList List of values
   * @return The read String
   * @throws MalformedURLException If the given URL is wrong
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   */
  public static String readStringFromUrlPost(String url, Token token, List<String> keyList,
      List<String> valueList) throws IOException, XrelException {
    Objects.requireNonNull(url, "url missing");
    Objects.requireNonNull(token, "token missing");
    Objects.requireNonNull(keyList, "keyList missing");
    Objects.requireNonNull(valueList, "valueList missing");

    return readStringFromUrl(url, token, keyList, valueList);
  }

  /**
   * Reads a JSON String from a given URL using GET.
   * 
   * @param url The URL you want to read the JSON String from.
   * @return The read JSON String
   * @throws MalformedURLException If the given URL is wrong
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   */
  public static String readStringFromUrlGet(String url) throws IOException, XrelException {
    Objects.requireNonNull(url, "url missing");

    return readStringFromUrl(url, null, null, null);
  }

  /**
   * Reads a JSON String from a given URL using GET.
   * 
   * @param url The URL you want to read the JSON String from.
   * @param token The {@link Token} to be used for authentication
   * @return The read JSON String
   * @throws MalformedURLException If the given URL is wrong
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   */
  public static String readStringFromUrlGet(String url, Token token)
      throws IOException, XrelException {
    Objects.requireNonNull(url, "url missing");
    Objects.requireNonNull(token, "token missing");

    return readStringFromUrl(url, token, null, null);
  }

  /**
   * Reads byte[] from a given URL.
   * 
   * @param url The URL you want to read the byte[] from.
   * @param token The {@link Token} to be used for authentication
   * @return The read byte[]
   * @throws MalformedURLException If the given URL is wrong
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   */
  public static byte[] readByteFromUrlGet(String url, Token token)
      throws IOException, XrelException {
    Objects.requireNonNull(url, "url missing");
    Objects.requireNonNull(token, "token missing");

    InputStream inputStream = connectToXrelGet(url, token);
    if (inputStream == null) {
      throw XrelException.createXrelException(Optional.empty(), getResponseCode());
    }

    // We need to save the input we get first because we have to process it
    // twice
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int currentByte = inputStream.read();
    while (currentByte != -1) {
      out.write(currentByte);
      currentByte = inputStream.read();
    }
    byte[] byteResponse = out.toByteArray();
    inputStream.close();

    // If there is an error we are given a JSONObject instead of an image
    // which we need to parse differently
    String jsonString = new String(byteResponse);
    if (jsonString.contains("\"error\":")) {
      throw XrelException.createXrelException(Optional.of(jsonString), getResponseCode());
    }

    return byteResponse;
  }

  /**
   * Gets the maximum number of requests that the consumer is permitted to make per hour as returned
   * by the last request. -1 if not yet set.
   * 
   * @return The X-RateLimit-Limit
   * @see <a href="https://www.xrel.to/wiki/2727/api-rate-limiting.html">API: Rate Limiting</a>
   */
  public static int getXRateLimitLimit() {
    return xRateLimitLimit;
  }

  /**
   * Sets the maximum number of requests that the consumer is permitted to make per hour as returned
   * by the last request. Use -1 to unset.
   * 
   * @param xRateLimitLimit The X-RateLimit-Limit to set
   * @see <a href="https://www.xrel.to/wiki/2727/api-rate-limiting.html">API: Rate Limiting</a>
   */
  private static void setXRateLimitLimit(int xRateLimitLimit) {
    NetworkingHelper.xRateLimitLimit = xRateLimitLimit;
  }

  /**
   * Gets the number of requests remaining in the current rate limit window as returned by the last
   * request. -1 if not yet set.
   * 
   * @return The X-RateLimit-Remaining
   * @see <a href="https://www.xrel.to/wiki/2727/api-rate-limiting.html">API: Rate Limiting</a>
   */
  public static int getXRateLimitRemaining() {
    return xRateLimitRemaining;
  }

  /**
   * Sets the number of requests remaining in the current rate limit window as returned by the last
   * request. Use -1 to unset.
   * 
   * @param xRateLimitRemaining The X-RateLimit-Remaining to set
   * @see <a href="https://www.xrel.to/wiki/2727/api-rate-limiting.html">API: Rate Limiting</a>
   */
  private static void setXRateLimitRemaining(int xRateLimitRemaining) {
    NetworkingHelper.xRateLimitRemaining = xRateLimitRemaining;
  }

  /**
   * Gets the time at which the current rate limit window resets in UTC epoch seconds as returned by
   * the last request. -1 if not yet set.
   * 
   * @return The X-RateLimit-Reset
   * @see <a href="https://www.xrel.to/wiki/2727/api-rate-limiting.html">API: Rate Limiting</a>
   */
  public static int getXRateLimitReset() {
    return xRateLimitReset;
  }

  /**
   * Sets the time at which the current rate limit window resets in UTC epoch seconds as returned by
   * the last request. Use -1 to unset.
   * 
   * @param xRateLimitReset The X-RateLimit-Reset to set
   * @see <a href="https://www.xrel.to/wiki/2727/api-rate-limiting.html">API: Rate Limiting</a>
   */
  private static void setXRateLimitReset(int xRateLimitReset) {
    NetworkingHelper.xRateLimitReset = xRateLimitReset;
  }

  /**
   * Gets the response code returned by the last request. -1 if not yet set.
   * 
   * @return responseCode The response code
   */
  static int getResponseCode() {
    return responseCode;
  }

  /**
   * Sets the response code returned by the last request. Use -1 to unset.
   * 
   * @param responseCode The response code to set
   */
  private static void setResponseCode(int responseCode) {
    NetworkingHelper.responseCode = responseCode;
  }

}
