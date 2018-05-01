/*
 * Copyright 2018 saftsau
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

import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

/**
 * Class to handle gzip and deflate compressed responses and request them via a header.
 */
@Priority(Priorities.ENTITY_CODER)
@Provider
class CompressionHelper implements ReaderInterceptor, ClientRequestFilter {

  @Override
  public Object aroundReadFrom(ReaderInterceptorContext context)
      throws IOException, WebApplicationException {
    final MultivaluedMap<String, String> headers = context.getHeaders();
    final List<String> contentEncodingList = headers.get(HttpHeaders.CONTENT_ENCODING);

    // Check if we have any encoding set
    if (contentEncodingList != null) {
      // We must have exactly 1 encoding or we can't decompress the content
      if (contentEncodingList.size() == 1) {
        final String contentEncoding = contentEncodingList.get(0);
        // We only support gzip and deflate
        if (contentEncoding.equalsIgnoreCase("gzip")) {
          GZIPInputStream gzipInputStream = new GZIPInputStream(context.getInputStream());
          context.setInputStream(gzipInputStream);
        } else if (contentEncoding.equalsIgnoreCase("deflate")) {
          InflaterInputStream inflaterInputStream =
              new InflaterInputStream(context.getInputStream());
          context.setInputStream(inflaterInputStream);
        } else {
          throw new IOException("Given Content-Encoding is not supported: " + contentEncoding);
        }
      } else {
        throw new IOException("Multiple Content-Encoding headers found");
      }
    }
    return context.proceed();
  }

  @Override
  public void filter(ClientRequestContext requestContext) throws IOException {
    requestContext.getHeaders().putSingle(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate");
  }

}
