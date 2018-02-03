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

import com.github.saftsau.xrel4j.release.p2p.P2pRelease;
import com.github.saftsau.xrel4j.release.scene.Release;

/**
 * Class represents a comment on any {@link Release} or {@link P2pRelease}.
 */
public class Comment {

  private String id;
  private long time;
  private Author author;
  private String text;
  private String linkHref;
  private Rating rating;
  private Votes votes;
  private Edits edits;

  /**
   * Gets the ID of this Comment.
   * 
   * @return The ID
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the ID of this Comment.
   * 
   * @param id The ID to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets the time this Comment was posted. Returned as a UNIX timestamp.
   * 
   * @return The timestamp
   */
  public long getTime() {
    return time;
  }

  /**
   * Sets the time this Comment was posted. Should be given as a UNIX timestamp.
   * 
   * @param time The timestamp to set
   */
  public void setTime(long time) {
    this.time = time;
  }

  /**
   * Gets the author of this comment.
   * 
   * @return The author
   */
  public Author getAuthor() {
    return author;
  }

  /**
   * Sets the author of this comment.
   * 
   * @param author The author to set
   */
  public void setAuthor(Author author) {
    this.author = author;
  }

  /**
   * Gets the text content of this comment.
   * 
   * @return The text
   */
  public String getText() {
    return text;
  }

  /**
   * Sets the text content of this comment.
   * 
   * @param text The text to set
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * Gets the link of this comment.
   * 
   * @return The link
   */
  public String getLinkHref() {
    return linkHref;
  }

  /**
   * Sets the link of this comment.
   * 
   * @param linkHref The link to set
   */
  public void setLinkHref(String linkHref) {
    this.linkHref = linkHref;
  }

  /**
   * Gets the rating of this comment.
   * 
   * @return The rating
   */
  public Rating getRating() {
    return rating;
  }

  /**
   * Sets the rating of this comment.
   * 
   * @param rating The rating to set
   */
  public void setRating(Rating rating) {
    this.rating = rating;
  }

  /**
   * Gets the votes of this comment.
   * 
   * @return the votes
   */
  public Votes getVotes() {
    return votes;
  }

  /**
   * Sets the votes of this comment.
   * 
   * @param votes The votes to set
   */
  public void setVotes(Votes votes) {
    this.votes = votes;
  }

  /**
   * Gets the edits of this comment.
   * 
   * @return The edits
   */
  public Edits getEdits() {
    return edits;
  }

  /**
   * Sets the edits of this comment.
   * 
   * @param edits The edits to set
   */
  public void setEdits(Edits edits) {
    this.edits = edits;
  }

  @Override
  public String toString() {
    return "Comment [getId()=" + getId() + ", getTime()=" + getTime() + ", getAuthor()="
        + getAuthor() + ", getText()=" + getText() + ", getLinkHref()=" + getLinkHref()
        + ", getRating()=" + getRating() + ", getVotes()=" + getVotes() + ", getEdits()="
        + getEdits() + "]";
  }

}
