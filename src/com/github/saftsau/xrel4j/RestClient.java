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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

class RestClient {

  private static final class InstanceHolder {
    static final RestClient INSTANCE = new RestClient();
  }

  private static Retrofit retrofit;
  private static XrelService xrelService;
  private final static String xrelUrl = "https://api.xrel.to/v2/";

  public static RestClient getInstance() {
    if (retrofit == null) {
      retrofit = new Retrofit.Builder().baseUrl(xrelUrl)
          .addConverterFactory(JacksonConverterFactory.create())
          .client(new OkHttpClient.Builder().addInterceptor(new ResponseInterceptor()).build())
          .build();

      xrelService = retrofit.create(XrelService.class);
    }
    return InstanceHolder.INSTANCE;
  }

  private RestClient() {}

  public String getOAuth2Auth(String responseType, String clientId, Optional<String> redirectUri,
      Optional<String> state, Optional<String[]> scope) {
    String url;
    try {
      url = xrelUrl + "oauth2/auth?response_type=" + URLEncoder.encode(responseType, "UTF-8")
          + "&client_id=" + clientId;
      if (redirectUri.isPresent()) {
        url = url + "&redirect_uri=" + URLEncoder.encode(redirectUri.get(), "UTF-8");
      }
      if (state.isPresent()) {
        url = url + "&state=" + URLEncoder.encode(state.get(), "UTF-8");
      }
      if (scope.isPresent() && scope.get().length > 0) {
        url = url + "&scope=" + URLEncoder.encode(String.join(" ", scope.get()), "UTF-8");
      }
    } catch (UnsupportedEncodingException e) {
      // We just wrap this as a RuntimeException because every device should understand UTF-8 and if
      // it doesn't the whole library doesn't work anyway. This makes working with this method
      // easier.
      throw new RuntimeException(e);
    }
    return url;
  }

  public Retrofit getRetrofit() {
    return retrofit;
  }

  public XrelService getXrelService() {
    return xrelService;
  }
}
