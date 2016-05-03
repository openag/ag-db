package openag.db;

import openag.db.meta.TableColumnMetaData;

/**
 * Database Column model
 */
public class Column extends TableColumnMetaData {

  /**
   * Column owning table
   */
  private Table table;

  public Column() {
  }

  public Column(final TableColumnMetaData metaData) {
    super(metaData);
  }

  public Table getTable() {
    return table;
  }

  void setTable(final Table table) {
    this.table = table;
  }
}
