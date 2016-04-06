package openag.db;

/**
 * Database table metadata container
 * <p>
 *
 * @author Andrei Maus
 */
public class DBTable {

  /* table catalog (may be null) */
  private String catalog;

  /* table schema (may be null) */
  private String schema;

  /* table name */
  private String name;

  private TableType type;

  private String comment;

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

  void setCatalog(final String catalog) {
    this.catalog = catalog;
  }

  void setSchema(final String schema) {
    this.schema = schema;
  }

  void setName(final String name) {
    this.name = name;
  }

  void setType(final TableType type) {
    this.type = type;
  }

  void setComment(final String comment) {
    this.comment = comment;
  }

  @Override
  public String toString() {
    return "DBTable{" +
        "catalog='" + catalog + '\'' +
        ", schema='" + schema + '\'' +
        ", name='" + name + '\'' +
        ", type=" + type +
        ", comment='" + comment + '\'' +
        '}';
  }

  DBTable() {
  }
}
