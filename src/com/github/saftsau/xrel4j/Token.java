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

/**
 * Class represents a Token used by the xREL API to access protected methods or additional user
 * data.
 */
public class Token {

  @JsonbProperty("access_token")
  private String accessToken;
  @JsonbProperty("token_type")
  private String tokenType;
  @JsonbProperty("expires_in")
  private int expiresIn;
  @JsonbProperty("refresh_token")
  private String refreshToken;
  private long creationTime;

  /**
   * Default no argument constructor needed for JSON-B.
   */
  public Token() {
    this.creationTime = System.currentTimeMillis() / 1000L;
  }

  /**
   * Constructs a new Token manually by given all needed information.
   * 
   * @param accessToken The access token, with which you may do requests to protected resources. It
   *        is a JSON Web Token, which you can verify and decode for more information (e.g. scopes
   *        and lifetime).
   * @param tokenType This value should always be Bearer
   * @param expiresIn Lifetime of the access token in seconds
   * @param refreshToken With the refresh token you may request a new access_token after it has
   *        expired. Its lifetime is unlimited, however you can only use it once. You will get a new
   *        refresh_token when renewing the access_token.
   */
  public Token(String accessToken, String tokenType, int expiresIn, String refreshToken) {
    this.accessToken = accessToken;
    this.tokenType = tokenType;
    this.expiresIn = expiresIn;
    this.refreshToken = refreshToken;
    this.creationTime = System.currentTimeMillis() / 1000L;
  }

  /**
   * Gets the access token used to access protected resources.
   * 
   * @return The access token
   */
  public String getAccessToken() {
    return accessToken;
  }

  /**
   * Sets the access token used to access protected resources.
   * 
   * @param accessToken The access token to set
   */
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  /**
   * Gets the type of this token. Is always {@code Bearer}.
   * 
   * @return The token type
   */
  public String getTokenType() {
    return tokenType;
  }

  /**
   * Gets the type of this token. Should always be {@code Bearer}.
   * 
   * @param tokenType The token type to set
   */
  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  /**
   * Gets the lifetime the access token is valid in seconds.
   * 
   * @return The lifetime of this token
   */
  public int getExpiresIn() {
    return expiresIn;
  }

  /**
   * Sets the lifetime the access token is valid in seconds.
   * 
   * @param expiresIn The lifetime to set
   */
  public void setExpiresIn(int expiresIn) {
    this.expiresIn = expiresIn;
  }

  /**
   * Gets the refresh token used for refreshing the access token.
   * 
   * @return The refresh token
   */
  public String getRefreshToken() {
    return refreshToken;
  }

  /**
   * Sets the refresh token used for refreshing the access token.
   * 
   * @param refreshToken The refresh token to set
   */
  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  /**
   * Gets the timestamp when this Token was created.
   * 
   * @return The creation timestamp
   */
  public long getCreationTime() {
    return creationTime;
  }

  /**
   * Sets the timestamp when this Token was created.
   * 
   * @param creationTime The creation time to set
   */
  public void setCreationTime(long creationTime) {
    this.creationTime = creationTime;
  }

  /**
   * Gets the remaining time the access token is valid in seconds.
   * 
   * @return The time this token expires in
   */
  public long getRemainingTime() {
    long remaining = (getCreationTime() + getExpiresIn()) - (System.currentTimeMillis() / 1000L);
    if (remaining < 0) {
      remaining = 0;
    }
    return remaining;
  }

  @Override
  public String toString() {
    return "Token [getAccessToken()=" + getAccessToken() + ", getTokenType()=" + getTokenType()
        + ", getExpiresIn()=" + getExpiresIn() + ", getRefreshToken()=" + getRefreshToken()
        + ", getCreationTime()=" + getCreationTime() + "]";
  }

}
