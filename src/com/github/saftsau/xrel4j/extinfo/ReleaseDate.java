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

import java.time.LocalDate;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Class represents a release date an {@link ExtInfo} can contain.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ReleaseDate {

  private String type;
  @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
  private LocalDate date;

  /**
   * Gets the type of the release date. Possible values are {@code de-cine}, {@code de-retail},
   * {@code de-rental}, {@code de-hd}, {@code de-web}, {@code us-cine}, {@code us-retail},
   * {@code us-rental}, {@code us-hd}, {@code us-web}, {@code r5}.
   * 
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the type of the release date. Possible values are {@code de-cine}, {@code de-retail},
   * {@code de-rental}, {@code de-hd}, {@code de-web}, {@code us-cine}, {@code us-retail},
   * {@code us-rental}, {@code us-hd}, {@code us-web}, {@code r5}.
   * 
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Gets the actual date of the release date.
   * 
   * @return The date
   */
  public LocalDate getDate() {
    return date;
  }

  /**
   * Sets the actual date of the release date.
   * 
   * @param date The date to set
   */
  public void setDate(LocalDate date) {
    this.date = date;
  }

  @Override
  public String toString() {
    return "ReleaseDate [getType()=" + getType() + ", getDate()=" + getDate() + "]";
  }

}
