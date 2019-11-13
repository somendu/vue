/**
 *
 */
package com.shadow.text;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.fasterxml.jackson.core.JsonParseException;

/**
 * @author Somendu Maiti
 *
 *         Execution
 *
 *         1. gradle shadowJar
 * 
 *         2. searchreplace.exe txt “customer” “client” < manifesto.txt >
 *         result.txt
 */

public class SearchReplaceMain {

	/**
	 * @param args
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws ParseException
	 */
	public static void main(String[] args) throws JsonParseException, IOException, ParseException {
		CommandLineParser parser = new DefaultParser();

		Options options = new Options();
		options.addRequiredOption("fileType", "fileType", true, "Type of File");
		options.addRequiredOption("searchString", "searchString", true, "Search String");
		options.addRequiredOption("replaceString", "replaceString", true, "Replace String");
		options.addRequiredOption("sourceFile", "sourceFile", true, "Source File");
		options.addRequiredOption("targetFile", "targetFile", true, "Target File");

		CommandLine commandLine;

		try {
			commandLine = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			exitSystem(options);
			return;
		}

		String fileType = commandLine.getOptionValue("fileType");
		String searchString = commandLine.getOptionValue("searchString");
		String replaceString = commandLine.getOptionValue("replaceString");
		String sourceFile = commandLine.getOptionValue("sourceFile");
		String targetFile = commandLine.getOptionValue("targetFile");

		SearchReplace searchReplace = new SearchReplace();

		searchReplace.setSourceFile(sourceFile).setFileType(fileType).setSearchString(searchString)
				.setReplaceString(replaceString).setTargetFile(targetFile);

		new Thread(searchReplace).start();

	}

	private static void exitSystem(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("SearchReplaceMain", options);
		System.exit(1);
	}
}
