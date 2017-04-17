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
 * Class represents external info an {@link ExtInfo} can contain. Used for
 * adding plots from other websites.
 */
public class ExtInfoExternal {

	private int sourceId;
	private String sourceName;
	private String linkUrl;
	private String plot;

	/**
	 * Constructs a new ExtInfoExternal from a {@link JSONObject} returned from
	 * the xREL API.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this ExtInfoExternal
	 * @throws JSONException
	 */
	ExtInfoExternal(JSONObject jsonObject) throws JSONException {
		parseJsonObject(jsonObject);
	}

	/**
	 * Parses a given {@link JSONObject} and saves its values to this object.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this ExtInfoExternal
	 * @throws JSONException
	 */
	void parseJsonObject(JSONObject jsonObject) throws JSONException {
		JSONObject source = jsonObject.getJSONObject("source");
		setSourceId(source.getInt("id"));
		setSourceName(source.getString("name"));
		setLinkUrl(jsonObject.getString("link_url"));
		setPlot(jsonObject.optString("plot"));
	}

	/**
	 * Gets the API ID of the source of this ExtInfoExternal.
	 * 
	 * @return The source's API ID
	 */
	public int getSourceId() {
		return sourceId;
	}

	/**
	 * Sets the API ID of the source of this ExtInfoExternal.
	 * 
	 * @param sourceId
	 *            The source's API ID to set
	 */
	private void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	/**
	 * Gets the name of the source of this ExtInfoExternal.
	 * 
	 * @return The source's name
	 */
	public String getSourceName() {
		return sourceName;
	}

	/**
	 * Sets the name of the source of this ExtInfoExternal.
	 * 
	 * @param sourceName
	 *            The source's name to set
	 */
	private void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	/**
	 * Gets the link of this ExtInfoExternal. This is the link to the original
	 * source, not a link to an xREL website where this is embedded.
	 * 
	 * @return The link
	 */
	public String getLinkUrl() {
		return linkUrl;
	}

	/**
	 * Sets the link of this ExtInfoExternal. This is the link to the original
	 * source, not a link to an xREL website where this is embedded.
	 * 
	 * @param linkUrl
	 *            The link to set
	 */
	private void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	/**
	 * Gets the plot of this ExtInfoExternal. Can be an empty String if not
	 * specified.
	 * 
	 * @return The plot
	 */
	public String getPlot() {
		return plot;
	}

	/**
	 * Sets the plot of this ExtInfoExternal. Use an empty {@link String} to
	 * unset.
	 * 
	 * @param plot
	 *            The plot to set
	 */
	private void setPlot(String plot) {
		this.plot = plot;
	}
}