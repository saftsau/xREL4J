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

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Helper class used to handle all networking related operations, e.g. GET and
 * POST requests on the xREL API. This class also implements the handling of
 * errors and throws corresponding exceptions.
 */
abstract class NetworkingHelper {

	private static int xRateLimitLimit = -1;
	private static int xRateLimitRemaining = -1;
	private static int xRateLimitReset = -1;
	private static int responseCode = -1;

	/**
	 * Builds the query parameters needed for POST requests. This implementation
	 * is using 2 {@link ArrayList} instead of a {@link HashMap} because to
	 * provide arrays as parameters we need the same key twice or more times.
	 *
	 * @param keyList
	 *            List of keys
	 * @param valueList
	 *            List of values
	 * @return The built String to be used for the POST request
	 * @throws UnsupportedEncodingException
	 * @throws XrelException
	 */
	private static String buildQueryParameter(ArrayList<String> keyList, ArrayList<String> valueList)
			throws UnsupportedEncodingException {
		if (keyList == null || valueList == null) {
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
			result.append(URLEncoder.encode(keyList.get(i), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(valueList.get(i), "UTF-8"));
		}

		return result.toString();
	}

	/**
	 * Reads all input by a given {@link Reader} and converts it to a
	 * {@link String}.
	 * 
	 * @param reader
	 *            The {@link Reader} to read the input from
	 * @return The input as a {@link String}
	 * @throws IOException
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
	 * Connects to the xREL API using a GET request and retrieves the
	 * {@link InputStream}. This can be the ErrorStream if there is a problem
	 * while processing your request.
	 * 
	 * @param url
	 *            The URL
	 * @param token
	 *            The {@link Token} to be used for authentication or
	 *            {@code null}
	 * @return The retrieved {@link InputStream}
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private static InputStream connectToXrelGet(String url, Token token) throws MalformedURLException, IOException {
		HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) new URL(url).openConnection();
		httpsUrlConnection.setRequestMethod("GET");
		if (token != null) {
			httpsUrlConnection.addRequestProperty("Authorization", "Bearer " + token.getAccessToken());
		}
		httpsUrlConnection.setDoInput(true);
		httpsUrlConnection.setDoOutput(true);

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
	 * Connects to the xREL API using a POST request and retrieves the
	 * {@link InputStream}. This can be the ErrorStream if there is a problem
	 * while processing your request.
	 * 
	 * @param url
	 *            The URL.
	 * @param params
	 *            The POST parameters.
	 * @param token
	 *            The {@link Token} to be used for authentication or
	 *            {@code null}.
	 * @return The retrieved {@link InputStream}
	 * @throws IOException
	 */
	private static InputStream connectToXrelPost(String url, String params, Token token) throws IOException {
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
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
		writer.write(params);
		writer.close();

		// Handle the response code
		setResponseCode(httpsUrlConnection.getResponseCode());

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
	 * Reads a {@link JSONObject} from a given URL. If you are performing a POST
	 * request, use the keyList and valueList to provide parameters. This
	 * implementation is using 2 {@link ArrayList} instead of a {@link HashMap}
	 * because to provide arrays as parameters we need the same key twice or
	 * more.
	 * 
	 * @param url
	 *            The URL you want to read the {@link JSONObject} from.
	 * @param token
	 *            The {@link Token} to be used for authentication or
	 *            {@code null}
	 * @param keyList
	 *            List of keys or {@code null}
	 * @param valueList
	 *            List of values or {@code null}
	 * @return The read {@link JSONObject}
	 * @throws IOException
	 * @throws XrelException
	 */
	private static JSONObject readJsonObjectFromUrl(String url, Token token, ArrayList<String> keyList,
			ArrayList<String> valueList) throws IOException, XrelException {
		InputStream inputStream;
		if (keyList != null && valueList != null) {
			String params = buildQueryParameter(keyList, valueList);
			inputStream = connectToXrelPost(url, params, token);
		} else {
			inputStream = connectToXrelGet(url, token);
		}
		if (inputStream == null) {
			throw XrelException.createXrelException(null, getResponseCode());
		}

		try {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream, Charset.forName("UTF-8")));
			String jsonString = readAll(bufferedReader);
			JSONObject jsonObject = new JSONObject(jsonString);
			if (jsonObject.has("error")) {
				throw XrelException.createXrelException(jsonObject, getResponseCode());
			}
			return jsonObject;
		} catch (JSONException ex) {
			throw XrelException.createXrelException(null, getResponseCode());
		} finally {
			inputStream.close();
		}
	}

	/**
	 * Reads a {@link JSONObject} from a given URL. Use the keyList and
	 * valueList to provide parameters. This implementation is using 2
	 * {@link ArrayList} instead of a {@link HashMap} because to provide arrays
	 * as parameters we need the same key twice or more.
	 * 
	 * @param url
	 *            The URL you want to read the {@link JSONObject} from.
	 * @param token
	 *            The {@link Token} to be used for authentication or
	 *            {@code null}
	 * @param keyList
	 *            List of keys
	 * @param valueList
	 *            List of values
	 * @return The read {@link JSONObject}
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws XrelException
	 */
	static JSONObject readJsonObjectFromUrlPost(String url, Token token, ArrayList<String> keyList,
			ArrayList<String> valueList) throws MalformedURLException, IOException, XrelException {
		return readJsonObjectFromUrl(url, token, keyList, valueList);
	}

