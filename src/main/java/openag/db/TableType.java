package openag.db;

import java.util.ArrayList;

/**
 * Available table types (specific type availability depends on database implementation)
 * <p>
 *
 * @author Andrei Maus
 */
public enum TableType {

  TABLE("TABLE"),

  VIEW("VIEW"),

  SYSTEM_TABLE("SYSTEM TABLE"),

  GLOBAL_TEMPORARY("GLOBAL TEMPORARY"),

  LOCAL_TEMPORARY("LOCAL TEMPORARY"),

  ALIAS("ALIAS"),

  SYNONYM("SYNONYM"),

  UNDEFINED("UNDEFINED");

  private final String value;

  TableType(final String value) {
    this.value = value;
  }

  public static TableType parse(final String s) {
    for (TableType type : values()) {
      if (type.value.equalsIgnoreCase(s)) {
        return type;
      }
    }
    return UNDEFINED;
  }

  public static String[] toValues(final TableType[] types) {
    if (types == null || types.length == 0) {
      return null;
    }

    final ArrayList<String> result = new ArrayList<>(types.length);
    for (TableType type : types) {
      result.add(type.value);
    }

    return result.toArray(new String[result.size()]);
  }
}
