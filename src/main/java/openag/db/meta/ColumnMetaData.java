package openag.db.meta;

import openag.db.ThreeState;

public abstract class ColumnMetaData {

  /* column name */
  private String name;

  /* table name */
  private String tableName;

  /* table catalog (may be null) */
  private String catalog;

  /* table schema (may be null) */
  private String schema;

  /* column size */
  private int size;

  /* the number of fractional digits. 0 is returned for data types where it is not applicable. */
  private int decimals;

  /* SQL type from java.sql.Types */
  private int type;

  /* Data source dependent type name, for a UDT the type name is fully qualified */
  private String typeName;

  private ThreeState autoincrement;
  private ThreeState nullable;

  public ColumnMetaData() {
  }

  public ColumnMetaData(ColumnMetaData copy) {
    this.catalog = copy.catalog;
    this.schema = copy.schema;
    this.tableName = copy.tableName;
    this.name = copy.name;
    this.type = copy.type;
    this.typeName = copy.typeName;
    this.size = copy.size;
    this.decimals = copy.decimals;
    this.nullable = copy.nullable;
    this.autoincrement = copy.autoincrement;
  }

  public ThreeState getAutoincrement() {
    return autoincrement;
  }

  public String getCatalog() {
    return catalog;
  }

  public String getSchema() {
    return schema;
  }

  public String getTableName() {
    return tableName;
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

  public ThreeState getNullable() {
    return nullable;
  }

  public void setCatalog(final String catalog) {
    this.catalog = catalog;
  }

  public void setSchema(final String schema) {
    this.schema = schema;
  }

  public void setTableName(final String tableName) {
    this.tableName = tableName;
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

  public void setNullable(final ThreeState nullable) {
    this.nullable = nullable;
  }

  public void setAutoincrement(final ThreeState autoincrement) {
    this.autoincrement = autoincrement;
  }

  @Override
  public String toString() {
    return "ColumnMetaData{" +
        "name='" + name + '\'' +
        ", tableName='" + tableName + '\'' +
        ", catalog='" + catalog + '\'' +
        ", schema='" + schema + '\'' +
        ", size=" + size +
        ", decimals=" + decimals +
        ", type=" + type +
        ", typeName='" + typeName + '\'' +
        ", autoincrement=" + autoincrement +
        ", nullable=" + nullable +
        '}';
  }
}
