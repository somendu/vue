package nl.jibes.pim.export;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import nl.jibes.pim.export.function.IOSupplier;
import org.junit.Test;

public class PimExportSplitterTest {

	private IOSupplier<Reader> readerSupplier = () -> {
		return new InputStreamReader(
				getClass().getClassLoader().getResourceAsStream("Export-AnyPoint.json"),
				Charset.forName("UTF-8"));
	};

	/**
	 * Test the header reader, which reads the content except for the objects array
	 *
	 * @throws IOException
	 */
	@Test
	public void testReadHeader() throws IOException {
		PimExportSplitter splitter = new PimExportSplitter();

		Map<String, Object> header = splitter.readHeader(readerSupplier);

		assertNotNull(header);
		// it should only contain catalog, dateTimeNL and lastUpdateNL
		assertEquals(3, header.size());
		assertEquals(new TextNode("MASTER"), header.get("catalog"));
		assertEquals(new TextNode("2018-07-30T08:55:47"), header.get("dateTimeNL"));
		assertEquals(new TextNode("2018-07-30T08:50:00"), header.get("lastUpdateNL"));
	}

	@Test
	public void testSplitter() throws IOException {
		PimExportSplitter splitter = new PimExportSplitter();
		Map<String, Object> header = splitter.readHeader(readerSupplier);

		AtomicInteger total = new AtomicInteger(0);

		splitter.splitJson(readerSupplier, header, (data, count) -> {
			total.addAndGet(count);
		});

		// the source actually states 1124, but there the 1125th is an empty object
		// .. meaning we are correct and the source isn't ..
		assertEquals(1125, total.get());
	}
}
