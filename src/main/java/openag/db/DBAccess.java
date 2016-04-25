package openag.db;

import openag.db.meta.ColumnMetaData;
import openag.db.meta.TableMetaData;
import openag.db.meta.TableType;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * TODO: add comment
 * <p>
 *
 * @author Andrei Maus
 */
public class DBAccess {

  private final String url;
  private final String user;
  private final String password;
  private final String catalog;
  private final String schema;

  private volatile Connection connection;
  private List<DBType> types;

  public DBAccess(String url,
                  String user,
                  String password,
                  String catalog,
                  String schema) {
    this.url = url;
    this.user = user;
    this.password = password;
    this.catalog = catalog;
    this.schema = schema;
  }

  public List<TableMetaData> getTables() throws SQLException {
    return DBUtil.getTables(connection(), catalog, schema, null, TableType.TABLE);
  }

  public List<ColumnMetaData> getColumns(String tableName) throws SQLException {
    return DBUtil.getColumns(connection(), catalog, schema, tableName, null);
  }

  void createTable(String name, List<ColumnMetaData> columns) throws SQLException {
    final Connection connection = connection();

    final StringBuilder query = new StringBuilder("create table ");

    if (schema != null) {
      query.append(schema).append(".");
    }

    query.append(name).append(" (");

    for (ColumnMetaData column : columns) {
      query.append(column.getName()).append(" ").append(toLocalDataType(column));
      if (column.getSize() > 0 && columnSizeApplicable(column)) {
        query.append("(").append(column.getSize());
        if (column.getDecimals() > 0) {
          query.append(",").append(column.getDecimals());
        }
        query.append(")");
      }
      query.append(",");
    }

    query.deleteCharAt(query.length() - 1);
    query.append(");");

    DBUtil.execute(connection, query.toString());
  }

  ResultSetIterator select(String name) throws SQLException { //todo:
    final PreparedStatement statement = connection().prepareStatement("select * from " + name);
    return new ResultSetIterator(statement.executeQuery());
  }

  void insert(String name, Map<String, Object> data) throws SQLException {
    final List<String> qMarks = Collections.nCopies(data.size(), "?");

    final PreparedStatement statement = connection().prepareStatement("insert into " + name + " (" + (String.join(",", data.keySet())) + ") values (" +
        String.join(",", qMarks) + ")");

    int i = 0;

    for (Object o : data.values()) {
      statement.setObject(++i, o);
    }

    statement.execute();
  }

  private String toLocalDataType(final ColumnMetaData column) {
    final Optional<DBType> nameMatch = this.types.stream()
        .filter(dbType -> dbType.getName().equalsIgnoreCase(column.getTypeName())).findFirst();

    if (nameMatch.isPresent()) {
      return nameMatch.get().getName();
    }

    final Optional<DBType> typeMatch = this.types.stream()
        .filter(dbType -> dbType.getSqlType() == column.getType()).findFirst();

    if (typeMatch.isPresent()) {
      return typeMatch.get().getName();
    }

    throw new IllegalStateException("Unable to find matching SQL type for column type: " + column.getTypeName());
  }

  private boolean columnSizeApplicable(final ColumnMetaData column) {
    final int type = column.getType();
    return type != Types.TIMESTAMP
        && type != Types.FLOAT
        && type != Types.CLOB;
  }

  private Connection connection() throws SQLException {
    if (this.connection == null) {
      final Connection c = DriverManager.getConnection(url, user, password);
      c.setAutoCommit(true);

      types = DBUtil.getTypes(c);

      this.connection = c;
    }
    return this.connection;
  }

  public void close() {
    DBUtil.closeQuietly(connection);
  }

}
