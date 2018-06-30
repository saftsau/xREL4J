/*
 * Copyright 2018 saftsau
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

import javax.json.bind.annotation.JsonbProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class represents the Response you get when marking an entry as read.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "fav_list_markread")
public class FavoriteMarkRead {

  @JsonbProperty(value = "fav_list")
  @XmlElement(name = "fav_list")
  private Favorite favList;

  /**
   * Gets the favorite list that the entry was marked as read.
   * 
   * @return The favorite
   */
  public Favorite getFavList() {
    return favList;
  }

  /**
   * Sets the favorite list that the entry was was marked as read.
   * 
   * @param favList The favorite to set
   */
  public void setFavList(Favorite favList) {
    this.favList = favList;
  }

  @Override
  public String toString() {
    return "FavoriteMarkRead [getFavList()=" + getFavList() + "]";
  }

}
