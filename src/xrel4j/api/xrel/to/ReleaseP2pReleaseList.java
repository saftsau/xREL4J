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

/**
 * Class represents an object containing two lists of {@link Release} and
 * {@link P2pRelease}. This is used for search operations on the xREL API, which
 * can return both types.
 */
public class ReleaseP2pReleaseList {

	private final ArrayList<Release> releaseList;
	private final ArrayList<P2pRelease> p2pReleaseList;

	/**
	 * Constructs a new {@link ReleaseP2pReleaseList} consisting of a list of
	 * {@link Release} {@link P2pRelease}.
	 * 
	 * @param releaseList
	 *            The list of {@link Release} or {@code null}
	 * @param p2pReleaseList
	 *            The list of {@link P2pRelease} or {@code null}
	 */
	public ReleaseP2pReleaseList(ArrayList<Release> releaseList, ArrayList<P2pRelease> p2pReleaseList) {
		if (releaseList != null) {
			this.releaseList = releaseList;
		} else {
			this.releaseList = new ArrayList<Release>();
		}
		if (p2pReleaseList != null) {
			this.p2pReleaseList = p2pReleaseList;
		} else {
			this.p2pReleaseList = new ArrayList<P2pRelease>();
		}
	}

	/**
	 * Gets the list of {@link Release}. Can be empty.
	 * 
	 * @return The list of {@link Release}
	 */
	public ArrayList<Release> getReleaseList() {
		return releaseList;
	}

	/**
	 * Gets the list of {@link P2pRelease}. Can be empty.
	 * 
	 * @return The list of {@link P2pRelease}
	 */
	public ArrayList<P2pRelease> getP2pReleaseList() {
		return p2pReleaseList;
	}

}