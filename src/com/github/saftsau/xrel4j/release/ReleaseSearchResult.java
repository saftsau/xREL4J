/*
 * Copyright 2017 - 2019 saftsau
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

package com.github.saftsau.xrel4j.release;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.saftsau.xrel4j.release.p2p.P2pRelease;
import com.github.saftsau.xrel4j.release.scene.Release;
import java.util.List;

/**
 * Class represents an object containing two lists of {@link Release} and {@link P2pRelease}. This
 * is used for {@link Release} / {@link P2pRelease} search operations on the xREL API, which can
 * return both types.
 */
public class ReleaseSearchResult {

  private int total;
  private List<Release> results;
  @JsonProperty("p2p_results")
  private List<P2pRelease> p2pResults;

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
   * Gets the list of {@link Release}. Can be {@code null}.
   * 
   * @return The list of {@link Release}
   */
  public List<Release> getResults() {
    return results;
  }

  /**
   * Sets the list of {@link Release}. {@code null} to unset.
   * 
   * @param results The list of {@link Release} to set
   */
  public void setResults(List<Release> results) {
    this.results = results;
  }

  /**
   * Gets the list of {@link P2pRelease}. Can be {@code null}.
   * 
   * @return The list of {@link P2pRelease}
   */
  public List<P2pRelease> getP2pResults() {
    return p2pResults;
  }

  /**
   * Sets the list of {@link P2pRelease}. {@code null} to unset.
   * 
   * @param p2pResults The list of {@link P2pRelease} to set
   */
  public void setP2pResults(List<P2pRelease> p2pResults) {
    this.p2pResults = p2pResults;
  }

  @Override
  public String toString() {
    return "ReleaseSearchResult [getTotal()=" + getTotal() + ", getResults()=" + getResults()
        + ", getP2pResults()=" + getP2pResults() + "]";
  }

}
