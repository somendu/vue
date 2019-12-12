package com.vqd.tme.na2a.controller;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.support.JavaScriptRenderer;
import com.vqd.tme.na2a.support.Maps;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ConfigController {
  private final JavaScriptRenderer jsRenderer;
  private final InformaticaPimProperties props;

  @GetMapping("/api/config.js")
  public void getConfig(HttpServletResponse response) throws IOException {
    Map<String, Object> environment = Maps.of(
        "pimConfig",
        Maps.of("externalUrl", props.getExternalUrl()));
    jsRenderer.render(response.getWriter(), environment);
  }
}
