package com.vqd.tme.na2a.support;

import com.vqd.tme.na2a.model.KeyValue;
import com.vqd.tme.na2a.model.KeyValueCategory;
import com.vqd.tme.na2a.model.ResponseModel;
import com.vqd.tme.na2a.model.ResponseVariant;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class VariantUtils {
  private VariantUtils() {
  }

  public static String formatId(String id) {
    if (StringUtils.isEmpty(id)) {
      return "";
    }
    if (StringUtils.endsWith(id, "@1")) {
      return id;
    }
    return StringUtils.join(id, "@1");
  }

  public static String toString(ResponseVariant variant) {
    StringBuilder sb = new StringBuilder();

    appendModel(sb, variant.getModel());
    appendOptions(sb, variant.getVehicle());
    appendOptions(sb, variant.getEquipment());
    appendValues(sb, "Exterior Colours", variant.getExteriorColours());
    appendValues(sb, "Interior Colours", variant.getInteriorColours());
    appendValues(sb, "Trim Colours", variant.getTrimColours());
    appendValues(sb, "Countries", variant.getCountries(), true);

    return sb.toString();
  }

  private static void appendValues(StringBuilder sb, String topic, List<KeyValue> values) {
    appendValues(sb, topic, values, false);
  }

  private static void appendValues(StringBuilder sb, String topic, List<KeyValue> values, boolean addKey) {
    if (values != null && !values.isEmpty()) {
      appendValue(
          sb,
          topic,
          values.stream()
          .filter(v -> v != null)
          .map(kv -> {
            if(addKey) {
              return kv.getValue() + " [" + Optional.ofNullable(kv.getKey()).orElse("") + "]";
            } else {
              return kv.getValue();
            }
          })
          .filter(v -> v != null)
          .collect(Collectors.joining(", ")));
    }
  }

  private static void appendOptions(StringBuilder sb, List<KeyValueCategory> options) {
    if (options != null && !options.isEmpty()) {
      Map<String, List<String>> byCategory = new LinkedHashMap<>();

      for (KeyValueCategory option : options) {
        if (option != null && option.getCategory() != null && option.getValue() != null) {
          byCategory
          .computeIfAbsent(option.getCategory(), c -> new ArrayList<>())
          .add(option.getValue());
        }
      }

      for (Map.Entry<String, List<String>> entry : byCategory.entrySet()) {
        appendValue(sb, entry.getKey(), entry.getValue().stream().collect(Collectors.joining(", ")));
      }
    }
  }

  private static void appendModel(StringBuilder sb, ResponseModel model) {
    if (model == null || model.isAll()) {
      appendValue(sb, "Model", "All models");
    } else {
      appendValue(sb, "Brand", model.getBrand());
      appendValue(sb, "Model", model.getModel());
      appendValue(sb, "FMC/MMC", model.getProject());
    }
  }

  private static void appendValue(StringBuilder sb, String topic, String value) {
    if (value != null) {
      addSeparator(sb);
      sb.append(topic).append(": ").append(value);
    }
  }

  private static void appendValue(StringBuilder sb, String topic, KeyValue value) {
    if (value != null) {
      appendValue(sb, topic, value.getValue());
    }
  }

  private static void addSeparator(StringBuilder sb) {
    if (sb.length() != 0) {
      sb.append(", ");
    }
  }
}
