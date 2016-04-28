package openag.db.meta;

/**
 * Database primary key metadata container
 */
public class PrimaryKeyMetaData {

  private String tableCatalog;
  private String tableSchema;
  private String tableName;
  private String columnName;

  private int sequence;

  private String name;

  public String getTableCatalog() {
    return tableCatalog;
  }

  public void setTableCatalog(final String tableCatalog) {
    this.tableCatalog = tableCatalog;
  }

  public String getTableSchema() {
    return tableSchema;
  }

  public void setTableSchema(final String tableSchema) {
    this.tableSchema = tableSchema;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(final String tableName) {
    this.tableName = tableName;
  }

  public String getColumnName() {
    return columnName;
  }

  public void setColumnName(final String columnName) {
    this.columnName = columnName;
  }

  public int getSequence() {
    return sequence;
  }

  public void setSequence(final int sequence) {
    this.sequence = sequence;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }
}
