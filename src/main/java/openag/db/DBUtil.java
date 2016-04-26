package openag.db;

import openag.db.meta.ColumnMetaData;
import openag.db.meta.TableMetaData;
import openag.db.meta.TableType;

import javax.sql.DataSource;
import java.io.PrintStream;
import java.sql.*;
import java.util.*;

/**
 * Utility methods for working with {@link Connection} and related objects
 *
 * @author Andrei Maus
 */
public class DBUtil {

  /**
   * Detects database type indicated by the provided connection. NB! The method doesn't close the provided connection!
   *
   * @param connection {@link Connection} instance
   * @return {@link DatabaseType} instance; never NULL
   * @throws SQLException
   */
  public static DatabaseType detectType(Connection connection) throws SQLException {
    assertNotNull(connection);

    final String name = connection.getMetaData().getDatabaseProductName();
    if ("HSQL Database Engine".equals(name)) {
      return DatabaseType.HSQLDB;
    }
    if ("Oracle".equals(name)) {
      return DatabaseType.ORACLE;
    }
    //todo: add other databases
    return DatabaseType.UNKNOWN;
  }

  /**
   * Detects database type indicated by the provided datasource;
   *
   * @param dataSource {@link DataSource} instance
   * @return {@link DatabaseType} instance; never NULL
   * @throws SQLException
   */
  public static DatabaseType detectType(DataSource dataSource) throws SQLException {
    return withConnection(dataSource, DBUtil::detectType);
  }

