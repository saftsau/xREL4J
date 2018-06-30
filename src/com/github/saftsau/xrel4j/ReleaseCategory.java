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

import java.util.Map;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Class represents a category as used in the Release section on xREL.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "category")
public class ReleaseCategory {

  private String name;
  @JsonbTransient
  @XmlTransient
  private ReleaseCategory parentCat;
  @JsonbProperty(value = "parent_cat")
  @XmlElement(name = "parent_cat")
  private String parentCatName;

  /**
   * Sets the {@link #parentCat} of this ReleaseCategory. As this depends on other
   * {@link ReleaseCategory}, this should be called after all of them are saved and can be compared.
   * 
   * @param releaseCategories The {@link Map} containing all {@link ReleaseCategory} and their
   *        {@link #name}.
   */
  void setParentCategories(Map<String, ReleaseCategory> releaseCategories) {
    if (!getParentCatName().isEmpty() && releaseCategories.containsKey(getParentCatName())) {
      setParentCat(releaseCategories.get(getParentCatName()));
    }
  }

  /**
   * Gets the name of this ReleaseCategory.
   * 
   * @return The name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of this ReleaseCategory.
   * 
   * @param name The name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the parent category of this ReleaseCategory. {@code null} if not set.
   * 
   * @return The parent category
   */
  public ReleaseCategory getParentCat() {
    return parentCat;
  }

  /**
   * Sets the parent category of this ReleaseCategory. {@code null} to unset.
   * 
   * @param parentCat The parent category to set
   */
  public void setParentCat(ReleaseCategory parentCat) {
    this.parentCat = parentCat;
  }

  /**
   * Gets the parent category of this ReleaseCategory in {@link String} representation. Can be an
   * empty {@link String}.
   * 
   * @return The parent category as a {@link String}
   */
  public String getParentCatName() {
    return parentCatName;
  }

  /**
   * Sets the parent category of this ReleaseCategory in {@link String} representation. Use an empty
   * {@link String} to unset.
   * 
   * @param parentCatName The parent category as a {@link String} to set
   */
  public void setParentCatName(String parentCatName) {
    this.parentCatName = parentCatName;
  }

  @Override
  public String toString() {
    return "ReleaseCategory [getName()=" + getName() + ", getParentCat()=" + getParentCat()
        + ", getParentCatName()=" + getParentCatName() + "]";
  }

}
