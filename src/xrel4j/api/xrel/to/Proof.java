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
 * Class represents a proof. This is retrieved partially by the xREL API after
 * the user posted one.
 */
public class Proof {

	private String proofUrl;
	private ArrayList<Release> releaseList;

	/**
	 * Constructs a new Proof from a URL and a single release. Returned from the
	 * xREL API.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this P2pGroup
	 * @param releaseList
	 *            The {@link Release} this proof belongs to or {@code null},
	 *            which will result in the creation of an empty list
	 * @throws JSONException
	 */
	Proof(JSONObject jsonObject, Release release) throws JSONException {
		this.releaseList = new ArrayList<Release>();
		if (release != null) {
			getReleaseList().add(release);
		}
		parseJsonObject(jsonObject);
	}

	/**
	 * Constructs a new Proof from a URL and a list of releases. Returned from
	 * the xREL API.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this P2pGroup
	 * @param releaseList
	 *            List of {@link Release} this proof belongs to or {@code null},
	 *            which will result in the creation of an empty list
	 * @throws JSONException
	 */
	Proof(JSONObject jsonObject, ArrayList<Release> releaseList) throws JSONException {
		if (releaseList != null) {
			this.releaseList = releaseList;
		} else {
			this.releaseList = new ArrayList<Release>();
		}
		parseJsonObject(jsonObject);
	}

	/**
	 * Parses a given {@link JSONObject} and saves its values to this object.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this Proof
	 * @throws JSONException
	 */
	void parseJsonObject(JSONObject jsonObject) throws JSONException {
		setProofUrl(jsonObject.getString("proof_url"));
	}

	/**
	 * Gets the link of this proof.
	 * 
	 * @return The link
	 */
	public String getProofUrl() {
		return proofUrl;
	}

	/**
	 * Sets the link of this proof.
	 * 
	 * @param proofUrl
	 *            The link to set
	 */
	private void setProofUrl(String proofUrl) {
		this.proofUrl = proofUrl;
	}

	/**
	 * Gets the list of {@link Release} this Proof belongs to. Can be empty.
	 * 
	 * @return The list of {@link Release}
	 */
	public ArrayList<Release> getReleaseList() {
		return releaseList;
	}

}