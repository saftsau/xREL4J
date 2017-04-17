/*
 * Copyright 2017 saftsau
 *
 * This file is part of xREL4J.
 *
 * xREL4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * xREL4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with xREL4J.  If not, see <http://www.gnu.org/licenses/>.
 */
package xrel4j.api.xrel.to;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class represents a comment on any {@link Release} or {@link P2pRelease}.
 */
public class Comment {

	private String id;
	private long time;
	private String authorId;
	private String authorName;
	private String text;
	private String linkHref;
	private int ratingVideo;
	private int ratingAudio;
	private int votesPositive;
	private int votesNegative;
	private int editsCount;
	private long editsLast;

	/**
	 * Constructs a new Comment from a {@link JSONObject} returned from the xREL
	 * API.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this Comment
	 * @throws JSONException
	 */
	Comment(JSONObject jsonObject) throws JSONException {
		parseJsonObject(jsonObject);
	}

	/**
	 * Parses a given {@link JSONObject} and saves its values to this object.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this Comment
	 * @throws JSONException
	 */
	void parseJsonObject(JSONObject jsonObject) throws JSONException {
		setId(jsonObject.getString("id"));
		setTime(jsonObject.getLong("time"));
		JSONObject author = jsonObject.getJSONObject("author");
		setAuthorId(author.getString("id"));
		setAuthorName(author.getString("name"));
		setText(jsonObject.getString("text"));
		setLinkHref(jsonObject.getString("link_href"));
		JSONObject rating = jsonObject.getJSONObject("rating");
		setRatingVideo(rating.optInt("video", 0));
		setRatingAudio(rating.optInt("audio", 0));
		JSONObject votes = jsonObject.getJSONObject("votes");
		setVotesPositive(votes.optInt("positive", 0));
		setVotesNegative(votes.optInt("negative", 0));
		JSONObject edits = jsonObject.getJSONObject("edits");
		setEditsCount(edits.optInt("count", 0));
		setEditsLast(edits.optLong("last", 0));
	}

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
	 * @param id
	 *            The ID to set
	 */
	private void setId(String id) {
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
	 * Sets the time this Comment was posted. Should be given as a UNIX
	 * timestamp.
	 * 
	 * @param time
	 *            The timestamp to set
	 */
	private void setTime(long time) {
		this.time = time;
	}

	/**
	 * Gets the ID of the author who posted this comment.
	 * 
	 * @return The author's ID
	 */
	public String getAuthorId() {
		return authorId;
	}

	/**
	 * Sets the ID of the author who posted this comment.
	 * 
	 * @param authorId
	 *            The author's ID to set
	 */
	private void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	/**
	 * Gets the name of the author who posted this comment.
	 * 
	 * @return The author's name
	 */
	public String getAuthorName() {
		return authorName;
	}

	/**
	 * Sets the name of the author who posted this comment.
	 * 
	 * @param authorName
	 *            The author's name
	 */
	private void setAuthorName(String authorName) {
		this.authorName = authorName;
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
	 * @param text
	 *            The text to set
	 */
	private void setText(String text) {
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
	 * @param linkHref
	 *            The link to set
	 */
	private void setLinkHref(String linkHref) {
		this.linkHref = linkHref;
	}

	/**
	 * Gets the video rating of this comment. {@code 0} if not rated.
	 * 
	 * @return The video rating
	 */
	public int getRatingVideo() {
		return ratingVideo;
	}

	/**
	 * Sets the video rating of this comment. Use {@code 0} to unset.
	 * 
	 * @param ratingVideo
	 *            The video rating to set
	 */
	private void setRatingVideo(int ratingVideo) {
		this.ratingVideo = ratingVideo;
	}

	/**
	 * Gets the audio rating of this comment. {@code 0} if not rated.
	 * 
	 * @return The audio rating
	 */
	public int getRatingAudio() {
		return ratingAudio;
	}

	/**
	 * Sets the audio rating of this comment. Use {@code 0} to unset.
	 * 
	 * @param ratingAudio
	 *            The audio rating
	 */
	private void setRatingAudio(int ratingAudio) {
		this.ratingAudio = ratingAudio;
	}

	/**
	 * Gets the number of positive votes of this comment.
	 * 
	 * @return The number of positive votes
	 */
	public int getVotesPositive() {
		return votesPositive;
	}

	/**
	 * Sets the number of positive votes of this comment.
	 * 
	 * @param votesPositive
	 *            The number of positive votes to set
	 */
	private void setVotesPositive(int votesPositive) {
		this.votesPositive = votesPositive;
	}

	/**
	 * Gets the number of negative votes of this comment.
	 * 
	 * @return The number of negative votes
	 */
	public int getVotesNegative() {
		return votesNegative;
	}

	/**
	 * Sets the number of negative votes of this comment.
	 * 
	 * @param votesNegative
	 *            The number of negative votes to set
	 */
	private void setVotesNegative(int votesNegative) {
		this.votesNegative = votesNegative;
	}

	/**
	 * Gets the number of times this comment was edited.
	 * 
	 * @return The number of edits
	 */
	public int getEditsCount() {
		return editsCount;
	}

	/**
	 * Sets the number of times this comment was edited.
	 * 
	 * @param editsCount
	 *            The number of edits to set
	 */
	private void setEditsCount(int editsCount) {
		this.editsCount = editsCount;
	}

	/**
	 * Gets the timestamp when this comment was last edited. Returned as a UNIX
	 * timestamp. {@code 0} if never edited.
	 * 
	 * @return The timestamp of the last edit
	 */
	public long getEditsLast() {
		return editsLast;
	}

	/**
	 * Sets the timestamp when this comment was last edited. Should be given as
	 * a UNIX timestamp. Use {@code 0} to unset.
	 * 
	 * @param editsLast
	 *            The timestamp of the last edit to set
	 */
	private void setEditsLast(long editsLast) {
		this.editsLast = editsLast;
	}

}