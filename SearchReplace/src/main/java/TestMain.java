import org.apache.commons.io.FilenameUtils;

/**
 * 
 */

/**
 * @author somendu
 *
 */
public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String fileName = "SampleText.txt";

		String extensionArray = FilenameUtils.getExtension(fileName);

		System.out.println(extensionArray);

	}

}
