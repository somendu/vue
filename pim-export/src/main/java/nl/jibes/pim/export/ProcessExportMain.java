/**
 *
 */
package nl.jibes.pim.export;

import com.fasterxml.jackson.core.JsonParseException;
import java.io.IOException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author Somendu Maiti
 *
 *         Execution
 *
 *         1. gradle shadowJar
 *
 *         2. java -jar build/libs/pim-export-shadow-all.jar --bufferSize 100
 *         --sourceJson /download/Export-AnyPoint.json --target
 *         https://postman-echo.com/post
 */
public class ProcessExportMain {

	/**
	 * @param args
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws ParseException
	 */
	public static void main(String[] args) throws JsonParseException, IOException, ParseException {
		CommandLineParser parser = new DefaultParser();

		Options options = new Options();
		options.addRequiredOption("sourceJson", "sourceJson", true, "Source JSON");
		options.addRequiredOption("target", "target", true, "Target URL");
		options.addOption("bufferSize", "bufferSize", true,
				String.format("Number of articles per chunk (default %d)", PimExportSplitter.DEFAULT_BUFFER_SIZE));

		CommandLine commandLine;
		try {
			commandLine = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			exitSystem(options);
			return;
		}

		String sourceJson = commandLine.getOptionValue("sourceJson");
		String targetURL = commandLine.getOptionValue("target");
		String bufferSize = commandLine.getOptionValue("bufferSize");

		int bufferInteger = PimExportSplitter.DEFAULT_BUFFER_SIZE;
		if (bufferSize != null) {
			bufferInteger = Integer.parseInt(bufferSize);
		}

		new PimExportSplitter()
		.setSource(sourceJson)
		.setBufferSize(bufferInteger)
		.setTargetURL(targetURL)
		.run();
	}

	private static void exitSystem(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("ProcessExportMain", options);
		System.exit(1);
	}
}
