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

package com.github.saftsau.xrel4j.comment;

/**
 * Class used to represent votes used on {@link Comment}.
 */
public class Votes {

  private int positive;
  private int negative;

  /**
   * Gets the number of positive votes of this comment.
   * 
   * @return The number of positive votes
   */
  public int getPositive() {
    return positive;
  }

  /**
   * Sets the number of positive votes of this comment.
   * 
   * @param positive The number of positive votes to set
   */
  public void setPositive(int positive) {
    this.positive = positive;
  }

  /**
   * Gets the number of negative votes of this comment.
   * 
   * @return The number of negative votes
   */
  public int getNegative() {
    return negative;
  }

  /**
   * Sets the number of negative votes of this comment.
   * 
   * @param negative The number of negative votes to set
   */
  public void setNegative(int negative) {
    this.negative = negative;
  }

  @Override
  public String toString() {
    return "Votes [getPositive()=" + getPositive() + ", getNegative()=" + getNegative() + "]";
  }

}
