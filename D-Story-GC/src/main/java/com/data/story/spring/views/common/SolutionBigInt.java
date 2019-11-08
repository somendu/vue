/**
 * 
 */
package com.data.story.spring.views.common;

import java.math.BigInteger;
import java.util.Scanner;

/**
 * @author somendu
 *
 */
public class SolutionBigInt {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);

		BigInteger firstNumber = in.nextBigInteger();
		BigInteger secondNumber = in.nextBigInteger();

		System.out.println(firstNumber.add(secondNumber));
		System.out.println(firstNumber.multiply(secondNumber));

		in.close();
	}
}
