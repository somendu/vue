package com.shadow.text;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

import java.io.IOException;

import org.junit.Test;

import com.shadow.text.service.ReadFileService;
import com.shadow.text.service.ReplaceTextService;

public class SearchReplaceTest {

	/**
	 * Test the header reader, which reads the content except for the objects array
	 *
	 * @throws IOException
	 */
	@Test
	public void testExtension() throws IOException {

		ReadFileService readFileService = new ReadFileService();

		assertFalse(!readFileService.checkFileExtension("Sample.txt"));

		assertFalse(readFileService.checkFileExtension("Sample.pdf"));
	}

	@Test
	public void testReplace() throws IOException {

		ReplaceTextService replaceTextService = new ReplaceTextService();

		assertNotSame("client", replaceTextService.replaceText("client", "client", "customer"));

		assertNotSame("customer", replaceTextService.replaceText("client", "customer", "client"));
	}

}
