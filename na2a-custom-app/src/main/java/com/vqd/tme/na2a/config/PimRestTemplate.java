package com.vqd.tme.na2a.config;

import java.io.IOException;
import java.util.Arrays;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import com.vqd.tme.na2a.auth.AuthorizationInterceptor;
import com.vqd.tme.na2a.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;

/**
 * Customized {@link RestTemplate}, adding authorization.
 *
 * @author edgardegraaff
 *
 */
@Slf4j
public class PimRestTemplate extends RestTemplate {
  /**
   * This error handler ensures an {@link UnauthorizedException} is thrown upon 401's,
   * so that the client knows a new token is needed.
   *
   * @author edgardegraaff
   *
   */
  private static class PimResponseErrorHandler extends DefaultResponseErrorHandler {
    @Override
    protected void handleError(ClientHttpResponse response, HttpStatus statusCode)
        throws IOException {
      if (statusCode == HttpStatus.UNAUTHORIZED) {
        throw new UnauthorizedException(statusCode, response.getStatusText(),
            response.getHeaders(), getResponseBody(response), getCharset(response));
      }

      try {
        super.handleError(response);
      } catch (HttpServerErrorException e) {
        log.warn("server error returned: {}", e.getResponseBodyAsString());
        throw e;
      } catch (RestClientResponseException e) {
        log.debug("client error returned: {}", e.getResponseBodyAsString());
        throw e;
      }
    }
  }

  public PimRestTemplate() {
    super(Arrays.asList(
        new MappingJackson2HttpMessageConverter()));

    getInterceptors().add(new AuthorizationInterceptor());
    getInterceptors().add((request, body, execution) -> {
      log.debug("invoke {} {}", request.getMethod(), request.getURI());
      return execution.execute(request, body);
    });
    setErrorHandler(new PimResponseErrorHandler());
  }

  @Override
  public void delete(String url, Object... uriVariables) throws RestClientException {
    // patch DELETE calls by enforcing content-type
    RequestCallback requestCallback = request -> {
      request.getHeaders().setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    };

    execute(url, HttpMethod.DELETE, requestCallback, null, uriVariables);
  }
}
