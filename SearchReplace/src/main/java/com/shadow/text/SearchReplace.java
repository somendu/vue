package com.shadow.text;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.shadow.text.exception.FileExtensionException;
import com.shadow.text.exception.FileTypeException;
import com.shadow.text.service.ReadFileService;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * The class to execute reading file and further inner methods
 * 
 * @author somendu
 *
 */

@Accessors(chain = true)
public class SearchReplace implements Runnable {
	public static int DEFAULT_BUFFER_SIZE = 100;

	@Getter
	@Setter
	private String fileType;

	@Getter
	@Setter
	private String searchString;

	@Getter
	@Setter
	private String replaceString;

	@Getter
	@Setter
	private String sourceFile;
	@Getter
	@Setter
	private String targetFile;

	private ReadFileService readFileService = new ReadFileService();

	public SearchReplace() {

	}

	@Override
	public void run() {
		try {

			long startTime = System.currentTimeMillis();

			readFileService.readFile(sourceFile, fileType, searchString, replaceString, targetFile);

			long endTime = System.currentTimeMillis();
			NumberFormat formatter = new DecimalFormat("#0.00000");

//			System.out.println("Execution time is " + formatter.format((endTime - startTime) / 1000d) + " seconds");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		catch (FileExtensionException e) {
			e.printStackTrace();
		} catch (FileTypeException e) {
			e.printStackTrace();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
