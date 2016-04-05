package openag.db;


/**
 * Database single table column metadata container
 *
 * @author Andrei Maus
 */
public class DBColumn {

  /* table catalog (may be null) */
  private String catalog;

  /* table schema (may be null) */
  private String schema;

  /* table name */
  private String table;

  /* column name */
  private String name;

  /* SQL type from java.sql.Types */
  private int type;

  /* Data source dependent type name, for a UDT the type name is fully qualified */
  private String typeName;

  /* column size */
  private int size;

  /* the number of fractional digits. 0 is returned for data types where it is not applicable. */
  private int decimals;

  /* comment describing column (may be null) */
  private String comment;

  /* default value for the column (may be null) */
  private String defaultValue;

  /* for char types the maximum number of bytes in the column */
  private int charSize;

  /* index of column in table (starting at 1) */
  private int ordinal;

  private ThreeState nullable;

  private ThreeState autoincrement;

  public String getCatalog() {
    return catalog;
  }

  public String getSchema() {
    return schema;
  }

  public String getTable() {
    return table;
  }

  public String getName() {
    return name;
  }

  public int getType() {
    return type;
  }

  public String getTypeName() {
    return typeName;
  }

  public int getSize() {
    return size;
  }

  public int getDecimals() {
    return decimals;
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

  public ThreeState getNullable() {
    return nullable;
  }

  public ThreeState getAutoincrement() {
    return autoincrement;
  }

  void setCatalog(String catalog) {
    this.catalog = catalog;
  }

  void setSchema(String schema) {
    this.schema = schema;
  }

  void setTable(String table) {
    this.table = table;
  }

  void setName(String name) {
    this.name = name;
  }

  void setType(int type) {
    this.type = type;
  }

  void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  void setSize(int size) {
    this.size = size;
  }

  void setDecimals(int decimals) {
    this.decimals = decimals;
  }

  void setComment(String comment) {
    this.comment = comment;
  }

  void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  void setCharSize(int charSize) {
    this.charSize = charSize;
  }

  void setOrdinal(int ordinal) {
    this.ordinal = ordinal;
  }

  void setNullable(ThreeState nullable) {
    this.nullable = nullable;
  }

  void setAutoincrement(ThreeState autoincrement) {
    this.autoincrement = autoincrement;
  }

  @Override
  public String toString() {
    return "DBColumn{" +
        "catalog='" + catalog + '\'' +
        ", schema='" + schema + '\'' +
        ", table='" + table + '\'' +
        ", name='" + name + '\'' +
        ", type=" + type +
        ", typeName='" + typeName + '\'' +
        ", size=" + size +
        ", decimals=" + decimals +
        ", comment='" + comment + '\'' +
        ", defaultValue='" + defaultValue + '\'' +
        ", charSize=" + charSize +
        ", ordinal=" + ordinal +
        ", nullable=" + nullable +
        ", autoincrement=" + autoincrement +
        '}';
  }

  DBColumn() {
  }
}
