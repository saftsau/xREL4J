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
 * Class represents a filter, which can be used to customize the
 * getLatestRelease() method.
 */
public class Filter {

	private long id;
	private String name;

	/**
	 * Constructs a new Filter from a {@link JSONObject} returned from the xREL
	 * API.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this Filter
	 * @throws JSONException
	 */
	Filter(JSONObject jsonObject) throws JSONException {
		parseJsonObject(jsonObject);
	}

	/**
	 * Parses a given {@link JSONObject} and saves its values to this object.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this Filter
	 * @throws JSONException
	 */
	void parseJsonObject(JSONObject jsonObject) throws JSONException {
		setId(jsonObject.getLong("id"));
		setName(jsonObject.getString("name"));
	}

	/**
	 * Gets the ID of this Filter.
	 * 
	 * @return The ID
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the ID of this Filter.
	 * 
	 * @param id
	 *            The ID to set
	 */
	private void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the name of this filter.
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this filter.
	 * 
	 * @param name
	 *            The name to set
	 */
	private void setName(String name) {
		this.name = name;
	}

}