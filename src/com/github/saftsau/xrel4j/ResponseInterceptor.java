/*
 * Copyright 2018, 2019 saftsau
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

package com.github.saftsau.xrel4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import okhttp3.Interceptor;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

/**
 * {@link ClientResponseFilter} class used to handle rate limit headers.
 */
class ResponseInterceptor implements Interceptor {

  private static int xRateLimitLimit = -1;
  private static int xRateLimitRemaining = -1;
  private static int xRateLimitReset = -1;
  private static int responseCode = -1;

  @Override
  public Response intercept(Chain chain) throws IOException {
    Response response = chain.proceed(chain.request());

    setResponseCode(response.code());

    final String xRateLimitLimit = response.headers().get("X-RateLimit-Limit");
    if (xRateLimitLimit != null) {
      setXRateLimitLimit(Integer.valueOf(xRateLimitLimit));
    }

    final String xRateLimitRemaining = response.headers().get("X-RateLimit-Remaining");
    if (xRateLimitRemaining != null) {
      setXRateLimitRemaining(Integer.valueOf(xRateLimitRemaining));
    }

    final String xRateLimitReset = response.headers().get("X-RateLimit-Reset");
    if (xRateLimitReset != null) {
      setXRateLimitReset(Integer.valueOf(xRateLimitReset));
    }

    // Try to handle an error. We have to rely on this method because currently the status codes
    // returned by the xREL API can't be trusted, e.g. returning 2xx responses for errors.
    // Otherwise we have an error
    BufferedSource source = response.body().source();
    source.request(Long.MAX_VALUE); // request the entire body.
    Buffer buffer = source.buffer();
    // clone buffer before reading from it
    String responseString = buffer.clone().readString(StandardCharsets.UTF_8);
    ObjectMapper objectMapper = new ObjectMapper();
    Error error = null;
    try {
      error = objectMapper.readValue(responseString, Error.class);
    } catch (Exception e) {
      // Nothing, either there was no error or it was not an xREL API error
    }
    if (error != null) {
      throw new XrelException(error.getErrorDescription(), error, getResponseCode());
    } else if (!response.isSuccessful()) {
      throw new XrelException(getResponseCode());
    }

    return response;
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
    ResponseInterceptor.xRateLimitLimit = xRateLimitLimit;
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
    ResponseInterceptor.xRateLimitRemaining = xRateLimitRemaining;
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
    ResponseInterceptor.xRateLimitReset = xRateLimitReset;
  }

  /**
   * Gets the response code returned by the last request. -1 if not yet set.
   * 
   * @return responseCode The response code
   */
  public static int getResponseCode() {
    return responseCode;
  }

  /**
   * Sets the response code returned by the last request. Use -1 to unset.
   * 
   * @param responseCode The response code to set
   */
  private static void setResponseCode(int responseCode) {
    ResponseInterceptor.responseCode = responseCode;
  }

}
