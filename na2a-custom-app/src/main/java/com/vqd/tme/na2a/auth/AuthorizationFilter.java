package com.vqd.tme.na2a.auth;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * Filter for catching authorization header and setting it for this
 * thread.
 *
 * @author edgardegraaff
 *
 */
@Component
public class AuthorizationFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String authorization = ((HttpServletRequest) request).getHeader("Authorization");

    if (authorization != null) {
      AuthorizationHolder.set(authorization);
    }
    try {
      chain.doFilter(request, response);
    } finally {
      if (authorization != null) {
        AuthorizationHolder.reset();
      }
    }
  }
}
