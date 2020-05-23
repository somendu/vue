package com.partspointgroup.priceapproval.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

public enum ApprovalStatus {
	REJECTED(3, "Afgekeurd"),
	APPROVED(4, "Goedgekeurd");

	private static final Map<Integer, ApprovalStatus> BY_VALUE;

	static {
		BY_VALUE = new HashMap<>();

		for (ApprovalStatus value : ApprovalStatus.values()) {
			BY_VALUE.put(value.getPimValue(), value);
		}
	}

	@Getter
	private final int pimValue;
	@Getter
	private final String displayValue;

	private ApprovalStatus(int pimValue, String dbValue) {
		this.pimValue = pimValue;
		displayValue = String.format("%02d - %s", pimValue, dbValue);
	}

	public static ApprovalStatus byValue(Integer value) {
		return BY_VALUE.get(value);
	}
}
