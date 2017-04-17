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

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class represents an ExtInfo, used for all information xREL has about any
 * product (movie, game, ...).
 */
public class ExtInfo {

	private String type;
	private String id;
	private String title;
	private String linkHref;
	private String genre;
	private String altTitle;
	private String coverUrl;
	private HashMap<String, String> releaseDates = new HashMap<String, String>();
	private ArrayList<ExtInfoExternal> extInfoExternalList = new ArrayList<ExtInfoExternal>();
	private double rating;
	private int ownRating;
	private long numRatings;
	private ArrayList<String> uris = new ArrayList<String>();
	private ArrayList<ExtInfoMedia> extInfoMedia = new ArrayList<ExtInfoMedia>();
	private ArrayList<Release> unreadReleases = new ArrayList<Release>();
	private ArrayList<P2pRelease> unreadP2pReleases = new ArrayList<P2pRelease>();

	/**
	 * Constructs a new ExtInfo from a {@link JSONObject} returned from the xREL
	 * API.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this ExtInfo
	 * @throws JSONException
	 */
	ExtInfo(JSONObject jsonObject) throws JSONException {
		parseJsonObject(jsonObject);
	}

	/**
	 * Parses a given {@link JSONObject} and saves its values to this object.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this ExtInfo
	 * @throws JSONException
	 */
	void parseJsonObject(JSONObject jsonObject) throws JSONException {
		setType(jsonObject.getString("type"));
		setId(jsonObject.getString("id"));
		setTitle(jsonObject.getString("title"));
		setLinkHref(jsonObject.getString("link_href"));
		setGenre(jsonObject.optString("genre"));
		setAltTitle(jsonObject.optString("alt_title"));
		setCoverUrl(jsonObject.optString("cover_url"));
		JSONArray releaseDates = jsonObject.optJSONArray("release_dates");
		if (releaseDates != null) {
			getReleaseDates().clear();
			for (int i = 0; i < releaseDates.length(); i++) {
				JSONObject releaseDate = releaseDates.getJSONObject(i);
				String type = releaseDate.getString("type");
				String date = releaseDate.getString("date");
				getReleaseDates().put(type, date);
			}
		}
		JSONArray externals = jsonObject.optJSONArray("externals");
		if (externals != null) {
			getExtInfoExternalList().clear();
			for (int i = 0; i < externals.length(); i++) {
				getExtInfoExternalList().add(new ExtInfoExternal(externals.getJSONObject(i)));
			}
		}
		setRating(jsonObject.optDouble("rating", 0));
		setOwnRating(jsonObject.optInt("own_rating", 0));
		setNumRatings(jsonObject.optLong("num_ratings", 0));
		JSONArray jsonArrayUris = jsonObject.optJSONArray("uris");
		if (jsonArrayUris != null) {
			getUris().clear();
			for (int i = 0; i < jsonArrayUris.length(); i++) {
				getUris().add(jsonArrayUris.getString(i));
			}
		}
		JSONArray jsonArrayUnreadReleases = jsonObject.optJSONArray("releases");
		if (jsonArrayUnreadReleases != null) {
			getUnreadReleases().clear();
			for (int i = 0; i < jsonArrayUnreadReleases.length(); i++) {
				getUnreadReleases().add(new Release(jsonArrayUnreadReleases.getJSONObject(i), this));
			}
		}
		JSONArray jsonArrayUnreadP2pReleases = jsonObject.optJSONArray("p2p_releases");
		if (jsonArrayUnreadP2pReleases != null) {
			getUnreadP2pReleases().clear();
			for (int i = 0; i < jsonArrayUnreadP2pReleases.length(); i++) {
				getUnreadP2pReleases().add(new P2pRelease(jsonArrayUnreadP2pReleases.getJSONObject(i), this));
			}
		}
	}

	/**
	 * Gets the type of this ExtInfo. Possible values are {@code movie},
	 * {@code console}, {@code music}, {@code nfo}, {@code game},
	 * {@code software}, {@code tv} and {@code xxx}.
	 * 
	 * @return The type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type of this ExtInfo. Should be one {@code movie},
	 * {@code console}, {@code music}, {@code nfo}, {@code game},
	 * {@code software}, {@code tv} or {@code xxx}.
	 * 
	 * @param type
	 *            The type to set
	 */
	private void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the API ID of this ExtInfo.
	 * 
	 * @return The ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the API ID of this ExtInfo.
	 * 
	 * @param id
	 *            The ID to set
	 */
	private void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the title of this ExtInfo.
	 * 
	 * @return The title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of this ExtInfo.
	 * 
	 * @param title
	 *            The title to set
	 */
	private void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the link to this ExtInfo.
	 * 
	 * @return The link
	 */
	public String getLinkHref() {
		return linkHref;
	}

	/**
	 * Sets the link to this ExtInfo.
	 * 
	 * @param linkHref
	 *            The link to set
	 */
	private void setLinkHref(String linkHref) {
		this.linkHref = linkHref;
	}

