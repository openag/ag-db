package openag.db;

import openag.db.meta.ColumnMetaData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DB primary key model
 */
public class PrimaryKey {

  private String name; //constraint name

  private Table table;

  //primary key columns in the order of their PK declaration
  private final List<Column> columns = new ArrayList<>();

  PrimaryKey() {
  }

  public String getName() {
    return name;
  }

  public Table getTable() {
    return table;
  }

  public List<Column> getColumns() {
    return Collections.unmodifiableList(columns);
  }

  public List<String> getColumnNames() {
    return columns.stream().map(ColumnMetaData::getName).collect(Collectors.toList());
  }

  void setName(final String name) {
    this.name = name;
  }

  void setTable(final Table table) {
    this.table = table;
  }

  void addColumn(Column column) {
    this.columns.add(column);
  }
}
