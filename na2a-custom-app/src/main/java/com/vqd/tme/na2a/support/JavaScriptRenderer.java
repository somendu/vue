package com.vqd.tme.na2a.support;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JavaScriptRenderer {
  private final ObjectMapper mapper;

  /**
   * Renders a JavaScript source file, setting all values as globals (window)
   *
   * @param out
   * @param values
   * @throws IOException
   */
  public void render(PrintWriter out, Map<String, Object> values) throws IOException {
    for (Entry<String, Object> entry : values.entrySet()) {
      out.print("window.");
      out.print(entry.getKey());
      out.print(" = ");
      mapper.writeValue(out, entry.getValue());
      out.println(";");
    }
  }
}