	/**
	 * Reads a {@link JSONObject} from a given URL.
	 * 
	 * @param url
	 *            The URL you want to read the {@link JSONObject} from.
	 * @param token
	 *            The {@link Token} to be used for authentication or
	 *            {@code null}
	 * @return The read {@link JSONObject}
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws XrelException
	 */
	static JSONObject readJsonObjectFromUrlGet(String url, Token token)
			throws MalformedURLException, IOException, XrelException {
		return readJsonObjectFromUrl(url, token, null, null);
	}

	/**
	 * Reads a {@link JSONArray} from a given URL.
	 * 
	 * @param url
	 *            The URL you want to read the {@link JSONArray} from.
	 * @param token
	 *            The {@link Token} to be used for authentication or
	 *            {@code null}
	 * @return The read {@link JSONArray} or {@code null} on error
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws XrelException
	 */
	static JSONArray readJsonArrayFromUrlGet(String url, Token token)
			throws MalformedURLException, IOException, XrelException {
		InputStream inputStream = connectToXrelGet(url, token);
		if (inputStream == null) {
			throw XrelException.createXrelException(null, getResponseCode());
		}

		String jsonString = "";
		try {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream, Charset.forName("UTF-8")));
			jsonString = readAll(bufferedReader);
		} finally {
			inputStream.close();
		}

		// If there is an error we are given a JSONObject instead of a JSONArray
		// which we need to parse differently
		boolean error = true;
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jsonString);
		} catch (JSONException ex) {
			error = false;
		}
		if (error) {
			throw XrelException.createXrelException(jsonObject, getResponseCode());
		}

		// Otherwise we can just use the JSONArray
		return new JSONArray(jsonString);
	}

	/**
	 * Reads a {@link BufferedImage} from a given URL.
	 * 
	 * @param url
	 *            The URL you want to read the {@link BufferedImage} from.
	 * @param token
	 *            The {@link Token} to be used for authentication or
	 *            {@code null}
	 * @return The read {@link BufferedImage} or {@code null} on error
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws XrelException
	 */
	static BufferedImage readBufferedImageFromUrlGet(String url, Token token)
			throws MalformedURLException, IOException, XrelException {
		InputStream inputStream = connectToXrelGet(url, token);
		if (inputStream == null) {
			throw XrelException.createXrelException(null, getResponseCode());
		}

		// We need to save the input we get first because we have to process it
		// twice
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int byte_ = inputStream.read();
		while (byte_ != -1) {
			out.write(byte_);
			byte_ = inputStream.read();
		}
		byte[] byteResponse = out.toByteArray();
		inputStream.close();

		// If there is an error we are given a JSONObject instead of an image
		// which we need to parse differently
		boolean error = true;
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(new String(byteResponse));
		} catch (JSONException ex) {
			error = false;
		}
		if (error) {
			throw XrelException.createXrelException(jsonObject, getResponseCode());
		}

		InputStream byteInputStream = null;
		try {
			byteInputStream = new ByteArrayInputStream(byteResponse);
			return ImageIO.read(byteInputStream);
		} finally {
			if (byteInputStream != null) {
				byteInputStream.close();
			}
		}
	}

	/**
	 * Gets the maximum number of requests that the consumer is permitted to
	 * make per hour as returned by the last request. -1 if not yet set.
	 * 
	 * @return The X-RateLimit-Limit
	 * @see https://www.xrel.to/wiki/2727/api-rate-limiting.html
	 */
	static int getXRateLimitLimit() {
		return xRateLimitLimit;
	}

	/**
	 * Sets the maximum number of requests that the consumer is permitted to
	 * make per hour as returned by the last request. Use -1 to unset.
	 * 
	 * @param xRateLimitLimit
	 *            The X-RateLimit-Limit to set
	 * @see https://www.xrel.to/wiki/2727/api-rate-limiting.html
	 */
	private static void setXRateLimitLimit(int xRateLimitLimit) {
		NetworkingHelper.xRateLimitLimit = xRateLimitLimit;
	}

	/**
	 * Gets the number of requests remaining in the current rate limit window as
	 * returned by the last request. -1 if not yet set.
	 * 
	 * @return The X-RateLimit-Remaining
	 * @see https://www.xrel.to/wiki/2727/api-rate-limiting.html
	 */
	static int getXRateLimitRemaining() {
		return xRateLimitRemaining;
	}

	/**
	 * Sets the number of requests remaining in the current rate limit window as
	 * returned by the last request. Use -1 to unset.
	 * 
	 * @param xRateLimitRemaining
	 *            The X-RateLimit-Remaining to set
	 * @see https://www.xrel.to/wiki/2727/api-rate-limiting.html
	 */
	private static void setXRateLimitRemaining(int xRateLimitRemaining) {
		NetworkingHelper.xRateLimitRemaining = xRateLimitRemaining;
	}

	/**
	 * Gets the time at which the current rate limit window resets in UTC epoch
	 * seconds as returned by the last request. -1 if not yet set.
	 * 
	 * @return The X-RateLimit-Reset
	 * @see https://www.xrel.to/wiki/2727/api-rate-limiting.html
	 */
	static int getXRateLimitReset() {
		return xRateLimitReset;
	}

	/**
	 * Sets the time at which the current rate limit window resets in UTC epoch
	 * seconds as returned by the last request. Use -1 to unset.
	 * 
	 * @param xRateLimitReset
	 *            The X-RateLimit-Reset to set
	 * @see https://www.xrel.to/wiki/2727/api-rate-limiting.html
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
	 * @param responseCode
	 *            The response code to set
	 */
	private static void setResponseCode(int responseCode) {
		NetworkingHelper.responseCode = responseCode;
	}

}