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
 * Class used to represent ratings used on {@link Comment}.
 */
public class Rating {

  private int video;
  private int audio;

  /**
   * Gets the video rating of this comment. {@code 0} if not rated.
   * 
   * @return The video rating
   */
  public int getVideo() {
    return video;
  }

  /**
   * Sets the video rating of this comment. {@code 0} to unset.
   * 
   * @param video The video rating to set
   */
  public void setVideo(int video) {
    this.video = video;
  }

  /**
   * Gets the audio rating of this comment. {@code 0} if not rated.
   * 
   * @return The audio rating
   */
  public int getAudio() {
    return audio;
  }

  /**
   * Sets the audio rating of this comment. {@code 0} to unset.
   * 
   * @param audio The audio rating to set
   */
  public void setAudio(int audio) {
    this.audio = audio;
  }

  @Override
  public String toString() {
    return "Rating [getVideo()=" + getVideo() + ", getAudio()=" + getAudio() + "]";
  }

}
