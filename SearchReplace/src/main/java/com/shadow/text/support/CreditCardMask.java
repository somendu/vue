/**
 * 
 */
package com.shadow.text.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author somendu
 *
 */
public class CreditCardMask {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

//		System.out.println(maskCardNumberTwo("1234123412341234", "xxxx-xxxx-xxxx-####"));

		System.out.println(polishNotation("3.3 2.7 +"));
	}

	/**
	 * Applies the specified mask to the card number.
	 *
	 * @param cardNumber The card number in plain format
	 * @param mask       The number mask pattern. Use # to include a digit from the
	 *                   card number at that position, use x to skip the digit at
	 *                   that position
	 *
	 * @return The masked card number
	 */
	public static String maskCardNumber(String cardNumber, String mask) {

		// format the number
		int index = 0;
		StringBuilder maskedNumber = new StringBuilder();
		for (int i = 0; i < mask.length(); i++) {
			char c = mask.charAt(i);
			if (c == '#') {
				maskedNumber.append(cardNumber.charAt(index));
				index++;
			} else if (c == 'x') {
				maskedNumber.append(c);
				index++;
			} else {
				maskedNumber.append(c);
			}
		}

		// return the masked number
		return maskedNumber.toString();
	}

	public static String maskCardNumberTwo(String cardNumber, String mask) {

		// format the number
		int index = 0;
		String maskedNumber = new String();

		char[] cardNumberArray = new char[cardNumber.length()];
		char[] maskArray = new char[mask.length()];

		char[] maskedNumberArray = new char[cardNumber.length()];

		int cnCount = 0;

		int maCount = 0;

		// Copy character by character into array
		for (int i = 0; i < cardNumber.length(); i++) {
			cardNumberArray[i] = cardNumber.charAt(i);
		}

		// Copy character by character into array
		for (int i = 0; i < mask.length(); i++) {
			maskArray[i] = mask.charAt(i);
		}

		// Copy character by character into array
		for (int i = 0; i < cardNumber.length(); i++) {

			maskedNumberArray[i] = mask.charAt(i);
		}

		// return the masked number
		return maskedNumber;
	}

	public static String maskify(String creditCardNumber) {

		StringBuilder output = new StringBuilder();

		if (creditCardNumber.length() > 6) {

			for (int cardLength = 0; cardLength < creditCardNumber.length(); cardLength++) {
				if (Character.isDigit(creditCardNumber.charAt(cardLength)) && cardLength > 1
						&& cardLength < creditCardNumber.length() - 4) {
					output.append("#");
				} else {
					output.append(creditCardNumber.charAt(cardLength));
				}
			}

		} else {
			return creditCardNumber;
		}

		return output.toString();
	}

	public static String ordinalNumber(int number) {

		String ordinalString = "";

		String[] numberSuffixArray = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };

		List<String> numberSuffixList = new ArrayList<String>();
		numberSuffixList.add(0, "th");
		numberSuffixList.add(1, "st");
		numberSuffixList.add(2, "nd");
		numberSuffixList.add(3, "rd");
		numberSuffixList.add(4, "th");
		numberSuffixList.add(4, "th");
		numberSuffixList.add(5, "th");
		numberSuffixList.add(6, "th");
		numberSuffixList.add(7, "th");
		numberSuffixList.add(8, "th");
		numberSuffixList.add(9, "th");

		if (number % 100 == 11 || number % 100 == 12 || number % 100 == 13) {
			ordinalString = number + "th";
		} else if (number == 0) {
			ordinalString = "";
		} else {
			ordinalString = number + numberSuffixList.get(number % 10);
		}

		return ordinalString;
	}

	public static double polishNotation(String expr) {

		String[] stringTokenArray = expr.split(" ");
		double finalOutput = 0.0;
		if (expr.length() == 0) {
			return finalOutput;
		}

		String operations = "+-*/";

		Stack<String> stringStack = new Stack<String>();

		for (String stringToken : stringTokenArray)

		{
			if (!operations.contains(stringToken)) {
				stringStack.push(stringToken);
			} else {
				double first = Double.valueOf(stringStack.pop());
				double second = Double.valueOf(stringStack.pop());

				int charIndex = operations.indexOf(stringToken);

				switch (charIndex) {
				case 0:
					stringStack.push(String.valueOf(first + second));
					break;
				case 1:
					stringStack.push(String.valueOf(second - first));
					break;
				case 2:
					stringStack.push(String.valueOf(first * second));
					break;
				case 3:
					stringStack.push(String.valueOf(second / first));
					break;

				}
			}
		}

		finalOutput = Double.valueOf(stringStack.pop());

		return finalOutput;
	}
}
