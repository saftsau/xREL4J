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

package com.github.saftsau.xrel4j.extinfo;

/**
 * Class represents an external source used on {@link External}.
 */
public class ExternalSource {

  private long id;
  private String name;

  /**
   * Gets the API ID of the source of this ExternalSource.
   * 
   * @return The ID
   */
  public long getId() {
    return id;
  }

  /**
   * Sets the API ID of the source of this ExternalSource.
   * 
   * @param id The ID to set
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * Gets the name of the source of this ExternalSource.
   * 
   * @return The name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the source of this ExternalSource.
   * 
   * @param name The name to set
   */
  public void setName(String name) {
    this.name = name;
  }

}
