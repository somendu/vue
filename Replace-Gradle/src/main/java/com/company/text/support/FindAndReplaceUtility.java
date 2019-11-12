package com.company.text.support;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ReplaceUtility {

	public void replaceText(String filePath, String source, String destination, String outputFilePath)
			throws IOException {
		Path paths = Paths.get(filePath);
		Stream<String> lines = Files.lines(paths, Charset.forName("UTF-8"));
		List<String> newFile = lines.map(line -> line.replaceAll(source, destination)).collect(Collectors.toList());
		resultFile(outputFilePath, newFile);
		lines.close();
	}

	private void resultFile(String resultFilePath, List<String> outputContent) throws IOException {
		Path newFilePath = Paths.get(resultFilePath);
		Files.write(newFilePath, outputContent, Charset.forName("UTF-8"));
		System.out.println("Kindly Check your result file :  filename is " + resultFilePath);
	}
}