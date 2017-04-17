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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Java implementation of the xREL API v2. Method and parameter names are based
 * on the xREL API with "_" replaced by camel case. Order of methods and
 * parameters are based on the xREL API documentation. It is STRONGLY encouraged
 * to read the xREL API documentation!
 * 
 * @see https://www.xrel.to/wiki/1681/API.html
 */
public class Xrel {

	private final String xrelUrl = "https://api.xrel.to/v2/";
	private final String format = ".json";
	private long totalCount = -1;
	private int paginationCurrentPage = -1;
	private int paginationPerPage = -1;
	private int paginationTotalPages = -1;
	private final int paginationPerPageMin = 5;
	private final int paginationPerPageMax = 100;
	private final String responseType = "code";
	private String clientId;
	private String clientSecret;
	private String redirectUri;
	private String state;
	private String[] scope;

	/**
	 * Constructs a new xREL object.
	 * 
	 * @param clientId
	 *            Your consumer key or {@code null}.
	 * @param clientSecret
	 *            Your consumer secret or {@code null}.
	 * @param redirectUri
	 *            URI to redirect to after the authentication or {@code null}.
	 *            Please read the Guide for more details.
	 * @param state
	 *            Any string or {@code null}. You may set this value to any
	 *            value, and it will be returned after the authentication. It
	 *            might also be useful to prevent CSRF attacks.
	 * @param scope
	 *            Needed to access protected methods. Most users must leave this
	 *            at {@code null}. If you do have scope access: you MUST supply
	 *            these while processing the Tokens, even if you only plan to
	 *            use them at a later stage. Rule of thumb: if you have these,
	 *            always add them here.
	 * @see https://www.xrel.to/wiki/6436/api-oauth2.html
	 */
	public Xrel(String clientId, String clientSecret, String redirectUri, String state, String[] scope) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.redirectUri = redirectUri;
		this.state = state;
		this.scope = scope;
	}

	/**
	 * Gets the set consumer key.
	 * 
	 * @return The consumer key
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * Gets the set consumer secret.
	 * 
	 * @return The consumer secret
	 */
	public String getClientSecret() {
		return clientSecret;
	}

	/**
	 * Gets the set redirect URI.
	 * 
	 * @return The redirect URI
	 */
	public String getRedirectUri() {
		return redirectUri;
	}

	/**
	 * Gets the set state used to prevent CSRF attacks.
	 * 
	 * @return The state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Gets the set scope.
	 * 
	 * @return The scope
	 */
	public String[] getScope() {
		return scope;
	}

	/**
	 * Gets the total count of items of the last request issued which returned a
	 * pagination.
	 * 
	 * @return The total count of items
	 */
	public long getTotalCount() {
		return totalCount;
	}

	/**
	 * Gets the current page of the last request issued which returned a
	 * pagination.
	 * 
	 * @return The current page
	 */
	public int getPaginationCurrentPage() {
		return paginationCurrentPage;
	}

	/**
	 * Gets the items per page of the last request issued which returned a
	 * pagination.
	 * 
	 * @return The items per page
	 */
	public int getPaginationPerPage() {
		return paginationPerPage;
	}

	/**
	 * Gets the total pages of the last request issued which returned a
	 * pagination.
	 * 
	 * @return The total pages
	 */
	public int getPaginationTotalPages() {
		return paginationTotalPages;
	}

	/**
	 * Retrieves the pagination elements from a {@link JSONObject} and saves
	 * them.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} to retrieve the information from
	 * @throws JSONException
	 */
	private void handlePagination(JSONObject jsonObject) throws JSONException {
		totalCount = jsonObject.getLong("total_count");
		JSONObject pagination = jsonObject.getJSONObject("pagination");
		paginationCurrentPage = pagination.getInt("current_page");
		paginationPerPage = pagination.getInt("per_page");
		paginationTotalPages = pagination.getInt("total_pages");
	}

	/**
	 * Normalizes the given perPage and page input. perPage is a value between
	 * {@link #paginationPerPageMin} and {@link #paginationPerPageMax} and page
	 * is a value greater zero.
	 * 
	 * @param perPage
	 *            The perPage value to normalize
	 * @param page
	 *            The page value to normalize
	 * @return An array with perPage on [0] and page on [1]
	 */
	private int[] normalizePageValues(int perPage, int page) {
		if (perPage < paginationPerPageMin) {
			perPage = paginationPerPageMin;
		} else if (perPage > paginationPerPageMax) {
			perPage = paginationPerPageMax;
		}
		if (page < 1) {
			page = 1;
		}

		int[] result = new int[2];
		result[0] = perPage;
		result[1] = page;
		return result;
	}

	/**
	 * Gets the maximum number of requests that the consumer is permitted to
	 * make per hour as returned by the last request. -1 if not yet set.
	 * 
	 * @return The X-RateLimit-Limit
	 * @see https://www.xrel.to/wiki/2727/api-rate-limiting.html
	 */
	public int getXRateLimitLimit() {
		return NetworkingHelper.getXRateLimitLimit();
	}

	/**
	 * Gets the number of requests remaining in the current rate limit window as
	 * returned by the last request. -1 if not yet set.
	 * 
	 * @return The X-RateLimit-Remaining
	 * @see https://www.xrel.to/wiki/2727/api-rate-limiting.html
	 */
	public int getXRateLimitRemaining() {
		return NetworkingHelper.getXRateLimitRemaining();
	}

	/**
	 * Gets the time at which the current rate limit window resets in UTC epoch
	 * seconds as returned by the last request. -1 if not yet set.
	 * 
	 * @return The X-RateLimit-Reset
	 * @see https://www.xrel.to/wiki/2727/api-rate-limiting.html
	 */
	public int getXRateLimitReset() {
		return NetworkingHelper.getXRateLimitReset();
	}

	/**
	 * Checks if a specific scope was given at creation time.
	 * 
	 * @param scope
	 *            The scope to check for
	 * @return {@code true} if found, {@code false} otherwise
	 */
	private boolean checkScope(String scope) {
		boolean found = false;
		for (int i = 0; i < getScope().length; i++) {
			if (getScope()[i].equals(scope)) {
				found = true;
			}
		}
		return found;
	}

	/**
	 * Returns information about a single release, specified by the complete
	 * dirname or an API release id.
	 * 
	 * @param idDir
	 *            Dirname or API id of the release
	 * @param useId
	 *            If {@code true} uses the idDir as an API ID for the request,
	 *            if {@code false} uses the idDir as a dirname for the request
	 * @return The corresponding {@link Release}
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/1680/api-release-info.html
	 */
	private Release getReleaseInfo(String idDir, boolean useId) throws JSONException, IOException, XrelException {
		if (idDir == null || idDir.isEmpty()) {
			throw new XrelException("parameters missing");
		}

		Release release;
		if (useId) {
			release = new Release(NetworkingHelper
					.readJsonObjectFromUrlGet(xrelUrl + "release/info" + format + "?id=" + idDir, null));
		} else {
			release = new Release(NetworkingHelper
					.readJsonObjectFromUrlGet(xrelUrl + "release/info" + format + "?dirname=" + idDir, null));
		}
		return release;
	}

	/**
	 * Returns information about a single release, specified by the complete
	 * dirname.
	 * 
	 * @param dir
	 *            Dirname of the release
	 * @return The corresponding {@link Release}
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/1680/api-release-info.html
	 */
	public Release getReleaseInfoDir(String dir) throws JSONException, IOException, XrelException {
		if (dir == null || dir.isEmpty()) {
			throw new XrelException("parameters missing");
		}

		return getReleaseInfo(dir, false);
	}

	/**
	 * Returns information about a single release, specified by the API release
	 * id.
	 * 
	 * @param id
	 *            API id of the release
	 * @return The corresponding {@link Release}
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/1680/api-release-info.html
	 */
	public Release getReleaseInfoId(String id) throws JSONException, IOException, XrelException {
		if (id == null || id.isEmpty()) {
			throw new XrelException("parameters missing");
		}

		return getReleaseInfo(id, true);
	}

	/**
	 * Returns the latest releases. Also allows to browse the archive by month.
	 * Please note that the latest release list with no archive defined does NOT
	 * return a total number of pages. Around 1000 releases are available for
	 * browsing in that mode. per_page continues to work. You can either provide
	 * a {@link Filter}, a {@code Token} or neither, but not both.
	 * 
	 * @param archive
	 *            {@code null} = current releases, YYYY-MM for archive.
	 * @param perPage
	 *            Number of releases per page. Min. 5, max. 100.
	 * @param page
	 *            Page number (1 to N).
	 * @param filter
	 *            Filter (from {@link #getReleaseCategories()}) or {@code null}.
	 * @param token
	 *            If provided uses the overview filter of the respective user
	 * @return The list of {@link Release} matching the criteria
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/2994/api-release-latest.html
	 */
	public ArrayList<Release> getReleaseLatest(String archive, int perPage, int page, Filter filter, Token token)
			throws JSONException, IOException, XrelException {
		int[] normalizedPageValues = normalizePageValues(perPage, page);

		String url = xrelUrl + "release/latest" + format + "?per_page=" + normalizedPageValues[0] + "&page="
				+ normalizedPageValues[1];
		if (archive != null && !archive.isEmpty()) {
			url += "&archive=" + archive;
		}
		if (filter != null && token == null) {
			url += "&filter=" + filter.getId();
		}
		if (filter == null && token != null) {
			url += "&filter=overview";
		}

		JSONObject jsonObject;
		if (token == null) {
			jsonObject = NetworkingHelper.readJsonObjectFromUrlGet(url, null);
		} else {
			jsonObject = NetworkingHelper.readJsonObjectFromUrlGet(url, token);
		}

		handlePagination(jsonObject);
		JSONArray list = jsonObject.getJSONArray("list");
		ArrayList<Release> releaseList = new ArrayList<Release>();
		for (int i = 0; i < list.length(); i++) {
			releaseList.add(new Release(list.getJSONObject(i)));
		}
		return releaseList;
	}

	/**
	 * Returns a list of available release categories. You can use the category
	 * name in
	 * {@link #getReleaseBrowseCategory(ReleaseCategory, String, int, long)}.
	 * You should avoid calling this method repeatedly and cache its result for
	 * at least 24 hours (where possible).
	 * 
	 * @return The list of {@link ReleaseCategory}
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/6318/api-release-categories.html
	 */
	public ArrayList<ReleaseCategory> getReleaseCategories() throws JSONException, IOException, XrelException {
		JSONArray jsonArray = NetworkingHelper.readJsonArrayFromUrlGet(xrelUrl + "release/categories" + format, null);
		ArrayList<ReleaseCategory> categoryList = new ArrayList<ReleaseCategory>();
		HashMap<String, ReleaseCategory> hashMap = new HashMap<String, ReleaseCategory>();
		for (int i = 0; i < jsonArray.length(); i++) {
			ReleaseCategory category = new ReleaseCategory(jsonArray.getJSONObject(i));
			categoryList.add(category);
			hashMap.put(category.getName(), category);
		}
		for (int i = 0; i < categoryList.size(); i++) {
			categoryList.get(i).setParentCategories(hashMap);
		}
		return categoryList;
	}

	/**
	 * Returns scene releases from the given category.
	 * 
	 * @param category
	 *            Category from {@link #getReleaseCategories()}
	 * @param extInfoType
	 *            Use one of:
	 *            {@code movie}|{@code tv}|{@code game}|{@code console}|{@code software}|{@code xxx}
	 *            - or {@code null} to browse releases of all types
	 * @param perPage
	 *            Number of releases per page. Min. 5, max. 100.
	 * @param page
	 *            Page number (1 to N).
	 * @return The list of {@link Release} matching the criteria
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/3751/api-release-browse-category.html
	 */
	public ArrayList<Release> getReleaseBrowseCategory(ReleaseCategory category, String extInfoType, int perPage,
			int page) throws JSONException, IOException, XrelException {
		if (category == null) {
			throw new XrelException("parameters missing");
		}

		int[] normalizedPageValues = normalizePageValues(perPage, page);

		String url = xrelUrl + "release/browse_category" + format + "?category_name=" + category.getName();
		if (extInfoType != null && !extInfoType.isEmpty()) {
			url += "&ext_info_type=" + extInfoType;
		}
		url += "&per_page=" + normalizedPageValues[0] + "&page=" + normalizedPageValues[1];
		JSONObject jsonObject = NetworkingHelper.readJsonObjectFromUrlGet(url, null);
		handlePagination(jsonObject);
		JSONArray list = jsonObject.getJSONArray("list");
		ArrayList<Release> releaseList = new ArrayList<Release>();
		for (int i = 0; i < list.length(); i++) {
			releaseList.add(new Release(list.getJSONObject(i)));
		}
		return releaseList;
	}

	/**
	 * Returns all releases associated with a given Ext Info.
	 * 
	 * @param extInfo
	 *            Ext info.
	 * @param perPage
	 *            Number of releases per page. Min. 5, max. 100.
	 * @param page
	 *            Page number (1 to N).
	 * @return The list of {@link Release} matching the criteria
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 */
	public ArrayList<Release> getReleaseExtInfo(ExtInfo extInfo, int perPage, int page)
			throws JSONException, IOException, XrelException {
		if (extInfo == null) {
			throw new XrelException("parameters missing");
		}

		int[] normalizedPageValues = normalizePageValues(perPage, page);

		String url = xrelUrl + "release/ext_info" + format + "?id=" + extInfo.getId() + "&per_page="
				+ normalizedPageValues[0] + "&page=" + normalizedPageValues[1];
		JSONObject jsonObject = NetworkingHelper.readJsonObjectFromUrlGet(url, null);
		handlePagination(jsonObject);
		JSONArray list = jsonObject.getJSONArray("list");
		ArrayList<Release> releaseList = new ArrayList<Release>();
		for (int i = 0; i < list.length(); i++) {
			releaseList.add(new Release(list.getJSONObject(i)));
		}
		return releaseList;
	}

	/**
	 * Returns a list of public, predefined release filters. You can use the
	 * filter ID in {@code #getReleaseLatest(String, int, int, Filter)}. You
	 * should avoid calling this method repeatedly and cache its result for at
	 * least 24 hours (where possible).
	 * 
	 * @return The list of {@link Filter}
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/2996/api-release-filters.html
	 */
	public ArrayList<Filter> getReleaseFilters() throws JSONException, IOException, XrelException {
		JSONArray jsonArray = NetworkingHelper.readJsonArrayFromUrlGet(xrelUrl + "release/filters" + format, null);
		ArrayList<Filter> filterList = new ArrayList<Filter>();
		for (int i = 0; i < jsonArray.length(); i++) {
			filterList.add(new Filter(jsonArray.getJSONObject(i)));
		}
		return filterList;
	}

	/**
	 * Adds a proof picture to a given API release id. This method requires the
	 * addproof scope. Please read the rules before posting proofs.
	 * 
	 * @param releaseList
	 *            The list of releases this proof should be added to.
	 * @param image
	 *            Base64 encoded image
	 * @param token
	 *            The {@link Token} with all needed info
	 * @return The {@link Proof} with the new URL.
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/6444/api-release-addproof.html
	 */
	public Proof postReleaseAddProof(ArrayList<Release> releaseList, String image, Token token)
			throws JSONException, IOException, XrelException {
		if (releaseList == null || releaseList.isEmpty() || image == null || image.isEmpty() || token == null) {
			throw new XrelException("parameters missing");
		}
		if (getScope() == null || !checkScope("addproof")) {
			throw new XrelException("addproof scope not provided");
		}

		ArrayList<String> keyList = new ArrayList<String>();
		ArrayList<String> valueList = new ArrayList<String>();
		if (releaseList.size() == 1) {
			keyList.add("id");
			valueList.add(releaseList.get(0).getId());
		} else {
			for (int i = 0; i < releaseList.size(); i++) {
				keyList.add("id[]");
				valueList.add(releaseList.get(i).getId());
			}
		}
		keyList.add("image");
		valueList.add(image);

		String url = xrelUrl + "release/addproof" + format;
		return new Proof(NetworkingHelper.readJsonObjectFromUrlPost(url, token, keyList, valueList), releaseList);
	}

	/**
	 * Browse P2P/non-scene releases. Please note that the latest release list
	 * does NOT return a total number of pages. Around 1000 releases are
	 * available for browsing in that mode. per_page continues to work.
	 * 
	 * @param perPage
	 *            Number of releases per page. Min. 5, max. 100.
	 * @param page
	 *            Page number (1 to N).
	 * @param p2pCategory
	 *            P2P category ID from {@link #getP2pCategories()} or
	 *            {@code null}
	 * @param p2pGroup
	 *            P2P release group or {@code null}
	 * @param extInfo
	 *            Ext Info ID or {@code null}
	 * @return The list of {@link P2pRelease} matching the criteria
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/3699/api-p2p-releases.html
	 */
	public ArrayList<P2pRelease> getP2pReleases(int perPage, int page, P2pCategory p2pCategory, P2pGroup p2pGroup,
			ExtInfo extInfo) throws JSONException, IOException, XrelException {
		int[] normalizedPageValues = normalizePageValues(perPage, page);

		String url = xrelUrl + "p2p/releases" + format + "?per_page=" + normalizedPageValues[0] + "&page="
				+ normalizedPageValues[1];
		if (p2pCategory != null) {
			url += "&category_id=" + p2pCategory.getId();
		}
		if (p2pGroup != null) {
			url += "&group_id=" + p2pGroup.getId();
		}
		if (extInfo != null) {
			url += "&ext_info_id=" + extInfo.getId();
		}

		JSONObject jsonObject = NetworkingHelper.readJsonObjectFromUrlGet(url, null);
		handlePagination(jsonObject);
		JSONArray list = jsonObject.getJSONArray("list");
		ArrayList<P2pRelease> p2pList = new ArrayList<P2pRelease>();
		for (int i = 0; i < list.length(); i++) {
			p2pList.add(new P2pRelease(list.getJSONObject(i)));
		}
		return p2pList;
	}

	/**
	 * Returns a list of available P2P release categories and their IDs. You can
	 * use the category in
	 * {@link #getP2pReleases(int, int, P2pCategory, P2pGroup, ExtInfo)}.
	 * 
	 * @return The list of {@link P2pCategory}
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/3698/api-p2p-categories.html
	 */
	public ArrayList<P2pCategory> getP2pCategories() throws JSONException, IOException, XrelException {
		JSONArray jsonArray = NetworkingHelper.readJsonArrayFromUrlGet(xrelUrl + "p2p/categories" + format, null);
		ArrayList<P2pCategory> categoryList = new ArrayList<P2pCategory>();
		for (int i = 0; i < jsonArray.length(); i++) {
			P2pCategory category = new P2pCategory(jsonArray.getJSONObject(i));
			categoryList.add(category);
		}
		return categoryList;
	}

	/**
	 * Returns information about a single P2P/non-scene release, specified by
	 * the complete dirname or an API P2P release id.
	 * 
	 * @param idDir
	 *            Dirname or API id of the release
	 * @param useId
	 *            If {@code true} uses the idDir as an API ID for the request,
	 *            if {@code false} uses the idDir as a dirname for the request
	 * @return The {@link P2pRelease}
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/3697/api-p2p-rls-info.html
	 */
	private P2pRelease getP2pRlsInfo(String idDir, boolean useId) throws JSONException, IOException, XrelException {
		if (idDir == null || idDir.isEmpty()) {
			throw new XrelException("parameters missing");
		}

		P2pRelease p2pRelease;
		if (useId) {
			p2pRelease = new P2pRelease(NetworkingHelper
					.readJsonObjectFromUrlGet(xrelUrl + "p2p/rls_info" + format + "?id=" + idDir, null));
		} else {
			p2pRelease = new P2pRelease(NetworkingHelper
					.readJsonObjectFromUrlGet(xrelUrl + "p2p/rls_info" + format + "?dirname=" + idDir, null));
		}
		return p2pRelease;
	}

	/**
	 * Returns information about a single P2P/non-scene release, specified by
	 * the complete dirname.
	 * 
	 * @param dir
	 *            The dirname of the P2P release
	 * @return The {@link P2pRelease}
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/3697/api-p2p-rls-info.html
	 */
	public P2pRelease getP2pRlsInfoDir(String dir) throws JSONException, IOException, XrelException {
		if (dir == null || dir.isEmpty()) {
			throw new XrelException("parameters missing");
		}

		return getP2pRlsInfo(dir, false);
	}

	/**
	 * Returns information about a single P2P/non-scene release, specified by
	 * the API P2P release id.
	 * 
	 * @param id
	 *            The API P2P release id
	 * @return The {@link P2pRelease}
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/3697/api-p2p-rls-info.html
	 */
	public P2pRelease getP2pRlsInfoId(String id) throws JSONException, IOException, XrelException {
		if (id == null || id.isEmpty()) {
			throw new XrelException("parameters missing");
		}

		return getP2pRlsInfo(id, true);
	}

	/**
	 * Returns an image of a NFO file for a given API release.
	 * 
	 * @param release
	 *            The {@link Release} you want the NFO of
	 * @param token
	 *            The {@link Token} with all needed info
	 * @return The NFO as a {@link BufferedImage}
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/6438/api-nfo-release.html
	 */
	public BufferedImage getNfoRelease(Release release, Token token) throws JSONException, IOException, XrelException {
		if (release == null || token == null) {
			throw new XrelException("parameters missing");
		}
		if (getScope() == null || !checkScope("viewnfo")) {
			throw new XrelException("viewnfo scope not provided");
		}

		return NetworkingHelper.readBufferedImageFromUrlGet(xrelUrl + "nfo/release" + format + "?id=" + release.getId(),
				token);
	}

	/**
	 * Returns an image of a NFO file for a given API P2P release.
	 * 
	 * @param p2pRelease
	 *            The {@link P2pRelease} you want the NFO of
	 * @param token
	 *            The {@link Token} with all needed info
	 * @return The NFO as a {@link BufferedImage}
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/6437/api-nfo-p2p-rls.html
	 */
	public BufferedImage getNfoP2pRls(P2pRelease p2pRelease, Token token)
			throws JSONException, IOException, XrelException {
		if (p2pRelease == null || token == null) {
			throw new XrelException("parameters missing");
		}
		if (getScope() == null || !checkScope("viewnfo")) {
			throw new XrelException("viewnfo scope not provided");
		}

		return NetworkingHelper
				.readBufferedImageFromUrlGet(xrelUrl + "nfo/p2p_rls" + format + "?id=" + p2pRelease.getId(), token);
	}

	/**
	 * Returns a list upcoming movies and their releases.
	 * 
	 * @param country
	 *            {@code de} for upcoming movies in germany, {@code us} for
	 *            upcoming movies in the US/international.
	 * @return The list of {@link ExtInfo}
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/1827/api-calendar-upcoming.html
	 */
	public ArrayList<ExtInfo> getCalendarUpcoming(String country) throws JSONException, IOException, XrelException {
		if (country == null || (!country.equals("de") && !country.equals("us"))) {
			throw new XrelException("parameters missing");
		}

		JSONArray jsonArray = NetworkingHelper.readJsonArrayFromUrlGet(xrelUrl + "calendar/upcoming" + format, null);
		ArrayList<ExtInfo> upcomingList = new ArrayList<ExtInfo>();
		for (int i = 0; i < jsonArray.length(); i++) {
			ExtInfo extInfo = new ExtInfo(jsonArray.getJSONObject(i));
			upcomingList.add(extInfo);
		}
		return upcomingList;
	}

	/**
	 * Returns information about an Ext Info.
	 * 
	 * @param extInfo
	 *            The {@link ExtInfo} you want more info about.
	 * @param token
	 *            Your {@link Token} or {@code null}. If supplied you will also
	 *            get {@code own_rating} from this {@link ExtInfo}.
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/2725/api-ext-info-info.html
	 */
	public void getExtInfoInfo(ExtInfo extInfo, Token token) throws JSONException, IOException, XrelException {
		if (extInfo == null) {
			throw new XrelException("parameters missing");
		}

		JSONObject jsonObject = NetworkingHelper
				.readJsonObjectFromUrlGet(xrelUrl + "ext_info/info" + format + "?id=" + extInfo.getId(), token);
		extInfo.parseJsonObject(jsonObject);
	}

	/**
	 * Returns media associated with an Ext Info.
	 * 
	 * @param extInfo
	 *            The ExtInfo which media you want to retrieve. The found
	 *            ExtInfoMedia will also be added to the given ExtInfo.
	 * @return List of {@link ExtInfoMedia}
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/6314/api-ext-info-media.html
	 */
	public ArrayList<ExtInfoMedia> getExtInfoMedia(ExtInfo extInfo) throws JSONException, IOException, XrelException {
		if (extInfo == null) {
			throw new XrelException("parameters missing");
		}

		JSONArray jsonArray = NetworkingHelper
				.readJsonArrayFromUrlGet(xrelUrl + "ext_info/media" + format + "?id=" + extInfo.getId(), null);
		ArrayList<ExtInfoMedia> extInfoMediaList = new ArrayList<ExtInfoMedia>();
		for (int i = 0; i < jsonArray.length(); i++) {
			ExtInfoMedia extInfoMedia = new ExtInfoMedia(jsonArray.getJSONObject(i));
			extInfoMediaList.add(extInfoMedia);
			extInfo.getExtInfoMedia().add(extInfoMedia);
		}
		return extInfoMediaList;
	}

	/**
	 * Rate an Ext Info. Also updates the given {@link ExtInfo} object.
	 * 
	 * @param extInfo
	 *            The {@link ExtInfo} to rate.
	 * @param rating
	 *            Rating between 1 (bad) to 10 (good). You may only vote once,
	 *            and may not change your vote. Check the ownRating property
	 *            from the response to get the rating as displayed on the
	 *            website.
	 * @param token
	 *            The {@link Token} with all needed info.
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/6315/api-ext-info-rate.html
	 */
	public void postExtInfoRate(ExtInfo extInfo, int rating, Token token)
			throws JSONException, IOException, XrelException {
		if (extInfo == null || token == null) {
			throw new XrelException("parameters missing");
		}

		ArrayList<String> keyList = new ArrayList<String>();
		ArrayList<String> valueList = new ArrayList<String>();
		keyList.add("id");
		valueList.add(extInfo.getId());
		if (rating > 0 && rating < 11) {
			keyList.add("rating");
			valueList.add(String.valueOf(rating));
		} else {
			throw new XrelException("rating must be in the range of 1 - 10");
		}

		extInfo.parseJsonObject(NetworkingHelper.readJsonObjectFromUrlPost(xrelUrl + "ext_info/rate" + format, token,
				keyList, valueList));
	}

	/**
	 * Sanitizes a search term for use in the xREL API. This basically converts
	 * any "+" sign in a term to "%2b" and then converts spaces to "+".
	 * 
	 * @param q
	 *            Search term
	 * @return sanitized search term
	 */
	private String sanitizeSearchTerm(String q) {
		return q.trim().replaceAll("\\+", "%2b").replaceAll(" +", "+");
	}

	/**
	 * Searches for Scene and P2P releases. For all calls to search methods,
	 * additional rate limiting applies. Currently this limit is set at 2 calls
	 * per 10 seconds. Please keep track of that limit yourself.
	 * 
	 * @param q
	 *            Search keyword.
	 * @param scene
	 *            If {@code true}, Scene releases will be included in the search
	 *            results.
	 * @param p2p
	 *            If {@code true}, P2P releases will be included in the search
	 *            results.
	 * @param limit
	 *            Number of returned search results.
	 * @return A pair object containing the lists of {@link Release} and
	 *         {@link P2pRelease}
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/6320/api-search-releases.html
	 */
	public ReleaseP2pReleaseList getSearchReleases(String q, boolean scene, boolean p2p, int limit)
			throws JSONException, IOException, XrelException {
		if (q == null || q.isEmpty() || ((!p2p) && (!scene))) {
			throw new XrelException("parameters missing");
		}

		q = sanitizeSearchTerm(q);
		String url = xrelUrl + "search/releases" + format + "?q=" + q;
		if (scene) {
			url += "&scene=true";
		} else {
			url += "&scene=false";
		}
		if (p2p) {
			url += "&p2p=true";
		} else {
			url += "&p2p=false";
		}

		if (limit < 1) {
			limit = 1;
		}
		url += "&limit=" + limit;

		JSONObject jsonObject = NetworkingHelper.readJsonObjectFromUrlGet(url, null);

		ArrayList<Release> releaseList = null;
		ArrayList<P2pRelease> p2pReleaseList = null;
		if (scene) {
			releaseList = new ArrayList<Release>();
			JSONArray jsonArray = jsonObject.getJSONArray("results");
			for (int i = 0; i < jsonArray.length(); i++) {
				Release release = new Release(jsonArray.getJSONObject(i));
				releaseList.add(release);
			}
		}
		if (p2p) {
			p2pReleaseList = new ArrayList<P2pRelease>();
			JSONArray jsonArray = jsonObject.getJSONArray("p2p_results");
			for (int i = 0; i < jsonArray.length(); i++) {
				P2pRelease p2pRelease = new P2pRelease(jsonArray.getJSONObject(i));
				p2pReleaseList.add(p2pRelease);
			}
		}

		return new ReleaseP2pReleaseList(releaseList, p2pReleaseList);
	}

	/**
	 * Searches for Ext Infos. For all calls to search methods, additional rate
	 * limiting applies. Currently this limit is set at 2 calls per 10 seconds.
	 * Please keep track of that limit yourself.
	 * 
	 * @param q
	 *            Search keyword.
	 * @param type
	 *            One of:
	 *            {@code movie}|{@code tv}|{@code game}|{@code console}|{@code software}|{@code xxx}
	 *            - or {@code null} to browse releases of all types
	 * @param limit
	 *            Number of returned search results.
	 * @return List of {@link ExtInfoMedia}
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/6319/api-search-ext-info.html
	 */
	public ArrayList<ExtInfo> getSearchExtInfo(String q, String type, int limit)
			throws JSONException, IOException, XrelException {
		if (q == null || q.isEmpty()) {
			throw new XrelException("parameters missing");
		}

		q = sanitizeSearchTerm(q);
		String url = xrelUrl + "search/ext_info" + format + "?q=" + q;
		if (type != null && !type.isEmpty()) {
			url += "&type=" + type;
		}

		if (limit < 1) {
			limit = 1;
		}
		url += "&limit=" + limit;

		JSONObject jsonObject = NetworkingHelper.readJsonObjectFromUrlGet(url, null);

		ArrayList<ExtInfo> extInfoList = new ArrayList<ExtInfo>();
		JSONArray jsonArray = jsonObject.getJSONArray("results");
		for (int i = 0; i < jsonArray.length(); i++) {
			ExtInfo extInfo = new ExtInfo(jsonArray.getJSONObject(i));
			extInfoList.add(extInfo);
		}

		return extInfoList;
	}

	/**
	 * Returns a list of all the current user's favorite lists.
	 * 
	 * @param token
	 *            The {@link Token} with all needed info.
	 * @return A list of all {@link Favorite}.
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/1754/api-favs-lists.html
	 */
	public ArrayList<Favorite> getFavsLists(Token token) throws MalformedURLException, IOException, XrelException {
		if (token == null) {
			throw new XrelException("parameters missing");
		}

		JSONArray jsonArray = NetworkingHelper.readJsonArrayFromUrlGet(xrelUrl + "favs/lists" + format, token);

		ArrayList<Favorite> favoriteList = new ArrayList<Favorite>();
		for (int i = 0; i < jsonArray.length(); i++) {
			favoriteList.add(new Favorite(jsonArray.getJSONObject(i)));
		}

		return favoriteList;
	}

	/**
	 * Returns entries of a favorite list. Also adds the retrieved entries to
	 * the {@link Favorite} object you provided.
	 * 
	 * @param favorite
	 *            The favorite list, as obtained through
	 *            {@link #getFavsLists(Token)}.
	 * @param getReleases
	 *            If {@code true}, a list of unread(!) releases will be returned
	 *            with each ext_info entry.
	 * @param token
	 *            The {@link Token} with all needed info.
	 * @return List of {@link ExtInfo} from the favorite list.
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/1823/api-favs-list-entries.html
	 */
	public ArrayList<ExtInfo> getFavsListEntries(Favorite favorite, boolean getReleases, Token token)
			throws MalformedURLException, IOException, XrelException {
		if (favorite == null || token == null) {
			throw new XrelException("parameters missing");
		}

		String url = xrelUrl + "favs/list_entries" + format + "?id=" + favorite.getId() + "&get_releases=";
		if (getReleases) {
			url += "true";
		} else {
			url += "false";
		}
		JSONArray jsonArray = NetworkingHelper.readJsonArrayFromUrlGet(url, token);

		favorite.getEntries().clear();
		for (int i = 0; i < jsonArray.length(); i++) {
			favorite.getEntries().add(new ExtInfo(jsonArray.getJSONObject(i)));
		}

		return favorite.getEntries();
	}

	/**
	 * Add or removes an Ext Info to / from a favorite list.
	 * 
	 * @param favorite
	 *            The favorite list, as obtained through
	 *            {@link #getFavsLists(Token)}.
	 * @param extInfo
	 *            The {@link ExtInfo} to add or remove.
	 * @param token
	 *            The {@link Token} with all needed info.
	 * @param delete
	 *            {@code true} if {@link ExtInfo} should be removed or
	 *            {@code false} if {@link} should be added.
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/6316/api-favs-list-addentry.html
	 */
	private void postFavsListAddDelEntry(Favorite favorite, ExtInfo extInfo, Token token, boolean delete)
			throws MalformedURLException, IOException, XrelException {
		if (favorite == null || extInfo == null || token == null) {
			throw new XrelException("parameters missing");
		}

		ArrayList<String> keyList = new ArrayList<String>();
		ArrayList<String> valueList = new ArrayList<String>();
		keyList.add("id");
		valueList.add(String.valueOf(favorite.getId()));
		keyList.add("ext_info_id");
		valueList.add(String.valueOf(extInfo.getId()));

		String url = xrelUrl;
		if (delete) {
			url += "favs/list_delentry" + format;
		} else {
			url += "favs/list_addentry" + format;
		}

		JSONObject jsonObject = NetworkingHelper.readJsonObjectFromUrlPost(url, token, keyList, valueList)
				.getJSONObject("fav_list");
		favorite.parseJsonObject(jsonObject);
	}

	/**
	 * Add an Ext Info to a favorite list.
	 * 
	 * @param favorite
	 *            The favorite list, as obtained through
	 *            {@link #getFavsLists(Token)}.
	 * @param extInfo
	 *            The {@link ExtInfo} to add.
	 * @param token
	 *            The {@link Token} with all needed info.
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/6316/api-favs-list-addentry.html
	 */
	public void postFavsListAddEntry(Favorite favorite, ExtInfo extInfo, Token token)
			throws MalformedURLException, IOException, XrelException {
		if (favorite == null || extInfo == null || token == null) {
			throw new XrelException("parameters missing");
		}

		postFavsListAddDelEntry(favorite, extInfo, token, false);
	}

	/**
	 * Removes an Ext Info from a favorite list.
	 * 
	 * @param favorite
	 *            The favorite list, as obtained through
	 *            {@link #getFavsLists(Token)}.
	 * @param extInfo
	 *            The {@link ExtInfo} to remove.
	 * @param token
	 *            The {@link Token} with all needed info.
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/6317/api-favs-list-delentry.html
	 */
	public void postFavsListDelEntry(Favorite favorite, ExtInfo extInfo, Token token)
			throws MalformedURLException, IOException, XrelException {
		if (favorite == null || extInfo == null || token == null) {
			throw new XrelException("parameters missing");
		}

		postFavsListAddDelEntry(favorite, extInfo, token, true);
	}

	/**
	 * Mark a {@link Release} or {@link P2pRelease} on a favorite list as read.
	 * 
	 * @param favorite
	 *            The favorite list, as obtained through
	 *            {@link #getFavsLists(Token)}.
	 * @param release
	 *            The {@link Release} to be marked as read or {@code null}.
	 * @param p2pRelease
	 *            The {@link P2pRelease} to be marked as read or {@code null}.
	 * @param token
	 *            The {@link Token} with all needed info.
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/6344/api-favs-list-markread.html
	 */
	private void postFavsListMarkRead(Favorite favorite, Release release, P2pRelease p2pRelease, Token token)
			throws MalformedURLException, IOException, XrelException {
		if (favorite == null || (release == null && p2pRelease == null) || token == null) {
			throw new XrelException("parameters missing");
		}

		String releaseId;
		String type;
		if (release != null) {
			releaseId = release.getId();
			type = "release";
		} else {
			releaseId = p2pRelease.getId();
			type = "p2p_rls";
		}
		ArrayList<String> keyList = new ArrayList<String>();
		ArrayList<String> valueList = new ArrayList<String>();
		keyList.add("id");
		valueList.add(String.valueOf(favorite.getId()));
		keyList.add("release_id");
		valueList.add(String.valueOf(releaseId));
		keyList.add("type");
		valueList.add(type);

		JSONObject jsonObject = NetworkingHelper
				.readJsonObjectFromUrlPost(xrelUrl + "favs/list_markread" + format, token, keyList, valueList)
				.getJSONObject("fav_list");
		favorite.parseJsonObject(jsonObject);
	}

	/**
	 * Mark a {@link Release} on a favorite list as read.
	 * 
	 * @param favorite
	 *            The favorite list, as obtained through
	 *            {@link #getFavsLists(Token)}.
	 * @param release
	 *            The {@link Release} to be marked as read.
	 * @param token
	 *            The {@link Token} with all needed info.
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/6344/api-favs-list-markread.html
	 */
	public void postFavsListMarkRead(Favorite favorite, Release release, Token token)
			throws MalformedURLException, IOException, XrelException {
		if (favorite == null || release == null || token == null) {
			throw new XrelException("parameters missing");
		}

		postFavsListMarkRead(favorite, release, null, token);
	}

	/**
	 * Mark a {@link P2pRelease} on a favorite list as read.
	 * 
	 * @param favorite
	 *            The favorite list, as obtained through
	 *            {@link #getFavsLists(Token)}.
	 * @param p2pRelease
	 *            The {@link P2pRelease} to be marked as read.
	 * @param token
	 *            The {@link Token} with all needed info.
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/6344/api-favs-list-markread.html
	 */
	public void postFavsListMarkRead(Favorite favorite, P2pRelease p2pRelease, Token token)
			throws MalformedURLException, IOException, XrelException {
		if (favorite == null || p2pRelease == null || token == null) {
			throw new XrelException("parameters missing");
		}

		postFavsListMarkRead(favorite, null, p2pRelease, token);
	}

	/**
	 * Returns comments for a given {@link Release} or {@link P2pRelease}.
	 * 
	 * @param release
	 *            The corresponding {@link Release} or {@code null}.
	 * @param p2pRelease
	 *            The corresponding {@link P2pRelease} or {@code null}.
	 * @param perPage
	 *            Number of releases per page. Min. 5, max. 100.
	 * @param page
	 *            Page number (1 to N).
	 * @return The list of {@link Comment}.
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/6313/api-comments-get.html
	 */
	private ArrayList<Comment> getCommentsGet(Release release, P2pRelease p2pRelease, int perPage, int page)
			throws JSONException, IOException, XrelException {
		if (release == null && p2pRelease == null) {
			throw new XrelException("parameters missing");
		}

		int[] normalizedPageValues = normalizePageValues(perPage, page);

		JSONObject jsonObject;
		if (release != null) {
			jsonObject = NetworkingHelper.readJsonObjectFromUrlGet(
					xrelUrl + "comments/get" + format + "?id=" + release.getId() + "&type=release&per_page="
							+ normalizedPageValues[0] + "&page=" + normalizedPageValues[1],
					null);
		} else {
			jsonObject = NetworkingHelper.readJsonObjectFromUrlGet(xrelUrl + "comments/get" + format + "?id="
					+ p2pRelease.getId() + "&type=p2p_rls&per_page=" + perPage + "&page=" + page, null);
		}
		handlePagination(jsonObject);
		JSONArray list = jsonObject.getJSONArray("list");
		ArrayList<Comment> commentList = new ArrayList<Comment>();
		for (int i = 0; i < list.length(); i++) {
			commentList.add(new Comment(list.getJSONObject(i)));
		}
		return commentList;
	}

	/**
	 * Returns comments for a given {@link Release}.
	 * 
	 * @param release
	 *            The corresponding {@link Release}.
	 * @param perPage
	 *            Number of releases per page. Min. 5, max. 100.
	 * @param page
	 *            Page number (1 to N).
	 * @return The list of {@link Comment}.
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/6313/api-comments-get.html
	 */
	public ArrayList<Comment> getCommentsGet(Release release, int perPage, int page)
			throws JSONException, IOException, XrelException {
		if (release == null) {
			throw new XrelException("parameters missing");
		}

		return getCommentsGet(release, null, perPage, page);
	}

	/**
	 * Returns comments for a given {@link P2pRelease}.
	 * 
	 * @param p2pRelease
	 *            The corresponding {@link P2pRelease}.
	 * @param perPage
	 *            Number of releases per page. Min. 5, max. 100.
	 * @param page
	 *            Page number (1 to N).
	 * @return The list of {@link Comment}.
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/6313/api-comments-get.html
	 */
	public ArrayList<Comment> getCommentsGet(P2pRelease p2pRelease, int perPage, int page)
			throws JSONException, IOException, XrelException {
		if (p2pRelease == null) {
			throw new XrelException("parameters missing");
		}

		return getCommentsGet(null, p2pRelease, perPage, page);
	}

	/**
	 * Add a comment to a given {@link Release} or {@link P2pRelease}. The text
	 * may contain BBCode. Supplying either a text, a rating (both audio and
	 * video) or both is mandatory.
	 * 
	 * @param release
	 *            The {@link Release} to add a comment to or {@code null}.
	 * @param p2pRelease
	 *            The {@link P2pRelease} to add a comment to or {@code null}.
	 * @param text
	 *            The comment. You may use BBCode to format the text. Can be
	 *            {@code null} if both videoRating and audioRating are set.
	 * @param videoRating
	 *            Video rating between 1 (bad) to 10 (good). You must always
	 *            rate both or none. You may only vote once, and may not change
	 *            your vote. Check the vote property from the response to get
	 *            the rating as displayed on the website. Use 0 if no rating.
	 * @param audioRating
	 *            Audio rating between 1 (bad) to 10 (good). You must always
	 *            rate both or none. You may only vote once, and may not change
	 *            your vote. Check the vote property from the response to get
	 *            the rating as displayed on the website. Use 0 if no rating.
	 * @param token
	 *            The {@link Token} with all needed info.
	 * @return The added {@link Comment}
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/6312/api-comments-add.html
	 */
	private Comment postCommentsAdd(Release release, P2pRelease p2pRelease, String text, int videoRating,
			int audioRating, Token token) throws JSONException, IOException, XrelException {
		if ((release == null && p2pRelease == null) || token == null) {
			throw new XrelException("parameters missing");
		}

		ArrayList<String> keyList = new ArrayList<String>();
		ArrayList<String> valueList = new ArrayList<String>();
		String id;
		if (release != null) {
			keyList.add("type");
			valueList.add("release");
			id = release.getId();
		} else {
			keyList.add("type");
			valueList.add("p2p_rls");
			id = p2pRelease.getId();
		}
		keyList.add("id");
		valueList.add(id);

		boolean correctComment = false;
		if (videoRating > 0 && audioRating > 0 && videoRating < 11 && audioRating < 11) {
			correctComment = true;
			keyList.add("video_rating");
			valueList.add(String.valueOf(videoRating));
			keyList.add("audio_rating");
			valueList.add(String.valueOf(audioRating));
		}
		if (text != null && !text.isEmpty()) {
			correctComment = true;
			keyList.add("text");
			valueList.add(text);
		}

		if (!correctComment) {
			throw new XrelException("either a text or a rating is mandatory");
		}

		return new Comment(NetworkingHelper.readJsonObjectFromUrlPost(xrelUrl + "comments/add" + format, token, keyList,
				valueList));
	}

	/**
	 * Add a comment to a given {@link Release}. The text may contain BBCode.
	 * Supplying either a text, a rating (both audio and video) or both is
	 * mandatory.
	 * 
	 * @param release
	 *            The {@link Release} to add a comment to.
	 * @param text
	 *            The comment. You may use BBCode to format the text. Can be
	 *            {@code null} if both videoRating and audioRating are set.
	 * @param videoRating
	 *            Video rating between 1 (bad) to 10 (good). You must always
	 *            rate both or none. You may only vote once, and may not change
	 *            your vote. Check the vote property from the response to get
	 *            the rating as displayed on the website. Use 0 if no rating.
	 * @param audioRating
	 *            Audio rating between 1 (bad) to 10 (good). You must always
	 *            rate both or none. You may only vote once, and may not change
	 *            your vote. Check the vote property from the response to get
	 *            the rating as displayed on the website. Use 0 if no rating.
	 * @param token
	 *            The {@link Token} with all needed info.
	 * @return The added {@link Comment}
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/6312/api-comments-add.html
	 */
	public Comment postCommentsAdd(Release release, String text, int videoRating, int audioRating, Token token)
			throws JSONException, IOException, XrelException {
		if (release == null || token == null) {
			throw new XrelException("parameters missing");
		}

		return postCommentsAdd(release, null, text, videoRating, audioRating, token);
	}

	/**
	 * Add a comment to a given {@link P2pRelease}. The text may contain BBCode.
	 * 
	 * 
	 * @param p2pRelease
	 *            The {@link P2pRelease} to add a comment to.
	 * @param text
	 *            The comment. You may use BBCode to format the text. Can be
	 *            {@code null} if both videoRating and audioRating are set.
	 * @param videoRating
	 *            Video rating between 1 (bad) to 10 (good). You must always
	 *            rate both or none. You may only vote once, and may not change
	 *            your vote. Check the vote property from the response to get
	 *            the rating as displayed on the website. Use 0 if no rating.
	 * @param audioRating
	 *            Audio rating between 1 (bad) to 10 (good). You must always
	 *            rate both or none. You may only vote once, and may not change
	 *            your vote. Check the vote property from the response to get
	 *            the rating as displayed on the website. Use 0 if no rating.
	 * @param token
	 *            The {@link Token} with all needed info
	 * @return The added {@link Comment}
	 * @throws JSONException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/6312/api-comments-add.html
	 */
	public Comment postCommentsAdd(P2pRelease p2pRelease, String text, int videoRating, int audioRating, Token token)
			throws JSONException, IOException, XrelException {
		if (p2pRelease == null || token == null) {
			throw new XrelException("parameters missing");
		}

		return postCommentsAdd(null, p2pRelease, text, videoRating, audioRating, token);
	}

	/**
	 * Returns information about the active user.
	 * 
	 * @param token
	 *            The token used for authentication
	 * @return The {@link User}
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws XrelException
	 * @see https://www.xrel.to/wiki/6441/api-user-info.html
	 */
	public User getUserInfo(Token token) throws MalformedURLException, IOException, XrelException {
		if (token == null) {
			throw new XrelException("parameters missing");
		}

		JSONObject jsonObject = NetworkingHelper.readJsonObjectFromUrlGet(xrelUrl + "user/info" + format, token);
		User.getInstance().parseJsonObject(jsonObject);
		return User.getInstance();
	}

	/**
	 * This is the endpoint for the first step of the OAuth 2.0 authentication
	 * flow. This basically returns the URL you have to point the user to with
	 * all the information you provided while constructing the {@link Xrel}
	 * object.
	 * 
	 * @return The url you point the user to
	 * @see https://www.xrel.to/wiki/6439/api-oauth2-auth.html
	 */
	public String getOauth2Auth() {
		String url = xrelUrl + "oauth2/auth?response_type=" + responseType + "&client_id=" + getClientId();
		if (getRedirectUri() != null && !getRedirectUri().isEmpty()) {
			url += "&redirect_uri=" + getRedirectUri();
		}
		if (getState() != null && !getState().isEmpty()) {
			url += "&state=" + getState();
		}
		if (getScope() != null && getScope().length > 0) {
			url += "&scope=";
			boolean first = true;
			for (int i = 0; i < getScope().length; i++) {
				if (!first) {
					url += "%20";
				} else {
					first = false;
				}
				url += getScope()[i];
			}
		}
		return url;
	}

	/**
	 * This is the endpoint for the last step of the OAuth 2.0 authentication
	 * flow.
	 * 
	 * @param grantType
	 *            {@code authorization_code} for User authentication,
	 *            {@code client_credentials} for Application authentication,
	 *            {@code refresh_token} for refreshing an access token
	 * @param code
	 *            When performing the {@code authorization_code} grant, you must
	 *            specify the code provided from {@code #getOauth2Auth()}
	 * @param token
	 *            The {@link Token} with all needed info if performing
	 *            {@code refresh_token} or {@code null} otherwise
	 * @return The new {@link Token}
	 * @throws XrelException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @see https://www.xrel.to/wiki/6440/api-oauth2-token.html
	 */
	public Token postOauth2Token(String grantType, String code, Token token)
			throws XrelException, IllegalArgumentException, IOException {
		if (grantType != "authorization_code" && grantType != "client_credentials" && grantType != "refresh_token") {
			throw new XrelException("Invalid grant_type");
		}
		String unsetParameters = "Needed parameters not set:";
		boolean error = false;
		if (getClientId().isEmpty()) {
			error = true;
			unsetParameters += " client_id";
		}
		if (getClientSecret().isEmpty()) {
			error = true;
			unsetParameters += " client_secret";
		}
		if (grantType == "authorization_code" && code.isEmpty()) {
			error = true;
			unsetParameters += " code";
		}
		if (grantType == "refresh_token" && (token == null || token.getRefreshToken().isEmpty())) {
			error = true;
			unsetParameters += " refresh_token";
		}
		if (error) {
			throw new XrelException(unsetParameters);
		}

		ArrayList<String> keyList = new ArrayList<String>();
		ArrayList<String> valueList = new ArrayList<String>();
		keyList.add("grant_type");
		valueList.add(grantType);
		keyList.add("client_id");
		valueList.add(getClientId());
		keyList.add("client_secret");
		valueList.add(getClientSecret());

		if (grantType == "authorization_code") {
			keyList.add("code");
			valueList.add(code);
		}
		if (grantType == "refresh_token") {
			keyList.add("refresh_token");
			valueList.add(token.getRefreshToken());
		}
		if (grantType != "refresh_token" && getRedirectUri() != null && !getRedirectUri().isEmpty()) {
			keyList.add("redirect_uri");
			valueList.add(getRedirectUri());
		}
		if (getScope() != null && getScope().length > 0) {
			keyList.add("scope");
			String scopeValues = "";
			boolean first = true;
			for (int i = 0; i < getScope().length; i++) {
				if (!first) {
					scopeValues += " ";
				} else {
					first = false;
				}
				scopeValues += getScope()[i];
			}
			valueList.add(scopeValues);
		}

		return new Token(NetworkingHelper.readJsonObjectFromUrlPost(xrelUrl + "oauth2/token" + format, null, keyList,
				valueList));
	}

}