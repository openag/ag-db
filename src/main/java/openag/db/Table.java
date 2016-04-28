package openag.db;

import openag.db.meta.TableMetaData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DB Table model
 */
public class Table extends TableMetaData {

  private final List<Column> columns = new ArrayList<>();

  private PrimaryKey primaryKey;

  public Table() {
  }

  public Table(final TableMetaData metaData) {
    super(metaData);
  }

  public List<Column> getColumns() {
    return Collections.unmodifiableList(columns);
  }

  public PrimaryKey getPrimaryKey() {
    return primaryKey;
  }

  public String getQualifiedName() {
    if (getSchema() != null) {
      return getSchema() + "." + getName();
    }
    return getName();
  }

  void addColumn(Column column) {
    this.columns.add(column);
  }

  Column findColumn(String name) {
    return this.columns.stream().filter(column -> column.getName().equals(name)).findFirst().get();
  }

  void setPrimaryKey(final PrimaryKey primaryKey) {
    this.primaryKey = primaryKey;
  }
}
