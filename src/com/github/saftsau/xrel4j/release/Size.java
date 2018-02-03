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

package com.github.saftsau.xrel4j.release;

import com.github.saftsau.xrel4j.release.scene.Release;

/**
 * Class represents a size as used in {@link Release} objects.
 */
public class Size {

  private int number;
  private String unit;

  /**
   * Gets the number of the size. {@code -1} if not retrieved.
   * 
   * @return The size number
   */
  public int getNumber() {
    return number;
  }

  /**
   * Sets the number of the size. Use {@code -1} to unset.
   * 
   * @param number The size number to set
   */
  public void setNumber(int number) {
    this.number = number;
  }

  /**
   * Gets the unit of the size. Can be an empty {@link String} if not retrieved.
   * 
   * @return The size's unit
   */
  public String getUnit() {
    return unit;
  }

  /**
   * Sets the unit of the size of this Release. Use an empty {@link String} to unset.
   * 
   * @param unit The size's unit to set
   */
  public void setUnit(String unit) {
    this.unit = unit;
  }

  @Override
  public String toString() {
    return "Size [getNumber()=" + getNumber() + ", getUnit()=" + getUnit() + "]";
  }

}
