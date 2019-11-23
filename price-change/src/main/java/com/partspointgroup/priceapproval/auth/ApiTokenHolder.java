package com.partspointgroup.priceapproval.auth;

/**
 * Holds API token per thread (which handles the request)
 *
 * @author edgardegraaff
 *
 */
public class ApiTokenHolder {
	private static final ThreadLocal<String> holder = new ThreadLocal<>();

	public static String get() {
		return holder.get();
	}

	public static void set(String apiToken) {
		holder.set(apiToken);
	}

	public static void remove() {
		holder.remove();
	}
}
