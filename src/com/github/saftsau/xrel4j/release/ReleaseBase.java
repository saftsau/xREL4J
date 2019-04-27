/*
 * Copyright 2017 - 2019 saftsau
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

package com.github.saftsau.xrel4j.release;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.saftsau.xrel4j.extinfo.ExtInfo;
import com.github.saftsau.xrel4j.release.p2p.P2pRelease;
import com.github.saftsau.xrel4j.release.scene.Release;

/**
 * Class represents a base release which contains information shared by {@link Release} and
 * {@link P2pRelease}. This is used as the parent class of these two classes.
 */
public abstract class ReleaseBase {

  private String id;
  private String dirname;
  @JsonProperty("link_href")
  private String linkHref;
  @JsonProperty("num_ratings")
  private int numRatings;
  @JsonProperty("video_rating")
  private double videoRating;
  @JsonProperty("audio_rating")
  private double audioRating;
  @JsonProperty("ext_info")
  private ExtInfo extInfo;
  @JsonProperty("tv_season")
  private int tvSeason;
  @JsonProperty("tv_episode")
  private int tvEpisode;
  private int comments;

  /**
   * Gets the API ID of this Release.
   * 
   * @return The ID
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the API ID of this Release.
   * 
   * @param id The ID to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets the dirname of this Release.
   * 
   * @return The dirname
   */
  public String getDirname() {
    return dirname;
  }

  /**
   * Sets the dirname of this Release.
   * 
   * @param dirname The dirname to set
   */
  public void setDirname(String dirname) {
    this.dirname = dirname;
  }

  /**
   * Gets the link of this Release.
   * 
   * @return The link
   */
  public String getLinkHref() {
    return linkHref;
  }

  /**
   * Sets the link of this Release.
   * 
   * @param linkHref The link to set
   */
  public void setLinkHref(String linkHref) {
    this.linkHref = linkHref;
  }

  /**
   * Gets the number of ratings of this Release. {@code -1} if not retrieved.
   * 
   * @return The number of ratings
   */
  public int getNumRatings() {
    return numRatings;
  }

  /**
   * Sets the number of ratings of this Release. Use {@code -1} to unset.
   * 
   * @param numRatings The number of ratings to set
   */
  public void setNumRatings(int numRatings) {
    this.numRatings = numRatings;
  }

  /**
   * Gets the video rating of this release. {@code -1} if not retrieved, {@code 0} if not rated.
   * 
   * @return The video rating
   */
  public double getVideoRating() {
    return videoRating;
  }

  /**
   * Sets the video rating of this release. Use {@code -1} to unset.
   * 
   * @param videoRating The video rating to set
   */
  public void setVideoRating(double videoRating) {
    this.videoRating = videoRating;
  }

  /**
   * Gets the audio rating of this release. {@code -1} if not retrieved, {@code 0} if not rated.
   * 
   * @return The audio rating
   */
  public double getAudioRating() {
    return audioRating;
  }

  /**
   * Sets the audio rating of this release. {@code -1} if not retrieved, {@code 0} if not rated.
   * 
   * @param audioRating The audio rating to set
   */
  public void setAudioRating(double audioRating) {
    this.audioRating = audioRating;
  }

  /**
   * Gets the ExtInfo associated with this Release. Is never {@code null}.
   * 
   * @return The ExtInfo
   */
  public ExtInfo getExtInfo() {
    return extInfo;
  }

  /**
   * Sets the ExtInfo associated with this Release. Should not be set to {@code null}.
   * 
   * @param extInfo The ExtInfo to set
   */
  public void setExtInfo(ExtInfo extInfo) {
    this.extInfo = extInfo;
  }

  /**
   * Gets the season number of this release. {@code -1} if not retrieved or it has no season.
   * 
   * @return The season number
   */
  public int getTvSeason() {
    return tvSeason;
  }

  /**
   * Sets the season number of this release. {@code -1} to unset.
   * 
   * @param tvSeason The season number to set
   */
  public void setTvSeason(int tvSeason) {
    this.tvSeason = tvSeason;
  }

  /**
   * Gets the episode number of this release. {@code -1} if not retrieved or it has no episode.
   * 
   * @return The episode number
   */
  public int getTvEpisode() {
    return tvEpisode;
  }

  /**
   * Sets the episode number of this release. {@code -1} to unset.
   * 
   * @param tvEpisode The episode number to set
   */
  public void setTvEpisode(int tvEpisode) {
    this.tvEpisode = tvEpisode;
  }

  /**
   * Gets the number of comments this release has. {@code -1} if not retrieved.
   * 
   * @return The number of comments
   */
  public int getComments() {
    return comments;
  }

  /**
   * Sets the number of comments this release has. {@code -1} to unset.
   * 
   * @param comments The comments to set
   */
  public void setComments(int comments) {
    this.comments = comments;
  }

  @Override
  public String toString() {
    return "ReleaseBase [getId()=" + getId() + ", getDirname()=" + getDirname() + ", getLinkHref()="
        + getLinkHref() + ", getNumRatings()=" + getNumRatings() + ", getVideoRating()="
        + getVideoRating() + ", getAudioRating()=" + getAudioRating() + ", getExtInfo()="
        + getExtInfo() + ", getTvSeason()=" + getTvSeason() + ", getTvEpisode()=" + getTvEpisode()
        + ", getComments()=" + getComments() + "]";
  }

}
