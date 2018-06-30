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

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class represents an object containing a lists of {@link ExtInfo}. This is used for
 * {@link ExtInfo} search operations on the xREL API.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ext_info_search")
public class ExtInfoSearchResult {

  private int total;
  @XmlElement(name = "ext_info", type = ExtInfo.class)
  @XmlElementWrapper
  private List<ExtInfo> results;

  /**
   * Gets the total number of result (as returned by the API, not computed).
   * 
   * @return The total number of results
   */
  public int getTotal() {
    return total;
  }

  /**
   * Sets the total number of result.
   * 
   * @param total The total number of results to set
   */
  public void setTotal(int total) {
    this.total = total;
  }

  /**
   * Gets the list of {@link ExtInfo}. Can be {@code null}.
   * 
   * @return The list of {@link ExtInfo}
   */
  public List<ExtInfo> getResults() {
    return results;
  }

  /**
   * Sets the list of {@link ExtInfo}. {@code null} to unset.
   * 
   * @param results The list of {@link ExtInfo} to set
   */
  public void setResults(List<ExtInfo> results) {
    this.results = results;
  }

  @Override
  public String toString() {
    return "ExtInfoSearchResult [getTotal()=" + getTotal() + ", getResults()=" + getResults() + "]";
  }

}
