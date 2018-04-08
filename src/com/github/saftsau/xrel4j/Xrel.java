/*
 * Copyright 2017 saftsau
 *
 * This file is part of xREL4J.
 *
 * xREL4J is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * xREL4J is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with xREL4J. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.github.saftsau.xrel4j;

import com.github.saftsau.xrel4j.comment.Comment;
import com.github.saftsau.xrel4j.extinfo.ExtInfo;
import com.github.saftsau.xrel4j.extinfo.ExtInfoMedia;
import com.github.saftsau.xrel4j.extinfo.ExtInfoSearchResult;
import com.github.saftsau.xrel4j.release.ReleaseSearchResult;
import com.github.saftsau.xrel4j.release.p2p.P2pCategory;
import com.github.saftsau.xrel4j.release.p2p.P2pGroup;
import com.github.saftsau.xrel4j.release.p2p.P2pRelease;
import com.github.saftsau.xrel4j.release.scene.Release;
import com.github.saftsau.xrel4j.util.JsonbSingleton;
import com.github.saftsau.xrel4j.util.NetworkingHelper;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Java implementation of the xREL API v2. Method and parameter names are based on the xREL API with
 * "_" replaced by camel case. Order of methods and parameters are based on the xREL API
 * documentation. It is STRONGLY encouraged to read the xREL API documentation!
 * 
 * @see <a href="https://www.xrel.to/wiki/1681/API.html">API Overview</a>
 */
public class Xrel {

  private final String xrelUrl = "https://api.xrel.to/v2/";
  private final String format = ".json";
  private final int paginationPerPageMin = 5;
  private final int paginationPerPageMax = 100;
  private final String responseType = "code";
  private Optional<String> clientId;
  private Optional<String> clientSecret;
  private Optional<String> redirectUri;
  private Optional<String> state;
  private Optional<String[]> scope;

  /**
   * Constructs a new xREL object without any oAuth information.
   * 
   * @see <a href="https://www.xrel.to/wiki/6436/api-oauth2.html">API: OAuth 2.0</a>
   */
  public Xrel() {
    this.clientId = Optional.empty();
  }

  /**
   * Constructs a new xREL object with oAuth information and no scopes.
   * 
   * @param clientId Your consumer key.
   * @param clientSecret Your consumer secret.
   * @see <a href="https://www.xrel.to/wiki/6436/api-oauth2.html">API: OAuth 2.0</a>
   */
  public Xrel(String clientId, String clientSecret) {
    this.clientId = Optional.of(clientId);
    this.clientSecret = Optional.of(clientSecret);
    this.redirectUri = Optional.empty();
    this.state = Optional.empty();
    this.scope = Optional.empty();
  }

  /**
   * Constructs a new xREL object with oAuth information and no scopes.
   * 
   * @param clientId Your consumer key.
   * @param clientSecret Your consumer secret.
   * @param redirectUri Optional URI to redirect to after the authentication. Please read the Guide
   *        for more details.
   * @param state Optionally any string. You may set this value to any value, and it will be
   *        returned after the authentication. It might also be useful to prevent CSRF attacks.
   * @see <a href="https://www.xrel.to/wiki/6436/api-oauth2.html">API: OAuth 2.0</a>
   */
  public Xrel(String clientId, String clientSecret, Optional<String> redirectUri,
      Optional<String> state) {
    this.clientId = Optional.of(clientId);
    this.clientSecret = Optional.of(clientSecret);
    this.redirectUri = redirectUri;
    this.state = state;
    this.scope = Optional.empty();
  }

  /**
   * Constructs a new xREL object. If you have oAuth access but no additional scopes you should use
   * {@link #Xrel(String, String, Optional, Optional)}.
   * 
   * @param clientId Your consumer key.
   * @param clientSecret Your consumer secret.
   * @param scope Needed to access protected methods. If you do have scope access: you MUST supply
   *        these while processing the Tokens, even if you only plan to use them at a later stage.
   *        Rule of thumb: if you have these, always add them here.
   * @see <a href="https://www.xrel.to/wiki/6436/api-oauth2.html">API: OAuth 2.0</a>
   */
  public Xrel(String clientId, String clientSecret, String[] scope) {
    this.clientId = Optional.of(clientId);
    this.clientSecret = Optional.of(clientSecret);
    this.redirectUri = Optional.empty();
    this.state = Optional.empty();
    this.scope = Optional.of(scope);
  }

  /**
   * Constructs a new xREL object. If you have oAuth access but no additional scopes you should use
   * {@link #Xrel(String, String, Optional, Optional)}.
   * 
   * @param clientId Your consumer key.
   * @param clientSecret Your consumer secret.
   * @param redirectUri Optional URI to redirect to after the authentication. Please read the Guide
   *        for more details.
   * @param state Optionally any string. You may set this value to any value, and it will be
   *        returned after the authentication. It might also be useful to prevent CSRF attacks.
   * @param scope Needed to access protected methods. If you do have scope access: you MUST supply
   *        these while processing the Tokens, even if you only plan to use them at a later stage.
   *        Rule of thumb: if you have these, always add them here.
   * @see <a href="https://www.xrel.to/wiki/6436/api-oauth2.html">API: OAuth 2.0</a>
   */
  public Xrel(String clientId, String clientSecret, Optional<String> redirectUri,
      Optional<String> state, String[] scope) {
    this.clientId = Optional.of(clientId);
    this.clientSecret = Optional.of(clientSecret);
    this.redirectUri = redirectUri;
    this.state = state;
    this.scope = Optional.of(scope);
  }


  /**
   * Gets the xREL base URL.
   * 
   * @return The xREL URL
   */
  private String getXrelUrl() {
    return xrelUrl;
  }

  /**
   * Gets the format of the wanted response.
   * 
   * @return The format of the response
   */
  private String getFormat() {
    return format;
  }

  /**
   * Gets the minimum amount of entries in pagination.
   * 
   * @return The minimum pagination amount
   */
  private int getPaginationPerPageMin() {
    return paginationPerPageMin;
  }

  /**
   * Gets the maximum amount of entries in pagination.
   * 
   * @return The maximum pagination amount
   */
  private int getPaginationPerPageMax() {
    return paginationPerPageMax;
  }

  /**
   * Gets the response type used for OAuth.
   * 
   * @return The response type
   */
  private String getResponseType() {
    return responseType;
  }

  /**
   * Gets the set consumer key.
   * 
   * @return The consumer key
   */
  public Optional<String> getClientId() {
    return clientId;
  }

  /**
   * Gets the set consumer secret.
   * 
   * @return The consumer secret
   */
  public Optional<String> getClientSecret() {
    return clientSecret;
  }

  /**
   * Gets the set redirect URI.
   * 
   * @return The redirect URI
   */
  public Optional<String> getRedirectUri() {
    return redirectUri;
  }

  /**
   * Gets the set state used to prevent CSRF attacks.
   * 
   * @return The state
   */
  public Optional<String> getState() {
    return state;
  }

  /**
   * Gets the set scope.
   * 
   * @return The scope
   */
  public Optional<String[]> getScope() {
    return scope;
  }

