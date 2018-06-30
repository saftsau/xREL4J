/*
 * Copyright 2017, 2018 saftsau
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
import com.github.saftsau.xrel4j.favorite.Favorite;
import com.github.saftsau.xrel4j.favorite.FavoriteAddDelEntry;
import com.github.saftsau.xrel4j.favorite.FavoriteMarkRead;
import com.github.saftsau.xrel4j.release.ReleaseSearchResult;
import com.github.saftsau.xrel4j.release.p2p.P2pCategory;
import com.github.saftsau.xrel4j.release.p2p.P2pGroup;
import com.github.saftsau.xrel4j.release.p2p.P2pRelease;
import com.github.saftsau.xrel4j.release.scene.Release;
import com.github.saftsau.xrel4j.release.scene.ReleaseAddProof;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Java implementation of the xREL API v2. Method and parameter names are based on the xREL API with
 * "_" replaced by camel case. Order of methods and parameters are based on the xREL API
 * documentation. It is STRONGLY encouraged to read the xREL API documentation!
 * 
 * @see <a href="https://www.xrel.to/wiki/1681/API.html">API Overview</a>
 */
public class Xrel {

  private final String xrelUrl = "https://api.xrel.to/v2";
  private final MediaType format;
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
   * @param mediaType The {@link MediaType} to use. Must be either application/json or text/xml.
   * @throws UnsupportedEncodingException If the given encoding is neither application/json nor
   *         application/xml.
   * 
   * @see <a href="https://www.xrel.to/wiki/6436/api-oauth2.html">API: OAuth 2.0</a>
   */
  public Xrel(MediaType mediaType) throws UnsupportedEncodingException {
    if (mediaType.equals(MediaType.APPLICATION_JSON_TYPE)
        || mediaType.equals(MediaType.TEXT_XML_TYPE)) {
      this.format = mediaType;
    } else {
      throw new UnsupportedEncodingException("Encoding must be application/json or text/xml");
    }
    this.clientId = Optional.empty();
  }


  /**
   * Constructs a new xREL object with oAuth information and no scopes.
   * 
   * @param mediaType The {@link MediaType} to use. Must be either application/json or text/xml.
   * @param clientId Your consumer key.
   * @param clientSecret Your consumer secret.
   * @throws UnsupportedEncodingException If the given encoding is neither application/json nor
   *         application/xml.
   * @see <a href="https://www.xrel.to/wiki/6436/api-oauth2.html">API: OAuth 2.0</a>
   */
  public Xrel(MediaType mediaType, String clientId, String clientSecret)
      throws UnsupportedEncodingException {
    if (mediaType.equals(MediaType.APPLICATION_JSON_TYPE)
        || mediaType.equals(MediaType.TEXT_XML_TYPE)) {
      this.format = mediaType;
    } else {
      throw new UnsupportedEncodingException("Encoding must be application/json or text/xml");
    }
    this.clientId = Optional.of(clientId);
    this.clientSecret = Optional.of(clientSecret);
    this.redirectUri = Optional.empty();
    this.state = Optional.empty();
    this.scope = Optional.empty();
  }

  /**
   * Constructs a new xREL object with oAuth information and no scopes.
   * 
   * @param mediaType The {@link MediaType} to use. Must be either application/json or text/xml.
   * @param clientId Your consumer key.
   * @param clientSecret Your consumer secret.
   * @param redirectUri Optional URI to redirect to after the authentication. Please read the Guide
   *        for more details.
   * @param state Optionally any string. You may set this value to any value, and it will be
   *        returned after the authentication. It might also be useful to prevent CSRF attacks.
   * @throws UnsupportedEncodingException If the given encoding is neither application/json nor
   *         application/xml.
   * @see <a href="https://www.xrel.to/wiki/6436/api-oauth2.html">API: OAuth 2.0</a>
   */
  public Xrel(MediaType mediaType, String clientId, String clientSecret,
      Optional<String> redirectUri, Optional<String> state) throws UnsupportedEncodingException {
    if (mediaType.equals(MediaType.APPLICATION_JSON_TYPE)
        || mediaType.equals(MediaType.TEXT_XML_TYPE)) {
      this.format = mediaType;
    } else {
      throw new UnsupportedEncodingException("Encoding must be application/json or text/xml");
    }
    this.clientId = Optional.of(clientId);
    this.clientSecret = Optional.of(clientSecret);
    this.redirectUri = redirectUri;
    this.state = state;
    this.scope = Optional.empty();
  }

  /**
   * Constructs a new xREL object. If you have oAuth access but no additional scopes you should use
   * {@link #Xrel(MediaType, String, String, Optional, Optional)}.
   * 
   * @param mediaType The {@link MediaType} to use. Must be either application/json or text/xml.
   * @param clientId Your consumer key.
   * @param clientSecret Your consumer secret.
   * @param scope Needed to access protected methods. If you do have scope access: you MUST supply
   *        these while processing the Tokens, even if you only plan to use them at a later stage.
   *        Rule of thumb: if you have these, always add them here.
   * @throws UnsupportedEncodingException If the given encoding is neither application/json nor
   *         application/xml.
   * @see <a href="https://www.xrel.to/wiki/6436/api-oauth2.html">API: OAuth 2.0</a>
   */
  public Xrel(MediaType mediaType, String clientId, String clientSecret, String[] scope)
      throws UnsupportedEncodingException {
    if (mediaType.equals(MediaType.APPLICATION_JSON_TYPE)
        || mediaType.equals(MediaType.TEXT_XML_TYPE)) {
      this.format = mediaType;
    } else {
      throw new UnsupportedEncodingException("Encoding must be application/json or text/xml");
    }
    this.clientId = Optional.of(clientId);
    this.clientSecret = Optional.of(clientSecret);
    this.redirectUri = Optional.empty();
    this.state = Optional.empty();
    this.scope = Optional.of(scope);
  }

