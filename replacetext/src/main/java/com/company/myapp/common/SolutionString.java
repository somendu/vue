/**
 * 
 */
package com.company.myapp.common;

import java.util.Scanner;

/**
 * @author somendu
 *
 */
public class SolutionString {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Scanner inputOne = new Scanner(System.in);
		Scanner inputTwo = new Scanner(System.in);
		Scanner inputThree = new Scanner(System.in);

		String mainString = inputOne.nextLine();

		String searchString = inputTwo.next();

		String replaceString = inputThree.next();

		if (mainString.contains(searchString)) {
			mainString = mainString.replace(searchString, replaceString);
		}

		System.out.println("Final String : " + mainString);

		inputOne.close();
		inputTwo.close();
		inputThree.close();
	}
}
