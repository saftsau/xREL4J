/*
 * Copyright 2018 saftsau
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

import java.io.IOException;
import java.util.List;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * {@link ClientResponseFilter} class used to handle rate limit headers.
 */
@Priority(Priorities.HEADER_DECORATOR)
@Provider
class RateLimitFilter implements ClientResponseFilter {

  private static int xRateLimitLimit = -1;
  private static int xRateLimitRemaining = -1;
  private static int xRateLimitReset = -1;
  private static int responseCode = -1;

  @Override
  public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext)
      throws IOException {
    if (responseContext != null) {
      setResponseCode(responseContext.getStatus());
      if (responseContext.getHeaders() != null && responseContext.getHeaders().size() > 0) {
        // X-RateLimit-Limit
        final List<String> xRateLimitLimitList =
            responseContext.getHeaders().get("X-RateLimit-Limit");
        if (xRateLimitLimitList != null && xRateLimitLimitList.size() == 1) {
          setXRateLimitLimit(Integer.valueOf(xRateLimitLimitList.get(0)));
        }

        // X-RateLimit-Remaining
        final List<String> xRateLimitRemainingList =
            responseContext.getHeaders().get("X-RateLimit-Remaining");
        if (xRateLimitRemainingList != null && xRateLimitRemainingList.size() == 1) {
          setXRateLimitRemaining(Integer.valueOf(xRateLimitRemainingList.get(0)));
        }

        // X-RateLimit-Reset
        final List<String> xRateLimitLimitReset =
            responseContext.getHeaders().get("X-RateLimit-Reset");
        if (xRateLimitLimitReset != null && xRateLimitLimitReset.size() == 1) {
          setXRateLimitReset(Integer.valueOf(xRateLimitLimitReset.get(0)));
        }
      }
    }
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
    RateLimitFilter.xRateLimitLimit = xRateLimitLimit;
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
    RateLimitFilter.xRateLimitRemaining = xRateLimitRemaining;
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
    RateLimitFilter.xRateLimitReset = xRateLimitReset;
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
    RateLimitFilter.responseCode = responseCode;
  }

}
