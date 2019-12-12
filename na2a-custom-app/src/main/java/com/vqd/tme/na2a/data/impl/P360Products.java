package com.vqd.tme.na2a.data.impl;

import com.vqd.tme.na2a.adapter.RowToProductAdapter;
import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.data.Products;
import com.vqd.tme.na2a.model.Product;
import com.vqd.tme.na2a.model.p360.P360EnumEntry;
import com.vqd.tme.na2a.p360.GetResponse;

import java.util.*;

import com.vqd.tme.na2a.support.CastUtils;
import com.vqd.tme.na2a.support.VariantUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class P360Products implements Products {

  private final InformaticaPimProperties pimProperties;
  private final RestTemplate rest;
  private final RowToProductAdapter productAdapter;

  @Override
  public Product get(String productId) {
    Product result = null;

    log.debug("getProduct: {}", productId);

    GetResponse res = rest.getForObject(
        pimProperties.getServer()
        + "/rest/V2.0/list/Product2G/byItems"
        + "?items={productId}"
        + "&fields=Product2G.Id," +
                "Product2G.ProductNo," +
                "Product2GLang.DescriptionShort(EN)," +
                "Product2G.TMEColour," +
                "Product2G.TMEMaterial",
        GetResponse.class,
        String.format("%s@1", productId));

    log.trace("raw response: {}", res);

    Iterator<GetResponse.Row> iter = res.getRows().iterator();
    if (iter.hasNext()) {
      result = productAdapter.convert(iter.next());
    }

    log.debug("getProduct result: {}", result);
    return result;
  }

  @Override
  public Product getForVariant(String variantId) {
    Product result = null;

    log.debug("getForVariant: {}", variantId);

    GetResponse res = rest.getForObject(
            pimProperties.getServer()
                    + "/rest/V2.0/list/Variant/SuperordinateProductReference/byItems/" +
                    "?items={item}" +
                    "&fields=SuperordinateProductReference.ReferencedProduct",
            GetResponse.class,
            VariantUtils.formatId(variantId));

    log.trace("raw response: {}", res);

    Iterator<GetResponse.Row> iter = res.getRows().iterator();

    if (iter.hasNext()) {
      List<Object> values = iter.next().getValues();
      //TODO : fix dangerous cast
      LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) values.get(0);
      String id = map.get("id");
      result = Product.builder()
              .id(id)
              .build();
    }

    log.debug("getProduct result: {}", result);

    return result;
  }


}
