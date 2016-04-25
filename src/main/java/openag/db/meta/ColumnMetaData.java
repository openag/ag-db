package openag.db.meta;


import openag.db.ThreeState;

/**
 * Database single table column metadata container
 *
 * @author Andrei Maus
 */
public class ColumnMetaData {

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

  public void setCatalog(final String catalog) {
    this.catalog = catalog;
  }

  public void setSchema(final String schema) {
    this.schema = schema;
  }

  public void setTable(final String table) {
    this.table = table;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setType(final int type) {
    this.type = type;
  }

  public void setTypeName(final String typeName) {
    this.typeName = typeName;
  }

  public void setSize(final int size) {
    this.size = size;
  }

  public void setDecimals(final int decimals) {
    this.decimals = decimals;
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

  public void setNullable(final ThreeState nullable) {
    this.nullable = nullable;
  }

  public void setAutoincrement(final ThreeState autoincrement) {
    this.autoincrement = autoincrement;
  }

  @Override
  public String toString() {
    return "ColumnMetaData{" +
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
}
