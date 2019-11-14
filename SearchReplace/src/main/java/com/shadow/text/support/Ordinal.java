/**
 * 
 */
package com.shadow.text.support;

/**
 * @author somendu
 *
 */
public class Ordinal {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int[] edgeCases = { 0, 1, 2, 3, 4, 5, 10, 11, 12, 13, 14, 20, 21, 22, 23, 24, 100, 101, 102, 103, 104, 111, 112,
				113, 114 };
		for (int edgeCase : edgeCases) {
			System.out.println(ordinal(edgeCase));
		}

	}

	public static String ordinal(int i) {
		String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
		switch (i % 100) {
		case 11:
		case 12:
		case 13:
			return i + "th";
		default:
			return i + sufixes[i % 10];

		}
	}
}
