/*
 * Copyright 2017 saftsau
 *
 * This file is part of xREL4J.
 *
 * xREL4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * xREL4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with xREL4J.  If not, see <http://www.gnu.org/licenses/>.
 */
package xrel4j.api.xrel.to;

import org.json.JSONObject;

/**
 * Class represents an xREL API error wrapped into a standard Java
 * {@link Exception}. Check xREL for used error codes and their meaning.
 * Additionally offers the response code of the server.
 * 
 * @see https://www.xrel.to/wiki/6435/api-errors.html
 */
public class XrelException extends Exception {

	private static final long serialVersionUID = -6556750388263888839L;
	private String error;
	private String errorType;
	private String errorDescription;
	private int responseCode;

	/**
	 * Creates an XrelException based on a {@link JSONObject} as returned by the
	 * xREL API.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} to create an XrelException from
	 * @param responseCode
	 *            The response code given from the xREL API
	 * @return The created XrelException
	 */
	static XrelException createXrelException(JSONObject jsonObject, int responseCode) {
		String message;
		if (jsonObject == null || !jsonObject.has("error")) {
			message = "response_code: ";
			message += responseCode;
			return new XrelException(message, responseCode);
		} else {
			String error = jsonObject.getString("error");
			String errorType = jsonObject.getString("error_type");
			String errorDescription = jsonObject.getString("error_description");
			message = "error: ";
			message += error;
			message += " - error_type: ";
			message += errorType;
			message += " - error_description: ";
			message += errorDescription;
			message += " - response_code: ";
			message += responseCode;
			return new XrelException(message, error, errorType, errorDescription, responseCode);
		}
	}

	/**
	 * Constructs an XrelException with solely an error message.
	 * 
	 * @param message
	 *            The error message
	 */
	public XrelException(String message) {
		super(message);
		this.error = "";
		this.errorType = "";
		this.errorDescription = "";
		this.responseCode = -1;
	}

	/**
	 * Constructs an XrelException with the response code from the xREL API but
	 * no {@link JSONObject}. Can be used if no network connection is available.
	 * 
	 * @param message
	 *            The error message
	 * @param responseCode
	 */
	public XrelException(String message, int responseCode) {
		super(message);
		this.error = "";
		this.errorType = "";
		this.errorDescription = "";
		this.responseCode = responseCode;
	}

	/**
	 * Constructs an XrelException with all extra information the xREL API gives
	 * when returning an error.
	 * 
	 * @param message
	 *            The error message
	 * @param error
	 *            The error from the xREL API
	 * @param errorType
	 *            The error type from the xREL API
	 * @param errorDescription
	 *            The error description from the xREL API
	 * @param responseCode
	 *            The response code from the xREL API
	 */
	public XrelException(String message, String error, String errorType, String errorDescription, int responseCode) {
		super(message);
		this.error = error;
		this.errorType = errorType;
		this.errorDescription = errorDescription;
		this.responseCode = responseCode;
	}

	/**
	 * Gets the error given by the xREL API. Can be an empty {@link String}.
	 * 
	 * @return The error
	 */
	public String getError() {
		return error;
	}

	/**
	 * Gets the error type given by the xREL API. Can be an empty
	 * {@link String}.
	 * 
	 * @return The error type
	 */
	public String getErrorType() {
		return errorType;
	}

	/**
	 * Gets the error description given by the xREL API. Can be an empty
	 * {@link String}.
	 * 
	 * @return The error description
	 */
	public String getErrorDescription() {
		return errorDescription;
	}

	/**
	 * Gets the response code given by the xREL API. {@code -1} if not given.
	 * 
	 * @return The response code
	 */
	public int getResponseCode() {
		return responseCode;
	}

}