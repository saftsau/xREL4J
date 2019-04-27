/*
 * Copyright 2019 saftsau
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

package com.github.saftsau.xrel4j;

import com.github.saftsau.xrel4j.comment.Comment;
import com.github.saftsau.xrel4j.extinfo.ExtInfo;
import com.github.saftsau.xrel4j.extinfo.ExtInfoMedia;
import com.github.saftsau.xrel4j.extinfo.ExtInfoSearchResult;
import com.github.saftsau.xrel4j.favorite.Favorite;
import com.github.saftsau.xrel4j.favorite.FavoriteAddDelEntry;
import com.github.saftsau.xrel4j.favorite.FavoriteMarkRead;
import com.github.saftsau.xrel4j.release.ReleaseSearchResult;
import com.github.saftsau.xrel4j.release.p2p.P2pCategory;
import com.github.saftsau.xrel4j.release.p2p.P2pRelease;
import com.github.saftsau.xrel4j.release.scene.Release;
import com.github.saftsau.xrel4j.release.scene.ReleaseAddProof;
import java.util.List;
import java.util.Set;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Retrofit2 interface used to define all xREL v2 API endpoints and their parameters.
 */
public interface XrelService {

  static final String FORMAT = ".json";
  static final String AUTHORIZATION_HEADER = "Authorization";

  @GET("release/info" + FORMAT)
  Call<Release> releaseInfo(@Query(value = "id") String id,
      @Query(value = "dirname") String dirname);

  @GET("release/latest" + FORMAT)
  Call<PaginationList<Release>> releaseLatest(@Header(AUTHORIZATION_HEADER) String bearerToken,
      @Query(value = "per_page") int perPage, @Query(value = "page") int page,
      @Query(value = "archive") String archive, @Query(value = "filter") String filter);

  @GET("release/categories" + FORMAT)
  Call<Set<ReleaseCategory>> releaseCategories();

  @GET("release/browse_category" + FORMAT)
  Call<PaginationList<Release>> releaseBrowseCategory(
      @Query(value = "category_name") String categoryName,
      @Query(value = "ext_info_type") String extInfoType, @Query(value = "per_page") int perPage,
      @Query(value = "page") int page);

  @GET("release/ext_info" + FORMAT)
  Call<PaginationList<Release>> releaseExtInfo(@Query(value = "id") String id,
      @Query(value = "per_page") int perPage, @Query(value = "page") int page);

  @GET("release/filters" + FORMAT)
  Call<Set<Filter>> releaseFilters();

  @FormUrlEncoded
  @POST("release/addproof" + FORMAT)
  Call<ReleaseAddProof> releaseAddproof(@Header("Authorization") String authorization,
      @Field("id") Set<String> id, @Field("image") String image);

  @GET("p2p/releases" + FORMAT)
  Call<PaginationList<P2pRelease>> p2pReleases(@Query(value = "per_page") int perPage,
      @Query(value = "page") int page, @Query(value = "category_id") String categoryId,
      @Query(value = "group_id") String groupId, @Query(value = "ext_info_id") String extInfoId);

  @GET("p2p/categories" + FORMAT)
  Call<Set<P2pCategory>> p2pCategories();

  @GET("p2p/rls_info" + FORMAT)
  Call<P2pRelease> p2pRlsInfo(@Query(value = "id") String id,
      @Query(value = "dirname") String dirname);

  @GET("nfo/release" + FORMAT)
  Call<ResponseBody> nfoRelease(@Header("Authorization") String authorization,
      @Query(value = "id") String id);

  @GET("nfo/p2p_rls" + FORMAT)
  Call<ResponseBody> nfoP2pRelease(@Header("Authorization") String authorization,
      @Query(value = "id") String id);

  @GET("calendar/upcoming" + FORMAT)
  Call<List<ExtInfo>> calendarUpcoming(@Query(value = "country") String country);

  @GET("ext_info/info" + FORMAT)
  Call<ExtInfo> extInfoInfo(@Header("Authorization") String authorization,
      @Query(value = "id") String id);

  @GET("ext_info/media" + FORMAT)
  Call<List<ExtInfoMedia>> extInfoMedia(@Query(value = "id") String id);

  @FormUrlEncoded
  @POST("ext_info/rate" + FORMAT)
  Call<ExtInfo> extInfoRate(@Header("Authorization") String authorization, @Field("id") String id,
      @Field("rating") int rating);

  @GET("search/releases" + FORMAT)
  Call<ReleaseSearchResult> searchReleases(@Query(value = "q") String q,
      @Query(value = "scene") boolean scene, @Query(value = "p2p") boolean p2p,
      @Query(value = "limit") Integer limit);

  @GET("search/ext_info" + FORMAT)
  Call<ExtInfoSearchResult> searchExtInfo(@Query(value = "q") String q,
      @Query(value = "type") String type, @Query(value = "limit") Integer limit);

  @GET("favs/lists" + FORMAT)
  Call<List<Favorite>> favsLists(@Header("Authorization") String authorization);

  @GET("favs/list_entries" + FORMAT)
  Call<List<ExtInfo>> favsListEntries(@Header("Authorization") String authorization,
      @Query(value = "id") long id, @Query(value = "get_releases") boolean getReleases);

  @FormUrlEncoded
  @POST("favs/list_addentry" + FORMAT)
  Call<FavoriteAddDelEntry> favsListAddEntry(@Header("Authorization") String authorization,
      @Field("id") long id, @Field("ext_info_id") String extInfoId);

  @FormUrlEncoded
  @POST("favs/list_delentry" + FORMAT)
  Call<FavoriteAddDelEntry> favsListDelEntry(@Header("Authorization") String authorization,
      @Field("id") long id, @Field("ext_info_id") String extInfoId);

  @FormUrlEncoded
  @POST("favs/list_markread" + FORMAT)
  Call<FavoriteMarkRead> favsListMarkread(@Header("Authorization") String authorization,
      @Field("id") long id, @Field("release_id") String releaseId, @Field("type") String type);

  @GET("comments/get" + FORMAT)
  Call<PaginationList<Comment>> commentsGet(@Query(value = "id") String id,
      @Query(value = "type") String type, @Query(value = "per_page") int perPage,
      @Query(value = "page") int page);

  @FormUrlEncoded
  @POST("comments/add" + FORMAT)
  Call<Comment> commentsAdd(@Header("Authorization") String authorization,
      @Field(value = "id") String id, @Field(value = "type") String type,
      @Field(value = "text") String text, @Field(value = "video_rating") Integer videoRating,
      @Field(value = "audio_rating") Integer audioRating);

  @POST("user/info" + FORMAT)
  Call<User> userInfo(@Header("Authorization") String authorization);

  @FormUrlEncoded
  @POST("oauth2/token" + FORMAT)
  Call<Token> oauth2Token(@Field(value = "grant_type") String grantType,
      @Field(value = "client_id") String clientId,
      @Field(value = "client_secret") String clientSecret, @Field(value = "code") String code,
      @Field(value = "redirect_uri") String redirectUri,
      @Field(value = "refresh_token") String refreshToken, @Field(value = "scope") String scope);

}
