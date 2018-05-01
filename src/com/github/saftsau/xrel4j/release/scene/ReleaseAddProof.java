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

package com.github.saftsau.xrel4j.release.scene;

import com.github.saftsau.xrel4j.Xrel;
import java.util.List;
import javax.json.bind.annotation.JsonbProperty;

/**
 * Class represents an
 * {@link Xrel#postReleaseAddProof(List, String, com.github.saftsau.xrel4j.Token)} result as
 * provided by the xREL API.
 */
public class ReleaseAddProof {

  @JsonbProperty("proof_url")
  private String proofUrl;
  private List<Release> releases;

  /**
   * Gets the URL of the proof image.
   * 
   * @return The proof URL
   */
  public String getProofUrl() {
    return proofUrl;
  }

  /**
   * Sets the URL of the proof image.
   * 
   * @param proofUrl The proof URL to set
   */
  public void setProofUrl(String proofUrl) {
    this.proofUrl = proofUrl;
  }

  /**
   * Gets the {@link List} of {@link Release} this proof belongs to.
   * 
   * @return The releases
   */
  public List<Release> getReleases() {
    return releases;
  }

  /**
   * Sets the {@link List} of {@link Release} this proof belongs to.
   * 
   * @param releases The releases to set
   */
  public void setReleases(List<Release> releases) {
    this.releases = releases;
  }

  @Override
  public String toString() {
    return "ReleaseAddProof [getProofUrl()=" + getProofUrl() + ", getReleases()=" + getReleases()
        + "]";
  }

}
