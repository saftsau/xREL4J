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
 * Class represents a category as used in the P2P section on xREL.
 */
public class P2pCategory {

	private String metaCat;
	private String subCat;
	private String id;

	/**
	 * Constructs a new P2pCategory from a {@link JSONObject} returned from the
	 * xREL API.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this P2pCategory
	 * @throws JSONException
	 */
	P2pCategory(JSONObject jsonObject) throws JSONException {
		parseJsonObject(jsonObject);
	}

	/**
	 * Parses a given {@link JSONObject} and saves its values to this object.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this P2pCategory
	 * @throws JSONException
	 */
	void parseJsonObject(JSONObject jsonObject) throws JSONException {
		setMetaCat(jsonObject.getString("meta_cat"));
		setSubCat(jsonObject.getString("sub_cat"));
		setId(jsonObject.getString("id"));
	}

	/**
	 * Gets the meta category. Can be seen as a parent category.
	 * 
	 * @return The meta category
	 */
	public String getMetaCat() {
		return metaCat;
	}

	/**
	 * Sets the meta category. Can be seen as a parent category.
	 * 
	 * @param metaCat
	 *            The meta category to set
	 */
	private void setMetaCat(String metaCat) {
		this.metaCat = metaCat;
	}

	/**
	 * Gets the sub category. Can be seen as the name of this P2pCategory.
	 * 
	 * @return The sub category
	 */
	public String getSubCat() {
		return subCat;
	}

	/**
	 * Sets the sub category. Can be seen as the name of this P2pCategory.
	 * 
	 * @param subCat
	 *            The sub category to set
	 */
	private void setSubCat(String subCat) {
		this.subCat = subCat;
	}

	/**
	 * Gets the API ID of this P2pCategory.
	 * 
	 * @return The ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the API ID of this P2pCategory.
	 * 
	 * @param id
	 *            The ID to set
	 */
	private void setId(String id) {
		this.id = id;
	}

}