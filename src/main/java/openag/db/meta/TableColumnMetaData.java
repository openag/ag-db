package openag.db.meta;


/**
 * Database single table column metadata container
 *
 * @author Andrei Maus
 */
public class TableColumnMetaData extends ColumnMetaData {

  /* comment describing column (may be null) */
  private String comment;

  /* default value for the column (may be null) */
  private String defaultValue;

  /* for char types the maximum number of bytes in the column */
  private int charSize;

  /* index of column in table (starting at 1) */
  private int ordinal;

  public TableColumnMetaData() {
  }

  public TableColumnMetaData(TableColumnMetaData copy) {
    super(copy);
    this.comment = copy.comment;
    this.defaultValue = copy.defaultValue;
    this.charSize = copy.charSize;
    this.ordinal = copy.ordinal;
  }

  public String getComment() {
    return comment;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public int getCharSize() {
    return charSize;
  }

  public int getOrdinal() {
    return ordinal;
  }

  public void setComment(final String comment) {
    this.comment = comment;
  }

  public void setDefaultValue(final String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public void setCharSize(final int charSize) {
    this.charSize = charSize;
  }

  public void setOrdinal(final int ordinal) {
    this.ordinal = ordinal;
  }

  @Override
  public String toString() {
    return "TableColumnMetaData{" +
        "comment='" + comment + '\'' +
        ", defaultValue='" + defaultValue + '\'' +
        ", charSize=" + charSize +
        ", ordinal=" + ordinal +
        '}' + super.toString();
  }
}
