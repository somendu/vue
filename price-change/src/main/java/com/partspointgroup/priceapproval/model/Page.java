package com.partspointgroup.priceapproval.model;

import java.util.Collection;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class Page<E> {
	private int start;
	private int end;
	private Integer count;
	private Collection<E> rows;
}
