package com.vqd.tme.na2a.data;

import com.vqd.tme.na2a.model.Product;

public interface ProductRepository {

    Product findByApplicationId(String applicationId);
}
