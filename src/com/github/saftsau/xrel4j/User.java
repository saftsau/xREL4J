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

import javax.json.bind.annotation.JsonbProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class represents a user as returned by the xREL API. This is implemented using a singleton
 * pattern.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class User {

  private String id;
  private String name;
  private String secret;
  private String locale;
  @JsonbProperty("avatar_url")
  @XmlElement(name = "avatar_url")
  private String avatarUrl;
  @JsonbProperty("avatar_thumb_url")
  @XmlElement(name = "avatar_thumb_url")
  private String avatarThumbUrl;

  /**
   * Gets the API ID of this user.
   * 
   * @return The ID
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the API ID of this user.
   * 
   * @param id The ID to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets the name of this user.
   * 
   * @return The name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of this user.
   * 
   * @param name The name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the secret of this user.
   * 
   * @return The secret
   */
  public String getSecret() {
    return secret;
  }

  /**
   * Sets the secret of this user.
   * 
   * @param secret The secret to set
   */
  public void setSecret(String secret) {
    this.secret = secret;
  }

  /**
   * Gets the locale of this user. Can be either {@code de_DE} or {@code en_US}.
   * 
   * @return The locale
   */
  public String getLocale() {
    return locale;
  }

  /**
   * Sets the locale of this user. Should be either {@code de_DE} or {@code en_US}.
   * 
   * @param locale The locale to set
   */
  public void setLocale(String locale) {
    this.locale = locale;
  }

  /**
   * Gets the avatar link of this user.
   * 
   * @return The avatar link
   */
  public String getAvatarUrl() {
    return avatarUrl;
  }

  /**
   * Sets the avatar link of this user.
   * 
   * @param avatarUrl The avatar link to set
   */
  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  /**
   * Gets the avatar thumbnail link of this user.
   * 
   * @return The avatar thumbnail link
   */
  public String getAvatarThumbUrl() {
    return avatarThumbUrl;
  }

  /**
   * Sets the avatar thumbnail link of this user.
   * 
   * @param avatarThumbUrl The avatar thumbnail link to set
   */
  public void setAvatarThumbUrl(String avatarThumbUrl) {
    this.avatarThumbUrl = avatarThumbUrl;
  }

  @Override
  public String toString() {
    return "User [getId()=" + getId() + ", getName()=" + getName() + ", getSecret()=" + getSecret()
        + ", getLocale()=" + getLocale() + ", getAvatarUrl()=" + getAvatarUrl()
        + ", getAvatarThumbUrl()=" + getAvatarThumbUrl() + "]";
  }

}
