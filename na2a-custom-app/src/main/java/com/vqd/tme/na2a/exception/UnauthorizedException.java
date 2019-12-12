package com.vqd.tme.na2a.exception;

import java.nio.charset.Charset;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class UnauthorizedException extends HttpStatusCodeException {
  private static final long serialVersionUID = 1L;

  public UnauthorizedException(
      HttpStatus statusCode,
      String statusText,
      HttpHeaders responseHeaders,
      byte[] responseBody,
      Charset responseCharset) {
    super(statusCode,statusText,responseHeaders,responseBody,responseCharset);
  }
}
