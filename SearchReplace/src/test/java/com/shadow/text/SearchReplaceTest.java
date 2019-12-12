package com.shadow.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.TextNode;

public class SearchReplaceTest {

	/**
	 * Test the header reader, which reads the content except for the objects array
	 *
	 * @throws IOException
	 */
	@Test
	public void testReadHeader() throws IOException {
		SearchReplace splitter = new SearchReplace();

		Map<String, Object> header = new HashMap<String, Object>();

		assertNotNull(header);
		// it should only contain catalog, dateTimeNL and lastUpdateNL
		assertEquals(3, header.size());
		assertEquals(new TextNode("MASTER"), header.get("catalog"));
		assertEquals(new TextNode("2018-07-30T08:55:47"), header.get("dateTimeNL"));
		assertEquals(new TextNode("2018-07-30T08:50:00"), header.get("lastUpdateNL"));
	}

	@Test
	public void testSplitter() throws IOException {
		SearchReplace splitter = new SearchReplace();
		Map<String, Object> header = new HashMap<String, Object>();

		AtomicInteger total = new AtomicInteger(0);

//		splitter.splitJson(readerSupplier, header, (data, count) -> {
//			total.addAndGet(count);
//		});

		// the source actually states 1124, but there the 1125th is an empty object
		// .. meaning we are correct and the source isn't ..
		assertEquals(1125, total.get());
	}
}