  /**
   * Closes provided {@link ResultSet} without throwing exception
   *
   * @param rs {@link ResultSet} to close
   */
  public static void closeQuietly(ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (Exception e) {
        //ignore
      }
    }
  }

  /**
   * Closes provided {@link Statement} without throwing exception
   *
   * @param statement {@link Statement} to close
   */
  public static void closeQuietly(Statement statement) {
    if (statement != null) {
      try {
        statement.close();
      } catch (Exception e) {
        //ignore
      }
    }
  }

  /**
   * Closes provided {@link Connection} without throwing exception
   *
   * @param connection {@link Connection} to close
   */
  public static void closeQuietly(Connection connection) {
    if (connection != null) {
      try {
        connection.close();
      } catch (Exception e) {
        //ignore
      }
    }
  }

  /**
   * Executes provided sql string
   */
  public static boolean execute(Connection connection, String sql) throws SQLException {
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement(sql);
      return statement.execute();
    } finally {
      closeQuietly(statement);
    }
  }


  /**
   * List of SQL types supported by the database
   */
  public static List<DBType> getTypes(Connection connection) throws SQLException {
    assertNotNull(connection);

    ResultSet rs = null;
    try {
      rs = connection.getMetaData().getTypeInfo();

      final List<DBType> types = new LinkedList<>();
      while (rs.next()) {
        final DBType type = new DBType();
        type.setName(rs.getString("TYPE_NAME"));
        type.setSqlType(rs.getInt("DATA_TYPE"));
        types.add(type);
      }
      return types;
    } finally {
      closeQuietly(rs);
    }
  }


  /**
   * todo:
   */
  public static List<TableMetaData> getTables(Connection connection,
                                              String catalog,
                                              String schemaPattern,
                                              String tableNamePattern,
                                              TableType... types) throws SQLException {
    assertNotNull(connection);

    ResultSet rs = null;
    try {
      rs = connection.getMetaData().getTables(catalog, schemaPattern, tableNamePattern,
          TableType.toValues(types));

      final List<TableMetaData> tables = new LinkedList<>();
      while (rs.next()) {
        final TableMetaData table = new TableMetaData();
        table.setCatalog(rs.getString("TABLE_CAT"));
        table.setSchema(rs.getString("TABLE_SCHEM"));
        table.setName(rs.getString("TABLE_NAME"));
        table.setType(TableType.parse(rs.getString("TABLE_TYPE")));
        table.setComment(rs.getString("REMARKS"));

        tables.add(table);
      }
      return tables;
    } finally {
      closeQuietly(rs);
    }
  }

  /**
   * Returns column metadata for the specified table
   */
  public static List<ColumnMetaData> getColumns(Connection connection,
                                                String catalog,
                                                String schemaPattern,
                                                String tableNamePattern,
                                                String columnNamePattern) throws SQLException {
    ResultSet rs = null;

    final List<ColumnMetaData> result = new LinkedList<>();
    try {
      rs = connection.getMetaData().getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
      while (rs.next()) {
        final ColumnMetaData c = new ColumnMetaData();
        c.setCatalog(rs.getString("TABLE_CAT"));
        c.setSchema(rs.getString("TABLE_SCHEM"));
        c.setTable(rs.getString("TABLE_NAME"));
        c.setName(rs.getString("COLUMN_NAME"));
        c.setType(rs.getInt("DATA_TYPE"));
        c.setTypeName(rs.getString("TYPE_NAME"));
        c.setSize(rs.getInt("COLUMN_SIZE"));
        c.setDecimals(rs.getInt("DECIMAL_DIGITS"));
        c.setComment(rs.getString("REMARKS"));
        c.setDefaultValue(rs.getString("COLUMN_DEF"));
        c.setCharSize(rs.getInt("CHAR_OCTET_LENGTH"));
        c.setOrdinal(rs.getInt("ORDINAL_POSITION"));
        c.setNullable(ThreeState.parse(rs.getString("IS_NULLABLE")));
        try {
          c.setAutoincrement(ThreeState.parse(rs.getString("IS_AUTOINCREMENT")));
        } catch (SQLException e) {
          //some databases don't return autoincrement info, ignore this error
        }
        result.add(c);
      }
    } finally {
      closeQuietly(rs);
    }
    return result;
  }


  /**
   * Returns list of primary key columns for the specified table
   *
   * @param table  target table name
   * @param schema target schema name
   * @return list of primary key column names; return empty list when table has no columns; never null
   */
  public static List<String> getPrimaryKeyColumns(Connection connection, String schema, String table) throws SQLException {
    assertNotNull(connection);

    ResultSet rs = null;
    try {
      rs = connection.getMetaData().getPrimaryKeys(null, schema, table);

      final Map<Integer, String> columns = new TreeMap<>();
      while (rs.next()) {
        columns.put(rs.getInt("KEY_SEQ"), rs.getString("COLUMN_NAME")); //sort by position in PK constraint
      }
      return new ArrayList<>(columns.values());
    } finally {
      closeQuietly(rs);
    }
  }

  public static List<String> getPrimaryKeyColumns(DataSource dataSource, final String schema, final String table) throws SQLException {
    return withConnection(dataSource, connection -> getPrimaryKeyColumns(connection, schema, table));
  }

  /**
   * Retrieves primary key index name (usually same as primary key constraint name) for the specified table
   *
   * @return primary key index name; returns NULL if name can not be identified or there is no PK constraint on the
   * table
   */
  public static String getPrimaryKeyConstraintName(Connection connection, String schema, String table) throws SQLException {
    ResultSet rs = null;
    try {
      rs = connection.getMetaData().getPrimaryKeys(null, schema, table);
      if (rs.next()) {
        return rs.getString("PK_NAME");
      }
    } finally {
      closeQuietly(rs);
    }
    return null;
  }

  /**
   * Retrieves list of unique index names for the specified table
   *
   * @return list of unique index names; can return empty list; never NULL
   */
  public static List<String> getUniqueConstraintNames(Connection connection, String schema, String table) throws SQLException {
    final String primaryKeyIndexName = getPrimaryKeyConstraintName(connection, schema, table);

    final HashSet<String> names = new HashSet<>(); //using set since may be many columns within one index

    ResultSet rs = null;
    try {
      rs = connection.getMetaData().getIndexInfo(null, schema, table, true, false);
      while (rs.next()) {
        final String indexName = rs.getString("INDEX_NAME");
        if (indexName != null && !indexName.equals(primaryKeyIndexName)) { //skip stats and PK indexes
          names.add(indexName);
        }
      }
    } finally {
      closeQuietly(rs);
    }
    return new ArrayList<>(names);
  }

  /**
   * Returns list of column names for the specified index
   *
   * @param indexName name of the index
   * @return list of index columns; can return empty list; never NULL
   */
  public static List<String> getIndexColumns(Connection connection, String schema, String table, String indexName) throws SQLException {
    final Map<Integer, String> columns = new TreeMap<>();

    ResultSet rs = null;
    try {
      rs = connection.getMetaData().getIndexInfo(null, schema, table, true, false);
      while (rs.next()) {
        final String name = rs.getString("INDEX_NAME");
        if (name != null && name.equals(indexName)) {
          columns.put(rs.getInt("ORDINAL_POSITION"), rs.getString("COLUMN_NAME"));
        }
      }
    } finally {
      closeQuietly(rs);
    }
    return new ArrayList<>(columns.values());
  }


  /**
   * Prints data contained in provided {@link ResultSet} instance as tab-separated table; first line will contain column
   * names. Mainly for debug purposes, quite easy to copy-paste output to spreadsheet application
   *
   * @param rs {@link ResultSet} instance
   * @throws SQLException
   */
  public static void print(ResultSet rs, PrintStream out) throws SQLException {
    final ResultSetMetaData metaData = rs.getMetaData();
    final int count = metaData.getColumnCount();

    for (int i = 1; i <= count; i++) {
      out.print(metaData.getColumnName(i) + "\t");
    }
    out.println();

    while (rs.next()) {
      for (int i = 1; i <= count; i++) {
        out.print(rs.getObject(i) + "\t");
      }
      out.println();
    }
  }


  /**
   * Fetching connection from provided datasource, run the callback on the connection and properly closes it in the end
   *
   * @param dataSource {@link DataSource} instance
   * @param callback   {@link ConnectionCallback} instance
   * @return value returned from the callback
   */
  public static <T> T withConnection(DataSource dataSource, ConnectionCallback<T> callback) throws SQLException {
    assertNotNull(dataSource);

    Connection connection = null;
    try {
      connection = dataSource.getConnection();
      return callback.exec(connection);
    } finally {
      closeQuietly(connection);
    }
  }

  /**
   * Throws {@link IllegalArgumentException} if provided datasource is NULL
   *
   * @param dataSource {@link DataSource} instance
   */
  public static void assertNotNull(DataSource dataSource) {
    if (dataSource == null) {
      throw new IllegalArgumentException("Provided datasource instance is NULL, can't detect database type");
    }
  }

  /**
   * Throws {@link IllegalArgumentException} if provided connection is NULL
   *
   * @param connection {@link Connection} instance
   */
  public static void assertNotNull(Connection connection) {
    if (connection == null) {
      throw new IllegalArgumentException("Provided connection instance is NULL, can't detect database type");
    }
  }

  private DBUtil() {
  }
}
