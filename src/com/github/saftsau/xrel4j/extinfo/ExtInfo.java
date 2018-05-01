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

package com.github.saftsau.xrel4j.extinfo;

import com.github.saftsau.xrel4j.release.p2p.P2pRelease;
import com.github.saftsau.xrel4j.release.scene.Release;
import java.util.List;
import javax.json.bind.annotation.JsonbProperty;

/**
 * Class represents an ExtInfo, used for all information xREL has about any product (movie, game,
 * ...).
 */
public class ExtInfo {

  private String type;
  private String id;
  private String title;
  @JsonbProperty("link_href")
  private String linkHref;
  private String genre;
  @JsonbProperty("alt_title")
  private String altTitle;
  @JsonbProperty("cover_url")
  private String coverUrl;
  private List<String> uris;
  private double rating;
  @JsonbProperty("own_rating")
  private int ownRating;
  @JsonbProperty("num_ratings")
  private long numRatings;
  @JsonbProperty("release_dates")
  private List<ReleaseDate> releaseDates;
  private List<External> externals;
  @JsonbProperty("ext_info_media")
  private List<ExtInfoMedia> extInfoMedia;
  private List<Release> releases;
  @JsonbProperty("p2p_releases")
  private List<P2pRelease> p2pReleases;

  /**
   * Gets the type of this ExtInfo. Possible values are {@code movie}, {@code console},
   * {@code music}, {@code nfo}, {@code game}, {@code software}, {@code tv} and {@code xxx}.
   * 
   * @return The type
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the type of this ExtInfo. Should be one {@code movie}, {@code console}, {@code music},
   * {@code nfo}, {@code game}, {@code software}, {@code tv} or {@code xxx}.
   * 
   * @param type The type to set
   */
  public void setType(String type) {
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
   * @param id The ID to set
   */
  public void setId(String id) {
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
   * @param title The title to set
   */
  public void setTitle(String title) {
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
   * @param linkHref The link to set
   */
  public void setLinkHref(String linkHref) {
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
   * @param rating The rating to set
   */
  public void setRating(double rating) {
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
   * @param numRatings The number of ratings to set
   */
  public void setNumRatings(long numRatings) {
    this.numRatings = numRatings;
  }

  /**
   * Gets the URIs of this ExtInfo. Can be empty.
   * 
   * @return The URIs
   */
  public List<String> getUris() {
    return uris;
  }

  /**
   * Sets the URIs of this ExtInfo. Can be empty.
   * 
   * @param uris The URIs to set
   */
  public void setUris(List<String> uris) {
    this.uris = uris;
  }

  /**
   * Gets the unread {@link Release} of this ExtInfo. Only available if this ExtInfo is part of a
   * favorite list, otherwise always returns an empty list.
   * 
   * @return The list of unread releases
   */
  public List<Release> getReleases() {
    return releases;
  }

  /**
   * Sets the unread {@link Release} of this ExtInfo. Only available if this ExtInfo is part of a
   * favorite list, otherwise always returns an empty list.
   * 
   * @param unreadReleases The list of unread releases to set
   */
  public void setReleases(List<Release> unreadReleases) {
    this.releases = unreadReleases;
  }

  /**
   * Gets the unread {@link P2pRelease} of this ExtInfo. Only available if this ExtInfo is part of a
   * favorite list, otherwise always returns an empty list.
   * 
   * @return The list of unread P2P releases
   */
  public List<P2pRelease> getP2pReleases() {
    return p2pReleases;
  }

  /**
   * Sets the unread {@link P2pRelease} of this ExtInfo. Only available if this ExtInfo is part of a
   * favorite list, otherwise always returns an empty list.
   * 
   * @param unreadP2pReleases The list of unread P2P releases to set
   */
  public void setP2pReleases(List<P2pRelease> unreadP2pReleases) {
    this.p2pReleases = unreadP2pReleases;
  }

  /**
   * Gets the {@link ExtInfoMedia} of this ExtInfo. Can be empty if not yet retrieved or nothing
   * available.
   * 
   * @return The list of media
   */
  public List<ExtInfoMedia> getExtInfoMedia() {
    return extInfoMedia;
  }

  /**
   * Sets the {@link ExtInfoMedia} of this ExtInfo. Can be empty if not yet retrieved or nothing
   * available.
   * 
   * @param extInfoMedia The list of media to set
   */
  public void setExtInfoMedia(List<ExtInfoMedia> extInfoMedia) {
    this.extInfoMedia = extInfoMedia;
  }

  /**
   * Gets the genre of this ExtInfo. Can be an empty {@link String} if no genre specified or not yet
   * retrieved.
   * 
   * @return The genre
   */
  public String getGenre() {
    return genre;
  }

  /**
   * Sets the genre of this ExtInfo. Use an empty {@link String} to unset.
   * 
   * @param genre The genre to set
   */
  public void setGenre(String genre) {
    this.genre = genre;
  }

  /**
   * Gets the alternative title of this ExtInfo. Can be an empty {@link String} if no alternative
   * title specified or not yet retrieved.
   * 
   * @return The alternative title
   */
  public String getAltTitle() {
    return altTitle;
  }

  /**
   * Sets the alternative title of this ExtInfo. Use an empty {@link String} to unset.
   * 
   * @param altTitle The alternative title to set
   */
  public void setAltTitle(String altTitle) {
    this.altTitle = altTitle;
  }

  /**
   * Gets the link to the cover of this ExtInfo. Can be an empty {@link String} if no cover
   * specified or not yet retrieved.
   * 
   * @return The link to the cover
   */
  public String getCoverUrl() {
    return coverUrl;
  }

  /**
   * Gets the link to the cover of this ExtInfo. Use an empty {@link String} to unset.
   * 
   * @param coverUrl The link to the cover to set
   */
  public void setCoverUrl(String coverUrl) {
    this.coverUrl = coverUrl;
  }

  /**
   * Gets the release dates of this ExtInfo.
   * 
   * @return The release dates
   */
  public List<ReleaseDate> getReleaseDates() {
    return releaseDates;
  }

  /**
   * Sets the release dates of this ExtInfo.
   * 
   * @param releaseDates The release dates
   */
  public void setReleaseDates(List<ReleaseDate> releaseDates) {
    this.releaseDates = releaseDates;
  }

  /**
   * Gets the external info of this ExtInfo. Can be empty if nothing available or not yet retrieved.
   * 
   * @return The list of external info
   */
  public List<External> getExternals() {
    return externals;
  }

  /**
   * Sets the external info of this ExtInfo. Can be empty if nothing available or not yet retrieved.
   * 
   * @param externals The list of external info to set
   */
  public void setExternals(List<External> externals) {
    this.externals = externals;
  }

  /**
   * Gets the rating of the currently logged in user of this ExtInfo. Returns 0 if no user is logged
   * in or the user has not rated this ExtInfo yet.
   * 
   * @return The current user's rating
   */
  public int getOwnRating() {
    return ownRating;
  }

  /**
   * Gets the rating of the currently logged in user of this ExtInfo. Use {@code 0} to unset.
   * 
   * @param ownRating The current user's rating to set
   */
  public void setOwnRating(int ownRating) {
    this.ownRating = ownRating;
  }

  @Override
  public String toString() {
    return "ExtInfo [getType()=" + getType() + ", getId()=" + getId() + ", getTitle()=" + getTitle()
        + ", getLinkHref()=" + getLinkHref() + ", getRating()=" + getRating() + ", getNumRatings()="
        + getNumRatings() + ", getUris()=" + getUris() + ", getUnreadReleases()=" + getReleases()
        + ", getUnreadP2pReleases()=" + getP2pReleases() + ", getExtInfoMedia()="
        + getExtInfoMedia() + ", getGenre()=" + getGenre() + ", getAltTitle()=" + getAltTitle()
        + ", getCoverUrl()=" + getCoverUrl() + ", getReleaseDates()=" + getReleaseDates()
        + ", getExternals()=" + getExternals() + ", getOwnRating()=" + getOwnRating() + "]";
  }

}
