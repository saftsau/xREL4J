/*
 * Copyright 2017 - 2019 saftsau
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

import java.util.Optional;

/**
 * Class represents an xREL API error wrapped into a standard Java {@link Exception}. Check xREL for
 * used error codes and their meaning. Additionally offers the response code of the server.
 * 
 * @see <a href="https://www.xrel.to/wiki/6435/api-errors.html">API: Error Handling</a>
 */
public class XrelException extends RuntimeException {

  private static final long serialVersionUID = -6556750388263888839L;
  private Optional<Error> error;
  private int responseCode;

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
   * Constructs an XrelException with solely a response code.
   * 
   * @param responseCode The HTTP response code
   */
  public XrelException(int responseCode) {
    super();
    this.error = Optional.empty();
    this.responseCode = responseCode;
  }

  /**
   * Constructs an XrelException based on another Throwable.
   * 
   * @param throwable The parent {@link Throwable}
   */
  public XrelException(Throwable throwable) {
    super(throwable);
    this.error = Optional.empty();
  }

  /**
   * Constructs an XrelException with the response code from the xREL API but no content. This is
   * generally used, when we can't unmarshal the object that was returned.
   * 
   * @param throwable The parent {@link Throwable}
   * @param responseCode The HTTP response code
   */
  public XrelException(Throwable throwable, int responseCode) {
    super(throwable);
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
