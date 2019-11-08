/**
 * 
 */
package com.data.story.spring.views.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author somendu
 *
 */
public class CheckValueAgain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

//		Scanner in = new Scanner(System.in);
//
//		int row = in.nextInt();
//		int column = in.nextInt();
//
//		in.close();

		CheckValueAgain checkValue = new CheckValueAgain();

		List<List<Integer>> arr = new ArrayList<List<Integer>>();

		List<Integer> firstInt = new ArrayList<Integer>();
		firstInt.add(1);
		firstInt.add(1);
		firstInt.add(1);

		List<Integer> secondInt = new ArrayList<Integer>();
		secondInt.add(1);
		secondInt.add(1);
		secondInt.add(0);

		List<Integer> thirdInt = new ArrayList<Integer>();
		thirdInt.add(1);
		thirdInt.add(0);
		thirdInt.add(1);

		arr.add(firstInt);
		arr.add(secondInt);
		arr.add(thirdInt);

		int i = checkValue.largestMatrix(arr);

	}

	private int chooseFlask(List<Integer> requirements, int m, List<List<Integer>> markings) {
		List<Integer> lossList = new ArrayList<Integer>();

		Map<Integer, List<Integer>> markingsMap = new TreeMap<Integer, List<Integer>>();

		for (List<Integer> marking : markings) {

			if (markingsMap.size() > 0 && markingsMap.containsKey(marking.get(0))) {
				ArrayList<Integer> size = new ArrayList<Integer>();
				size.add(marking.get(1));
				markingsMap.put(marking.get(0), size);
			} else {
				markingsMap.get(marking.get(0)).add(marking.get(1));
			}

		}

		for (int mark = 0; mark < m; mark++) {

			List<Integer> givenMarkings = markingsMap.get(mark);

			if (givenMarkings.get(givenMarkings.size() - 1) >= requirements.get(requirements.size() - 1)) {

				for (Integer marking : givenMarkings) {

					Integer loss = 0;

					for (Integer requirement : requirements) {

						if (marking >= requirement) {

							loss = loss + (marking - requirement);

						}
						lossList.add(loss);
					}

				}
			}

		}

		return lossList.indexOf(Collections.min(lossList));
	}

	private int largestMatrix(List<List<Integer>> arr) {

		int count = 0;

		Map<Integer, List<Integer>> positionMap = new HashMap<Integer, List<Integer>>();

		int listCount = 0;
		for (List<Integer> listInt : arr) {

			positionMap.put(listCount, listInt);

			listCount++;
		}

		System.out.println(positionMap);

		return 0;
	}

}
