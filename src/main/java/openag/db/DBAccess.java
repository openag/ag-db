package openag.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

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

  public List<DBTable> getTables(String name) throws SQLException {
    return DBUtil.getTables(connection(), catalog, schema, name, TableType.TABLE);
  }

  public List<DBColumn> getColumns(String tableName) throws SQLException {
    return DBUtil.getColumns(connection(), catalog, schema, tableName, null);
  }

  public void createTable(String name, List<DBColumn> columns) {
    final StringBuilder query = new StringBuilder("create table ")
        .append(schema).append(".").append(name).append(" (");

    for (DBColumn column : columns) {
      query.append(column.getName());
      // column_name1 data_type(size), //todo: translate data type
    }

    query.append(");");

    //todo: implement
  }

  public void rows() {
    //todo: implement data iteration
  }

  public void close() {
    DBUtil.closeQuietly(connection);
  }

  private Connection connection() throws SQLException {
    if (this.connection == null) {
      final Connection c = DriverManager.getConnection(url, user, password);

      final List<DBType> types = DBUtil.getTypes(c);
      //todo: implement

      this.connection = c;
    }
    return this.connection;
  }

}
