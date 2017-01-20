package com.gebros.platform.net.okhttp;

/** The source of an HTTP response. */
public enum ResponseSource {

  /** The response was returned from the local cache. */
  CACHE,

  /**
   * The response is available in the cache but must be validated with the
   * network. The cache result will be used if it is still valid; otherwise
   * the network's response will be used.
   */
  CONDITIONAL_CACHE,

  /** The response was returned from the network. */
  NETWORK,

  /**
   * The request demanded a cached response that the cache couldn't satisfy.
   * This yields a 504 (Gateway Timeout) response as specified by
   * http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.9.4.
   */
  NONE;

  public boolean requiresConnection() {
    return this == CONDITIONAL_CACHE || this == NETWORK;
  }

  public boolean usesCache() {
    return this == CACHE || this == CONDITIONAL_CACHE;
  }
}
