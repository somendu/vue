/**
 * 
 */
package com.shadow.text.service;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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

		FileChannel fileChannel = FileChannel.open(paths);
		ByteBuffer buffer = ByteBuffer.allocate(1000);
		int noOfBytesRead = fileChannel.read(buffer);

		while (noOfBytesRead != -1) {
			System.out.println("Number of bytes read: " + noOfBytesRead);
			buffer.flip();
			System.out.print("Buffer contents: ");

			while (buffer.hasRemaining()) {
				System.out.print((char) buffer.get());
			}

			System.out.println(" ");
			buffer.clear();
			noOfBytesRead = fileChannel.read(buffer);
		}
		fileChannel.close();

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