	/**
	 * Gets the rating of this ExtInfo. {@code 0} if not rated yet.
	 * 
	 * @return The rating
	 */
	public double getRating() {
		return rating;
	}

	/**
	 * Sets the rating of this ExtInfo. Use {@code 0} to unset.
	 * 
	 * @param rating
	 *            The rating to set
	 */
	private void setRating(double rating) {
		this.rating = rating;
	}

	/**
	 * Gets the number of ratings of this ExtInfo.
	 * 
	 * @return The number of ratings
	 */
	public long getNumRatings() {
		return numRatings;
	}

	/**
	 * Sets the number of ratings of this ExtInfo.
	 * 
	 * @param numRatings
	 *            The number of ratings to set
	 */
	private void setNumRatings(long numRatings) {
		this.numRatings = numRatings;
	}

	/**
	 * Gets the URIs of this ExtInfo. Can be empty.
	 * 
	 * @return The URIs
	 */
	public ArrayList<String> getUris() {
		return uris;
	}

	/**
	 * Gets the unread {@link Release} of this ExtInfo. Only available if this
	 * ExtInfo is part of a favorite list, otherwise always returns an empty
	 * list.
	 * 
	 * @return The list of unread releases
	 */
	public ArrayList<Release> getUnreadReleases() {
		return unreadReleases;
	}

	/**
	 * Gets the unread {@link P2pRelease} of this ExtInfo. Only available if
	 * this ExtInfo is part of a favorite list, otherwise always returns an
	 * empty list.
	 * 
	 * @return The list of unread P2P releases
	 */
	public ArrayList<P2pRelease> getUnreadP2pReleases() {
		return unreadP2pReleases;
	}

	/**
	 * Gets the {@link ExtInfoMedia} of this ExtInfo. Can be empty if not yet
	 * retrieved or nothing available.
	 * 
	 * @return The list of media
	 */
	public ArrayList<ExtInfoMedia> getExtInfoMedia() {
		return extInfoMedia;
	}

	/**
	 * Gets the genre of this ExtInfo. Can be an empty {@link String} if no
	 * genre specified or not yet retrieved.
	 * 
	 * @return The genre
	 */
	public String getGenre() {
		return genre;
	}

	/**
	 * Sets the genre of this ExtInfo. Use an empty {@link String} to unset.
	 * 
	 * @param genre
	 *            The genre to set
	 */
	private void setGenre(String genre) {
		this.genre = genre;
	}

	/**
	 * Gets the alternative title of this ExtInfo. Can be an empty
	 * {@link String} if no alternative title specified or not yet retrieved.
	 * 
	 * @return The alternative title
	 */
	public String getAltTitle() {
		return altTitle;
	}

	/**
	 * Sets the alternative title of this ExtInfo. Use an empty {@link String}
	 * to unset.
	 * 
	 * @param altTitle
	 *            The alternative title to set
	 */
	private void setAltTitle(String altTitle) {
		this.altTitle = altTitle;
	}

	/**
	 * Gets the link to the cover of this ExtInfo. Can be an empty
	 * {@link String} if no cover specified or not yet retrieved.
	 * 
	 * @return The link to the cover
	 */
	public String getCoverUrl() {
		return coverUrl;
	}

	/**
	 * Gets the link to the cover of this ExtInfo. Use an empty {@link String}
	 * to unset.
	 * 
	 * @param coverUrl
	 *            The link to the cover to set
	 */
	private void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	/**
	 * Gets the release dates of this ExtInfo. Can be an empty HashMap if no
	 * release dates specified or not yet retrieved. Possible values are
	 * {@code de-cine}, {@code de-retail}, {@code de-rental}, {@code de-hd},
	 * {@code de-web}, {@code us-cine}, {@code us-retail}, {@code us-rental},
	 * {@code us-hd}, {@code us-web}, {@code r5}.
	 * 
	 * @return The release dates with their types
	 */
	public HashMap<String, String> getReleaseDates() {
		return releaseDates;
	}

	/**
	 * Gets the external info of this ExtInfo. Can be empty if nothing available
	 * or not yet retrieved.
	 * 
	 * @return The list of external info
	 */
	public ArrayList<ExtInfoExternal> getExtInfoExternalList() {
		return extInfoExternalList;
	}

	/**
	 * Gets the rating of the currently logged in user of this ExtInfo. Returns
	 * 0 if no user is logged in or the user has not rated this ExtInfo yet.
	 * 
	 * @return The current user's rating
	 */
	public int getOwnRating() {
		return ownRating;
	}

	/**
	 * Gets the rating of the currently logged in user of this ExtInfo. Use
	 * {@code 0} to unset.
	 * 
	 * @param ownRating
	 *            The current user's rating to set
	 */
	private void setOwnRating(int ownRating) {
		this.ownRating = ownRating;
	}

}