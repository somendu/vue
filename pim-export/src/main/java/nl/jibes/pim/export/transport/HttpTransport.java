package nl.jibes.pim.export.transport;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class HttpTransport implements Transport {
	private final ObjectMapper mapper;
	private final String targetURL;

	@Override
	public void write(Map<String, Object> value, int numberOfItems) throws IOException {
		log.info("sending {} items to {}", numberOfItems, targetURL);

		// TODO write directly
		String content = mapper.writeValueAsString(value);
		HttpResponse response = Request.Post(targetURL).bodyString(content, ContentType.APPLICATION_JSON).execute()
				.returnResponse();

		log.debug("returned response code: {}", response.getStatusLine().getStatusCode());

		EntityUtils.consume(response.getEntity());
	}

}
