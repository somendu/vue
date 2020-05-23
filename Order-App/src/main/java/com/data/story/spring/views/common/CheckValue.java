/**
 * 
 */
package com.data.story.spring.views.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * @author somendu
 *
 */
public class CheckValue {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);

		int start = in.nextInt();
		// int close = in.nextInt();

		in.close();

		CheckValue checkValue = new CheckValue();

		// checkValue.printString(start);

		// checkValue.getRandomDoubleBetweenRange(0, 9);

		Map<Integer, LinkedHashSet<Integer>> columnMap = checkValue.getColumnMap(start);

//		System.out.println("Print columnList:	" + columnMap.get(1));
//		System.out.println("Print imageMapList:	" + columnMap.get(2));

	}

	private Map<Integer, LinkedHashSet<Integer>> getColumnMap(int sliderValue) {

		Map<Integer, LinkedHashSet<Integer>> columnMap = new HashMap<Integer, LinkedHashSet<Integer>>();

		int modulus = Math.round(sliderValue % 10);

		int nearestMultiple = 10 * (Math.round(sliderValue / 10)) + 10;

		int columnCount = nearestMultiple / 10;

		System.out.println("Nearest Multiple : " + nearestMultiple);

		System.out.println("Each Column number of image : " + columnCount);

		System.out.println("Modulus : " + modulus + "\n");

		LinkedHashSet<Integer> columnList = new LinkedHashSet<Integer>();

		// When Slider value less than 10 -> 1 image will be random selected from any
		// column
		if (columnCount == 0) {
			while (columnList.size() < modulus) {

				int columnValue = getRandomRow();
				columnList.add(columnValue);
			}

			columnMap.put(columnCount, columnList);
		}

		// When Slider value More than 10 -> n images will be random selected from all
		// columns
		else {

			while (columnList.size() < 10)

			{
				int columnValue = getRandomRow();
				columnList.add(columnValue);
			}

			// System.out.println("Column List : " + columnList);

			// Creating the map for the first multiple of 10

			// LinkedHashSet<Integer> imageListRemoved = new LinkedHashSet<Integer>();

			LinkedHashSet<Integer> imageList = new LinkedHashSet<Integer>();

			int imageCountToRemove = 10 - modulus;

			// System.out.println("\n Image count to remove " + imageCountToRemove);

			for (Integer column : columnList) {
				imageList = getRandomList(columnCount, modulus);

				columnMap.put(column, imageList);

			}

			System.out.println("\n Column Map BEF: " + columnMap);

			// TODO Create image list for remaining images - modulus

			for (int i = 0; i < imageCountToRemove; i++)

			{

				LinkedHashSet<Integer> imageListRemoved = new LinkedHashSet<Integer>();
				imageListRemoved = removeImage(columnMap.get(i));

				columnMap.put(i, imageListRemoved);

			}
		}

		System.out.println("\n Column Map AFT : " + columnMap);

		return columnMap;
	}

	private LinkedHashSet<Integer> removeImage(LinkedHashSet<Integer> imageList) {

		imageList.remove(imageList.iterator().next());

		return imageList;

	}

	public LinkedHashSet<Integer> getRandomList(int columnCount, int modulus) {

		LinkedHashSet<Integer> imageList = new LinkedHashSet<Integer>();

		while (imageList.size() < columnCount)

		{
			int columnValue = getRandomRow();
			imageList.add(columnValue);
		}

		for (Integer column : imageList) {
			// System.out.println("Column : " + column);
		}

		return imageList;

	}

	public int getRandomRow() {

		// int randomRow = ((int) (Math.random() * ((9 - 0) + 1)) + 0);

		Random rand = new Random();

		// Generate random integers in range 0 to 999
		int rand_int1 = rand.nextInt(10);

		return rand_int1;
	}

	public int getRandomRowAgain() {

		// int randomRow = ((int) (Math.random() * ((9 - 0) + 1)) + 0);

		Random rand = new Random();

		// Generate random integers in range 0 to 999
		int rand_int1 = rand.nextInt(10);

		//
//		for (int i = 0; i < columnCount; i++)
//
//		{
//			int columnValue = getRandomRow();
//
//			columnList.add(columnValue);
//
//		}
//
//		columnList = columnList.stream().distinct().collect(Collectors.toSet());
//
//		if (columnList.size() < columnCount) {
//			System.out.println("coming here");
//
//			int columnValue = getRandomRowAgain() + 1;
//			for (int i = 0; i < columnCount - columnList.size(); i++) {
//				columnValue = getRandomRowAgain();
//				System.out.println("column Value : " + columnValue);
//				if (columnList.contains(columnValue)) {
//					columnValue = getRandomRow();
//				}
//				columnList.add(columnValue);
//			}
//		}
//
//		for (Integer column : columnList) {
//			System.out.println(" Column : " + column);
//		}
		return rand_int1;
	}

	public double getRandomDoubleBetweenRange(double min, double max) {
		double x = ((double) (Math.random() * ((max - min) + 1)) + min);
		System.out.println(" X here " + Math.round(x));
		return x;
	}

	private void printString(int count) {

		String o = "*";
		String t = "*";
		String th = "*";
		String f = "*";
		String fi = "*";
		String s = "*";
		String se = "*";
		String e = "*";
		String n = "*";
		String te = "*";

		String together = o + t + th + f + fi + s + se + e + n + te;

		List<String> myList = Arrays.asList(o, t, th, f, fi, s, se, e, n, te);

		myList.stream().filter(string -> string.contentEquals("*")).map(String::toUpperCase).sorted()
				.forEach(System.out::println);

	}

}
