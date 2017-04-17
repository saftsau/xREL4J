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
 * Class represents a P2P release with all its information provided by the xREL
 * API.
 */
public class P2pRelease extends ReleaseBase {

	private P2pCategory category;
	private String mainLang;
	private long pubTime;
	private long postTime;
	private long sizeMb;
	private P2pGroup group;

	/**
	 * Constructs a new P2pRelease from a {@link JSONObject} returned from the
	 * xREL API.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this P2pRelease
	 * @throws JSONException
	 */
	P2pRelease(JSONObject jsonObject) throws JSONException {
		super(jsonObject);
		parseJsonObject(jsonObject, null);
	}

	/**
	 * Constructs a new P2pRelease from a {@link JSONObject} returned from the
	 * xREL API and an already existing ExtInfo.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this P2pRelease
	 * @param extInfo
	 *            The previously existing {@link ExtInfo}
	 * @throws JSONException
	 */
	P2pRelease(JSONObject jsonObject, ExtInfo extInfo) throws JSONException {
		super(jsonObject, extInfo);
		parseJsonObject(jsonObject, extInfo);
	}

	/**
	 * Parses a given {@link JSONObject} and saves its values to this object.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this P2pRelease
	 * @throws JSONException
	 */
	void parseJsonObject(JSONObject jsonObject, ExtInfo extInfo) throws JSONException {
		JSONObject category = jsonObject.optJSONObject("category");
		if (category != null) {
			setCategory(new P2pCategory(category));
		}
		setMainLang(jsonObject.optString("main_lang"));
		setPubTime(jsonObject.optLong("pub_time", -1));
		setPostTime(jsonObject.optLong("post_time", -1));
		setSizeMb(jsonObject.optLong("size_mb", -1));
		JSONObject group = jsonObject.optJSONObject("group");
		if (jsonObject != null) {
			setGroup(new P2pGroup(group));
		}
	}

	/**
	 * Gets the category of this P2pRelease. Can be {@code null}.
	 * 
	 * @return The category
	 */
	public P2pCategory getCategory() {
		return category;
	}

	/**
	 * Gets the category of this P2pRelease. Use {@code null} to unset.
	 * 
	 * @param category
	 *            The category to set
	 */
	private void setCategory(P2pCategory category) {
		this.category = category;
	}

	/**
	 * Gets the main language of this P2pRelease. Can be an empty
	 * {@link String}.
	 * 
	 * @return The main language
	 */
	public String getMainLang() {
		return mainLang;
	}

	/**
	 * Sets the main language of this P2pRelease. Can be an empty
	 * {@link String}.
	 * 
	 * @param mainLang
	 *            The main language to set
	 */
	private void setMainLang(String mainLang) {
		this.mainLang = mainLang;
	}

	/**
	 * Gets the publication time of this P2pRelease. {@code -1} if not
	 * available.
	 * 
	 * @return The publication time
	 */
	public long getPubTime() {
		return pubTime;
	}

	/**
	 * Sets the publication time of this P2pRelease. Use {@code -1} to unset.
	 * 
	 * @param pubTime
	 *            The publication time to set
	 */
	private void setPubTime(long pubTime) {
		this.pubTime = pubTime;
	}

	/**
	 * Gets the post time of this P2pRelease. {@code -1} if not available.
	 * 
	 * @return The post time
	 */
	public long getPostTime() {
		return postTime;
	}

	/**
	 * Sets the post time of this P2pRelease. Use {@code -1} to unset.
	 * 
	 * @param postTime
	 *            The post time to set
	 */
	private void setPostTime(long postTime) {
		this.postTime = postTime;
	}

	/**
	 * Gets the size of this P2pRelease in MB. {@code -1} if not available.
	 * 
	 * @return The size in MB
	 */
	public long getSizeMb() {
		return sizeMb;
	}

	/**
	 * Sets the size of this P2pRelease in MB. Use {@code -1} to unset.
	 * 
	 * @param sizeMb
	 *            The size in MB to set
	 */
	private void setSizeMb(long sizeMb) {
		this.sizeMb = sizeMb;
	}

	/**
	 * Gets the group of this P2pRelease. Can be {@code null}.
	 * 
	 * @return The group
	 */
	public P2pGroup getGroup() {
		return group;
	}

	/**
	 * Sets the group of this P2pRelease. Use {@code null} to unset.
	 * 
	 * @param group
	 *            The group to set
	 */
	private void setGroup(P2pGroup group) {
		this.group = group;
	}

}