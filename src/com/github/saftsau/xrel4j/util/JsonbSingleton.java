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

package com.github.saftsau.xrel4j.util;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.config.PropertyNamingStrategy;

/**
 * Class used for creating a single Jsonb instance to be used in the whole program.
 */
public class JsonbSingleton {

  private static JsonbSingleton instance;
  private Jsonb jsonb;

  /**
   * Either creates a new instance of this Singleton or returns the already existing one.
   * 
   * @return The JsonbSingleton
   */
  public static synchronized JsonbSingleton getInstance() {
    if (JsonbSingleton.instance == null) {
      JsonbSingleton.instance = new JsonbSingleton();
    }
    return JsonbSingleton.instance;
  }

  private JsonbSingleton() {
    JsonbConfig jsonbConfig = new JsonbConfig();
    jsonbConfig.withPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES);
    this.jsonb = JsonbBuilder.create(jsonbConfig);
  }

  /**
   * The {@link Jsonb} object to use throughout the use of this library with the needed
   * configurations set.
   * 
   * @return The {@link Jsonb} object to use
   */
  public Jsonb getJsonb() {
    return jsonb;
  }

}
