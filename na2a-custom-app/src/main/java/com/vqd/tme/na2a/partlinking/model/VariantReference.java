package com.vqd.tme.na2a.partlinking.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * Describes a variant -> article reference (and its parameters)
 *
 * @author edgardegraaff
 *
 */
@Data
@Accessors(chain = true)
public class VariantReference {
  // Variant2ArticleReference.ReferencedArticleNo
  private String articleCode;
  // Variant2ArticleReference.Type
  private String type;
  // Variant2ArticleReference.Quantity
  private BigDecimal quantity;
}
