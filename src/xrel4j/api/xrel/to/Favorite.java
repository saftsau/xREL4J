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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class represents a Favorite list by the currently logged in user.
 */
public class Favorite {

	private long id;
	private String name;
	private boolean public_;
	private boolean notify;
	private boolean autoRead;
	private boolean includeP2p;
	private String description;
	private String passwordHash;
	private long entryCount;
	private long unreadReleases;
	private ArrayList<ExtInfo> entries = new ArrayList<ExtInfo>();

	/**
	 * Constructs a new Favorite from a {@link JSONObject} returned from the
	 * xREL API.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this Favorite
	 * @throws JSONException
	 */
	Favorite(JSONObject jsonObject) throws JSONException {
		parseJsonObject(jsonObject);
	}

	/**
	 * Parses a given {@link JSONObject} and saves its values to this object.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this Favorite
	 * @throws JSONException
	 */
	void parseJsonObject(JSONObject jsonObject) throws JSONException {
		setId(jsonObject.getLong("id"));
		setName(jsonObject.getString("name"));
		setPublic(jsonObject.optBoolean("public"));
		setNotify(jsonObject.optBoolean("notify"));
		setAutoRead(jsonObject.optBoolean("auto_read"));
		setIncludeP2p(jsonObject.optBoolean("include_p2p"));
		setDescription(jsonObject.optString("description"));
		setPasswordHash(jsonObject.optString("password_hash"));
		setEntryCount(jsonObject.getLong("entry_count"));
		setUnreadReleases(jsonObject.getLong("unread_releases"));
	}

	/**
	 * Gets the API ID of the Favorite list.
	 * 
	 * @return The ID
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the API ID of the Favorite list.
	 * 
	 * @param id
	 *            The ID to set
	 */
	private void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the name of this Favorite list.
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this Favorite list.
	 * 
	 * @param name
	 *            The name to set
	 */
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the public status of this Favorite list. {@code true} if public,
	 * {@code false} if private. Default is {@code false}.
	 * 
	 * @return The public status
	 */
	public boolean isPublic() {
		return public_;
	}

	/**
	 * Sets the public status of this Favorite list. {@code true} if public,
	 * {@code false} if private.
	 * 
	 * @param public_
	 *            The public status to set
	 */
	private void setPublic(boolean public_) {
		this.public_ = public_;
	}

	/**
	 * Gets the notify status of this Favorite list. {@code true} if user wants
	 * notifications, {@code false} otherwise. Defaults to {@code false}.
	 * 
	 * @return The notify status
	 */
	public boolean isNotify() {
		return notify;
	}

	/**
	 * Sets the notify status of this Favorite list. {@code true} if user wants
	 * notifications, {@code false} otherwise.
	 * 
	 * @param notify
	 *            The notify status to set
	 */
	private void setNotify(boolean notify) {
		this.notify = notify;
	}

	/**
	 * Gets the mark automatically as read status of this Favorite list.
	 * {@code true} if new releases are marked as read once the user opened
	 * them, {@code false} otherwise. Defaults to {@code false}.
	 * 
	 * @return The automatically read status
	 */
	public boolean isAutoRead() {
		return autoRead;
	}

	/**
	 * Sets the mark automatically as read status of this Favorite list.
	 * {@code true} if new releases are marked as read once the user opened
	 * them, {@code false} otherwise.
	 * 
	 * @param autoRead
	 *            The automatically read status to set
	 */
	private void setAutoRead(boolean autoRead) {
		this.autoRead = autoRead;
	}

	/**
	 * Gets whether the user wants {@link P2pRelease} to be included in this
	 * Favorite list. Defaults to {@code false}.
	 * 
	 * @return The inclusion of {@link P2pRelease} status
	 */
	public boolean isIncludeP2p() {
		return includeP2p;
	}

	/**
	 * Sets whether the user wants {@link P2pRelease} to be included in this
	 * Favorite list.
	 * 
	 * @param includeP2p
	 *            The inclusion of {@link P2pRelease} status to set
	 */
	private void setIncludeP2p(boolean includeP2p) {
		this.includeP2p = includeP2p;
	}

	/**
	 * Gets the description of this Favorite list. Can be an empty
	 * {@link String} if not set.
	 * 
	 * @return The description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of this Favorite list. Use an empty {@link String}
	 * to unset.
	 * 
	 * @param description
	 *            The description to set
	 */
	private void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the password hash of this Favorite list. If the list has a password,
	 * this will be a hash value that has been composed like this:<br>
	 * {@code $password_hash = sha1($list->id . "\r\n" . $list->password)}<br>
	 * So if you want to check whether if a password for a list is correct,
	 * compose this hash and compare it with the hash from this method. For
	 * lists without a password, password_hash will be empty.
	 * 
	 * @return The password hash
	 */
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * Sets the password hash of this Favorite list. The hash value should be
	 * composed like this: <br>
	 * {@code $password_hash = sha1($list->id . "\r\n" .
	 * $list->password)}<br>
	 * Use an empty {@link String} to unset.
	 * 
	 * @param passwordHash
	 *            The password hash to set
	 */
	private void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	/**
	 * Gets the number of entries this Favorite list has.
	 * 
	 * @return The number of entries
	 */
	public long getEntryCount() {
		return entryCount;
	}

	/**
	 * Sets the number of entries this Favorite list has.
	 * 
	 * @param entryCount
	 *            The number of entries to set
	 */
	private void setEntryCount(long entryCount) {
		this.entryCount = entryCount;
	}

	/**
	 * Gets the number of unread releases this Favorite list has.
	 * 
	 * @return The number of unread releases
	 */
	public long getUnreadReleases() {
		return unreadReleases;
	}

	/**
	 * Sets the number of unread releases this Favorite list has.
	 * 
	 * @param unreadReleases
	 *            The number of unread releases to set
	 */
	private void setUnreadReleases(long unreadReleases) {
		this.unreadReleases = unreadReleases;
	}

	/**
	 * Gets the {@link ExtInfo} entries of this Favorite list. Can be empty if
	 * not yet retrieved.
	 * 
	 * @return The entries
	 */
	public ArrayList<ExtInfo> getEntries() {
		return entries;
	}

}