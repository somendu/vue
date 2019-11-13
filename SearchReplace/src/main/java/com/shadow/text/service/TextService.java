/**
 * 
 */
package com.shadow.text.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

/**
 * 
 * 
 * @author somendu
 *
 */
public class TextService {

	// java -jar build/libs/SearchReplace.jar --fileType txt --searchString customer
	// --replaceString client --sourceFile SampleText.txt --targetFile Result.txt

	private ReplaceTextService replaceTextService = new ReplaceTextService();

	private volatile int currentWriterId = 0;

	/**
	 * 
	 * Read the text file using File.lines creating a string stream
	 * 
	 * @param sourceFile
	 * @param fileType
	 * @param searchString
	 * @param replaceString
	 * @param targetFile
	 * @throws IOException
	 */
	public void readTextFile(String sourceFile, String fileType, String searchString, String replaceString,
			String targetFile) throws IOException {

		Path paths = Paths.get(sourceFile);

		Stream<String> lines = Files.lines(paths, Charset.forName("UTF-8"));

		List<String> fileLinesList = lines
				.map(line -> replaceTextService.replaceText(line, searchString, replaceString))
				.collect(Collectors.toList());

		Path newFilePath = Paths.get(targetFile);
		Files.write(newFilePath, fileLinesList, Charset.forName("UTF-8"));
		System.out.println("Kindly Check your result file :  filename is " + targetFile);

		lines.close();

	}

	/**
	 * 
	 * Read the text file using File.lines creating a string stream
	 * 
	 * @param sourceFile
	 * @param fileType
	 * @param searchString
	 * @param replaceString
	 * @param targetFile
	 * @throws IOException
	 */
	public void testRead(String sourceFile, String fileType, String searchString, String replaceString,
			String targetFile) throws IOException {

		Path paths = Paths.get(sourceFile);
		Vector<String> fileContents = new Vector<>(Collections.nCopies(1, sourceFile));

		CountDownLatch readWriteLatch = new CountDownLatch(1);

		final int writerId = 0;

		long startTime = System.currentTimeMillis();

		// Reader.
		new Thread(() -> {
			try {

				System.out.println(System.currentTimeMillis());
				Stream<String> lines = Files.lines(paths, Charset.forName("UTF-8"));

				List<String> fileLinesList = lines
						.map(line -> replaceTextService.replaceText(line, searchString, replaceString))
						.collect(Collectors.toList());

				String content = String.join("\n", fileLinesList);

				fileContents.set(writerId, content);
				readWriteLatch.countDown();
				lines.close();
			} catch (IOException e) {
				/* NOP */ }
		}).start();

		// Writer.
		new Thread(() -> {
			try {
				// Wait for corresponding reader to set content.
				readWriteLatch.await();

				Path newFilePath = Paths.get(targetFile);

				// Wait for writer ID.
				synchronized (this) {

					Files.write(newFilePath, fileContents, Charset.forName("UTF-8"));
					notifyAll();
					System.out.println(System.currentTimeMillis());
				}
			} catch (InterruptedException | IOException e) {
				/* NOP */ }
		}).start();

		long endTime = System.currentTimeMillis();
		NumberFormat formatter = new DecimalFormat("#0.00000");

		System.out.println("Execution time is " + formatter.format((endTime - startTime) / 1000d) + " seconds");

	}

	/**
	 * Read using File.readAllLines
	 * 
	 * @param sourceFile
	 * @param fileType
	 * @param searchString
	 * @param replaceString
	 * @param targetFile
	 * @throws IOException
	 */
	public void readUsingReadAllLines(String sourceFile, String fileType, String searchString, String replaceString,
			String targetFile) throws IOException {

		File file = new File(sourceFile);

		List<String> fileLinesList = Files.readAllLines(file.toPath(), Charset.forName("UTF-8"));

		for (String line : fileLinesList) {
			replaceTextService.replaceText(line, searchString, replaceString);
		}

//		String content = String.join("\n", fileLinesList);

//		System.out.println(content);

		Path newFilePath = Paths.get(targetFile);
		Files.write(newFilePath, fileLinesList, Charset.forName("UTF-8"));
		System.out.println("Kindly Check your result file :  filename is " + targetFile);
	}

	/**
	 * Read file using File Utils from Apache
	 * 
	 * @param sourceFile
	 * @param fileType
	 * @param searchString
	 * @param replaceString
	 * @param targetFile
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void readUsingFileUtils(String sourceFile, String fileType, String searchString, String replaceString,
			String targetFile) throws IOException, InterruptedException {

		File file = new File(sourceFile);

		List<String> fileLinesList = FileUtils.readLines(file, "UTF-8");

		for (String line : fileLinesList) {
			replaceTextService.replaceText(line, searchString, replaceString);
		}

		Path newFilePath = Paths.get(targetFile);
		Files.write(newFilePath, fileLinesList, Charset.forName("UTF-8"));
		System.out.println("Kindly Check your result file :  filename is " + targetFile);

	}

	private void resultFile(String resultFilePath, List<String> outputContent) throws IOException {
		Path newFilePath = Paths.get(resultFilePath);
		Files.write(newFilePath, outputContent, Charset.forName("UTF-8"));
		System.out.println("Kindly Check your result file :  filename is " + resultFilePath);

	}
}
