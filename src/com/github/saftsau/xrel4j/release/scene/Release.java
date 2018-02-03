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

package com.github.saftsau.xrel4j.release.scene;

import com.github.saftsau.xrel4j.release.ReleaseBase;
import com.github.saftsau.xrel4j.release.Size;
import java.util.Map;

/**
 * Class represents a release with all its information provided by the xREL API.
 */
public class Release extends ReleaseBase {

  private long time;
  private String groupName;
  private String nukeReason;
  private Size size;
  private String videoType;
  private String audioType;
  private String proofUrl;
  private Map<String, Boolean> flags;

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
   * @param time The timestamp to set
   */
  public void setTime(long time) {
    this.time = time;
  }

  /**
   * Gets the group name of this Release. Can be an empty {@link String} if not retrieved.
   * 
   * @return The group name
   */
  public String getGroupName() {
    return groupName;
  }

  /**
   * Sets the group name of this Release. Use an empty {@link String} to unset.
   * 
   * @param groupName The group name to set
   */
  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  /**
   * Gets the nuke reason of this Release. Can be an empty {@link String} if not retrieved or not
   * nuked.
   * 
   * @return The nuke reason
   */
  public String getNukeReason() {
    return nukeReason;
  }

  /**
   * Sets the nuke reason of this Release. Use an empty {@link String} to unset.
   * 
   * @param nukeReason The nuke reason to set
   */
  public void setNukeReason(String nukeReason) {
    this.nukeReason = nukeReason;
  }

  /**
   * Gets the size of this Release.
   * 
   * @return The size
   */
  public Size getSize() {
    return size;
  }

  /**
   * Sets the size of this Release.
   * 
   * @param size The size to set
   */
  public void setSize(Size size) {
    this.size = size;
  }

  /**
   * Gets the video type of this release. Can be an empty {@link String} if not retrieved.
   * 
   * @return The video type
   */
  public String getVideoType() {
    return videoType;
  }

  /**
   * Sets the video type of this release. Use an empty {@link String} to unset.
   * 
   * @param videoType The video type to set
   */
  public void setVideoType(String videoType) {
    this.videoType = videoType;
  }

  /**
   * Gets the audio type of this release. Can be an empty {@link String} if not retrieved.
   * 
   * @return The audio type
   */
  public String getAudioType() {
    return audioType;
  }

  /**
   * Sets the audio type of this release. Use an empty {@link String} to unset.
   * 
   * @param audioType The audio type to set
   */
  public void setAudioType(String audioType) {
    this.audioType = audioType;
  }

  /**
   * Gets the proof url of this release. Can be {@code null} if no proof available.
   * 
   * @return The proof url
   */
  public String getProofUrl() {
    return proofUrl;
  }

  /**
   * Sets the proof url of this release. Use {@code null} to unset.
   * 
   * @param proofUrl The proof proofUrl to set
   */
  public void setProofUrl(String proofUrl) {
    this.proofUrl = proofUrl;
  }

  /**
   * Gets the flags of this release. Can be an empty HashMap. Possible values are
   * {@code <top_rls, true>}, {@code <fix_rls, true>}, {@code <read_nfo, true>} and
   * {@code <english, true>}.
   * 
   * @return The flags
   */
  public Map<String, Boolean> getFlags() {
    return flags;
  }

  /**
   * Sets the flags of this release. Can be an empty HashMap. Possible values are
   * {@code <top_rls, true>}, {@code <fix_rls, true>}, {@code <read_nfo, true>} and
   * {@code <english, true>}.
   * 
   * @param flags The flags to set
   */
  public void setFlags(Map<String, Boolean> flags) {
    this.flags = flags;
  }

  @Override
  public String toString() {
    return "Release [getTime()=" + getTime() + ", getGroupName()=" + getGroupName()
        + ", getNukeReason()=" + getNukeReason() + ", getSize()=" + getSize() + ", getVideoType()="
        + getVideoType() + ", getAudioType()=" + getAudioType() + ", getProofUrl()=" + getProofUrl()
        + ", getFlags()=" + getFlags() + ", getId()=" + getId() + ", getDirname()=" + getDirname()
        + ", getLinkHref()=" + getLinkHref() + ", getNumRatings()=" + getNumRatings()
        + ", getVideoRating()=" + getVideoRating() + ", getAudioRating()=" + getAudioRating()
        + ", getExtInfo()=" + getExtInfo() + ", getTvSeason()=" + getTvSeason()
        + ", getTvEpisode()=" + getTvEpisode() + ", getComments()=" + getComments() + "]";
  }

}
