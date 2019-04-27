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

package com.github.saftsau.xrel4j.comment;

/**
 * Class used to represent edits used on {@link Comment}.
 */
public class Edits {

  private int count;
  private long last;

  /**
   * Gets the number of times this edit has.
   * 
   * @return The number of edits
   */
  public int getCount() {
    return count;
  }

  /**
   * Sets the number of times this edit has.
   * 
   * @param count The number of edits to set
   */
  public void setCount(int count) {
    this.count = count;
  }

  /**
   * Gets the timestamp when this comment was last edited. Returned as a UNIX timestamp. {@code 0}
   * if never edited.
   * 
   * @return The timestamp of the last edit
   */
  public long getLast() {
    return last;
  }

  /**
   * Sets the timestamp when this comment was last edited. Should be given as a UNIX timestamp. Use
   * {@code 0} to unset.
   * 
   * @param last The timestamp of the last edit to set
   */
  public void setLast(long last) {
    this.last = last;
  }

  @Override
  public String toString() {
    return "Edits [getCount()=" + getCount() + ", getLast()=" + getLast() + "]";
  }

}
