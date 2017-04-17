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

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class represents a category as used in the Release section on xREL.
 */
public class ReleaseCategory {

	private String name;
	private ReleaseCategory parentCat;
	private String parentCatString;

	/**
	 * Constructs a new ReleaseCategory from a {@link JSONObject} returned from
	 * the xREL API.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this ReleaseCategory
	 * @throws JSONException
	 */
	ReleaseCategory(JSONObject jsonObject) throws JSONException {
		parseJsonObject(jsonObject);
	}

	/**
	 * Parses a given {@link JSONObject} and saves its values to this object.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this ReleaseCategory
	 * @throws JSONException
	 */
	void parseJsonObject(JSONObject jsonObject) throws JSONException {
		setName(jsonObject.getString("name"));
		setParentCatString(jsonObject.optString("parent_cat"));
	}

	/**
	 * Sets the {@link #parentCat} of this ReleaseCategory. As this depends on
	 * other {@link ReleaseCategory}, this should be called after all of them
	 * are saved and can be compared.
	 * 
	 * @param hashMap
	 *            The {@link HashMap} containing all {@link ReleaseCategory} and
	 *            their {@link #name}.
	 */
	void setParentCategories(HashMap<String, ReleaseCategory> hashMap) {
		if (!getParentCatString().isEmpty() && hashMap.containsKey(getParentCatString())) {
			setParentCat(hashMap.get(getParentCatString()));
		}
	}

	/**
	 * Gets the name of this ReleaseCategory.
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this ReleaseCategory.
	 * 
	 * @param name
	 *            The name to set
	 */
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the parent category of this ReleaseCategory. {@code null} if not
	 * set.
	 * 
	 * @return The parent category
	 */
	public ReleaseCategory getParentCat() {
		return parentCat;
	}

	/**
	 * Sets the parent category of this ReleaseCategory. {@code null} to unset.
	 * 
	 * @param parentCat
	 *            The parent category to set
	 */
	private void setParentCat(ReleaseCategory parentCat) {
		this.parentCat = parentCat;
	}

	/**
	 * Gets the parent category of this ReleaseCategory in {@link String}
	 * representation. Can be an empty {@link String}.
	 * 
	 * @return The parent category as a {@link String}
	 */
	private String getParentCatString() {
		return parentCatString;
	}

	/**
	 * Sts the parent category of this ReleaseCategory in {@link String}
	 * representation. Use an empty {@link String} to unset.
	 * 
	 * @param parentCatString
	 *            The parent category as a {@link String} to set
	 */
	private void setParentCatString(String parentCatString) {
		this.parentCatString = parentCatString;
	}

}