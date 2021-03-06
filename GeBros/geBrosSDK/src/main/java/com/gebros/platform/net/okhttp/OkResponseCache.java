package com.gebros.platform.net.okhttp;

import com.gebros.platform.net.okhttp.internal.http.Request;
import com.gebros.platform.net.okhttp.internal.http.Response;

import java.io.IOException;
import java.net.CacheRequest;

/**
 * An extended response cache API. Unlike {@link java.net.ResponseCache}, this
 * interface supports conditional caching and statistics.
 *
 * <h3>Warning: Experimental OkHttp 2.0 API</h3>
 * This class is in beta. APIs are subject to change!
 */
public interface OkResponseCache {
  Response get(Request request) throws IOException;

  CacheRequest put(Response response) throws IOException;

  /**
   * Remove any cache entries for the supplied {@code uri}. Returns true if the
   * supplied {@code requestMethod} potentially invalidates an entry in the
   * cache.
   */
  // TODO: this shouldn't return a boolean.
  boolean maybeRemove(Request request) throws IOException;

  /**
   * Handles a conditional request hit by updating the stored cache response
   * with the headers from {@code network}. The cached response body is not
   * updated. If the stored response has changed since {@code cached} was
   * returned, this does nothing.
   */
  void update(Response cached, Response network) throws IOException;

  /** Track an conditional GET that was satisfied by this cache. */
  void trackConditionalCacheHit();

  /** Track an HTTP response being satisfied by {@code source}. */
  void trackResponse(ResponseSource source);
}
