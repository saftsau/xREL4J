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
 * Class represents a group as used in the P2P section on xREL.
 */
public class P2pGroup {

	private String id;
	private String name;

	/**
	 * Constructs a new P2pGroup from a {@link JSONObject} returned from the
	 * xREL API.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this P2pGroup
	 * @throws JSONException
	 */
	P2pGroup(JSONObject jsonObject) throws JSONException {
		parseJsonObject(jsonObject);
	}

	/**
	 * Parses a given {@link JSONObject} and saves its values to this object.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this P2pGroup
	 * @throws JSONException
	 */
	void parseJsonObject(JSONObject jsonObject) throws JSONException {
		setId(jsonObject.getString("id"));
		setName(jsonObject.getString("name"));
	}

	/**
	 * Gets the API ID of this P2pGroup.
	 * 
	 * @return The ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the API ID of this P2pGroup.
	 * 
	 * @param id
	 *            The ID to set
	 */
	private void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the name of this P2pGroup.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this P2pGroup.
	 * 
	 * @param name
	 *            The name to set
	 */
	private void setName(String name) {
		this.name = name;
	}

}