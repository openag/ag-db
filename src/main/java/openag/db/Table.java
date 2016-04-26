package openag.db;

import openag.db.meta.TableMetaData;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: add comment
 */
public class Table extends TableMetaData {

  private final List<Column> columns = new ArrayList<>();

  public Table() {
  }

  public Table(final TableMetaData metaData) {
    super(metaData);
  }

  public List<Column> getColumns() {
    return columns;
  }

  public void addColumn(Column column) { //todo: check for duplicates!
    this.columns.add(column);
  }


}
