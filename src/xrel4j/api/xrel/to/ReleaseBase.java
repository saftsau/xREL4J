package xrel4j.api.xrel.to;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class represents a base release which contains information shared by
 * {@link Release} and {@link P2pRelease}. This is used as the parent class of
 * these two classes.
 */
public abstract class ReleaseBase {

	private String id;
	private String dirname;
	private String linkHref;
	private int numRatings;
	private double videoRating;
	private double audioRating;
	private ExtInfo extInfo;
	private int tvSeason;
	private int tvEpisode;
	private int comments;

	/**
	 * Constructs a new ReleaseBase from a {@link JSONObject} returned from the
	 * xREL API.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this ReleaseBase
	 * @throws JSONException
	 */
	ReleaseBase(JSONObject jsonObject) throws JSONException {
		parseBaseJsonObject(jsonObject, null);
	}

	/**
	 * Constructs a new Release from a {@link JSONObject} returned from the xREL
	 * API and an already existing ExtInfo.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this ReleaseBase
	 * @param extInfo
	 *            The previously existing {@link ExtInfo}
	 * @throws JSONException
	 */
	ReleaseBase(JSONObject jsonObject, ExtInfo extInfo) throws JSONException {
		parseBaseJsonObject(jsonObject, extInfo);
	}

	/**
	 * Parses a given {@link JSONObject} and saves its values to this object.
	 * 
	 * @param jsonObject
	 *            The {@link JSONObject} representation of this ReleaseBase
	 * @throws JSONException
	 */
	void parseBaseJsonObject(JSONObject jsonObject, ExtInfo extInfo) throws JSONException {
		setId(jsonObject.getString("id"));
		setDirname(jsonObject.getString("dirname"));
		setLinkHref(jsonObject.getString("link_href"));
		setNumRatings(jsonObject.optInt("num_ratings", -1));
		setVideoRating(jsonObject.optDouble("video_rating", -1));
		setAudioRating(jsonObject.optDouble("audio_rating", -1));
		if (extInfo != null) {
			setExtInfo(extInfo);
		} else {
			setExtInfo(new ExtInfo(jsonObject.getJSONObject("ext_info")));
		}
		setTvSeason(jsonObject.optInt("tv_season", -1));
		setTvEpisode(jsonObject.optInt("tv_episode", -1));
		setComments(jsonObject.optInt("comments", -1));
	}

	/**
	 * Gets the API ID of this Release.
	 * 
	 * @return The ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the API ID of this Release.
	 * 
	 * @param id
	 *            The ID to set
	 */
	private void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the dirname of this Release.
	 * 
	 * @return The dirname
	 */
	public String getDirname() {
		return dirname;
	}

	/**
	 * Sets the dirname of this Release.
	 * 
	 * @param dirname
	 *            The dirname to set
	 */
	private void setDirname(String dirname) {
		this.dirname = dirname;
	}

	/**
	 * Gets the link of this Release.
	 * 
	 * @return The link
	 */
	public String getLinkHref() {
		return linkHref;
	}

	/**
	 * Sets the link of this Release.
	 * 
	 * @param linkHref
	 *            The link to set
	 */
	private void setLinkHref(String linkHref) {
		this.linkHref = linkHref;
	}

	/**
	 * Gets the number of ratings of this Release. {@code -1} if not retrieved.
	 * 
	 * @return The number of ratings
	 */
	public int getNumRatings() {
		return numRatings;
	}

	/**
	 * Sets the number of ratings of this Release. Use {@code -1} to unset.
	 * 
	 * @param numRatings
	 *            The number of ratings to set
	 */
	private void setNumRatings(int numRatings) {
		this.numRatings = numRatings;
	}

	/**
	 * Gets the video rating of this release. {@code -1} if not retrieved,
	 * {@code 0} if not rated.
	 * 
	 * @return The video rating
	 */
	public double getVideoRating() {
		return videoRating;
	}

	/**
	 * Sets the video rating of this release. Use {@code -1} to unset.
	 * 
	 * @param videoRating
	 *            The video rating to set
	 */
	private void setVideoRating(double videoRating) {
		this.videoRating = videoRating;
	}

	/**
	 * Gets the audio rating of this release. {@code -1} if not retrieved,
	 * {@code 0} if not rated.
	 * 
	 * @return The audio rating
	 */
	public double getAudioRating() {
		return audioRating;
	}

	/**
	 * Sets the audio rating of this release. {@code -1} if not retrieved,
	 * {@code 0} if not rated.
	 * 
	 * @param audioRating
	 *            The audio rating to set
	 */
	private void setAudioRating(double audioRating) {
		this.audioRating = audioRating;
	}

	/**
	 * Gets the ExtInfo associated with this Release. Is never {@code null}.
	 * 
	 * @return The ExtInfo
	 */
	public ExtInfo getExtInfo() {
		return extInfo;
	}

	/**
	 * Sets the ExtInfo associated with this Release. Should not be set to
	 * {@code null}.
	 * 
	 * @param extInfo
	 *            The ExtInfo to set
	 */
	private void setExtInfo(ExtInfo extInfo) {
		this.extInfo = extInfo;
	}

	/**
	 * Gets the season number of this release. {@code -1} if not retrieved or it
	 * has no season.
	 * 
	 * @return The season number
	 */
	public int getTvSeason() {
		return tvSeason;
	}

	/**
	 * Sets the season number of this release. {@code -1} to unset.
	 * 
	 * @param tvSeason
	 *            The season number to set
	 */
	private void setTvSeason(int tvSeason) {
		this.tvSeason = tvSeason;
	}

	/**
	 * Gets the episode number of this release. {@code -1} if not retrieved or
	 * it has no episode.
	 * 
	 * @return The episode number
	 */
	public int getTvEpisode() {
		return tvEpisode;
	}

	/**
	 * Sets the episode number of this release. {@code -1} to unset.
	 * 
	 * @param tvEpisode
	 *            The episode number to set
	 */
	private void setTvEpisode(int tvEpisode) {
		this.tvEpisode = tvEpisode;
	}

	/**
	 * Gets the number of comments this release has. {@code -1} if not
	 * retrieved.
	 * 
	 * @return The number of comments
	 */
	public int getComments() {
		return comments;
	}

	/**
	 * Sets the number of comments this release has. {@code -1} to unset.
	 * 
	 * @param comments
	 *            The comments to set
	 */
	private void setComments(int comments) {
		this.comments = comments;
	}

}