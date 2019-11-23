/**
 *
 */
package com.partspointgroup.priceapproval.support;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Exception class for remarks update
 *
 * @author Somendu Maiti <smaiti@jibes.nl>
 *
 * @since 14 Sep 2018
 */
@RequiredArgsConstructor
public class ItemUpdateException extends Exception {
	private static final long serialVersionUID = 1L;

	@Getter
	private final List<String> messages;
}
