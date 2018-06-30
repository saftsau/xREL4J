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

package com.github.saftsau.xrel4j.release.scene;

import java.util.List;
import javax.json.bind.annotation.JsonbProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class represents a proof. This is retrieved partially by the xREL API after the user posted one.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Proof {

  @JsonbProperty("proof_url")
  @XmlElement(name = "proof_url")
  private String proofUrl;
  @JsonbProperty("release_list")
  @XmlElement(name = "release_list")
  private List<Release> releaseList;

  /**
   * Gets the link of this proof.
   * 
   * @return The link
   */
  public String getProofUrl() {
    return proofUrl;
  }

  /**
   * Sets the link of this proof.
   * 
   * @param proofUrl The link to set
   */
  public void setProofUrl(String proofUrl) {
    this.proofUrl = proofUrl;
  }

  /**
   * Gets the list of {@link Release} this Proof belongs to. Can be empty.
   * 
   * @return The list of {@link Release}
   */
  public List<Release> getReleaseList() {
    return releaseList;
  }

  /**
   * Sets the list of {@link Release} this Proof belongs to. Can be empty.
   * 
   * @param releaseList The list of {@link Release} to set
   */
  public void setReleaseList(List<Release> releaseList) {
    this.releaseList = releaseList;
  }

  @Override
  public String toString() {
    return "Proof [getProofUrl()=" + getProofUrl() + ", getReleaseList()=" + getReleaseList() + "]";
  }

}
