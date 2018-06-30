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

package com.github.saftsau.xrel4j.release.p2p;

import com.github.saftsau.xrel4j.release.ReleaseBase;
import javax.json.bind.annotation.JsonbProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class represents a P2P release with all its information provided by the xREL API.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "p2p_rls")
public class P2pRelease extends ReleaseBase {

  private P2pCategory category;
  @JsonbProperty("main_lang")
  @XmlElement(name = "main_lang")
  private String mainLang;
  @JsonbProperty("pub_time")
  @XmlElement(name = "pub_time")
  private long pubTime;
  @JsonbProperty("post_time")
  @XmlElement(name = "post_time")
  private long postTime;
  @JsonbProperty("size_mb")
  @XmlElement(name = "size_mb")
  private long sizeMb;
  private P2pGroup group;

  /**
   * Gets the category of this P2pRelease. Can be {@code null}.
   * 
   * @return The category
   */
  public P2pCategory getCategory() {
    return category;
  }

  /**
   * Gets the category of this P2pRelease. Use {@code null} to unset.
   * 
   * @param category The category to set
   */
  public void setCategory(P2pCategory category) {
    this.category = category;
  }

  /**
   * Gets the main language of this P2pRelease. Can be an empty {@link String}.
   * 
   * @return The main language
   */
  public String getMainLang() {
    return mainLang;
  }

  /**
   * Sets the main language of this P2pRelease. Can be an empty {@link String}.
   * 
   * @param mainLang The main language to set
   */
  public void setMainLang(String mainLang) {
    this.mainLang = mainLang;
  }

  /**
   * Gets the publication time of this P2pRelease. {@code -1} if not available.
   * 
   * @return The publication time
   */
  public long getPubTime() {
    return pubTime;
  }

  /**
   * Sets the publication time of this P2pRelease. Use {@code -1} to unset.
   * 
   * @param pubTime The publication time to set
   */
  public void setPubTime(long pubTime) {
    this.pubTime = pubTime;
  }

  /**
   * Gets the post time of this P2pRelease. {@code -1} if not available.
   * 
   * @return The post time
   */
  public long getPostTime() {
    return postTime;
  }

  /**
   * Sets the post time of this P2pRelease. Use {@code -1} to unset.
   * 
   * @param postTime The post time to set
   */
  public void setPostTime(long postTime) {
    this.postTime = postTime;
  }

  /**
   * Gets the size of this P2pRelease in MB. {@code -1} if not available.
   * 
   * @return The size in MB
   */
  public long getSizeMb() {
    return sizeMb;
  }

  /**
   * Sets the size of this P2pRelease in MB. Use {@code -1} to unset.
   * 
   * @param sizeMb The size in MB to set
   */
  public void setSizeMb(long sizeMb) {
    this.sizeMb = sizeMb;
  }

  /**
   * Gets the group of this P2pRelease. Can be {@code null}.
   * 
   * @return The group
   */
  public P2pGroup getGroup() {
    return group;
  }

  /**
   * Sets the group of this P2pRelease. Use {@code null} to unset.
   * 
   * @param group The group to set
   */
  public void setGroup(P2pGroup group) {
    this.group = group;
  }

  @Override
  public String toString() {
    return "P2pRelease [getCategory()=" + getCategory() + ", getMainLang()=" + getMainLang()
        + ", getPubTime()=" + getPubTime() + ", getPostTime()=" + getPostTime() + ", getSizeMb()="
        + getSizeMb() + ", getGroup()=" + getGroup() + ", getId()=" + getId() + ", getDirname()="
        + getDirname() + ", getLinkHref()=" + getLinkHref() + ", getNumRatings()=" + getNumRatings()
        + ", getVideoRating()=" + getVideoRating() + ", getAudioRating()=" + getAudioRating()
        + ", getExtInfo()=" + getExtInfo() + ", getTvSeason()=" + getTvSeason()
        + ", getTvEpisode()=" + getTvEpisode() + ", getComments()=" + getComments() + "]";
  }

}
