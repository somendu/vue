/**
 * 
 */
package com.company.myapp.backend.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

/**
 * @author somendu
 *
 */
@Service
public class UploadService {

	private File outputFile;

	private String uploadPlace = "/Users/somendu/Work-Area/Workspace/Vue/replacetext/upload/";

	public String readFile(File file, String fileName) {

		try {

//			FileOutputStream fos = new FileOutputStream(new File("myFile"));

//			fileStream.writeTo(fos);
//
//			File file = new File(fileName);
//

			List<String> fileLinesList = FileUtils.readLines(file, "UTF-8");

//			Path paths = Paths.get(fileName);
//			Stream<String> lines = Files.lines(paths, Charset.forName("UTF-8"));
//			List<String> stringListReplaced = lines.map(line -> line.replaceAll("client", "customer"))
//					.collect(Collectors.toList());
			resultFile(fileName, fileLinesList);
//			lines.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO Read the file

		// TODO Check mimetype

		// TODO Check contents

		// TODO Replace contents

		// TODO Create new file

		// TODO Return the file

		return fileName;
	}

	private void resultFile(String resultFilePath, List<String> outputContent) throws IOException {
		Path newFilePath = Paths.get(resultFilePath);
		Files.write(newFilePath, outputContent, Charset.forName("UTF-8"));
		System.out.println("Kindly Check your result file :  filename is " + resultFilePath);
	}

	/**
	 * Gets the unique instance of this Singleton.
	 *
	 * @return the unique instance of this Singleton
	 */
	public static UploadService getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {
		static final UploadService INSTANCE = createDemoReviewService();

		/** This class is not meant to be instantiated. */
		private SingletonHolder() {
		}

		private static UploadService createDemoReviewService() {
			final UploadService uploadService = new UploadService();

			return uploadService;
		}
	}
}
