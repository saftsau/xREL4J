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
 * Class represents a release with all its information provided by the xREL API.
 */
public class Release extends ReleaseBase {

	private long time;
	private String groupName;
	private String nukeReason;
	private long sizeNumber;
	private String sizeUnit;
	private String videoType;
	private String audioType;
	private Proof proof;
	private HashMap<String, Boolean> flags = new HashMap<String, Boolean>();

	/**
	 * Constructs a new Release from a {@link JSONObject} returned from the xREL
	 * API.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this Release
	 * @throws JSONException
	 */
	Release(JSONObject jsonObject) throws JSONException {
		super(jsonObject);
		parseJsonObject(jsonObject, null);
	}

	/**
	 * Constructs a new Release from a {@link JSONObject} returned from the xREL
	 * API and an already existing ExtInfo.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this Release
	 * @param extInfo
	 *            The previously existing {@link ExtInfo}
	 * @throws JSONException
	 */
	Release(JSONObject jsonObject, ExtInfo extInfo) throws JSONException {
		super(jsonObject, extInfo);
		parseJsonObject(jsonObject, extInfo);
	}

	/**
	 * Parses a given {@link JSONObject} and saves its values to this object.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this Release
	 * @throws JSONException
	 */
	void parseJsonObject(JSONObject jsonObject, ExtInfo extInfo) throws JSONException {
		setTime(jsonObject.getLong("time"));
		setGroupName(jsonObject.optString("group_name"));
		setNukeReason(jsonObject.optString("nuke_reason"));
		JSONObject size = jsonObject.optJSONObject("size");
		if (size != null) {
			setSizeNumber(size.getLong("number"));
			setSizeUnit(size.getString("unit"));
		} else {
			setSizeNumber(-1);
			setSizeUnit("");
		}
		setVideoType(jsonObject.optString("video_type"));
		setAudioType(jsonObject.optString("audio_type"));
		if (jsonObject.has("proof_url")) {
			setProof(new Proof(jsonObject, this));
		}
		JSONObject flags = jsonObject.optJSONObject("flags");
		if (flags != null) {
			for (String key : flags.keySet()) {
				getFlags().put(key, flags.getBoolean(key));
			}
		}
	}

	/**
	 * Gets the timestamp of when this Release was pred.
	 * 
	 * @return The timestamp
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Sets the timestamp of when this Release was pred.
	 * 
	 * @param time
	 *            The timestamp to set
	 */
	private void setTime(long time) {
		this.time = time;
	}

	/**
	 * Gets the group name of this Release. Can be an empty {@link String} if
	 * not retrieved.
	 * 
	 * @return The group name
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * Sets the group name of this Release. Use an empty {@link String} to
	 * unset.
	 * 
	 * @param groupName
	 *            The group name to set
	 */
	private void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * Gets the nuke reason of this Release. Can be an empty {@link String} if
	 * not retrieved or not nuked.
	 * 
	 * @return The nuke reason
	 */
	public String getNukeReason() {
		return nukeReason;
	}

	/**
	 * Sets the nuke reason of this Release. Use an empty {@link String} to
	 * unset.
	 * 
	 * @param nukeReason
	 *            The nuke reason to set
	 */
	private void setNukeReason(String nukeReason) {
		this.nukeReason = nukeReason;
	}

	/**
	 * Gets the number of the size of this Release. {@code -1} if not retrieved.
	 * 
	 * @return The size number
	 */
	public long getSizeNumber() {
		return sizeNumber;
	}

	/**
	 * Sets the number of the size of this Release. Use {@code -1} to unset.
	 * 
	 * @param sizeNumber
	 *            The size number to set
	 */
	private void setSizeNumber(long sizeNumber) {
		this.sizeNumber = sizeNumber;
	}

	/**
	 * Gets the unit of the size of this Release. Can be an empty {@link String}
	 * if not retrieved.
	 * 
	 * @return The size's unit
	 */
	public String getSizeUnit() {
		return sizeUnit;
	}

	/**
	 * Sets the unit of the size of this Release. Use an empty {@link String} to
	 * unset.
	 * 
	 * @param sizeUnit
	 *            The size's unit to set
	 */
	private void setSizeUnit(String sizeUnit) {
		this.sizeUnit = sizeUnit;
	}

	/**
	 * Gets the video type of this release. Can be an empty {@link String} if
	 * not retrieved.
	 * 
	 * @return The video type
	 */
	public String getVideoType() {
		return videoType;
	}

	/**
	 * Sets the video type of this release. Use an empty {@link String} to
	 * unset.
	 * 
	 * @param videoType
	 *            The video type to set
	 */
	private void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	/**
	 * Gets the audio type of this release. Can be an empty {@link String} if
	 * not retrieved.
	 * 
	 * @return The audio type
	 */
	public String getAudioType() {
		return audioType;
	}

	/**
	 * Sets the audio type of this release. Use an empty {@link String} to
	 * unset.
	 * 
	 * @param audioType
	 *            The audio type to set
	 */
	private void setAudioType(String audioType) {
		this.audioType = audioType;
	}

	/**
	 * Gets the proof of this release. Can be {@code null} if no proof
	 * available.
	 * 
	 * @return The proof
	 */
	public Proof getProof() {
		return proof;
	}

	/**
	 * Sets the proof of this release. Use {@code null} to unset.
	 * 
	 * @param proof
	 *            The proof to set
	 */
	private void setProof(Proof proof) {
		this.proof = proof;
	}

	/**
	 * Gets the flags of this release. Can be an empty HashMap. Possible values
	 * are {@code <top_rls, true>}, {@code <fix_rls, true>},
	 * {@code <read_nfo, true>} and {@code <english, true>}.
	 * 
	 * @return The flags
	 */
	public HashMap<String, Boolean> getFlags() {
		return flags;
	}

}