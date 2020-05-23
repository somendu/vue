package com.vqd.tme.na2a.data;

import com.vqd.tme.na2a.model.tme.TmePartsWrapper;

public interface TmeProductRepository {
    TmePartsWrapper searchParts(String partNumber);
}
