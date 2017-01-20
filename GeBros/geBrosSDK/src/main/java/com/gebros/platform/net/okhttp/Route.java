package com.gebros.platform.net.okhttp;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * The concrete route used by a connection to reach an abstract origin server.
 * When creating a connection the client has many options:
 * <ul>
 *   <li><strong>HTTP proxy:</strong> a proxy server may be explicitly
 *       configured for the client. Otherwise the {@link java.net.ProxySelector
 *       proxy selector} is used. It may return multiple proxies to attempt.
 *   <li><strong>IP address:</strong> whether connecting directly to an origin
 *       server or a proxy, opening a socket requires an IP address. The DNS
 *       server may return multiple IP addresses to attempt.
 *   <li><strong>Modern TLS:</strong> whether to include advanced TLS options
 *       when attempting a HTTPS connection.
 * </ul>
 * Each route is a specific selection of these options.
 */
public class Route {
  final Address address;
  final Proxy proxy;
  final InetSocketAddress inetSocketAddress;
  final boolean modernTls;

  public Route(Address address, Proxy proxy, InetSocketAddress inetSocketAddress,
               boolean modernTls) {
    if (address == null) throw new NullPointerException("address == null");
    if (proxy == null) throw new NullPointerException("proxy == null");
    if (inetSocketAddress == null) throw new NullPointerException("inetSocketAddress == null");
    this.address = address;
    this.proxy = proxy;
    this.inetSocketAddress = inetSocketAddress;
    this.modernTls = modernTls;
  }

  /** Returns the {@link Address} of this route. */
  public Address getAddress() {
    return address;
  }

  /**
   * Returns the {@link Proxy} of this route.
   *
   * <strong>Warning:</strong> This may disagree with {@link Address#getProxy}
   * is null. When the address's proxy is null, the proxy selector will be used.
   */
  public Proxy getProxy() {
    return proxy;
  }

  /** Returns the {@link InetSocketAddress} of this route. */
  public InetSocketAddress getSocketAddress() {
    return inetSocketAddress;
  }

  /** Returns true if this route uses modern TLS. */
  public boolean isModernTls() {
    return modernTls;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Route) {
      Route other = (Route) obj;
      return (address.equals(other.address)
          && proxy.equals(other.proxy)
          && inetSocketAddress.equals(other.inetSocketAddress)
          && modernTls == other.modernTls);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + address.hashCode();
    result = 31 * result + proxy.hashCode();
    result = 31 * result + inetSocketAddress.hashCode();
    result = result + (modernTls ? (31 * result) : 0);
    return result;
  }
}
