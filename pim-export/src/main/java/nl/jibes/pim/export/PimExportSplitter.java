package nl.jibes.pim.export;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import nl.jibes.pim.export.function.IOConsumer;
import nl.jibes.pim.export.function.IOSupplier;
import nl.jibes.pim.export.transport.HttpTransport;
import nl.jibes.pim.export.transport.Transport;

@Slf4j
@Accessors(chain = true)
public class PimExportSplitter implements Runnable {
	public static int DEFAULT_BUFFER_SIZE = 100;

	public static final String OBJECTS_FIELD = "objects";
	public static final String OBJECT_COUNT_FIELD = "objectCount";

	private final ObjectMapper mapper;

	@Getter
	@Setter
	private String source;
	@Getter
	@Setter
	private int bufferSize = DEFAULT_BUFFER_SIZE;
	@Getter
	@Setter
	private String targetURL;

	public PimExportSplitter() {
		mapper = new ObjectMapper();
	}

	@Override
	public void run() {
		try {
			Transport transport = new HttpTransport(mapper, targetURL);

			IOSupplier<Reader> readerSupplier = () -> {
				return new InputStreamReader(new FileInputStream(source), Charset.forName("UTF-8"));
			};

			Map<String, Object> header = readHeader(readerSupplier);
			log.debug("header: {}", mapper.writeValueAsString(header));

			splitJson(readerSupplier, header, transport);
		} catch (IOException e) {
			log.warn("error handling file", e);
			System.exit(1);
		}
	}

	/**
	 * Reads the content of the source file, except for the fields we replace.
	 *
	 * @return
	 * @throws IOException
	 */
	Map<String, Object> readHeader(IOSupplier<Reader> readerSupplier) throws IOException {
		Map<String, Object> header = new LinkedHashMap<>();

		try (JsonParser parser = mapper.getFactory().createParser(readerSupplier.get())) {
			JsonToken token;

			boolean inMainObject = false;
			String fieldName = null;

			while ((token = parser.nextToken()) != null) {
				if (!inMainObject) {
					// basically this is the first token
					if (token == JsonToken.START_OBJECT) {
						inMainObject = true;
					}
				} else {
					if (token == JsonToken.END_OBJECT) {
						// we're done
						break;
					}

					if (token == JsonToken.FIELD_NAME) {
						fieldName = parser.getValueAsString();
					} else {
						if (OBJECTS_FIELD.equals(fieldName)) {
							skip(parser, token);
						} else {
							header.put(fieldName, mapper.readTree(parser));
						}
					}
				}
			}
		}

		return header;
	}

	/**
	 * Consumes the parser until the end of this value is reached.
	 *
	 * @param parser
	 * @throws IOException
	 */
	@SuppressWarnings("incomplete-switch")
	private void skip(JsonParser parser, JsonToken _token) throws IOException {
		JsonToken token = _token;

		// if this is an object or array, we have to keep reading until its end token is
		// found
		if (token == JsonToken.START_ARRAY || token == JsonToken.START_OBJECT) {
			int depth = 1;

			while ((token = parser.nextToken()) != null) {
				switch (token) {

				case START_ARRAY:
				case START_OBJECT:
					depth++;
					break;

				case END_ARRAY:
				case END_OBJECT:
					depth--;
					if (depth == 0) {
						return;
					}
					break;

				}
			}
		}
	}

	/**
	 * Splitting JSON and getting list of files
	 * 
	 * @param header
	 *
	 * @param sourceJsonString
	 * @return
	 * @throws JsonParseException
	 * @throws IOException
	 */
	void splitJson(IOSupplier<Reader> readerSupplier, Map<String, Object> header, Transport transport)
			throws IOException {
		ItemWriter writer = new ItemWriter(bufferSize, header, transport);

		try (JsonParser parser = mapper.getFactory().createParser(readerSupplier.get())) {
			JsonToken token;
			boolean expectingObjects = false;

			while ((token = parser.nextToken()) != null) {
				if (expectingObjects) {
					if (token == JsonToken.START_ARRAY) {
						forEachEntry(mapper, parser, writer::write);
					}
					expectingObjects = false;
					continue;
				}

				if (token == JsonToken.FIELD_NAME) {
					if (OBJECTS_FIELD.equals(parser.getValueAsString())) {
						expectingObjects = true;
					}
				}
			}
		} finally {
			writer.flush();
		}
	}

	/**
	 * Expects the parser to be at the start of the array, it handles every entry
	 * within. Calls consumer for every entry for handling the tree. Stops at end of
	 * array.
	 *
	 * @param mapper
	 * @param parser
	 * @param consumer
	 * @throws IOException
	 */
	private static void forEachEntry(ObjectMapper mapper, JsonParser parser, IOConsumer<TreeNode> consumer)
			throws IOException {
		JsonToken token;

		while ((token = parser.nextToken()) != null) {
			if (token == JsonToken.END_ARRAY) {
				break;
			}

			// let mapper read the next object in this array
			TreeNode tree = mapper.readTree(parser);
			consumer.accept(tree);
		}
	}
}
