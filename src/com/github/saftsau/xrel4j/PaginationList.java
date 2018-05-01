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

import java.util.List;
import javax.json.bind.annotation.JsonbProperty;

/**
 * Class used for storing a paginated list of any content.
 * 
 * @param <T> The type of the content of this list
 */
public class PaginationList<T> {

  @JsonbProperty("total_count")
  private long totalCount;
  private Pagination pagination;
  private List<T> list;

  /**
   * Gets the total count of entries that are available.
   * 
   * @return The total count
   */
  public long getTotalCount() {
    return totalCount;
  }

  /**
   * Sets the total count of entries that are available.
   * 
   * @param totalCount The total count to set
   */
  public void setTotalCount(long totalCount) {
    this.totalCount = totalCount;
  }

  /**
   * Gets the Pagination that is currently used.
   * 
   * @return The pagination
   */
  public Pagination getPagination() {
    return pagination;
  }

  /**
   * Sets the Pagination that is currently used.
   * 
   * @param pagination the pagination to set
   */
  public void setPagination(Pagination pagination) {
    this.pagination = pagination;
  }

  /**
   * Gets the currently retrieved list of entries.
   * 
   * @return the list
   */
  public List<T> getList() {
    return list;
  }

  /**
   * Sets the currently retrieved list of entries.
   * 
   * @param list the list to set
   */
  public void setList(List<T> list) {
    this.list = list;
  }

  @Override
  public String toString() {
    return "PaginationList [getTotalCount()=" + getTotalCount() + ", getPagination()="
        + getPagination() + ", getList()=" + getList() + "]";
  }

}
