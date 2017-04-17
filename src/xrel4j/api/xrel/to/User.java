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
 * Class represents a user as returned by the xREL API. This is implemented
 * using a singleton pattern.
 */
public class User {

	private String id;
	private String name;
	private String secret;
	private String locale;
	private String avatarUrl;
	private String avatarThumbUrl;
	private static User instance;

	/**
	 * Creates a new User object.
	 */
	private User() {
	}

	/**
	 * Gets the current instance of this class or creates a new one if this is
	 * the first call of this method.
	 * 
	 * @return The user instance
	 */
	static synchronized User getInstance() {
		if (User.instance == null) {
			User.instance = new User();
		}
		return User.instance;
	}

	/**
	 * Parses a given {@link JSONObject} and saves its values to this object.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this User
	 * @throws JSONException
	 */
	void parseJsonObject(JSONObject jsonObject) throws JSONException {
		setId(jsonObject.getString("id"));
		setName(jsonObject.getString("name"));
		setSecret(jsonObject.getString("secret"));
		setLocale(jsonObject.getString("locale"));
		setAvatarUrl(jsonObject.getString("avatar_url"));
		setAvatarThumbUrl(jsonObject.getString("avatar_thumb_url"));
	}

	/**
	 * Gets the API ID of this user.
	 * 
	 * @return The ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the API ID of this user.
	 * 
	 * @param id
	 *            The ID to set
	 */
	private void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the name of this user.
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this user.
	 * 
	 * @param name
	 *            The name to set
	 */
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the secret of this user.
	 * 
	 * @return The secret
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * Sets the secret of this user.
	 * 
	 * @param secret
	 *            The secret to set
	 */
	private void setSecret(String secret) {
		this.secret = secret;
	}

	/**
	 * Gets the locale of this user. Can be either {@code de_DE} or
	 * {@code en_US}.
	 * 
	 * @return The locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * Sets the locale of this user. Should be either {@code de_DE} or
	 * {@code en_US}.
	 * 
	 * @param locale
	 *            The locale to set
	 */
	private void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * Gets the avatar link of this user.
	 * 
	 * @return The avatar link
	 */
	public String getAvatarUrl() {
		return avatarUrl;
	}

	/**
	 * Sets the avatar link of this user.
	 * 
	 * @param avatarUrl
	 *            The avatar link to set
	 */
	private void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	/**
	 * Gets the avatar thumbnail link of this user.
	 * 
	 * @return The avatar thumbnail link
	 */
	public String getAvatarThumbUrl() {
		return avatarThumbUrl;
	}

	/**
	 * Sets the avatar thumbnail link of this user.
	 * 
	 * @param avatarThumbUrl
	 *            The avatar thumbnail link to set
	 */
	private void setAvatarThumbUrl(String avatarThumbUrl) {
		this.avatarThumbUrl = avatarThumbUrl;
	}

}