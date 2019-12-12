package com.vqd.tme.na2a.auth;

import org.springframework.core.NamedThreadLocal;

public class AuthorizationHolder {
  private static final ThreadLocal<String> tokenHolder =
      new NamedThreadLocal<>("Authorization token");

  public static void set(String value) {
    tokenHolder.set(value);
  }

  public static void reset() {
    tokenHolder.remove();
  }

  public static String get() {
    return tokenHolder.get();
  }
}
