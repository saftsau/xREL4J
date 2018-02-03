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

/**
 * Class represents an Error. Used for all information the xREL API offers when returning errors.
 * 
 * @see <a href="https://www.xrel.to/wiki/6435/api-errors.html">API: Error Handling</a>
 */
public class Error {

  public String error;
  public String errorDescription;
  public String errorType;

  /**
   * Gets the error.
   * 
   * @return The error
   */
  public String getError() {
    return error;
  }

  /**
   * Gets the description of the error.
   * 
   * @return The description of the error
   */
  public String getErrorDescription() {
    return errorDescription;
  }

  /**
   * Gets the type of the error.
   * 
   * @return The type of the error
   */
  public String getErrorType() {
    return errorType;
  }

  /**
   * Sets the error.
   * 
   * @param error The error to set
   */
  public void setError(String error) {
    this.error = error;
  }

  /**
   * Sets the description of the error.
   * 
   * @param errorDescription The description of the error to set
   */
  public void setErrorDescription(String errorDescription) {
    this.errorDescription = errorDescription;
  }

  /**
   * Sets the type of the error.
   * 
   * @param errorType The type of the error to set
   */
  public void setErrorType(String errorType) {
    this.errorType = errorType;
  }

  @Override
  public String toString() {
    return "Error [getError()=" + getError() + ", getErrorDescription()=" + getErrorDescription()
        + ", getErrorType()=" + getErrorType() + "]";
  }

}
