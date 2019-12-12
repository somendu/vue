package com.vqd.tme.na2a.support;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;

public class CastUtils {
  private CastUtils() {
  }

  public static String asString(Object obj) {
    if (obj == null) {
      return null;
    }
    if (!(obj instanceof String)) {
      return null;
    }
    if (obj instanceof String && ((String) obj).isEmpty()) {
      return null;
    }
    return obj.toString();
  }

  public static List<String> asStrings(Object obj) {
    List<String> result;

    if (obj instanceof List<?>) {
      List<?> list = (List<?>) obj;
      result = new ArrayList<>(list.size());

      for (Object entry : list) {
        String str = asString(entry);
        if (str != null) {
          result.add(str);
        }
      }
    } else {
      result = Collections.emptyList();
    }

    return result;
  }

  public static Map<String, String> asMap(Object obj) {
    Map<String, String> result;

    if(obj instanceof Map<?,?>) {
      Map<?,?> map = (Map<?,?>) obj;
      result = Maps.newHashMap();

      for (Object entyKey : map.keySet()) {
        String key = asString(entyKey);
        String value = asString(map.get(key));
        if (key != null && value != null) {
          result.put(key, value);
        }
      }

    } else {
      result = Collections.emptyMap();
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  public static <T> List<T> asList(Object obj) {
    if (obj instanceof List<?>) {
      return (List<T>) obj;
    }
    return null;
  }

  public static List<String> parameterToList(String elements) {
    return parameterToList(elements, ",");
  }

  public static List<String> parameterToList(String elements, String separator) {
    if (!elements.isEmpty()) {
      return Arrays.asList(elements.split(separator));
    } else {
      return Collections.emptyList();
    }
  }

  public static Integer asInteger(Object o) {
    if (o instanceof Integer) {
      return (Integer) o;
    }
    if (o instanceof Number) {
      return ((Number) o).intValue();
    }
    if (o instanceof String) {
      String str = (String) o;
      if (str.isEmpty()) {
        return null;
      }
      try {
        return Integer.parseInt(str);
      } catch (NumberFormatException nfe) {
      }
    }
    return null;
  }

  public static String toString(Object o) {
    if (o == null) {
      return "";
    }

    return o.toString();
  }

  public static Boolean asBoolean(Object o) {
    if (o instanceof Boolean) {
      return (Boolean) o;
    }
    if (o instanceof String) {
      String value = (String) o;
      if (StringUtils.isBlank(value)) {
        return null;
      } else if (value.equalsIgnoreCase("yes")) {
        return true;
      } else {
        return new Boolean(value);
      }
    }
    return false;
  }

  public static BigDecimal asBigDecimal(Object o) {
    if (o instanceof BigDecimal) {
      return (BigDecimal) o;
    }
    if (o instanceof String) {
      String str = (String) o;
      if (str.isEmpty()) {
        return BigDecimal.ZERO;
      }
      try {
        return new BigDecimal(str);
      } catch (NumberFormatException nfe) {
      }
    }
    return null;
  }
}
