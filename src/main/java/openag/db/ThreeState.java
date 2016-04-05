package openag.db;

/**
 * 3-values "boolean"
 *
 * @author Andrei Maus
 */
public enum ThreeState {

  YES,

  NO,

  UNDEFINED;

  /**
   * Parses provided string into {@link ThreeState} instance. yes, y, 1 will be interpreted to {@link #YES}; no, n, 0 -
   * {@link #NO}; {@link #UNDEFINED} will be returned otherwise
   */
  public static ThreeState parse(String s) {
    if (isYes(s)) {
      return YES;
    }

    if (isNo(s)) {
      return NO;
    }

    return UNDEFINED;
  }

  /**
   * Parses provided boolean instance into {@link ThreeState} instance. {@link Boolean#TRUE} will result in {@link
   * #YES}, {@link Boolean#FALSE} - {@link #NO}; {@link #UNDEFINED} will be returned if boolean is NULL
   */
  public static ThreeState parse(Boolean b) {
    if (b == null) {
      return UNDEFINED;
    }
    return b ? YES : NO;
  }

  private static boolean isYes(String s) {
    return "yes".equalsIgnoreCase(s)
        || "y".equalsIgnoreCase(s)
        || "1".equals(s);
  }

  private static boolean isNo(String s) {
    return "no".equalsIgnoreCase(s)
        || "n".equalsIgnoreCase(s)
        || "0".equals(s);
  }
}
