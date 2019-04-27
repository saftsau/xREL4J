/*
 * Copyright 2018, 2019 saftsau
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

package com.github.saftsau.xrel4j.favorite;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.saftsau.xrel4j.extinfo.ExtInfo;

/**
 * Class represents the Response you get when adding or deleting an entry.
 */
public class FavoriteAddDelEntry {

  @JsonProperty(value = "fav_list")
  private Favorite favList;
  @JsonProperty(value = "ext_info")
  private ExtInfo extInfo;

  /**
   * Gets the favorite list that the entry was added to or deleted from.
   * 
   * @return The favorite
   */
  public Favorite getFavList() {
    return favList;
  }

  /**
   * Sets the favorite list that the entry was added to or deleted from.
   * 
   * @param favList The favorite to set
   */
  public void setFavList(Favorite favList) {
    this.favList = favList;
  }

  /**
   * Gets the ExtInfo that was added or deleted.
   * 
   * @return The ExtInfo
   */
  public ExtInfo getExtInfo() {
    return extInfo;
  }

  /**
   * Sets the ExtInfo that was added or deleted.
   * 
   * @param extInfo The ExtInfo to set
   */
  public void setExtInfo(ExtInfo extInfo) {
    this.extInfo = extInfo;
  }

  @Override
  public String toString() {
    return "FavoriteAddDelEntry [getFavList()=" + getFavList() + ", getExtInfo()=" + getExtInfo()
        + "]";
  }

}
