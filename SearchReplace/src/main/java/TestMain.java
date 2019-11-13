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

	public static String sayHello(String name) {

		String helloString = "";

		if ("".equalsIgnoreCase(name)) {
			helloString = "Hello there!";
		} else {
			helloString = "Hello, " + name + "!";
		}
		return helloString;
	}
}