  /**
   * Constructs a new xREL object. If you have oAuth access but no additional scopes you should use
   * {@link #Xrel(MediaType, String, String, Optional, Optional)}.
   * 
   * @param mediaType The {@link MediaType} to use. Must be either application/json or text/xml.
   * @param clientId Your consumer key.
   * @param clientSecret Your consumer secret.
   * @param redirectUri Optional URI to redirect to after the authentication. Please read the Guide
   *        for more details.
   * @param state Optionally any string. You may set this value to any value, and it will be
   *        returned after the authentication. It might also be useful to prevent CSRF attacks.
   * @param scope Needed to access protected methods. If you do have scope access: you MUST supply
   *        these while processing the Tokens, even if you only plan to use them at a later stage.
   *        Rule of thumb: if you have these, always add them here.
   * @throws UnsupportedEncodingException If the given encoding is neither application/json nor
   *         application/xml.
   * @see <a href="https://www.xrel.to/wiki/6436/api-oauth2.html">API: OAuth 2.0</a>
   */
  public Xrel(MediaType mediaType, String clientId, String clientSecret,
      Optional<String> redirectUri, Optional<String> state, String[] scope)
      throws UnsupportedEncodingException {
    if (mediaType.equals(MediaType.APPLICATION_JSON_TYPE)
        || mediaType.equals(MediaType.TEXT_XML_TYPE)) {
      this.format = mediaType;
    } else {
      throw new UnsupportedEncodingException("Encoding must be application/json or text/xml");
    }
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
  private MediaType getFormat() {
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
   * Handles a {@link Response} by the API. This is used, so custom error handling can be
   * implemented.
   * 
   * @param <T> The type of the returned object you normally expect to get from the API
   * @param genericType The {@link GenericType} the {@link Response} should normally wrap
   * @param response The given {@link Response}
   * @return The read object or {@code null} if the response is not expected to return anything
   *         (201, 204, ...)
   * @throws XrelException If there is an error returned by the xREL API
   */
  private <T> T handleResponse(GenericType<T> genericType, Response response) throws XrelException {
    if (response.getStatus() == Status.OK.getStatusCode()) {
      // If we have an OK response, we can just read it
      return response.readEntity(genericType);
    } else if (response.getStatus() == Status.ACCEPTED.getStatusCode()
        || response.getStatus() == Status.NO_CONTENT.getStatusCode()
        || response.getStatus() == Status.CREATED.getStatusCode()) {
      // Some codes are OK but have no content
      return null;
    } else {
      // Otherwise we have an error
      if (response.hasEntity()) {
        // If we have some content, this should be an xREL API error object
        try {
          Error error = response.readEntity(Error.class);
          throw new XrelException(error.getErrorDescription(), error, response.getStatus());
        } catch (ProcessingException e) {
          // If we can't unmarshal to an Error object we can only use the HTTP status code and the
          // ProcessingException
          // Generally this should not happen and if it does it is an error in the xREL API
          throw new XrelException(e, response.getStatus());
        }
      } else {
        // If we we have no content we can only use the HTTP status code
        throw new XrelException(response.getStatus());
      }
    }
  }

  /**
   * Gets the maximum number of requests that the consumer is permitted to make per hour as returned
   * by the last request. -1 if not yet set.
   * 
   * @return The X-RateLimit-Limit
   * @see <a href="https://www.xrel.to/wiki/2727/api-rate-limiting.html">API: Rate Limiting</a>
   */
  public int getXRateLimitLimit() {
    return RateLimitFilter.getXRateLimitLimit();
  }

  /**
   * Gets the number of requests remaining in the current rate limit window as returned by the last
   * request. -1 if not yet set.
   * 
   * @return The X-RateLimit-Remaining
   * @see <a href="https://www.xrel.to/wiki/2727/api-rate-limiting.html">API: Rate Limiting</a>
   */
  public int getXRateLimitRemaining() {
    return RateLimitFilter.getXRateLimitRemaining();
  }

  /**
   * Gets the time at which the current rate limit window resets in UTC epoch seconds as returned by
   * the last request. -1 if not yet set.
   * 
   * @return The X-RateLimit-Reset
   * @see <a href="https://www.xrel.to/wiki/2727/api-rate-limiting.html">API: Rate Limiting</a>
   */
  public int getXRateLimitReset() {
    return RateLimitFilter.getXRateLimitReset();
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
   * Creates the base {@link WebTarget} to be used for every request on the xREL API. This already
   * sets the complete URL with the specified path and the format to be used for the request.
   * 
   * @param path The path to use
   * @return The constructed {@link WebTarget}
   */
  private WebTarget createBaseWebTarget(String path) {
    final Client client = ClientBuilder.newClient();
    client.register(RateLimitFilter.class);
    client.register(CompressionHelper.class);
    WebTarget webTarget = client.target(getXrelUrl());
    return webTarget.path(path + "." + getFormat().getSubtype());
  }

  /**
   * Changes the name of an {@link XmlRootElement} annotation of a given class. This is used,
   * because in XML we have root elements that we need to map, while in JSON this is not present.
   * This is used as a workaround and uses Java internals that can possibly break on any new Java
   * version.
   * 
   * @param clazz The class to alter
   * @param name The name of the root element
   * @throws XrelException
   */
  private void changeXmlRootName(@SuppressWarnings("rawtypes") Class clazz, String name)
      throws XrelException {
    // If we are not using XML we do not have to alter anything
    if (!getFormat().equals(MediaType.TEXT_XML_TYPE)) {
      return;
    }

    // Get the already present XmlRootElement annotation
    Annotation annotation = null;
    for (Annotation currentAnnotation : clazz.getAnnotations()) {
      if (currentAnnotation.annotationType().isAssignableFrom(XmlRootElement.class)) {
        annotation = currentAnnotation;
        break;
      }
    }
    if (annotation == null) {
      throw new IllegalArgumentException("Given class has no XmlRootElement");
    }

    // Construct the new Annotation
    final XmlRootElement xmlRootElement = (XmlRootElement) annotation;
    Annotation newAnnotation = new XmlRootElement() {
      @Override
      public Class<? extends Annotation> annotationType() {
        return xmlRootElement.annotationType();
      }

      @Override
      public String namespace() {
        return xmlRootElement.namespace();
      }

      @Override
      public String name() {
        return name;
      }
    };

    try {
      // Reflection to get the private method
      Method method = Class.class.getDeclaredMethod("annotationData");
      method.setAccessible(true);
      Object annotationData = method.invoke(clazz);

      // Get the present annotations
      Field annotations = annotationData.getClass().getDeclaredField("annotations");
      annotations.setAccessible(true);
      @SuppressWarnings("unchecked")
      Map<Class<? extends Annotation>, Annotation> annotationDataMap =
          (Map<Class<? extends Annotation>, Annotation>) annotations.get(annotationData);

      // Set the new annotation
      annotationDataMap.put(XmlRootElement.class, newAnnotation);
    } catch (Exception e) {
      throw new XrelException(e);
    }
  }

  /**
   * Returns information about a single release, specified by the complete dirname or an API release
   * id.
   * 
   * @param idDir Dirname or API id of the release
   * @param useId If {@code true} uses the idDir as an API ID for the request, if {@code false} uses
   *        the idDir as a dirname for the request
   * @return The corresponding {@link Release}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/1680/api-release-info.html">API: release/info method</a>
   */
  private Release getReleaseInfo(String idDir, boolean useId) throws XrelException {
    Objects.requireNonNull(idDir, "idDir missing");

    WebTarget webTarget = createBaseWebTarget("release/info");
    if (useId) {
      webTarget = webTarget.queryParam("id", idDir);
    } else {
      webTarget = webTarget.queryParam("dirname", idDir);
    }
    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    Response response = invocationBuilder.get();
    Release release = handleResponse(new GenericType<Release>() {}, response);

    return release;
  }

  /**
   * Returns information about a single release, specified by the complete dirname.
   * 
   * @param dir Dirname of the release
   * @return The corresponding {@link Release}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/1680/api-release-info.html">API: release/info method</a>
   */
  public Release getReleaseInfoDir(String dir) throws XrelException {
    Objects.requireNonNull(dir, "dir missing");

    return getReleaseInfo(dir, false);
  }

  /**
   * Returns information about a single release, specified by the API release id.
   * 
   * @param id API id of the release
   * @return The corresponding {@link Release}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/1680/api-release-info.html">API: release/info method</a>
   */
  public Release getReleaseInfoId(String id) throws XrelException {
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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/2994/api-release-latest.html">API: release/latest
   *      method</a>
   */
  public PaginationList<Release> getReleaseLatestPrivate(String archive, int perPage, int page,
      Filter filter, Token token) throws XrelException {
    int[] normalizedPageValues = normalizePageValues(perPage, page);

    WebTarget webTarget = createBaseWebTarget("release/latest");
    webTarget = webTarget.queryParam("per_page", normalizedPageValues[0]);
    webTarget = webTarget.queryParam("page", normalizedPageValues[1]);
    if (archive != null) {
      webTarget = webTarget.queryParam("archive", archive);
    }
    if (filter != null && token == null) {
      webTarget = webTarget.queryParam("filter", filter.getId());
    }
    if (filter == null && token != null) {
      webTarget = webTarget.queryParam("filter", "overview");
    }

    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    if (token != null) {
      invocationBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken());
    }

    Response response = invocationBuilder.get();
    changeXmlRootName(PaginationList.class, "releases");
    PaginationList<Release> releaseList =
        handleResponse(new GenericType<PaginationList<Release>>() {}, response);
    changeXmlRootName(PaginationList.class, "##default");

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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/2994/api-release-latest.html">API: release/latest
   *      method</a>
   */
  public PaginationList<Release> getReleaseLatest(int perPage, int page) throws XrelException {
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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/2994/api-release-latest.html">API: release/latest
   *      method</a>
   */
  public PaginationList<Release> getReleaseLatest(String archive, int perPage, int page)
      throws XrelException {
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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/2994/api-release-latest.html">API: release/latest
   *      method</a>
   */
  public PaginationList<Release> getReleaseLatest(int perPage, int page, Filter filter)
      throws XrelException {
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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/2994/api-release-latest.html">API: release/latest
   *      method</a>
   */
  public PaginationList<Release> getReleaseLatest(String archive, int perPage, int page,
      Filter filter) throws XrelException {
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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/2994/api-release-latest.html">API: release/latest
   *      method</a>
   */
  public PaginationList<Release> getReleaseLatest(int perPage, int page, Token token)
      throws XrelException {
    Objects.requireNonNull(token, "token missing");

    return getReleaseLatestPrivate(null, perPage, page, null, token);
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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/2994/api-release-latest.html">API: release/latest
   *      method</a>
   */
  public PaginationList<Release> getReleaseLatest(String archive, int perPage, int page,
      Token token) throws XrelException {
    Objects.requireNonNull(archive, "archive missing");
    Objects.requireNonNull(token, "token missing");

    return getReleaseLatestPrivate(archive, perPage, page, null, token);
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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/2994/api-release-latest.html">API: release/latest
   *      method</a>
   */
  public PaginationList<Release> getReleaseLatest(String archive, int perPage, int page,
      Filter filter, Token token) throws XrelException {
    Objects.requireNonNull(archive, "archive missing");
    Objects.requireNonNull(filter, "filter missing");
    Objects.requireNonNull(token, "token missing");

    return getReleaseLatestPrivate(archive, perPage, page, filter, token);
  }

  /**
   * Returns a list of available release categories. You can use the category name in
   * {@link #getReleaseBrowseCategory(ReleaseCategory, String, int, int)}. You should avoid calling
   * this method repeatedly and cache its result for at least 24 hours (where possible).
   *
   * @return The set of {@link ReleaseCategory}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href="https://www.xrel.to/wiki/6318/api-release-categories.html">API:
   *      release/categories method</a>
   */
  public Set<ReleaseCategory> getReleaseCategories() throws XrelException {
    WebTarget webTarget = createBaseWebTarget("release/categories");
    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    Response response = invocationBuilder.get();
    Set<ReleaseCategory> categorySet =
        handleResponse(new GenericType<Set<ReleaseCategory>>() {}, response);

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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3751/api-release-browse-category.html">API:
   *      release/browse_category method</a>
   */
  private PaginationList<Release> getReleaseBrowseCategoryPrivate(ReleaseCategory category,
      String extInfoType, int perPage, int page) throws XrelException {
    int[] normalizedPageValues = normalizePageValues(perPage, page);

    WebTarget webTarget = createBaseWebTarget("release/browse_category");
    webTarget = webTarget.queryParam("category_name", category.getName());

    if (extInfoType != null) {
      webTarget = webTarget.queryParam("ext_info_type", extInfoType);
    }
    webTarget = webTarget.queryParam("per_page", normalizedPageValues[0]);
    webTarget = webTarget.queryParam("page", normalizedPageValues[1]);

    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    Response response = invocationBuilder.get();
    changeXmlRootName(PaginationList.class, "releases");
    PaginationList<Release> releaseList =
        handleResponse(new GenericType<PaginationList<Release>>() {}, response);
    changeXmlRootName(PaginationList.class, "##default");

    return releaseList;
  }

  /**
   * Returns scene releases from the given category.
   *
   * @param category Category from {@link #getReleaseCategories()}
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @return The list of {@link Release} matching the criteria
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3751/api-release-browse-category.html">API:
   *      release/browse_category method</a>
   */
  public PaginationList<Release> getReleaseBrowseCategory(ReleaseCategory category, int perPage,
      int page) throws XrelException {
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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3751/api-release-browse-category.html">API:
   *      release/browse_category method</a>
   */
  public PaginationList<Release> getReleaseBrowseCategory(ReleaseCategory category,
      String extInfoType, int perPage, int page) throws XrelException {
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
   * @throws XrelException If there is an error returned by the xREL API
   */
  public PaginationList<Release> getReleaseExtInfo(ExtInfo extInfo, int perPage, int page)
      throws XrelException {
    Objects.requireNonNull(extInfo, "extInfo missing");

    int[] normalizedPageValues = normalizePageValues(perPage, page);

    WebTarget webTarget = createBaseWebTarget("release/ext_info");
    webTarget = webTarget.queryParam("id", extInfo.getId());
    webTarget = webTarget.queryParam("per_page", normalizedPageValues[0]);
    webTarget = webTarget.queryParam("page", normalizedPageValues[1]);

    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    Response response = invocationBuilder.get();
    changeXmlRootName(PaginationList.class, "releases");
    PaginationList<Release> releaseList =
        handleResponse(new GenericType<PaginationList<Release>>() {}, response);
    changeXmlRootName(PaginationList.class, "##default");

    return releaseList;
  }

  /**
   * Returns a set of public, predefined release filters. You can use the filter ID in
   * {@code #getReleaseLatest(Optional, int, int, Optional, Optional)}. You should avoid calling
   * this method repeatedly and cache its result for at least 24 hours (where possible).
   *
   * @return The set of {@link Filter}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/2996/api-release-filters.html">API: release/filters
   *      method</a>
   */
  public Set<Filter> getReleaseFilters() throws XrelException {
    WebTarget webTarget = createBaseWebTarget("release/filters");
    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    Response response = invocationBuilder.get();
    Set<Filter> filterSet = handleResponse(new GenericType<Set<Filter>>() {}, response);

    return filterSet;
  }

  /**
   * Adds a proof picture to a given API release id. This method requires the addproof scope. Please
   * read the rules before posting proofs.
   *
   * @param releaseList The list of releases this proof should be added to.
   * @param image Base64 encoded image
   * @param token The {@link Token} with all needed info
   * @return The {@link ReleaseAddProof} containing the URL of the proof and the {@link List} of
   *         {@link Release}. Careful: every {@link Release} only contains an ID.
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6444/api-release-addproof.html">API: release/addproof
   *      method</a>
   */
  public ReleaseAddProof postReleaseAddProof(List<Release> releaseList, String image, Token token)
      throws XrelException {
    Objects.requireNonNull(releaseList, "releaseList missing");
    Objects.requireNonNull(image, "image missing");
    Objects.requireNonNull(token, "token missing");

    if (!getScope().isPresent() || !checkScope("addproof")) {
      throw new XrelException("addproof scope not provided");
    }

    Form form = new Form();
    if (releaseList.size() == 1) {
      form.param("id", releaseList.get(0).getId());
    } else {
      for (Release release : releaseList) {
        form.param("id[]", release.getId());
      }
    }
    Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE);

    WebTarget webTarget = createBaseWebTarget("release/addproof");
    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    invocationBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken());
    Response response = invocationBuilder.post(entity);
    ReleaseAddProof releaseAddProof =
        handleResponse(new GenericType<ReleaseAddProof>() {}, response);

    return releaseAddProof;
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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3699/api-p2p-releases.html">API: p2p/releases</a>
   */
  private PaginationList<P2pRelease> getP2pReleasesPrivate(int perPage, int page,
      P2pCategory p2pCategory, P2pGroup p2pGroup, ExtInfo extInfo) throws XrelException {
    int[] normalizedPageValues = normalizePageValues(perPage, page);

    WebTarget webTarget = createBaseWebTarget("p2p/releases");
    webTarget = webTarget.queryParam("per_page", normalizedPageValues[0]);
    webTarget = webTarget.queryParam("page", normalizedPageValues[1]);

    if (p2pCategory != null) {
      webTarget = webTarget.queryParam("category_id", p2pCategory.getId());
    }
    if (p2pGroup != null) {
      webTarget = webTarget.queryParam("group_id", p2pGroup.getId());
    }
    if (extInfo != null) {
      webTarget = webTarget.queryParam("ext_info_id", extInfo.getId());
    }

    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    Response response = invocationBuilder.get();
    changeXmlRootName(PaginationList.class, "p2p_releases");
    PaginationList<P2pRelease> p2pList =
        handleResponse(new GenericType<PaginationList<P2pRelease>>() {}, response);
    changeXmlRootName(PaginationList.class, "##default");

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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3699/api-p2p-releases.html">API: p2p/releases</a>
   */
  public PaginationList<P2pRelease> getP2pReleases(int perPage, int page) throws XrelException {
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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3699/api-p2p-releases.html">API: p2p/releases</a>
   */
  public PaginationList<P2pRelease> getP2pReleases(int perPage, int page, P2pCategory p2pCategory)
      throws XrelException {
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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3699/api-p2p-releases.html">API: p2p/releases</a>
   */
  public PaginationList<P2pRelease> getP2pReleases(int perPage, int page, P2pCategory p2pCategory,
      P2pGroup p2pGroup) throws XrelException {
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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3699/api-p2p-releases.html">API: p2p/releases</a>
   */
  public PaginationList<P2pRelease> getP2pReleases(int perPage, int page, P2pGroup p2pGroup)
      throws XrelException {
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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3699/api-p2p-releases.html">API: p2p/releases</a>
   */
  public PaginationList<P2pRelease> getP2pReleases(int perPage, int page, P2pGroup p2pGroup,
      ExtInfo extInfo) throws XrelException {
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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3699/api-p2p-releases.html">API: p2p/releases</a>
   */
  public PaginationList<P2pRelease> getP2pReleases(int perPage, int page, ExtInfo extInfo)
      throws XrelException {
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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3699/api-p2p-releases.html">API: p2p/releases</a>
   */
  public PaginationList<P2pRelease> getP2pReleases(int perPage, int page, P2pCategory p2pCategory,
      P2pGroup p2pGroup, ExtInfo extInfo) throws XrelException {
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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3698/api-p2p-categories.html">API: p2p/categories</a>
   */
  public Set<P2pCategory> getP2pCategories() throws XrelException {
    WebTarget webTarget = createBaseWebTarget("p2p/categories");
    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    Response response = invocationBuilder.get();
    Set<P2pCategory> p2pSet = handleResponse(new GenericType<Set<P2pCategory>>() {}, response);

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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3697/api-p2p-rls-info.html">API: p2p/rls_info</a>
   */
  private P2pRelease getP2pRlsInfo(String idDir, boolean useId) throws XrelException {
    Objects.requireNonNull(idDir, "idDir missing");

    WebTarget webTarget = createBaseWebTarget("p2p/rls_info");
    if (useId) {
      webTarget = webTarget.queryParam("id", idDir);
    } else {
      webTarget = webTarget.queryParam("dirname", idDir);
    }

    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    Response response = invocationBuilder.get();
    P2pRelease p2pRelease = handleResponse(new GenericType<P2pRelease>() {}, response);

    return p2pRelease;
  }

  /**
   * Returns information about a single P2P/non-scene release, specified by the complete dirname.
   *
   * @param dir The dirname of the P2P release
   * @return The {@link P2pRelease}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3697/api-p2p-rls-info.html">API: p2p/rls_info</a>
   */
  public P2pRelease getP2pRlsInfoDir(String dir) throws XrelException {
    Objects.requireNonNull(dir, "dir missing");

    return getP2pRlsInfo(dir, false);
  }

  /**
   * Returns information about a single P2P/non-scene release, specified by the API P2P release id.
   *
   * @param id The API P2P release id
   * @return The {@link P2pRelease}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/3697/api-p2p-rls-info.html">API: p2p/rls_info</a>
   */
  public P2pRelease getP2pRlsInfoId(String id) throws XrelException {
    Objects.requireNonNull(id, "id missing");

    return getP2pRlsInfo(id, true);
  }

  /**
   * Returns an image of a NFO file for a given API release.
   *
   * @param release The {@link Release} you want the NFO of
   * @param token The {@link Token} with all needed info
   * @return The NFO as byte[]
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6438/api-nfo-release.html">API: nfo/release method</a>
   */
  public byte[] getNfoRelease(Release release, Token token) throws XrelException {
    Objects.requireNonNull(release, "release missing");
    Objects.requireNonNull(token, "token missing");

    if (!getScope().isPresent() || !checkScope("viewnfo")) {
      throw new XrelException("viewnfo scope not provided");
    }

    WebTarget webTarget = createBaseWebTarget("nfo/release");
    webTarget = webTarget.queryParam("id", release.getId());
    Invocation.Builder invocationBuilder = webTarget.request("image/png");
    invocationBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken());
    Response response = invocationBuilder.get();
    byte[] nfo = handleResponse(new GenericType<byte[]>() {}, response);

    return nfo;
  }

  /**
   * Returns an image of a NFO file for a given API P2P release.
   *
   * @param p2pRelease The {@link P2pRelease} you want the NFO of
   * @param token The {@link Token} with all needed info
   * @return The NFO as byte[]
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6437/api-nfo-p2p-rls.html">API: nfo/p2p_rls method</a>
   */
  public byte[] getNfoP2pRls(P2pRelease p2pRelease, Token token) throws XrelException {
    Objects.requireNonNull(p2pRelease, "p2pRelease missing");
    Objects.requireNonNull(token, "token missing");

    if (getScope() == null || !checkScope("viewnfo")) {
      throw new XrelException("viewnfo scope not provided");
    }

    WebTarget webTarget = createBaseWebTarget("nfo/p2p_rls");
    webTarget = webTarget.queryParam("id", p2pRelease.getId());
    Invocation.Builder invocationBuilder = webTarget.request("image/png");
    invocationBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken());
    Response response = invocationBuilder.get();
    byte[] nfo = handleResponse(new GenericType<byte[]>() {}, response);

    return nfo;
  }

  /**
   * Returns a list upcoming movies and their releases.
   *
   * @param country {@code de} for upcoming movies in germany, {@code us} for upcoming movies in the
   *        US/international.
   * @return The list of {@link ExtInfo}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/1827/api-calendar-upcoming.html">API: calendar/upcoming
   *      method</a>
   */
  public List<ExtInfo> getCalendarUpcoming(String country) throws XrelException {
    Objects.requireNonNull(country, "country missing");

    if (!country.equals("de") && !country.equals("us")) {
      throw new XrelException("country must be either de or en");
    }

    WebTarget webTarget = createBaseWebTarget("calendar/upcoming");
    webTarget = webTarget.queryParam("country", country);
    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    Response response = invocationBuilder.get();
    List<ExtInfo> upcomingList = handleResponse(new GenericType<List<ExtInfo>>() {}, response);

    return upcomingList;
  }

  /**
   * Returns information about an Ext Info.
   *
   * @param extInfo The {@link ExtInfo} you want more info about.
   * @param token Your optional {@link Token}. If supplied you will also get {@code own_rating} from
   *        this {@link ExtInfo}.
   * @return The new {@link ExtInfo}.
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/2725/api-ext-info-info.html">API: ext_info/info
   *      method</a>
   */
  private ExtInfo getExtInfoInfoPrivate(ExtInfo extInfo, Token token) throws XrelException {
    Objects.requireNonNull(extInfo, "extInfo missing");

    WebTarget webTarget = createBaseWebTarget("ext_info/info");
    webTarget = webTarget.queryParam("id", extInfo.getId());
    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    if (token != null) {
      invocationBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken());
    }
    Response response = invocationBuilder.get();
    ExtInfo extInfoNew = handleResponse(new GenericType<ExtInfo>() {}, response);

    return extInfoNew;
  }

  /**
   * Returns information about an Ext Info.
   *
   * @param extInfo The {@link ExtInfo} you want more info about.
   * @return The new {@link ExtInfo}.
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/2725/api-ext-info-info.html">API: ext_info/info
   *      method</a>
   */
  public ExtInfo getExtInfoInfo(ExtInfo extInfo) throws XrelException {
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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/2725/api-ext-info-info.html">API: ext_info/info
   *      method</a>
   */
  public ExtInfo getExtInfoInfo(ExtInfo extInfo, Token token) throws XrelException {
    Objects.requireNonNull(extInfo, "extInfo missing");
    Objects.requireNonNull(token, "token missing");

    return getExtInfoInfoPrivate(extInfo, token);
  }

  /**
   * Returns media associated with an Ext Info.
   *
   * @param extInfo The ExtInfo which media you want to retrieve. The found ExtInfoMedia will be
   *        added to the given ExtInfo.
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6314/api-ext-info-media.html">API: ext_info/media
   *      method</a>
   */
  public void getExtInfoMedia(ExtInfo extInfo) throws XrelException {
    Objects.requireNonNull(extInfo, "extInfo missing");

    WebTarget webTarget = createBaseWebTarget("ext_info/media");
    webTarget = webTarget.queryParam("id", extInfo.getId());
    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    Response response = invocationBuilder.get();
    List<ExtInfoMedia> extInfoMediaList =
        handleResponse(new GenericType<List<ExtInfoMedia>>() {}, response);

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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6315/api-ext-info-rate.html">API: ext_info/rate
   *      method</a>
   */
  public void postExtInfoRate(ExtInfo extInfo, int rating, Token token) throws XrelException {
    Objects.requireNonNull(extInfo, "extInfo missing");
    Objects.requireNonNull(token, "token missing");
    if (rating < 1 || rating > 10) {
      throw new XrelException("rating must be in the range of 1 - 10");
    }

    Form form = new Form();
    form.param("id", extInfo.getId());
    form.param("rating", String.valueOf(rating));
    Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE);

    WebTarget webTarget = createBaseWebTarget("ext_info/rate");
    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    invocationBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken());
    Response response = invocationBuilder.post(entity);
    ExtInfo extInfoRated = handleResponse(new GenericType<ExtInfo>() {}, response);
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
   * @param limit Number of returned search results. {@code -1} to disable.
   * @return A pair object containing the lists of {@link Release} and {@link P2pRelease}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6320/api-search-releases.html">API: search/releases
   *      method</a>
   */
  private ReleaseSearchResult getSearchReleasesPrivate(String q, boolean scene, boolean p2p,
      int limit) throws XrelException {
    Objects.requireNonNull(q, "q missing");

    if ((!p2p) && (!scene)) {
      throw new XrelException("either scene or p2p must be set to true");
    }
    if (limit != -1) {
      if (limit < 1) {
        throw new XrelException("limit must be either -1 or greater than 1");
      }
    }

    WebTarget webTarget = createBaseWebTarget("search/releases");
    webTarget = webTarget.queryParam("q", q);
    webTarget = webTarget.queryParam("scene", scene);
    webTarget = webTarget.queryParam("p2p", p2p);
    if (limit != -1) {
      webTarget = webTarget.queryParam("limit", limit);
    }
    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    Response response = invocationBuilder.get();
    ReleaseSearchResult releaseSearchResult =
        handleResponse(new GenericType<ReleaseSearchResult>() {}, response);

    return releaseSearchResult;
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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6320/api-search-releases.html">API: search/releases
   *      method</a>
   */
  public ReleaseSearchResult getSearchReleases(String q, boolean scene, boolean p2p, int limit)
      throws XrelException {
    Objects.requireNonNull(q, "q missing");

    if ((!p2p) && (!scene)) {
      throw new XrelException("either scene or p2p must be set to true");
    }
    if (limit < 1) {
      throw new XrelException("limit must be 1 or greater");
    }
    return getSearchReleasesPrivate(q, scene, p2p, limit);
  }

  /**
   * Searches for Scene and P2P releases. For all calls to search methods, additional rate limiting
   * applies. Currently this limit is set at 2 calls per 10 seconds. Please keep track of that limit
   * yourself.
   *
   * @param q Search keyword.
   * @param scene If {@code true}, Scene releases will be included in the search results.
   * @param p2p If {@code true}, P2P releases will be included in the search results.
   * @return A pair object containing the lists of {@link Release} and {@link P2pRelease}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6320/api-search-releases.html">API: search/releases
   *      method</a>
   */
  public ReleaseSearchResult getSearchReleases(String q, boolean scene, boolean p2p)
      throws XrelException {
    Objects.requireNonNull(q, "q missing");

    if ((!p2p) && (!scene)) {
      throw new XrelException("either scene or p2p must be set to true");
    }

    return getSearchReleasesPrivate(q, scene, p2p, -1);
  }

  /**
   * Searches for Ext Infos. For all calls to search methods, additional rate limiting applies.
   * Currently this limit is set at 2 calls per 10 seconds. Please keep track of that limit
   * yourself.
   *
   * @param q Search keyword.
   * @param type One of: {@code movie}|{@code tv}|{@code game}|{@code console}|{@code
   software}|{@code xxx} - or {@code null} to browse releases of all types
   * @param limit Number of returned search results. {@code -1} to disable.
   * @return List of {@link ExtInfo}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6319/api-search-ext-info.html">API: search/ext_info
   *      method</a>
   */
  private ExtInfoSearchResult getSearchExtInfoPrivate(String q, String type, int limit)
      throws XrelException {
    Objects.requireNonNull(q, "q missing");

    if (limit != -1) {
      if (limit < 1) {
        throw new XrelException("limit must be either -1 or greater than 1");
      }
    }

    WebTarget webTarget = createBaseWebTarget("search/ext_info");
    webTarget = webTarget.queryParam("q", q);
    if (type != null && !type.isEmpty()) {
      webTarget = webTarget.queryParam("type", type);
    }
    if (limit != -1) {
      webTarget = webTarget.queryParam("limit", limit);
    }
    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    Response response = invocationBuilder.get();
    ExtInfoSearchResult extInfoSearchResult =
        handleResponse(new GenericType<ExtInfoSearchResult>() {}, response);

    return extInfoSearchResult;
  }

  /**
   * Searches for Ext Infos. For all calls to search methods, additional rate limiting applies.
   * Currently this limit is set at 2 calls per 10 seconds. Please keep track of that limit
   * yourself.
   *
   * @param q Search keyword.
   * @return List of {@link ExtInfo}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6319/api-search-ext-info.html">API: search/ext_info
   *      method</a>
   */
  public ExtInfoSearchResult getSearchExtInfo(String q) throws XrelException {
    Objects.requireNonNull(q, "q missing");

    return getSearchExtInfoPrivate(q, null, -1);
  }

  /**
   * Searches for Ext Infos. For all calls to search methods, additional rate limiting applies.
   * Currently this limit is set at 2 calls per 10 seconds. Please keep track of that limit
   * yourself.
   *
   * @param q Search keyword.
   * @param limit Number of returned search results.
   * @return List of {@link ExtInfo}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6319/api-search-ext-info.html">API: search/ext_info
   *      method</a>
   */
  public ExtInfoSearchResult getSearchExtInfo(String q, int limit) throws XrelException {
    Objects.requireNonNull(q, "q missing");

    if (limit < 1) {
      throw new XrelException("limit must be 1 or greater");
    }

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
   * @return List of {@link ExtInfo}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6319/api-search-ext-info.html">API: search/ext_info
   *      method</a>
   */
  public ExtInfoSearchResult getSearchExtInfo(String q, String type) throws XrelException {
    Objects.requireNonNull(q, "q missing");
    Objects.requireNonNull(type, "type missing");

    return getSearchExtInfoPrivate(q, type, -1);
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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6319/api-search-ext-info.html">API: search/ext_info
   *      method</a>
   */
  public ExtInfoSearchResult getSearchExtInfo(String q, String type, int limit)
      throws XrelException {
    Objects.requireNonNull(q, "q missing");
    Objects.requireNonNull(type, "type missing");

    if (limit < 1) {
      throw new XrelException("limit must be 1 or greater");
    }

    return getSearchExtInfoPrivate(q, type, limit);
  }

  /**
   * Returns a list of all the current user's favorite lists.
   *
   * @param token The {@link Token} with all needed info.
   * @return A list of all {@link Favorite}.
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/1754/api-favs-lists.html">API: favs/lists method</a>
   */
  public List<Favorite> getFavsLists(Token token) throws XrelException {
    Objects.requireNonNull(token, "token missing");

    WebTarget webTarget = createBaseWebTarget("favs/lists");
    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    invocationBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken());
    Response response = invocationBuilder.get();
    List<Favorite> favoriteList = handleResponse(new GenericType<List<Favorite>>() {}, response);

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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/1823/api-favs-list-entries.html">API: favs/list_entries
   *      method</a>
   */
  public void getFavsListEntries(Favorite favorite, boolean getReleases, Token token)
      throws XrelException {
    Objects.requireNonNull(favorite, "favorite missing");
    Objects.requireNonNull(token, "token missing");

    WebTarget webTarget = createBaseWebTarget("favs/list_entries");
    webTarget = webTarget.queryParam("id", favorite.getId());
    webTarget = webTarget.queryParam("get_releases", getReleases);
    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    invocationBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken());
    Response response = invocationBuilder.get();
    List<ExtInfo> extInfoList = handleResponse(new GenericType<List<ExtInfo>>() {}, response);

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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6316/api-favs-list-addentry.html">API:
   *      favs/list_addentry method</a><br>
   *      <a href= "https://www.xrel.to/wiki/6317/api-favs-list-delentry.html">API:
   *      favs/list_delentry method</a>
   */
  private FavoriteAddDelEntry postFavsListAddDelEntry(Favorite favorite, ExtInfo extInfo,
      Token token, boolean delete) throws XrelException {
    Objects.requireNonNull(favorite, "favorite missing");
    Objects.requireNonNull(extInfo, "extInfo missing");
    Objects.requireNonNull(token, "token missing");

    Form form = new Form();
    form.param("id", String.valueOf(favorite.getId()));
    form.param("ext_info_id", String.valueOf(extInfo.getId()));
    Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE);

    WebTarget webTarget;
    if (delete) {
      webTarget = createBaseWebTarget("favs/list_delentry");
    } else {
      webTarget = createBaseWebTarget("favs/list_addentry");
    }
    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    invocationBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken());
    Response response = invocationBuilder.post(entity);
    if (delete) {
      changeXmlRootName(FavoriteAddDelEntry.class, "fav_list_delentry");
    } else {
      changeXmlRootName(FavoriteAddDelEntry.class, "fav_list_addentry");
    }
    FavoriteAddDelEntry favoriteAddDelEntry =
        handleResponse(new GenericType<FavoriteAddDelEntry>() {}, response);
    changeXmlRootName(FavoriteAddDelEntry.class, "##default");

    return favoriteAddDelEntry;
  }

  /**
   * Add an Ext Info to a favorite list.
   *
   * @param favorite The favorite list, as obtained through {@link #getFavsLists(Token)}.
   * @param extInfo The {@link ExtInfo} to add.
   * @param token The {@link Token} with all needed info.
   * @return The new {@link Favorite}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6316/api-favs-list-addentry.html">API:
   *      favs/list_addentry method</a>
   */
  public FavoriteAddDelEntry postFavsListAddEntry(Favorite favorite, ExtInfo extInfo, Token token)
      throws XrelException {
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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6317/api-favs-list-delentry.html">API:
   *      favs/list_delentry method</a>
   */
  public FavoriteAddDelEntry postFavsListDelEntry(Favorite favorite, ExtInfo extInfo, Token token)
      throws XrelException {
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
   * @return The new {@link FavoriteMarkRead}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6344/api-favs-list-markread.html">API:
   *      favs/list_markread method</a>
   */
  private FavoriteMarkRead postFavsListMarkRead(Favorite favorite, Release release,
      P2pRelease p2pRelease, Token token) throws XrelException {
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

    Form form = new Form();
    form.param("id", String.valueOf(favorite.getId()));
    form.param("release_id", String.valueOf(releaseId));
    form.param("type", type);
    Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE);

    WebTarget webTarget = createBaseWebTarget("favs/list_markread");
    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    invocationBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken());
    Response response = invocationBuilder.post(entity);
    FavoriteMarkRead favoriteMarkRead =
        handleResponse(new GenericType<FavoriteMarkRead>() {}, response);

    return favoriteMarkRead;
  }

  /**
   * Mark a {@link Release} on a favorite list as read.
   *
   * @param favorite The favorite list, as obtained through {@link #getFavsLists(Token)}.
   * @param release The {@link Release} to be marked as read.
   * @param token The {@link Token} with all needed info.
   * @return The new {@link FavoriteMarkRead}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6344/api-favs-list-markread.html">API:
   *      favs/list_markread method</a>
   */
  public FavoriteMarkRead postFavsListMarkRead(Favorite favorite, Release release, Token token)
      throws XrelException {
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
   * @return The new {@link FavoriteMarkRead}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6344/api-favs-list-markread.html">API:
   *      favs/list_markread method</a>
   */
  public FavoriteMarkRead postFavsListMarkRead(Favorite favorite, P2pRelease p2pRelease,
      Token token) throws XrelException {
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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6313/api-comments-get.html">API: comments/get
   *      method</a>
   */
  private PaginationList<Comment> getCommentsGet(Release release, P2pRelease p2pRelease,
      int perPage, int page) throws XrelException {
    int[] normalizedPageValues = normalizePageValues(perPage, page);

    WebTarget webTarget = createBaseWebTarget("comments/get");
    if (release != null) {
      webTarget = webTarget.queryParam("id", release.getId());
      webTarget = webTarget.queryParam("type", "release");
    } else {
      webTarget = webTarget.queryParam("id", p2pRelease.getId());
      webTarget = webTarget.queryParam("type", "p2p_rls");
    }
    webTarget = webTarget.queryParam("per_page", normalizedPageValues[0]);
    webTarget = webTarget.queryParam("page", normalizedPageValues[1]);
    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    Response response = invocationBuilder.get();
    changeXmlRootName(PaginationList.class, "comments");
    PaginationList<Comment> commentList =
        handleResponse(new GenericType<PaginationList<Comment>>() {}, response);
    changeXmlRootName(PaginationList.class, "##default");

    return commentList;
  }

  /**
   * Returns comments for a given {@link Release}.
   *
   * @param release The corresponding {@link Release}.
   * @param perPage Number of releases per page. Min. 5, max. 100.
   * @param page Page number (1 to N).
   * @return The {@link PaginationList} containing the {@link Comment}.
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6313/api-comments-get.html">API: comments/get
   *      method</a>
   */
  public PaginationList<Comment> getCommentsGet(Release release, int perPage, int page)
      throws XrelException {
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
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6313/api-comments-get.html">API: comments/get
   *      method</a>
   */
  public PaginationList<Comment> getCommentsGet(P2pRelease p2pRelease, int perPage, int page)
      throws XrelException {
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
   *        the response to get the rating as displayed on the website. Use {@code -1} to disable.
   * @param audioRating Audio rating between 1 (bad) to 10 (good). You must always rate both or
   *        none. You may only vote once, and may not change your vote. Check the vote property from
   *        the response to get the rating as displayed on the website. Use {@code -1} to disable.
   * @param token The {@link Token} with all needed info.
   * @return The added {@link Comment}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6312/api-comments-add.html">API: comments/add
   *      method</a>
   */
  private Comment postCommentsAdd(Release release, P2pRelease p2pRelease, String text,
      int videoRating, int audioRating, Token token) throws XrelException {
    Objects.requireNonNull(token, "token missing");

    Form form = new Form();
    if (release != null) {
      form.param("id", release.getId());
      form.param("type", "release");
    } else {
      form.param("id", p2pRelease.getId());
      form.param("type", "p2p_rls");
    }
    if (videoRating != -1 && audioRating != -1) {
      form.param("video_rating", String.valueOf(videoRating));
      form.param("audio_rating", String.valueOf(audioRating));
    }
    if (text != null) {
      form.param("text", text);
    }
    Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE);

    WebTarget webTarget = createBaseWebTarget("comments/add");
    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    invocationBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken());
    Response response = invocationBuilder.post(entity);
    Comment comment = handleResponse(new GenericType<Comment>() {}, response);

    return comment;
  }

  /**
   * Add a comment to a given {@link Release}. The text may contain BBCode. Supplying either a text,
   * a rating (both audio and video) or both is mandatory.
   *
   * @param release The {@link Release} to add a comment to.
   * @param videoRating Video rating between 1 (bad) to 10 (good). You must always rate both or
   *        none. You may only vote once, and may not change your vote. Check the vote property from
   *        the response to get the rating as displayed on the website.
   * @param audioRating Audio rating between 1 (bad) to 10 (good). You must always rate both or
   *        none. You may only vote once, and may not change your vote. Check the vote property from
   *        the response to get the rating as displayed on the website.
   * @param token The {@link Token} with all needed info.
   * @return The added {@link Comment}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6312/api-comments-add.html">API: comments/add
   *      method</a>
   */
  public Comment postCommentsAdd(Release release, int videoRating, int audioRating, Token token)
      throws XrelException {
    Objects.requireNonNull(release, "release missing");
    Objects.requireNonNull(token, "token missing");

    if (videoRating < 1 || audioRating < 1 || videoRating > 10 || audioRating > 10) {
      throw new XrelException("ratings must be between 1 and 10");
    }

    return postCommentsAdd(release, null, null, videoRating, audioRating, token);
  }

  /**
   * Add a comment to a given {@link Release}. The text may contain BBCode. Supplying either a text,
   * a rating (both audio and video) or both is mandatory.
   *
   * @param release The {@link Release} to add a comment to.
   * @param text The comment. You may use BBCode to format the text. Can be {@code null} if both
   *        videoRating and audioRating are set.
   * @param token The {@link Token} with all needed info.
   * @return The added {@link Comment}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6312/api-comments-add.html">API: comments/add
   *      method</a>
   */
  public Comment postCommentsAdd(Release release, String text, Token token) throws XrelException {
    Objects.requireNonNull(release, "release missing");
    Objects.requireNonNull(token, "token missing");
    Objects.requireNonNull(text, "text missing");

    return postCommentsAdd(release, null, text, -1, -1, token);
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
   *        the response to get the rating as displayed on the website.
   * @param audioRating Audio rating between 1 (bad) to 10 (good). You must always rate both or
   *        none. You may only vote once, and may not change your vote. Check the vote property from
   *        the response to get the rating as displayed on the website.
   * @param token The {@link Token} with all needed info.
   * @return The added {@link Comment}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6312/api-comments-add.html">API: comments/add
   *      method</a>
   */
  public Comment postCommentsAdd(Release release, String text, int videoRating, int audioRating,
      Token token) throws XrelException {
    Objects.requireNonNull(release, "release missing");
    Objects.requireNonNull(token, "token missing");
    Objects.requireNonNull(text, "text missing");

    if (videoRating < 1 || audioRating < 1 || videoRating > 10 || audioRating > 10) {
      throw new XrelException("ratings must be between 1 and 10");
    }

    return postCommentsAdd(release, null, text, videoRating, audioRating, token);
  }

  /**
   * Add a comment to a given {@link P2pRelease}. The text may contain BBCode.
   *
   *
   * @param p2pRelease The {@link P2pRelease} to add a comment to.
   * @param videoRating Video rating between 1 (bad) to 10 (good). You must always rate both or
   *        none. You may only vote once, and may not change your vote. Check the vote property from
   *        the response to get the rating as displayed on the website.
   * @param audioRating Audio rating between 1 (bad) to 10 (good). You must always rate both or
   *        none. You may only vote once, and may not change your vote. Check the vote property from
   *        the response to get the rating as displayed on the website.
   * @param token The {@link Token} with all needed info
   * @return The added {@link Comment}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6312/api-comments-add.html">API: comments/add
   *      method</a>
   */
  public Comment postCommentsAdd(P2pRelease p2pRelease, int videoRating, int audioRating,
      Token token) throws XrelException {
    Objects.requireNonNull(p2pRelease, "p2pRelease missing");
    Objects.requireNonNull(token, "token missing");

    if (videoRating < 1 || audioRating < 1 || videoRating > 10 || audioRating > 10) {
      throw new XrelException("ratings must be between 1 and 10");
    }

    return postCommentsAdd(null, p2pRelease, null, videoRating, audioRating, token);
  }

  /**
   * Add a comment to a given {@link P2pRelease}. The text may contain BBCode.
   *
   *
   * @param p2pRelease The {@link P2pRelease} to add a comment to.
   * @param text The comment. You may use BBCode to format the text. Can be {@code null} if both
   *        videoRating and audioRating are set.
   * @param token The {@link Token} with all needed info
   * @return The added {@link Comment}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6312/api-comments-add.html">API: comments/add
   *      method</a>
   */
  public Comment postCommentsAdd(P2pRelease p2pRelease, String text, Token token)
      throws XrelException {
    Objects.requireNonNull(p2pRelease, "p2pRelease missing");
    Objects.requireNonNull(token, "token missing");
    Objects.requireNonNull(text, "text missing");

    return postCommentsAdd(null, p2pRelease, text, -1, -1, token);
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
   *        the response to get the rating as displayed on the website.
   * @param audioRating Audio rating between 1 (bad) to 10 (good). You must always rate both or
   *        none. You may only vote once, and may not change your vote. Check the vote property from
   *        the response to get the rating as displayed on the website.
   * @param token The {@link Token} with all needed info
   * @return The added {@link Comment}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6312/api-comments-add.html">API: comments/add
   *      method</a>
   */
  public Comment postCommentsAdd(P2pRelease p2pRelease, String text, int videoRating,
      int audioRating, Token token) throws XrelException {
    Objects.requireNonNull(p2pRelease, "p2pRelease missing");
    Objects.requireNonNull(token, "token missing");
    Objects.requireNonNull(text, "text missing");

    if (videoRating < 1 || audioRating < 1 || videoRating > 10 || audioRating > 10) {
      throw new XrelException("ratings must be between 1 and 10");
    }

    return postCommentsAdd(null, p2pRelease, text, videoRating, audioRating, token);
  }

  /**
   * Returns information about the active user.
   *
   * @param token The token used for authentication
   * @return The {@link User}
   * @throws XrelException If there is an error returned by the xREL API
   * @see <a href= "https://www.xrel.to/wiki/6441/api-user-info.html">API: user/info method</a>
   */
  public User getUserInfo(Token token) throws XrelException {
    Objects.requireNonNull(token, "token missing");

    WebTarget webTarget = createBaseWebTarget("user/info");
    Invocation.Builder invocationBuilder = webTarget.request(getFormat());
    invocationBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken());
    Response response = invocationBuilder.get();
    User user = handleResponse(new GenericType<User>() {}, response);

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
    Client client = ClientBuilder.newClient();
    WebTarget webTarget = client.target(getXrelUrl());
    webTarget = webTarget.path("oauth2/auth");
    webTarget = webTarget.queryParam("response_type", getResponseType());
    webTarget = webTarget.queryParam("client_id", getClientId().get());
    if (getRedirectUri().isPresent()) {
      webTarget = webTarget.queryParam("redirect_uri", getRedirectUri().get());
    }
    if (getState().isPresent()) {
      webTarget = webTarget.queryParam("state", getState().get());
    }
    if (getScope().isPresent() && getScope().get().length > 0) {
      String scope = String.join(" ", getScope().get());
      webTarget = webTarget.queryParam("scope", scope);
    }

    return webTarget.getUri().toString();
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
   * @see <a href="https://www.xrel.to/wiki/6436/api-oauth2.html">API: OAuth 2.0</a>
   */
  private Token postOauth2TokenPrivate(String grantType, String code, Token token)
      throws XrelException {
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

    Form form = new Form();
    form.param("grant_type", grantType);
    form.param("client_id", getClientId().get());
    form.param("client_secret", getClientSecret().get());
    if (grantType == "authorization_code") {
      form.param("code", code);
    }
    if (grantType == "refresh_token") {
      form.param("refresh_token", token.getRefreshToken());
    }
    if (grantType != "refresh_token" && getRedirectUri().isPresent()) {
      form.param("redirect_uri", getRedirectUri().get());
    }
    if (getScope().isPresent() && getScope().get().length > 0) {
      String scope = String.join(" ", getScope().get());
      form.param("scope", scope);
    }
    Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE);

    final Client client = ClientBuilder.newClient();
    client.register(RateLimitFilter.class);
    client.register(CompressionHelper.class);
    WebTarget webTarget = client.target(getXrelUrl());
    webTarget = webTarget.path("oauth2/token");
    // As per spec this always uses JSON and is not available via XML
    Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
    Response response = invocationBuilder.post(entity);
    Token newToken = handleResponse(new GenericType<Token>() {}, response);

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
   * @see <a href="https://www.xrel.to/wiki/6436/api-oauth2.html">API: OAuth 2.0</a>
   */
  public Token postOauth2Token(String grantType, Token token) throws XrelException {
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
   * @see <a href="https://www.xrel.to/wiki/6436/api-oauth2.html">API: OAuth 2.0</a>
   */
  public Token postOauth2Token(String grantType, String code) throws XrelException {
    Objects.requireNonNull(grantType, "grantType missing");
    Objects.requireNonNull(code, "code missing");

    return postOauth2TokenPrivate(grantType, code, null);
  }

}
