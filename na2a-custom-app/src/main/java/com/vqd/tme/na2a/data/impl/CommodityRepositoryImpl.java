package com.vqd.tme.na2a.data.impl;

import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import com.vqd.tme.na2a.partlinking.model.VariantMetaData;
import com.vqd.tme.na2a.partlinking.persistence.impl.P360VariantMetaDataResolver;
import com.vqd.tme.na2a.data.CommodityRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommodityRepositoryImpl implements CommodityRepository {

	private final P360VariantMetaDataResolver resolver;

	@Override
	public String findByApplicationId(String applicationId) {
		Map<String, VariantMetaData> variantMetaData = resolver.resolve(Lists.newArrayList(applicationId));
		VariantMetaData metaData = variantMetaData.get(applicationId);

		return Optional.ofNullable(metaData).map(VariantMetaData::getCommodity).orElse(null);
	}

}
