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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class represents a Token used by the xREL API to access protected methods or
 * additional user data.
 */
public class Token {

	private final String accessToken;
	private final String tokenType;
	private final int expiresIn;
	private final String refreshToken;
	private final long creationTime;

	/**
	 * Constructs a new Token from a {@link JSONObject} returned from the xREL
	 * API.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this Token
	 * @throws JSONException
	 */
	public Token(JSONObject jsonObject) throws JSONException {
		this.accessToken = jsonObject.getString("access_token");
		this.tokenType = jsonObject.getString("token_type");
		this.expiresIn = jsonObject.getInt("expires_in");
		this.refreshToken = jsonObject.getString("refresh_token");
		this.creationTime = System.currentTimeMillis() / 1000L;
	}

	/**
	 * Constructs a new Token manually by given all needed information.
	 * 
	 * @param accessToken
	 *            The access token, with which you may do requests to protected
	 *            resources. It is a JSON Web Token, which you can verify and
	 *            decode for more information (e.g. scopes & lifetime).
	 * @param tokenType
	 *            This value should always be Bearer
	 * @param expiresIn
	 *            Lifetime of the access token in seconds
	 * @param refreshToken
	 *            With the refresh token you may request a new access_token
	 *            after it has expired. Its lifetime is unlimited, however you
	 *            can only use it once. You will get a new refresh_token when
	 *            renewing the access_token.
	 */
	public Token(String accessToken, String tokenType, int expiresIn, String refreshToken) {
		this.accessToken = accessToken;
		this.tokenType = tokenType;
		this.expiresIn = expiresIn;
		this.refreshToken = refreshToken;
		this.creationTime = System.currentTimeMillis() / 1000L;
	}

	/**
	 * Gets the access token used to access protected resources.
	 * 
	 * @return The access token
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * Gets the type of this token. Is always {@code Bearer}.
	 * 
	 * @return The token type
	 */
	public String getTokenType() {
		return tokenType;
	}

	/**
	 * Gets the remaining time the access token is valid.
	 * 
	 * @return The time this token expires in
	 */
	public long getExpiresIn() {
		long remaining = (getCreationTime() + expiresIn) - (System.currentTimeMillis() / 1000L);
		if (remaining < 0) {
			remaining = 0;
		}
		return remaining;
	}

	/**
	 * Gets the refresh token used for refreshing the access token.
	 * 
	 * @return The refresh token
	 */
	public String getRefreshToken() {
		return refreshToken;
	}

	/**
	 * Gets the timestamp when this Token was created.
	 * 
	 * @return The creation timestamp
	 */
	private long getCreationTime() {
		return creationTime;
	}

}