package com.vqd.tme.na2a.data.support;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.support.CastUtils;
import com.vqd.tme.na2a.support.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Utility class for reading a field from a REST response / writing a field to a REST request.
 *
 * @author edgardegraaff
 *
 * @param <T>
 * @param <V>
 */
@Accessors(fluent=true)
public class P360Field<T, V> {
  private static final List<String> EMPTY_MULTI_STRING = Collections.singletonList("");
  private static final List<String> EMPTY_MULTI_ENUM = Lists.newArrayList();

  public static enum Type {
    /**
     * Automatically convert empty strings to null and vice versa
     */
    STRING,
    /**
     * Automatically converts list, empty strings are omitted
     */
    MULTI_STRING,
    /**
     * Automatically converts list, empty strings are omitted
     */
    MULTI_ENUM,
    /**
     * Boolean conversion
     */
    BIT,
    /**
     * Automatically converts lookup refs (ID)
     */
    LOOKUP,
    /**
     * No conversion (do it yourself, or not at all)
     */
    CUSTOM
  }

  @Getter
  private final String name;
  @Getter
  private final boolean readOnly;
  @Getter
  private final Type type;
  private final Function<T, V> getter;
  private final BiConsumer<T, V> setter;

  public P360Field(String name, Type type, Function<T, V> getter, BiConsumer<T, V> setter) {
    this.name = name;
    this.type = type;
    this.readOnly = false;
    this.getter = getter;
    this.setter = setter;
  }

  public P360Field(String name, Type type, BiConsumer<T, V> setter) {
    this.name = name;
    this.type = type;
    this.readOnly = true;
    this.getter = null;
    this.setter = setter;
  }

  /**
   * Gets value from t
   *
   * @param t
   * @return
   */
  @SuppressWarnings("incomplete-switch")
  public Object get(T t) {
    V value = getter.apply(t);

    switch (type) {
      case STRING: {
        // substitute null by an empty string
        if (value == null) {
          return "";
        }
        break;
      }

      case MULTI_STRING: {
        if (value == null || ((Collection<?>) value).isEmpty()) {
          return EMPTY_MULTI_STRING;
        }
        break;
      }

      case MULTI_ENUM: {
        if(value == null || ((Collection<?>) value).isEmpty()) {
          return EMPTY_MULTI_ENUM;
        }
        break;
      }

      case BIT: {
        return Boolean.TRUE.equals(value) ? "true" : "";
      }

      case LOOKUP: {
        if (value == null) {
          return "";
        } else {
          String text = value.toString();
          int sepIdx = text.indexOf('_');
          if (sepIdx == -1) {
            return "";
          } else {
            return Maps.of(
                "entityId", Integer.parseInt(text.substring(0, sepIdx)),
                "id", text.substring(sepIdx + 1));
          }
        }
      }
    }

    return value;
  }

  /**
   * Sets value on t
   *
   * @param t
   * @param value
   */
  @SuppressWarnings({"unchecked", "incomplete-switch"})
  public void set(T t, Object value) {
    switch (type) {
      case STRING: {
        if (value instanceof String) {
          // replace empty string by null
          if (((String) value).isEmpty()) {
            value = null;
          }
        }
        break;
      }

      case MULTI_STRING:
      case MULTI_ENUM:{
        if (value instanceof Collection<?>) {
          // replace collection with one string by null
          Collection<?> coll = (Collection<?>) value;
          if (coll.size() == 1 && "".equals(coll.iterator().next())) {
            value = Collections.emptyList();
          }
        }
        break;
      }

      case BIT: {
        value = "true".equals(value);
        break;
      }

      case LOOKUP: {
        if (value instanceof String) {
          if (((String) value).isEmpty()) {
            value = null;
          }
        } else if (value instanceof Map<?, ?>) {
          Map<?, ?> map = (Map<?, ?>) value;
          value = String.format("%s_%s", CastUtils.asString(map.get("entityId")), CastUtils.asString(map.get("id")));
        }
      }
    }

    setter.accept(t, (V) value);
  }
}
