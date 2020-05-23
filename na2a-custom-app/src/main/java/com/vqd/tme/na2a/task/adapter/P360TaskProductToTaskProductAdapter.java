package com.vqd.tme.na2a.task.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.vqd.tme.na2a.adapter.Adapter;
import com.vqd.tme.na2a.model.Product;
import com.vqd.tme.na2a.task.model.TaskProduct;

@Component
public class P360TaskProductToTaskProductAdapter implements Adapter<Product, TaskProduct> {

    @Override
    public TaskProduct convert(Product product) {
    	return Optional.ofNullable(product)
    			.map(p -> TaskProduct.builder()
    					.id(p.getId())
    					.name(p.getShortDescription())
    					.productNumber(p.getProductNumber())
    					.build())
    			.orElse(TaskProduct.builder().build());
    }
}