  /**
   * Normalizes the given perPage and page input. perPage is a value between
   * {@link #paginationPerPageMin} and {@link #paginationPerPageMax} and page is a value greater
   * zero.
   * 
   * @param perPage The perPage value to normalize
   * @param page The page value to normalize
   * @return An array with perPage on [0] and page on [1]
   */
  private int[] normalizePageValues(int perPage, int page) {
    if (perPage < getPaginationPerPageMin()) {
      perPage = getPaginationPerPageMin();
    } else if (perPage > getPaginationPerPageMax()) {
      perPage = getPaginationPerPageMax();
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
   * Gets the maximum number of requests that the consumer is permitted to make per hour as returned
   * by the last request. -1 if not yet set.
   * 
   * @return The X-RateLimit-Limit
   * @see <a href="https://www.xrel.to/wiki/2727/api-rate-limiting.html">API: Rate Limiting</a>
   */
  public int getXRateLimitLimit() {
    return NetworkingHelper.getXRateLimitLimit();
  }

  /**
   * Gets the number of requests remaining in the current rate limit window as returned by the last
   * request. -1 if not yet set.
   * 
   * @return The X-RateLimit-Remaining
   * @see <a href="https://www.xrel.to/wiki/2727/api-rate-limiting.html">API: Rate Limiting</a>
   */
  public int getXRateLimitRemaining() {
    return NetworkingHelper.getXRateLimitRemaining();
  }

  /**
   * Gets the time at which the current rate limit window resets in UTC epoch seconds as returned by
   * the last request. -1 if not yet set.
   * 
   * @return The X-RateLimit-Reset
   * @see <a href="https://www.xrel.to/wiki/2727/api-rate-limiting.html">API: Rate Limiting</a>
   */
  public int getXRateLimitReset() {
    return NetworkingHelper.getXRateLimitReset();
  }

  /**
   * Checks if a specific scope was given at creation time.
   * 
   * @param scope The scope to check for
   * @return {@code true} if found, {@code false} otherwise
   */
  private boolean checkScope(String scope) {
    boolean found = false;
    for (int i = 0; i < getScope().get().length; i++) {
      if (getScope().get()[i].equals(scope)) {
        found = true;
      }
    }
    return found;
  }

  /**
   * Returns information about a single release, specified by the complete dirname or an API release
   * id.
   * 
   * @param idDir Dirname or API id of the release
   * @param useId If {@code true} uses the idDir as an API ID for the request, if {@code false} uses
   *        the idDir as a dirname for the request
   * @return The corresponding {@link Release}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/1680/api-release-info.html">API: release/info method</a>
   */
  private Release getReleaseInfo(String idDir, boolean useId) throws IOException, XrelException {
    Objects.requireNonNull(idDir, "idDir missing");
    String json;
    if (useId) {
      json = NetworkingHelper
          .readStringFromUrlGet(getXrelUrl() + "release/info" + getFormat() + "?id=" + idDir);
    } else {
      json = NetworkingHelper
          .readStringFromUrlGet(getXrelUrl() + "release/info" + getFormat() + "?dirname=" + idDir);
    }

    Release release = JsonbSingleton.getInstance().getJsonb().fromJson(json, Release.class);
    return release;
  }

  /**
   * Returns information about a single release, specified by the complete dirname.
   * 
   * @param dir Dirname of the release
   * @return The corresponding {@link Release}
   * @throws IOException If there is a networking problem Thrown in case
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/1680/api-release-info.html">API: release/info method</a>
   */
  public Release getReleaseInfoDir(String dir) throws IOException, XrelException {
    Objects.requireNonNull(dir, "dir missing");

    return getReleaseInfo(dir, false);
  }

  /**
   * Returns information about a single release, specified by the API release id.
   * 
   * @param id API id of the release
   * @return The corresponding {@link Release}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/1680/api-release-info.html">API: release/info method</a>
   */
  public Release getReleaseInfoId(String id) throws IOException, XrelException {
    Objects.requireNonNull(id, "id missing");

    return getReleaseInfo(id, true);
  }

  /**
   * Returns the latest releases. Also allows to browse the archive by month. Please note that the
   * latest release list with no archive defined does NOT return a total number of pages. Around
   * 1000 releases are available for browsing in that mode. per_page continues to work. You can
   * either provide a {@link Filter}, a {@code Token} or neither, but not both.
   *
   * @param archive YYYY-MM for archive.
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @param filter Filter (from {@link #getReleaseCategories()}).
   * @param token Uses the overview filter of the respective user
   * @return The list of {@link Release} matching the criteria
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/2994/api-release-latest.html">API: release/latest
   *      method</a>
   */
  public PaginationList<Release> getReleaseLatestPrivate(String archive, int perPage, int page,
      Filter filter, Token token) throws IOException, XrelException {
    int[] normalizedPageValues = normalizePageValues(perPage, page);

    String url = getXrelUrl() + "release/latest" + getFormat() + "?per_page="
        + normalizedPageValues[0] + "&page=" + normalizedPageValues[1];
    if (archive != null) {
      url += "&archive=" + archive;
    }
    if (filter != null && token == null) {
      url += "&filter=" + filter.getId();
    }
    if (filter == null && token != null) {
      url += "&filter=overview";
    }

    String json;
    json = NetworkingHelper.readStringFromUrlGet(url, token);

    PaginationList<Release> releaseList = JsonbSingleton.getInstance().getJsonb().fromJson(json,
        new PaginationList<Release>() {}.getClass().getGenericSuperclass());
    return releaseList;
  }

  /**
   * Returns the latest releases. Also allows to browse the archive by month. Please note that the
   * latest release list with no archive defined does NOT return a total number of pages. Around
   * 1000 releases are available for browsing in that mode. per_page continues to work.
   *
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @return The list of {@link Release} matching the criteria
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/2994/api-release-latest.html">API: release/latest
   *      method</a>
   */
  public PaginationList<Release> getReleaseLatest(int perPage, int page)
      throws IOException, XrelException {
    return getReleaseLatestPrivate(null, perPage, page, null, null);
  }

  /**
   * Returns the latest releases. Also allows to browse the archive by month. Please note that the
   * latest release list with no archive defined does NOT return a total number of pages. Around
   * 1000 releases are available for browsing in that mode. per_page continues to work.
   *
   * @param archive YYYY-MM for archive.
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @return The list of {@link Release} matching the criteria
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/2994/api-release-latest.html">API: release/latest
   *      method</a>
   */
  public PaginationList<Release> getReleaseLatest(String archive, int perPage, int page)
      throws IOException, XrelException {
    Objects.requireNonNull(archive, "archive missing");

    return getReleaseLatestPrivate(archive, perPage, page, null, null);
  }

  /**
   * Returns the latest releases. Also allows to browse the archive by month. Please note that the
   * latest release list with no archive defined does NOT return a total number of pages. Around
   * 1000 releases are available for browsing in that mode. per_page continues to work.
   *
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @param filter Filter (from {@link #getReleaseCategories()}).
   * @return The list of {@link Release} matching the criteria
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/2994/api-release-latest.html">API: release/latest
   *      method</a>
   */
  public PaginationList<Release> getReleaseLatest(int perPage, int page, Filter filter)
      throws IOException, XrelException {
    Objects.requireNonNull(filter, "filter missing");

    return getReleaseLatestPrivate(null, perPage, page, filter, null);
  }

  /**
   * Returns the latest releases. Also allows to browse the archive by month. Please note that the
   * latest release list with no archive defined does NOT return a total number of pages. Around
   * 1000 releases are available for browsing in that mode. per_page continues to work.
   *
   * @param archive YYYY-MM for archive.
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @param filter Filter (from {@link #getReleaseCategories()}).
   * @return The list of {@link Release} matching the criteria
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/2994/api-release-latest.html">API: release/latest
   *      method</a>
   */
  public PaginationList<Release> getReleaseLatest(String archive, int perPage, int page,
      Filter filter) throws IOException, XrelException {
    Objects.requireNonNull(archive, "archive missing");
    Objects.requireNonNull(filter, "filter missing");

    return getReleaseLatestPrivate(archive, perPage, page, filter, null);
  }

  /**
   * Returns the latest releases. Also allows to browse the archive by month. Please note that the
   * latest release list with no archive defined does NOT return a total number of pages. Around
   * 1000 releases are available for browsing in that mode. per_page continues to work.
   *
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @param token Uses the overview filter of the respective user
   * @return The list of {@link Release} matching the criteria
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/2994/api-release-latest.html">API: release/latest
   *      method</a>
   */
  public PaginationList<Release> getReleaseLatest(int perPage, int page, Token token)
      throws IOException, XrelException {
    Objects.requireNonNull(token, "token missing");

    return getReleaseLatest(null, perPage, page, null, token);
  }

  /**
   * Returns the latest releases. Also allows to browse the archive by month. Please note that the
   * latest release list with no archive defined does NOT return a total number of pages. Around
   * 1000 releases are available for browsing in that mode. per_page continues to work.
   *
   * @param archive YYYY-MM for archive.
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @param token Uses the overview filter of the respective user
   * @return The list of {@link Release} matching the criteria
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/2994/api-release-latest.html">API: release/latest
   *      method</a>
   */
  public PaginationList<Release> getReleaseLatest(String archive, int perPage, int page,
      Token token) throws IOException, XrelException {
    Objects.requireNonNull(archive, "archive missing");
    Objects.requireNonNull(token, "token missing");

    return getReleaseLatest(archive, perPage, page, null, token);
  }

  /**
   * Returns the latest releases. Also allows to browse the archive by month. Please note that the
   * latest release list with no archive defined does NOT return a total number of pages. Around
   * 1000 releases are available for browsing in that mode. per_page continues to work. You can
   * either provide a {@link Filter}, a {@code Token} or neither, but not both.
   *
   * @param archive YYYY-MM for archive.
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @param filter Filter (from {@link #getReleaseCategories()}).
   * @param token Uses the overview filter of the respective user
   * @return The list of {@link Release} matching the criteria
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/2994/api-release-latest.html">API: release/latest
   *      method</a>
   */
  public PaginationList<Release> getReleaseLatest(String archive, int perPage, int page,
      Filter filter, Token token) throws IOException, XrelException {
    Objects.requireNonNull(archive, "archive missing");
    Objects.requireNonNull(filter, "filter missing");
    Objects.requireNonNull(token, "token missing");

    return getReleaseLatest(archive, perPage, page, filter, token);
  }

  /**
   * Returns a list of available release categories. You can use the category name in
   * {@link #getReleaseBrowseCategory(ReleaseCategory, String, int, int)}. You should avoid calling
   * this method repeatedly and cache its result for at least 24 hours (where possible).
   *
   * @return The set of {@link ReleaseCategory}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/6318/api-release-categories.html">API:
   *      release/categories method</a>
   */
  public Set<ReleaseCategory> getReleaseCategories() throws IOException, XrelException {
    String json =
        NetworkingHelper.readStringFromUrlGet(getXrelUrl() + "release/categories" + getFormat());
    Set<ReleaseCategory> categorySet =
        JsonbSingleton.getInstance().getJsonb().fromJson(json, new HashSet<ReleaseCategory>() {
          private static final long serialVersionUID = 1L;
        }.getClass().getGenericSuperclass());

    // We put all categories we found into a map, so we can calculate the parrent
    // categories
    Map<String, ReleaseCategory> hashMap = new HashMap<String, ReleaseCategory>();
    for (ReleaseCategory releaseCategory : categorySet) {
      hashMap.put(releaseCategory.getName(), releaseCategory);
    }
    for (ReleaseCategory releaseCategory : categorySet) {
      releaseCategory.setParentCat(hashMap.get(releaseCategory.getParentCatName()));
    }
    return categorySet;
  }

  /**
   * Returns scene releases from the given category.
   *
   * @param category Category from {@link #getReleaseCategories()}
   * @param extInfoType Use one of: {@code movie}|{@code tv}|{@code game}|{@code console}|{@code
   software}|{@code xxx} or {@code null}
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @return The list of {@link Release} matching the criteria
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3751/api-release-browse-category.html">API:
   *      release/browse_category method</a>
   */
  private PaginationList<Release> getReleaseBrowseCategoryPrivate(ReleaseCategory category,
      String extInfoType, int perPage, int page) throws IOException, XrelException {
    int[] normalizedPageValues = normalizePageValues(perPage, page);

    String url = getXrelUrl() + "release/browse_category" + getFormat() + "?category_name="
        + category.getName();
    if (extInfoType != null) {
      url += "&ext_info_type=" + extInfoType;
    }
    url += "&per_page=" + normalizedPageValues[0] + "&page=" + normalizedPageValues[1];
    String json = NetworkingHelper.readStringFromUrlGet(url);
    PaginationList<Release> releaseList = JsonbSingleton.getInstance().getJsonb().fromJson(json,
        new PaginationList<Release>() {}.getClass().getGenericSuperclass());

    return releaseList;
  }

  /**
   * Returns scene releases from the given category.
   *
   * @param category Category from {@link #getReleaseCategories()}
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @return The list of {@link Release} matching the criteria
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3751/api-release-browse-category.html">API:
   *      release/browse_category method</a>
   */
  public PaginationList<Release> getReleaseBrowseCategory(ReleaseCategory category, int perPage,
      int page) throws IOException, XrelException {
    return getReleaseBrowseCategoryPrivate(category, null, perPage, page);
  }

  /**
   * Returns scene releases from the given category.
   *
   * @param category Category from {@link #getReleaseCategories()}
   * @param extInfoType Use one of: {@code movie}|{@code tv}|{@code game}|{@code console}|{@code
   software}|{@code xxx}
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @return The list of {@link Release} matching the criteria
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3751/api-release-browse-category.html">API:
   *      release/browse_category method</a>
   */
  public PaginationList<Release> getReleaseBrowseCategory(ReleaseCategory category,
      String extInfoType, int perPage, int page) throws IOException, XrelException {
    Objects.requireNonNull(category, "category missing");
    Objects.requireNonNull(extInfoType, "extInfoType missing");

    return getReleaseBrowseCategoryPrivate(category, extInfoType, perPage, page);
  }

  /**
   * Returns all releases associated with a given Ext Info.
   *
   * @param extInfo Ext info.
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @return The list of {@link Release} matching the criteria
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   */
  public PaginationList<Release> getReleaseExtInfo(ExtInfo extInfo, int perPage, int page)
      throws IOException, XrelException {
    Objects.requireNonNull(extInfo, "extInfo missing");

    int[] normalizedPageValues = normalizePageValues(perPage, page);

    String url = getXrelUrl() + "release/ext_info" + getFormat() + "?id=" + extInfo.getId()
        + "&per_page=" + normalizedPageValues[0] + "&page=" + normalizedPageValues[1];
    String json = NetworkingHelper.readStringFromUrlGet(url);
    PaginationList<Release> releaseList = JsonbSingleton.getInstance().getJsonb().fromJson(json,
        new PaginationList<Release>() {}.getClass().getGenericSuperclass());
    return releaseList;
  }

  /**
   * Returns a set of public, predefined release filters. You can use the filter ID in
   * {@code #getReleaseLatest(Optional, int, int, Optional, Optional)}. You should avoid calling
   * this method repeatedly and cache its result for at least 24 hours (where possible).
   *
   * @return The set of {@link Filter}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/2996/api-release-filters.html">API: release/filters
   *      method</a>
   */
  public Set<Filter> getReleaseFilters() throws IOException, XrelException {
    String json =
        NetworkingHelper.readStringFromUrlGet(getXrelUrl() + "release/filters" + getFormat());
    Set<Filter> filterSet =
        JsonbSingleton.getInstance().getJsonb().fromJson(json, new HashSet<Filter>() {
          private static final long serialVersionUID = 1L;
        }.getClass().getGenericSuperclass());
    return filterSet;
  }

  /**
   * Adds a proof picture to a given API release id. This method requires the addproof scope. Please
   * read the rules before posting proofs.
   *
   * @param releaseList The list of releases this proof should be added to.
   * @param image Base64 encoded image
   * @param token The {@link Token} with all needed info
   * @return The URL of the added proof
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6444/api-release-addproof.html">API: release/addproof
   *      method</a>
   */
  public String postReleaseAddProof(List<Release> releaseList, String image, Token token)
      throws IOException, XrelException {
    Objects.requireNonNull(releaseList, "releaseList missing");
    Objects.requireNonNull(image, "image missing");
    Objects.requireNonNull(token, "token missing");

    if (!getScope().isPresent() || !checkScope("addproof")) {
      throw new XrelException("addproof scope not provided");
    }

    List<String> keyList = new ArrayList<String>();
    List<String> valueList = new ArrayList<String>();
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

    String url = getXrelUrl() + "release/addproof" + getFormat();
    String json = NetworkingHelper.readStringFromUrlPost(url, token, keyList, valueList);

    // We only care about the proof url because the releases we get are known
    // already
    JsonReader jsonReader = Json.createReader(new StringReader(json));
    JsonObject jsonObject = jsonReader.readObject();
    String proofUrl = jsonObject.getString("proof_url");
    return proofUrl;
  }

  /**
   * Browse P2P/non-scene releases. Please note that the latest release list does NOT return a total
   * number of pages. Around 1000 releases are available for browsing in that mode. per_page
   * continues to work.
   *
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @param p2pCategory Optional P2P category ID from {@link #getP2pCategories()}
   * @param p2pGroup Optional P2P release group
   * @param extInfo Optional ExtInfo
   * @return The list of {@link P2pRelease} matching the criteria
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3699/api-p2p-releases.html">API: p2p/releases</a>
   */
  private PaginationList<P2pRelease> getP2pReleasesPrivate(int perPage, int page,
      P2pCategory p2pCategory, P2pGroup p2pGroup, ExtInfo extInfo)
      throws IOException, XrelException {
    int[] normalizedPageValues = normalizePageValues(perPage, page);

    String url = getXrelUrl() + "p2p/releases" + getFormat() + "?per_page="
        + normalizedPageValues[0] + "&page=" + normalizedPageValues[1];
    if (p2pCategory != null) {
      url += "&category_id=" + p2pCategory.getId();
    }
    if (p2pGroup != null) {
      url += "&group_id=" + p2pGroup.getId();
    }
    if (extInfo != null) {
      url += "&ext_info_id=" + extInfo.getId();
    }

    String json = NetworkingHelper.readStringFromUrlGet(url);
    PaginationList<P2pRelease> p2pList = JsonbSingleton.getInstance().getJsonb().fromJson(json,
        new PaginationList<Release>() {}.getClass().getGenericSuperclass());
    return p2pList;
  }

  /**
   * Browse P2P/non-scene releases. Please note that the latest release list does NOT return a total
   * number of pages. Around 1000 releases are available for browsing in that mode. per_page
   * continues to work.
   *
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @return The list of {@link P2pRelease} matching the criteria
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3699/api-p2p-releases.html">API: p2p/releases</a>
   */
  public PaginationList<P2pRelease> getP2pReleases(int perPage, int page)
      throws IOException, XrelException {
    return getP2pReleasesPrivate(perPage, page, null, null, null);
  }

  /**
   * Browse P2P/non-scene releases. Please note that the latest release list does NOT return a total
   * number of pages. Around 1000 releases are available for browsing in that mode. per_page
   * continues to work.
   *
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @param p2pCategory P2P category ID from {@link #getP2pCategories()}
   * @return The list of {@link P2pRelease} matching the criteria
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3699/api-p2p-releases.html">API: p2p/releases</a>
   */
  public PaginationList<P2pRelease> getP2pReleases(int perPage, int page, P2pCategory p2pCategory)
      throws IOException, XrelException {
    Objects.requireNonNull(p2pCategory, "p2pCategory missing");

    return getP2pReleasesPrivate(perPage, page, p2pCategory, null, null);
  }

  /**
   * Browse P2P/non-scene releases. Please note that the latest release list does NOT return a total
   * number of pages. Around 1000 releases are available for browsing in that mode. per_page
   * continues to work.
   *
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @param p2pCategory P2P category ID from {@link #getP2pCategories()}
   * @param p2pGroup P2P release group
   * @return The list of {@link P2pRelease} matching the criteria
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3699/api-p2p-releases.html">API: p2p/releases</a>
   */
  public PaginationList<P2pRelease> getP2pReleases(int perPage, int page, P2pCategory p2pCategory,
      P2pGroup p2pGroup) throws IOException, XrelException {
    Objects.requireNonNull(p2pCategory, "p2pCategory missing");
    Objects.requireNonNull(p2pGroup, "p2pGroup missing");

    return getP2pReleasesPrivate(perPage, page, p2pCategory, p2pGroup, null);
  }

  /**
   * Browse P2P/non-scene releases. Please note that the latest release list does NOT return a total
   * number of pages. Around 1000 releases are available for browsing in that mode. per_page
   * continues to work.
   *
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @param p2pGroup P2P release group
   * @return The list of {@link P2pRelease} matching the criteria
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3699/api-p2p-releases.html">API: p2p/releases</a>
   */
  public PaginationList<P2pRelease> getP2pReleases(int perPage, int page, P2pGroup p2pGroup)
      throws IOException, XrelException {
    Objects.requireNonNull(p2pGroup, "p2pGroup missing");

    return getP2pReleasesPrivate(perPage, page, null, p2pGroup, null);
  }

  /**
   * Browse P2P/non-scene releases. Please note that the latest release list does NOT return a total
   * number of pages. Around 1000 releases are available for browsing in that mode. per_page
   * continues to work.
   *
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @param p2pGroup P2P release group
   * @param extInfo ExtInfo
   * @return The list of {@link P2pRelease} matching the criteria
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3699/api-p2p-releases.html">API: p2p/releases</a>
   */
  public PaginationList<P2pRelease> getP2pReleases(int perPage, int page, P2pGroup p2pGroup,
      ExtInfo extInfo) throws IOException, XrelException {
    Objects.requireNonNull(p2pGroup, "p2pGroup missing");
    Objects.requireNonNull(extInfo, "extInfo missing");

    return getP2pReleasesPrivate(perPage, page, null, p2pGroup, extInfo);
  }

  /**
   * Browse P2P/non-scene releases. Please note that the latest release list does NOT return a total
   * number of pages. Around 1000 releases are available for browsing in that mode. per_page
   * continues to work.
   *
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @param extInfo ExtInfo
   * @return The list of {@link P2pRelease} matching the criteria
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3699/api-p2p-releases.html">API: p2p/releases</a>
   */
  public PaginationList<P2pRelease> getP2pReleases(int perPage, int page, ExtInfo extInfo)
      throws IOException, XrelException {
    Objects.requireNonNull(extInfo, "extInfo missing");

    return getP2pReleasesPrivate(perPage, page, null, null, extInfo);
  }

  /**
   * Browse P2P/non-scene releases. Please note that the latest release list does NOT return a total
   * number of pages. Around 1000 releases are available for browsing in that mode. per_page
   * continues to work.
   *
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @param p2pCategory P2P category ID from {@link #getP2pCategories()}
   * @param p2pGroup P2P release group
   * @param extInfo ExtInfo
   * @return The list of {@link P2pRelease} matching the criteria
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3699/api-p2p-releases.html">API: p2p/releases</a>
   */
  public PaginationList<P2pRelease> getP2pReleases(int perPage, int page, P2pCategory p2pCategory,
      P2pGroup p2pGroup, ExtInfo extInfo) throws IOException, XrelException {
    Objects.requireNonNull(p2pCategory, "p2pCategory missing");
    Objects.requireNonNull(p2pGroup, "p2pGroup missing");
    Objects.requireNonNull(extInfo, "extInfo missing");

    return getP2pReleasesPrivate(perPage, page, p2pCategory, p2pGroup, extInfo);
  }

  /**
   * Returns a set of available P2P release categories and their IDs. You can use the category in
   * {@link #getP2pReleases(int, int, P2pCategory, P2pGroup, ExtInfo)}.
   *
   * @return The list of {@link P2pCategory}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3698/api-p2p-categories.html">API: p2p/categories</a>
   */
  public Set<P2pCategory> getP2pCategories() throws IOException, XrelException {
    String json =
        NetworkingHelper.readStringFromUrlGet(getXrelUrl() + "p2p/categories" + getFormat());
    Set<P2pCategory> p2pSet =
        JsonbSingleton.getInstance().getJsonb().fromJson(json, new HashSet<P2pCategory>() {
          private static final long serialVersionUID = 1L;
        }.getClass().getGenericSuperclass());
    return p2pSet;
  }

  /**
   * Returns information about a single P2P/non-scene release, specified by the complete dirname or
   * an API P2P release id.
   *
   * @param idDir Dirname or API id of the release
   * @param useId If {@code true} uses the idDir as an API ID for the request, if {@code false} uses
   *        the idDir as a dirname for the request
   * @return The {@link P2pRelease}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3697/api-p2p-rls-info.html">API: p2p/rls_info</a>
   */
  private P2pRelease getP2pRlsInfo(String idDir, boolean useId) throws IOException, XrelException {
    Objects.requireNonNull(idDir, "idDir missing");

    String json;
    if (useId) {
      json = NetworkingHelper
          .readStringFromUrlGet(getXrelUrl() + "p2p/rls_info" + getFormat() + "?id=" + idDir);
    } else {
      json = NetworkingHelper
          .readStringFromUrlGet(getXrelUrl() + "p2p/rls_info" + getFormat() + "?dirname=" + idDir);
    }
    P2pRelease p2pRelease =
        JsonbSingleton.getInstance().getJsonb().fromJson(json, P2pRelease.class);
    return p2pRelease;
  }

  /**
   * Returns information about a single P2P/non-scene release, specified by the complete dirname.
   *
   * @param dir The dirname of the P2P release
   * @return The {@link P2pRelease}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3697/api-p2p-rls-info.html">API: p2p/rls_info</a>
   */
  public P2pRelease getP2pRlsInfoDir(String dir) throws IOException, XrelException {
    Objects.requireNonNull(dir, "dir missing");

    return getP2pRlsInfo(dir, false);
  }

  /**
   * Returns information about a single P2P/non-scene release, specified by the API P2P release id.
   *
   * @param id The API P2P release id
   * @return The {@link P2pRelease}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3697/api-p2p-rls-info.html">API: p2p/rls_info</a>
   */
  public P2pRelease getP2pRlsInfoId(String id) throws IOException, XrelException {
    Objects.requireNonNull(id, "id missing");

    return getP2pRlsInfo(id, true);
  }

  /**
   * Returns an image of a NFO file for a given API release.
   *
   * @param release The {@link Release} you want the NFO of
   * @param token The {@link Token} with all needed info
   * @return The NFO as byte[]
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6438/api-nfo-release.html">API: nfo/release method</a>
   */
  public byte[] getNfoRelease(Release release, Token token) throws IOException, XrelException {
    Objects.requireNonNull(release, "release missing");
    Objects.requireNonNull(token, "token missing");

    if (!getScope().isPresent() || !checkScope("viewnfo")) {
      throw new XrelException("viewnfo scope not provided");
    }

    return NetworkingHelper.readByteFromUrlGet(
        getXrelUrl() + "nfo/release" + getFormat() + "?id=" + release.getId(), token);
  }

  /**
   * Returns an image of a NFO file for a given API P2P release.
   *
   * @param p2pRelease The {@link P2pRelease} you want the NFO of
   * @param token The {@link Token} with all needed info
   * @return The NFO as byte[]
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6437/api-nfo-p2p-rls.html">API: nfo/p2p_rls method</a>
   */
  public byte[] getNfoP2pRls(P2pRelease p2pRelease, Token token) throws IOException, XrelException {
    Objects.requireNonNull(p2pRelease, "p2pRelease missing");
    Objects.requireNonNull(token, "token missing");

    if (getScope() == null || !checkScope("viewnfo")) {
      throw new XrelException("viewnfo scope not provided");
    }

    return NetworkingHelper.readByteFromUrlGet(
        getXrelUrl() + "nfo/p2p_rls" + getFormat() + "?id=" + p2pRelease.getId(), token);
  }

  /**
   * Returns a list upcoming movies and their releases.
   *
   * @param country {@code de} for upcoming movies in germany, {@code us} for upcoming movies in the
   *        US/international.
   * @return The list of {@link ExtInfo}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/1827/api-calendar-upcoming.html">API: calendar/upcoming
   *      method</a>
   */
  public List<ExtInfo> getCalendarUpcoming(String country) throws IOException, XrelException {
    Objects.requireNonNull(country, "country missing");

    if (!country.equals("de") && !country.equals("us")) {
      throw new XrelException("country must be either de or en");
    }

    String url = getXrelUrl() + "calendar/upcoming" + getFormat() + "?country=" + country;
    String json = NetworkingHelper.readStringFromUrlGet(url);
    List<ExtInfo> upcomingList =
        JsonbSingleton.getInstance().getJsonb().fromJson(json, new ArrayList<ExtInfo>() {
          private static final long serialVersionUID = 1L;
        }.getClass().getGenericSuperclass());
    return upcomingList;
  }

  /**
   * Returns information about an Ext Info.
   *
   * @param extInfo The {@link ExtInfo} you want more info about.
   * @param token Your optional {@link Token}. If supplied you will also get {@code own_rating} from
   *        this {@link ExtInfo}.
   * @return The new {@link ExtInfo}.
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/2725/api-ext-info-info.html">API: ext_info/info
   *      method</a>
   */
  private ExtInfo getExtInfoInfoPrivate(ExtInfo extInfo, Token token)
      throws IOException, XrelException {
    Objects.requireNonNull(extInfo, "extInfo missing");

    String json;
    if (token == null) {
      json = NetworkingHelper.readStringFromUrlGet(
          getXrelUrl() + "ext_info/info" + getFormat() + "?id=" + extInfo.getId());
    } else {
      json = NetworkingHelper.readStringFromUrlGet(
          getXrelUrl() + "ext_info/info" + getFormat() + "?id=" + extInfo.getId(), token);
    }
    ExtInfo extInfoNew = JsonbSingleton.getInstance().getJsonb().fromJson(json, ExtInfo.class);
    return extInfoNew;
  }

  /**
   * Returns information about an Ext Info.
   *
   * @param extInfo The {@link ExtInfo} you want more info about.
   * @return The new {@link ExtInfo}.
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/2725/api-ext-info-info.html">API: ext_info/info
   *      method</a>
   */
  public ExtInfo getExtInfoInfo(ExtInfo extInfo) throws IOException, XrelException {
    Objects.requireNonNull(extInfo, "extInfo missing");

    return getExtInfoInfoPrivate(extInfo, null);
  }

  /**
   * Returns information about an Ext Info.
   *
   * @param extInfo The {@link ExtInfo} you want more info about.
   * @param token Your {@link Token}. If supplied you will also get {@code own_rating} from this
   *        {@link ExtInfo}.
   * @return The new {@link ExtInfo}.
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/2725/api-ext-info-info.html">API: ext_info/info
   *      method</a>
   */
  public ExtInfo getExtInfoInfo(ExtInfo extInfo, Token token) throws IOException, XrelException {
    Objects.requireNonNull(extInfo, "extInfo missing");
    Objects.requireNonNull(token, "token missing");

    return getExtInfoInfoPrivate(extInfo, token);
  }

  /**
   * Returns media associated with an Ext Info.
   *
   * @param extInfo The ExtInfo which media you want to retrieve. The found ExtInfoMedia will be
   *        added to the given ExtInfo.
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6314/api-ext-info-media.html">API: ext_info/media
   *      method</a>
   */
  public void getExtInfoMedia(ExtInfo extInfo) throws IOException, XrelException {
    Objects.requireNonNull(extInfo, "extInfo missing");

    String json = NetworkingHelper.readStringFromUrlGet(
        getXrelUrl() + "ext_info/media" + getFormat() + "?id=" + extInfo.getId());
    List<ExtInfoMedia> extInfoMediaList =
        JsonbSingleton.getInstance().getJsonb().fromJson(json, new ArrayList<ExtInfoMedia>() {
          private static final long serialVersionUID = 1L;
        }.getClass().getGenericSuperclass());
    if (extInfo.getExtInfoMedia() != null) {
      extInfo.getExtInfoMedia().clear();
      extInfo.getExtInfoMedia().addAll(extInfoMediaList);
    } else {
      extInfo.setExtInfoMedia(extInfoMediaList);
    }
  }

  /**
   * Rate an Ext Info. Also updates the given {@link ExtInfo} object.
   *
   * @param extInfo The {@link ExtInfo} to rate.
   * @param rating Rating between 1 (bad) to 10 (good). You may only vote once, and may not change
   *        your vote. Check the ownRating property from the response to get the rating as displayed
   *        on the website.
   * @param token The {@link Token} with all needed info.
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6315/api-ext-info-rate.html">API: ext_info/rate
   *      method</a>
   */
  public void postExtInfoRate(ExtInfo extInfo, int rating, Token token)
      throws IOException, XrelException {
    Objects.requireNonNull(extInfo, "extInfo missing");
    Objects.requireNonNull(token, "token missing");

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

    String json = NetworkingHelper.readStringFromUrlPost(
        getXrelUrl() + "ext_info/rate" + getFormat(), token, keyList, valueList);
    ExtInfo extInfoRated = JsonbSingleton.getInstance().getJsonb().fromJson(json, ExtInfo.class);
    extInfo.setOwnRating(extInfoRated.getOwnRating());
  }

  /**
   * Searches for Scene and P2P releases. For all calls to search methods, additional rate limiting
   * applies. Currently this limit is set at 2 calls per 10 seconds. Please keep track of that limit
   * yourself.
   *
   * @param q Search keyword.
   * @param scene If {@code true}, Scene releases will be included in the search results.
   * @param p2p If {@code true}, P2P releases will be included in the search results.
   * @param limit Number of returned search results.
   * @return A pair object containing the lists of {@link Release} and {@link P2pRelease}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6320/api-search-releases.html">API: search/releases
   *      method</a>
   */
  public ReleaseSearchResult getSearchReleases(String q, boolean scene, boolean p2p, int limit)
      throws IOException, XrelException {
    Objects.requireNonNull(q, "q missing");

    if ((!p2p) && (!scene)) {
      throw new XrelException("either scene or p2p must be set to true");
    }

    q = URLEncoder.encode(q, StandardCharsets.UTF_8.name());
    String url = getXrelUrl() + "search/releases" + getFormat() + "?q=" + q;
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

    String json = NetworkingHelper.readStringFromUrlGet(url);
    ReleaseSearchResult searchResult =
        JsonbSingleton.getInstance().getJsonb().fromJson(json, ReleaseSearchResult.class);

    return searchResult;
  }

  /**
   * Searches for Ext Infos. For all calls to search methods, additional rate limiting applies.
   * Currently this limit is set at 2 calls per 10 seconds. Please keep track of that limit
   * yourself.
   *
   * @param q Search keyword.
   * @param type One of: {@code movie}|{@code tv}|{@code game}|{@code console}|{@code
   software}|{@code xxx} - or {@code null} to browse releases of all types
   * @param limit Number of returned search results.
   * @return List of {@link ExtInfo}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6319/api-search-ext-info.html">API: search/ext_info
   *      method</a>
   */
  private ExtInfoSearchResult getSearchExtInfoPrivate(String q, String type, int limit)
      throws IOException, XrelException {
    if (q == null || q.isEmpty()) {
      throw new XrelException("parameters missing");
    }

    q = URLEncoder.encode(q, StandardCharsets.UTF_8.name());
    String url = getXrelUrl() + "search/ext_info" + getFormat() + "?q=" + q;
    if (type != null && !type.isEmpty()) {
      url += "&type=" + type;
    }

    if (limit < 1) {
      limit = 1;
    }
    url += "&limit=" + limit;

    String json = NetworkingHelper.readStringFromUrlGet(url);

    ExtInfoSearchResult extInfoList =
        JsonbSingleton.getInstance().getJsonb().fromJson(json, ExtInfoSearchResult.class);

    return extInfoList;
  }

  /**
   * Searches for Ext Infos. For all calls to search methods, additional rate limiting applies.
   * Currently this limit is set at 2 calls per 10 seconds. Please keep track of that limit
   * yourself.
   *
   * @param q Search keyword.
   * @param limit Number of returned search results.
   * @return List of {@link ExtInfo}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6319/api-search-ext-info.html">API: search/ext_info
   *      method</a>
   */
  public ExtInfoSearchResult getSearchExtInfo(String q, int limit)
      throws IOException, XrelException {
    Objects.requireNonNull(q, "q missing");

    return getSearchExtInfoPrivate(q, null, limit);
  }

  /**
   * Searches for Ext Infos. For all calls to search methods, additional rate limiting applies.
   * Currently this limit is set at 2 calls per 10 seconds. Please keep track of that limit
   * yourself.
   *
   * @param q Search keyword.
   * @param type One of: {@code movie}|{@code tv}|{@code game}|{@code console}|{@code
   software}|{@code xxx}
   * @param limit Number of returned search results.
   * @return List of {@link ExtInfo}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6319/api-search-ext-info.html">API: search/ext_info
   *      method</a>
   */
  public ExtInfoSearchResult getSearchExtInfo(String q, String type, int limit)
      throws IOException, XrelException {
    Objects.requireNonNull(q, "q missing");
    Objects.requireNonNull(type, "type missing");

    return getSearchExtInfoPrivate(q, type, limit);
  }

  /**
   * Returns a list of all the current user's favorite lists.
   *
   * @param token The {@link Token} with all needed info.
   * @return A list of all {@link Favorite}.
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/1754/api-favs-lists.html">API: favs/lists method</a>
   */
  public List<Favorite> getFavsLists(Token token) throws IOException, XrelException {
    Objects.requireNonNull(token, "token missing");

    String json =
        NetworkingHelper.readStringFromUrlGet(getXrelUrl() + "favs/lists" + getFormat(), token);
    List<Favorite> favoriteList =
        JsonbSingleton.getInstance().getJsonb().fromJson(json, new ArrayList<Favorite>() {
          private static final long serialVersionUID = 1L;
        }.getClass().getGenericSuperclass());

    return favoriteList;
  }

  /**
   * Retrieves entries of a favorite list and adds the retrieved entries to the {@link Favorite}
   * object you provided.
   *
   * @param favorite The favorite list, as obtained through {@link #getFavsLists(Token)}.
   * @param getReleases If {@code true}, a list of unread(!) releases will be returned with each
   *        ext_info entry.
   * @param token The {@link Token} with all needed info.
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/1823/api-favs-list-entries.html">API: favs/list_entries
   *      method</a>
   */
  public void getFavsListEntries(Favorite favorite, boolean getReleases, Token token)
      throws IOException, XrelException {
    Objects.requireNonNull(favorite, "favorite missing");
    Objects.requireNonNull(token, "token missing");

    String url = getXrelUrl() + "favs/list_entries" + getFormat() + "?id=" + favorite.getId()
        + "&get_releases=";
    if (getReleases) {
      url += "true";
    } else {
      url += "false";
    }
    String json = NetworkingHelper.readStringFromUrlGet(url, token);
    List<ExtInfo> extInfoList =
        JsonbSingleton.getInstance().getJsonb().fromJson(json, new ArrayList<ExtInfo>() {
          private static final long serialVersionUID = 1L;
        }.getClass().getGenericSuperclass());

    if (favorite.getEntries() != null) {
      favorite.getEntries().clear();
      favorite.getEntries().addAll(extInfoList);
    } else {
      favorite.setEntries(extInfoList);
    }
  }

  /**
   * Add or removes an Ext Info to / from a favorite list.
   *
   * @param favorite The favorite list, as obtained through {@link #getFavsLists(Token)}.
   * @param extInfo The {@link ExtInfo} to add or remove.
   * @param token The {@link Token} with all needed info.
   * @param delete {@code true} if {@link ExtInfo} should be removed or {@code false} if
   *        {@link ExtInfo} should be added.
   * @return The new {@link Favorite}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6316/api-favs-list-addentry.html">API:
   *      favs/list_addentry method</a><br>
   *      <a href= "https://www.xrel.to/wiki/6317/api-favs-list-delentry.html">API:
   *      favs/list_delentry method</a>
   */
  private Favorite postFavsListAddDelEntry(Favorite favorite, ExtInfo extInfo, Token token,
      boolean delete) throws IOException, XrelException {
    Objects.requireNonNull(favorite, "favorite missing");
    Objects.requireNonNull(extInfo, "extInfo missing");
    Objects.requireNonNull(token, "token missing");

    ArrayList<String> keyList = new ArrayList<String>();
    ArrayList<String> valueList = new ArrayList<String>();
    keyList.add("id");
    valueList.add(String.valueOf(favorite.getId()));
    keyList.add("ext_info_id");
    valueList.add(String.valueOf(extInfo.getId()));

    String url = getXrelUrl();
    if (delete) {
      url += "favs/list_delentry" + getFormat();
    } else {
      url += "favs/list_addentry" + getFormat();
    }

    String json = NetworkingHelper.readStringFromUrlPost(url, token, keyList, valueList);
    Favorite favoriteNew = JsonbSingleton.getInstance().getJsonb().fromJson(json, Favorite.class);
    return favoriteNew;
  }

  /**
   * Add an Ext Info to a favorite list.
   *
   * @param favorite The favorite list, as obtained through {@link #getFavsLists(Token)}.
   * @param extInfo The {@link ExtInfo} to add.
   * @param token The {@link Token} with all needed info.
   * @return The new {@link Favorite}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6316/api-favs-list-addentry.html">API:
   *      favs/list_addentry method</a>
   */
  public Favorite postFavsListAddEntry(Favorite favorite, ExtInfo extInfo, Token token)
      throws IOException, XrelException {
    Objects.requireNonNull(favorite, "favorite missing");
    Objects.requireNonNull(extInfo, "extInfo missing");
    Objects.requireNonNull(token, "token missing");

    return postFavsListAddDelEntry(favorite, extInfo, token, false);
  }

  /**
   * Removes an Ext Info from a favorite list.
   *
   * @param favorite The favorite list, as obtained through {@link #getFavsLists(Token)}.
   * @param extInfo The {@link ExtInfo} to remove.
   * @param token The {@link Token} with all needed info.
   * @return The new {@link Favorite}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6317/api-favs-list-delentry.html">API:
   *      favs/list_delentry method</a>
   */
  public Favorite postFavsListDelEntry(Favorite favorite, ExtInfo extInfo, Token token)
      throws IOException, XrelException {
    Objects.requireNonNull(favorite, "favorite missing");
    Objects.requireNonNull(extInfo, "extInfo missing");
    Objects.requireNonNull(token, "token missing");

    return postFavsListAddDelEntry(favorite, extInfo, token, true);
  }

  /**
   * Mark a {@link Release} or {@link P2pRelease} on a favorite list as read.
   *
   * @param favorite The favorite list, as obtained through {@link #getFavsLists(Token)}.
   * @param release The {@link Release} to be marked as read or {@code null}.
   * @param p2pRelease The {@link P2pRelease} to be marked as read or {@code null}.
   * @param token The {@link Token} with all needed info.
   * @return The new {@link Favorite}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6344/api-favs-list-markread.html">API:
   *      favs/list_markread method</a>
   */
  private Favorite postFavsListMarkRead(Favorite favorite, Release release, P2pRelease p2pRelease,
      Token token) throws IOException, XrelException {
    Objects.requireNonNull(favorite, "favorite missing");
    Objects.requireNonNull(token, "token missing");

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

    String json = NetworkingHelper.readStringFromUrlPost(
        getXrelUrl() + "favs/list_markread" + getFormat(), token, keyList, valueList);
    Favorite favoriteNew = JsonbSingleton.getInstance().getJsonb().fromJson(json, Favorite.class);
    return favoriteNew;
  }

  /**
   * Mark a {@link Release} on a favorite list as read.
   *
   * @param favorite The favorite list, as obtained through {@link #getFavsLists(Token)}.
   * @param release The {@link Release} to be marked as read.
   * @param token The {@link Token} with all needed info.
   * @return The new {@link Favorite}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6344/api-favs-list-markread.html">API:
   *      favs/list_markread method</a>
   */
  public Favorite postFavsListMarkRead(Favorite favorite, Release release, Token token)
      throws IOException, XrelException {
    Objects.requireNonNull(favorite, "favorite missing");
    Objects.requireNonNull(release, "release missing");
    Objects.requireNonNull(token, "token missing");

    return postFavsListMarkRead(favorite, release, null, token);
  }

  /**
   * Mark a {@link P2pRelease} on a favorite list as read.
   *
   * @param favorite The favorite list, as obtained through {@link #getFavsLists(Token)}.
   * @param p2pRelease The {@link P2pRelease} to be marked as read.
   * @param token The {@link Token} with all needed info.
   * @return The new {@link Favorite}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6344/api-favs-list-markread.html">API:
   *      favs/list_markread method</a>
   */
  public Favorite postFavsListMarkRead(Favorite favorite, P2pRelease p2pRelease, Token token)
      throws IOException, XrelException {
    Objects.requireNonNull(favorite, "favorite missing");
    Objects.requireNonNull(p2pRelease, "p2pRelease missing");
    Objects.requireNonNull(token, "token missing");

    return postFavsListMarkRead(favorite, null, p2pRelease, token);
  }

  /**
   * Returns comments for a given {@link Release} or {@link P2pRelease}.
   *
   * @param release The corresponding {@link Release} or {@code null}.
   * @param p2pRelease The corresponding {@link P2pRelease} or {@code null}.
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @return The {@link PaginationList} containing the {@link Comment}.
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6313/api-comments-get.html">API: comments/get
   *      method</a>
   */
  private PaginationList<Comment> getCommentsGet(Release release, P2pRelease p2pRelease,
      int perPage, int page) throws IOException, XrelException {
    int[] normalizedPageValues = normalizePageValues(perPage, page);

    String json;
    if (release != null) {
      json = NetworkingHelper.readStringFromUrlGet(getXrelUrl() + "comments/get" + getFormat()
          + "?id=" + release.getId() + "&type=release&per_page=" + normalizedPageValues[0]
          + "&page=" + normalizedPageValues[1]);
    } else {
      json = NetworkingHelper.readStringFromUrlGet(getXrelUrl() + "comments/get" + getFormat()
          + "?id=" + p2pRelease.getId() + "&type=p2p_rls&per_page=" + perPage + "&page=" + page);
    }

    PaginationList<Comment> commentList = JsonbSingleton.getInstance().getJsonb().fromJson(json,
        new PaginationList<Comment>() {}.getClass().getGenericSuperclass());
    return commentList;
  }

  /**
   * Returns comments for a given {@link Release}.
   *
   * @param release The corresponding {@link Release}.
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @return The {@link PaginationList} containing the {@link Comment}.
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6313/api-comments-get.html">API: comments/get
   *      method</a>
   */
  public PaginationList<Comment> getCommentsGet(Release release, int perPage, int page)
      throws IOException, XrelException {
    Objects.requireNonNull(release, "release missing");

    return getCommentsGet(release, null, perPage, page);
  }

  /**
   * Returns comments for a given {@link P2pRelease}.
   *
   * @param p2pRelease The corresponding {@link P2pRelease}.
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @return The {@link PaginationList} containing the {@link Comment}.
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6313/api-comments-get.html">API: comments/get
   *      method</a>
   */
  public PaginationList<Comment> getCommentsGet(P2pRelease p2pRelease, int perPage, int page)
      throws IOException, XrelException {
    Objects.requireNonNull(p2pRelease, "p2pRelease missing");

    return getCommentsGet(null, p2pRelease, perPage, page);
  }

  /**
   * Add a comment to a given {@link Release} or {@link P2pRelease}. The text may contain BBCode.
   * Supplying either a text, a rating (both audio and video) or both is mandatory.
   *
   * @param release The {@link Release} to add a comment to or {@code null}.
   * @param p2pRelease The {@link P2pRelease} to add a comment to or {@code null}.
   * @param text The comment. You may use BBCode to format the text. Can be {@code null} if both
   *        videoRating and audioRating are set.
   * @param videoRating Video rating between 1 (bad) to 10 (good). You must always rate both or
   *        none. You may only vote once, and may not change your vote. Check the vote property from
   *        the response to get the rating as displayed on the website. Use 0 if no rating.
   * @param audioRating Audio rating between 1 (bad) to 10 (good). You must always rate both or
   *        none. You may only vote once, and may not change your vote. Check the vote property from
   *        the response to get the rating as displayed on the website. Use 0 if no rating.
   * @param token The {@link Token} with all needed info.
   * @return The added {@link Comment}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6312/api-comments-add.html">API: comments/add
   *      method</a>
   */
  private Comment postCommentsAdd(Release release, P2pRelease p2pRelease, String text,
      int videoRating, int audioRating, Token token) throws IOException, XrelException {
    Objects.requireNonNull(token, "token missing");

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

    String json = NetworkingHelper.readStringFromUrlPost(
        getXrelUrl() + "comments/add" + getFormat(), token, keyList, valueList);
    Comment comment = JsonbSingleton.getInstance().getJsonb().fromJson(json, Comment.class);

    return comment;
  }

  /**
   * Add a comment to a given {@link Release}. The text may contain BBCode. Supplying either a text,
   * a rating (both audio and video) or both is mandatory.
   *
   * @param release The {@link Release} to add a comment to.
   * @param text The comment. You may use BBCode to format the text. Can be {@code null} if both
   *        videoRating and audioRating are set.
   * @param videoRating Video rating between 1 (bad) to 10 (good). You must always rate both or
   *        none. You may only vote once, and may not change your vote. Check the vote property from
   *        the response to get the rating as displayed on the website. Use 0 if no rating.
   * @param audioRating Audio rating between 1 (bad) to 10 (good). You must always rate both or
   *        none. You may only vote once, and may not change your vote. Check the vote property from
   *        the response to get the rating as displayed on the website. Use 0 if no rating.
   * @param token The {@link Token} with all needed info.
   * @return The added {@link Comment}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6312/api-comments-add.html">API: comments/add
   *      method</a>
   */
  public Comment postCommentsAdd(Release release, String text, int videoRating, int audioRating,
      Token token) throws IOException, XrelException {
    Objects.requireNonNull(release, "release missing");
    Objects.requireNonNull(token, "token missing");

    return postCommentsAdd(release, null, text, videoRating, audioRating, token);
  }

  /**
   * Add a comment to a given {@link P2pRelease}. The text may contain BBCode.
   *
   *
   * @param p2pRelease The {@link P2pRelease} to add a comment to.
   * @param text The comment. You may use BBCode to format the text. Can be {@code null} if both
   *        videoRating and audioRating are set.
   * @param videoRating Video rating between 1 (bad) to 10 (good). You must always rate both or
   *        none. You may only vote once, and may not change your vote. Check the vote property from
   *        the response to get the rating as displayed on the website. Use 0 if no rating.
   * @param audioRating Audio rating between 1 (bad) to 10 (good). You must always rate both or
   *        none. You may only vote once, and may not change your vote. Check the vote property from
   *        the response to get the rating as displayed on the website. Use 0 if no rating.
   * @param token The {@link Token} with all needed info
   * @return The added {@link Comment}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6312/api-comments-add.html">API: comments/add
   *      method</a>
   */
  public Comment postCommentsAdd(P2pRelease p2pRelease, String text, int videoRating,
      int audioRating, Token token) throws IOException, XrelException {
    Objects.requireNonNull(p2pRelease, "p2pRelease missing");
    Objects.requireNonNull(token, "token missing");

    return postCommentsAdd(null, p2pRelease, text, videoRating, audioRating, token);
  }

  /**
   * Returns information about the active user.
   *
   * @param token The token used for authentication
   * @return The {@link User}
   * @throws IOException If there is a networking problem
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6441/api-user-info.html">API: user/info method</a>
   */
  public User getUserInfo(Token token) throws IOException, XrelException {
    Objects.requireNonNull(token, "token missing");

    String json =
        NetworkingHelper.readStringFromUrlGet(getXrelUrl() + "user/info" + getFormat(), token);
    User user = JsonbSingleton.getInstance().getJsonb().fromJson(json, User.class);

    return user;
  }

  /**
   * This is the endpoint for the first step of the OAuth 2.0 authentication flow. This basically
   * returns the URL you have to point the user to with all the information you provided while
   * constructing the {@link Xrel} object.
   * 
   * @return The url you point the user to
   * @see <a href="https://www.xrel.to/wiki/6436/api-oauth2.html">API: OAuth 2.0</a>
   */
  public String getOauth2Auth() {
    String url = getXrelUrl() + "oauth2/auth?response_type=" + getResponseType() + "&client_id="
        + getClientId().get();
    if (getRedirectUri().isPresent()) {
      url += "&redirect_uri=" + getRedirectUri().get();
    }
    if (getState().isPresent()) {
      url += "&state=" + getState();
    }
    if (getScope().isPresent() && getScope().get().length > 0) {
      url += "&scope=";
      boolean first = true;
      for (int i = 0; i < getScope().get().length; i++) {
        if (!first) {
          url += "%20";
        } else {
          first = false;
        }
        url += getScope().get()[i];
      }
    }
    return url;
  }

  /**
   * This is the endpoint for the last step of the OAuth 2.0 authentication flow.
   * 
   * @param grantType {@code authorization_code} for User authentication, {@code client_credentials}
   *        for Application authentication, {@code refresh_token} for refreshing an access token
   * @param code When performing the {@code authorization_code} grant, you must specify the code
   *        provided from {@code #getOauth2Auth()}
   * @param token The {@link Token} with all needed info if performing {@code refresh_token} or
   *        {@code null} otherwise
   * @return The new {@link Token}
   * @throws XrelException If there is an error returned by the xREL API
   * @throws IOException If there is a networking problem
   * @see <a href="https://www.xrel.to/wiki/6436/api-oauth2.html">API: OAuth 2.0</a>
   */
  private Token postOauth2TokenPrivate(String grantType, String code, Token token)
      throws XrelException, IOException {
    Objects.requireNonNull(grantType, "grantType missing");

    if (grantType != "authorization_code" && grantType != "client_credentials"
        && grantType != "refresh_token") {
      throw new XrelException("Invalid grant_type");
    }

    String unsetParameters = "Needed parameters not set:";
    boolean error = false;
    if (!getClientId().isPresent()) {
      error = true;
      unsetParameters += " client_id";
    }
    if (!getClientSecret().isPresent()) {
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
    valueList.add(getClientId().get());
    keyList.add("client_secret");
    valueList.add(getClientSecret().get());

    if (grantType == "authorization_code") {
      keyList.add("code");
      valueList.add(code);
    }
    if (grantType == "refresh_token") {
      keyList.add("refresh_token");
      valueList.add(token.getRefreshToken());
    }
    if (grantType != "refresh_token" && getRedirectUri().isPresent()) {
      keyList.add("redirect_uri");
      valueList.add(getRedirectUri().get());
    }
    if (getScope().isPresent() && getScope().get().length > 0) {
      keyList.add("scope");
      String scopeValues = "";
      boolean first = true;
      for (int i = 0; i < getScope().get().length; i++) {
        if (!first) {
          scopeValues += " ";
        } else {
          first = false;
        }
        scopeValues += getScope().get()[i];
      }
      valueList.add(scopeValues);
    }

    String tokenJsonString = NetworkingHelper
        .readStringFromUrlPost(getXrelUrl() + "oauth2/token" + getFormat(), keyList, valueList);
    Token newToken = JsonbSingleton.getInstance().getJsonb().fromJson(tokenJsonString, Token.class);
    return newToken;
  }

  /**
   * This is the endpoint for the last step of the OAuth 2.0 authentication flow.
   * 
   * @param grantType {@code authorization_code} for User authentication, {@code client_credentials}
   *        for Application authentication, {@code refresh_token} for refreshing an access token
   * @param token The {@link Token} with all needed info if performing {@code refresh_token}
   * @return The new {@link Token}
   * @throws XrelException If there is an error returned by the xREL API
   * @throws IOException If there is a networking problem
   * @see <a href="https://www.xrel.to/wiki/6436/api-oauth2.html">API: OAuth 2.0</a>
   */
  public Token postOauth2Token(String grantType, Token token) throws XrelException, IOException {
    Objects.requireNonNull(grantType, "grantType missing");
    Objects.requireNonNull(token, "token missing");

    return postOauth2TokenPrivate(grantType, null, token);
  }

  /**
   * This is the endpoint for the last step of the OAuth 2.0 authentication flow.
   * 
   * @param grantType {@code authorization_code} for User authentication, {@code client_credentials}
   *        for Application authentication, {@code refresh_token} for refreshing an access token
   * @param code When performing the {@code authorization_code} grant, you must specify the code
   *        provided from {@code #getOauth2Auth()}
   * @return The new {@link Token}
   * @throws XrelException If there is an error returned by the xREL API
   * @throws IOException If there is a networking problem
   * @see <a href="https://www.xrel.to/wiki/6436/api-oauth2.html">API: OAuth 2.0</a>
   */
  public Token postOauth2Token(String grantType, String code) throws XrelException, IOException {
    Objects.requireNonNull(grantType, "grantType missing");
    Objects.requireNonNull(code, "code missing");

    return postOauth2TokenPrivate(grantType, code, null);
  }

}
