package openag.db.meta;

/**
 * Database table metadata container
 * <p>
 *
 * @author Andrei Maus
 */
public class TableMetaData {

  /* table catalog (may be null) */
  private String catalog;

  /* table schema (may be null) */
  private String schema;

  /* table name */
  private String name;

  private TableType type;

  private String comment;

  public TableMetaData() {
  }

  public TableMetaData(TableMetaData copy) {
    this.catalog = copy.catalog;
    this.schema = copy.schema;
    this.name = copy.name;
    this.type = copy.type;
    this.comment = copy.comment;
  }

  public String getCatalog() {
    return catalog;
  }

  public String getSchema() {
    return schema;
  }

  public String getName() {
    return name;
  }

  public TableType getType() {
    return type;
  }

  public String getComment() {
    return comment;
  }

  public void setCatalog(final String catalog) {
    this.catalog = catalog;
  }

  public void setSchema(final String schema) {
    this.schema = schema;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setType(final TableType type) {
    this.type = type;
  }

  public void setComment(final String comment) {
    this.comment = comment;
  }

  @Override
  public String toString() {
    return "TableMetaData{" +
        "catalog='" + catalog + '\'' +
        ", schema='" + schema + '\'' +
        ", name='" + name + '\'' +
        ", type=" + type +
        ", comment='" + comment + '\'' +
        '}';
  }
}
