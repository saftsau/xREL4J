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

import javax.json.bind.annotation.JsonbProperty;

/**
 * Class represents media an {@link ExtInfo} can contain. Used for adding images (hosted on xREL)
 * and videos (hosted on YouTube).
 */
public class ExtInfoMedia {

  private String type;
  private String description;
  private long time;
  @JsonbProperty("url_full")
  private String urlFull;
  @JsonbProperty("url_thumb")
  private String urlThumb;
  @JsonbProperty("youtube_id")
  private String youtubeId;
  @JsonbProperty("video_url")
  private String videoUrl;

  /**
   * Gets the type of this ExtInfoMedia. Can be either {@code image} or {@code video}.
   * 
   * @return The type
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the type of this ExtInfoMedia. Should be either {@code image} or {@code video}.
   * 
   * @param type The type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Gets the description of this ExtInfoMedia.
   * 
   * @return The description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of this ExtInfoMedia.
   * 
   * @param description The description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Gets the time this ExtInfoMedia was added. Returned as a UNIX timestamp.
   * 
   * @return The timestamp
   */
  public long getTime() {
    return time;
  }

  /**
   * Sets the time this ExtInfoMedia was added. Should be given as a UNIX timestamp.
   * 
   * @param time The timestamp to set
   */
  public void setTime(long time) {
    this.time = time;
  }

  /**
   * Gets the link to the full version of an {@code image}. Returns an empty {@link String} for
   * videos.
   * 
   * @return The link
   */
  public String getUrlFull() {
    return urlFull;
  }

  /**
   * Sets the link to the full version of an {@code image}. Use an empty {@link String} to unset.
   * 
   * @param urlFull The link to set
   */
  public void setUrlFull(String urlFull) {
    this.urlFull = urlFull;
  }

  /**
   * Gets the link to the thumbnail of this ExtInfoMedia. Used for {@code image} and {@code video}.
   * 
   * @return The link
   */
  public String getUrlThumb() {
    return urlThumb;
  }

  /**
   * Sets the link to the thumbnail of this ExtInfoMedia. Used for {@code image} and {@code video}.
   * 
   * @param urlThumb The link to set
   */
  public void setUrlThumb(String urlThumb) {
    this.urlThumb = urlThumb;
  }

  /**
   * Gets the YouTube ID of this ExtInfoMedia. Returns an empty {@link String} for images.
   * 
   * @return The YouTube ID
   */
  public String getYoutubeId() {
    return youtubeId;
  }

  /**
   * Sets the YouTube ID of this ExtInfoMedia. Use an empty {@link String} to unset.
   * 
   * @param youtubeId The YouTube ID to set
   */
  public void setYoutubeId(String youtubeId) {
    this.youtubeId = youtubeId;
  }

  /**
   * Gets the link to the video of this ExtInfoMedia. Returns an empty {@link String} for images.
   * 
   * @return The video link
   */
  public String getVideoUrl() {
    return videoUrl;
  }

  /**
   * Sets the link to the video of this ExtInfoMedia. Use an empty {@link String} to unset.
   * 
   * @param videoUrl The video link to set
   */
  public void setVideoUrl(String videoUrl) {
    this.videoUrl = videoUrl;
  }

  @Override
  public String toString() {
    return "ExtInfoMedia [getType()=" + getType() + ", getDescription()=" + getDescription()
        + ", getTime()=" + getTime() + ", getUrlFull()=" + getUrlFull() + ", getUrlThumb()="
        + getUrlThumb() + ", getYoutubeId()=" + getYoutubeId() + ", getVideoUrl()=" + getVideoUrl()
        + "]";
  }

}
