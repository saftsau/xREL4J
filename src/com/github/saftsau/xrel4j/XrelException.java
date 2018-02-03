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

package com.github.saftsau.xrel4j;

import com.github.saftsau.xrel4j.util.JsonbSingleton;
import java.util.Optional;

/**
 * Class represents an xREL API error wrapped into a standard Java {@link Exception}. Check xREL for
 * used error codes and their meaning. Additionally offers the response code of the server.
 * 
 * @see <a href="https://www.xrel.to/wiki/6435/api-errors.html">API: Error Handling</a>
 */
public class XrelException extends Exception {

  private static final long serialVersionUID = -6556750388263888839L;
  private Optional<Error> error;
  private int responseCode;

  /**
   * Creates an XrelException based on a JSON String as returned by the xREL API.
   * 
   * @param json The JSON String to create an XrelException from
   * @param responseCode The response code given from the xREL API
   * @return The created XrelException
   */
  public static XrelException createXrelException(Optional<String> json, int responseCode) {
    String message;
    if (json.isPresent()) {
      message = "response_code: ";
      message += responseCode;
      return new XrelException(message, responseCode);
    } else {
      Error error = JsonbSingleton.getInstance().getJsonb().fromJson(json.get(), Error.class);
      message = "error: ";
      message += error.getError();
      message += " - error_type: ";
      message += error.getErrorType();
      message += " - error_description: ";
      message += error.getErrorDescription();
      message += " - response_code: ";
      message += responseCode;
      return new XrelException(message, error, responseCode);
    }
  }

  /**
   * Constructs an XrelException with solely an error message.
   * 
   * @param message The error message
   */
  public XrelException(String message) {
    super(message);
    this.error = Optional.empty();
    this.responseCode = -1;
  }

  /**
   * Constructs an XrelException with the response code from the xREL API but no JSON String. Can be
   * used if no network connection is available.
   * 
   * @param message The error message
   * @param responseCode The HTTP response code
   */
  public XrelException(String message, int responseCode) {
    super(message);
    this.error = Optional.empty();
    this.responseCode = responseCode;
  }

  /**
   * Constructs an XrelException with all extra information the xREL API gives when returning an
   * error.
   * 
   * @param message The error message
   * @param error The error from the xREL API
   * @param responseCode The response code from the xREL API
   */
  public XrelException(String message, Error error, int responseCode) {
    super(message);
    this.error = Optional.of(error);
    this.responseCode = responseCode;
  }

  /**
   * Returns the {@link Error} object associated with this Exception..
   * 
   * @return the error
   */
  public Optional<Error> getError() {
    return error;
  }

  /**
   * Returns the response code of this exception. {@code -1} if not set.
   * 
   * @return the responseCode
   */
  public int getResponseCode() {
    return responseCode;
  }

  @Override
  public String toString() {
    return "XrelException [getError()=" + getError() + ", getResponseCode()=" + getResponseCode()
        + ", getMessage()=" + getMessage() + "]";
  }

}
