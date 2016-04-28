package openag.db;

import openag.db.meta.ColumnMetaData;

/**
 * Database Column model
 */
public class Column extends ColumnMetaData {

  /**
   * Column owning table
   */
  private Table table;

  public Column() {
  }

  public Column(final ColumnMetaData metaData) {
    super(metaData);
  }

  public Table getTable() {
    return table;
  }

  void setTable(final Table table) {
    this.table = table;
  }
}
