package com.vqd.tme.na2a.data.impl;

import org.springframework.stereotype.Component;

import com.vqd.tme.na2a.data.impl.P360Products;
import com.vqd.tme.na2a.model.Product;
import com.vqd.tme.na2a.data.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final P360Products p360Products;

    @Override
    public Product findByApplicationId(String applicationId) {
        log.info("Fetching product for application ID " + applicationId);
    	Product product = p360Products.getForVariant(applicationId);
    	return Optional
                .ofNullable(product)
                .map(p -> p360Products.get(p.getId().replace("@1", "")))
                .orElse(null);
    }
}
