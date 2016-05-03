package openag.db;

import openag.db.meta.*;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static openag.db.DBUtil.*;

/**
 * TODO: add comment
 */
public abstract class Database implements Closeable {

  protected final DataSource dataSource;

  /**
   * DB Types supported by the current database
   */
  private final List<TypeMetaData> _types = new ArrayList<>();

  /**
   * Creates suitable {@link Database} instance for the provided {@link DataSource}
   */
  public static Database create(DataSource dataSource) throws SQLException {
    final DatabaseType type = withConnection(dataSource, DBUtil::detectType);

    final Database database = doCreate(dataSource, type);
    database.init();

    return database;
  }

  private static Database doCreate(final DataSource dataSource, final DatabaseType type) {
    switch (type) {
      case ORACLE:
        return new Oracle(dataSource);
      case HSQLDB:
        return new Hsqldb(dataSource);
    }
    throw new IllegalArgumentException("Database type " + type + " is not supported");
  }

  public static Database create(String url, String user, String password) throws SQLException {
    return create(new SingleConnectionDataSource(url, user, password));
  }

  private Database(final DataSource dataSource) {
    this.dataSource = dataSource;
  }

  /**
   * Possibility to perform any general initialization routine; runs before any other activity
   */
  protected void init() throws SQLException {
  }

  public List<Table> getTables(TableFilter filter) throws SQLException {
    return withConnection(dataSource, connection -> {
      final List<Table> result = new ArrayList<>();

      for (TableMetaData table : DBUtil.getTables(connection, filter.catalog, filter.schemaPattern, filter.tableNamePattern, filter.types)) {
        final Table t = new Table(table);
        result.add(t);

        for (TableColumnMetaData column : DBUtil.getColumns(connection, table.getCatalog(), table.getSchema(), table.getName(), null)) {
          final Column c = new Column(column);
          c.setTable(t);
          t.addColumn(c);
        }

        final List<PrimaryKeyMetaData> primaryKeys =
            DBUtil.getPrimaryKeys(connection, table.getCatalog(), table.getSchema(), table.getName());

        if (!primaryKeys.isEmpty()) {
          final PrimaryKey pk = new PrimaryKey();
          t.setPrimaryKey(pk);

          Collections.sort(primaryKeys, (o1, o2) -> o1.getSequence() - o2.getSequence()); //todo:

          pk.setName(primaryKeys.get(0).getName());
          pk.setTable(t);

          for (PrimaryKeyMetaData key : primaryKeys) {
            pk.addColumn(t.findColumn(key.getColumnName()));
          }
        }
      }

      return result;
    });
  }

  public void create(Table table) throws SQLException {
    if (table.getSchema() != null) {
      if (!getSchemas().contains(table.getSchema())) {
        createSchema(table.getSchema());
      }
    }

    final StringBuilder query = new StringBuilder("create table ");

    if (table.getSchema() != null) {
      query.append(table.getSchema()).append(".");
    }

    query.append(table.getName()).append(" (");

    for (Column column : table.getColumns()) {
      query.append(column.getName()).append(" ").append(toLocalDatabaseTypeName(column));
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

    withConnection(dataSource,
        (CallbackWithoutResult<Connection>) connection -> execute(connection, query.toString()));

    final PrimaryKey pk = table.getPrimaryKey();
    if (pk != null) {
      create(pk);
    }
  }

  public void create(PrimaryKey pk) throws SQLException {
    final StringBuilder query = new StringBuilder("alter table ")
        .append(pk.getTable().getQualifiedName())
        .append(" add constraint ")
        .append(pk.getName())
        .append(" primary key (")
        .append(String.join(",", pk.getColumnNames()))
        .append(")");

    withConnection(dataSource,
        (CallbackWithoutResult<Connection>) connection -> execute(connection, query.toString()));
  }

  protected boolean columnSizeApplicable(final TableColumnMetaData column) {
    final int type = column.getType();
    return type != Types.TIMESTAMP
        && type != Types.FLOAT
        && type != Types.CLOB;
  }

  /**
   * Returns most suitable local database data type name for the provided column.
   */
  protected String toLocalDatabaseTypeName(final Column column) throws SQLException {
    final Optional<TypeMetaData> typeMatch = this.types().stream()
        .filter(dbType -> dbType.getSqlType() == column.getType()).findFirst();
    if (typeMatch.isPresent()) {
      return typeMatch.get().getName();
    }

    final Optional<TypeMetaData> nameMatch = types().stream()
        .filter(dbType -> dbType.getName().equalsIgnoreCase(column.getTypeName())).findFirst();

    if (nameMatch.isPresent()) {
      return nameMatch.get().getName();
    }

    throw new IllegalStateException("Unable to find matching SQL type for column type: " + column.getTypeName());
  }

  protected final List<TypeMetaData> types() throws SQLException {
    if (_types.isEmpty()) {
      withConnection(dataSource, connection -> _types.addAll(DBUtil.getTypes(connection)));
    }
    return _types;
  }

  /**
   * @return all visible schema names
   */
  public List<String> getSchemas() throws SQLException {
    throw new UnsupportedOperationException("getSchemas");
  }

  /**
   * Creates new schema with given name
   */
  public void createSchema(String name) throws SQLException {
    throw new UnsupportedOperationException("createSchema");
  }


  @Override
  public void close() throws IOException {
    if (dataSource instanceof Closeable) {
      ((Closeable) dataSource).close();
    }
  }

  public static class TableFilter {
    private final String catalog;
    private final String schemaPattern;
    private final String tableNamePattern;
    private final TableType[] types;

    public TableFilter(final String catalog, final String schemaPattern, final String tableNamePattern, final TableType[] types) {
      this.catalog = catalog;
      this.schemaPattern = schemaPattern;
      this.tableNamePattern = tableNamePattern;
      this.types = types;
    }

    public static TableFilter allTablesForSchema(String schema) {
      return new TableFilter(null, schema, null, new TableType[]{TableType.TABLE});
    }
  }

  private static class Oracle extends Database {
    private Oracle(final DataSource dataSource) {
      super(dataSource);
    }
  }

  /**
   * HSQLDB - specific implementation
   */
  private static class Hsqldb extends Database {
    private Hsqldb(final DataSource dataSource) {
      super(dataSource);
    }

    @Override
    protected void init() throws SQLException {
      withConnection(dataSource, (CallbackWithoutResult<Connection>) connection -> {

        /* http://hsqldb.org/doc/guide/dbproperties-chapt.html
         * Allow underscore and dollar sign in database object names for compatibility */
        DBUtil.execute(connection, "SET DATABASE SQL REGULAR NAMES FALSE");
      });
    }

    @Override
    protected String toLocalDatabaseTypeName(final Column column) throws SQLException {
      if ("NVARCHAR2".equals(column.getTypeName())) {
        return "VARCHAR";
      }

      if ("NCLOB".equals(column.getTypeName())) {
        return "CLOB";
      }

      return super.toLocalDatabaseTypeName(column);
    }

    @Override
    public List<String> getSchemas() throws SQLException {
      return withPreparedStatement(dataSource, "select distinct schema_name from INFORMATION_SCHEMA.SCHEMATA",
          statement -> {
            final List<String> result = new ArrayList<>();
            for (ResultSet rs : asIterable(statement.executeQuery())) {
              result.add(rs.getString("schema_name"));
            }
            return result;
          });
    }

    @Override
    public void createSchema(final String name) throws SQLException {
      withConnection(dataSource, (CallbackWithoutResult<Connection>) connection -> {
        DBUtil.execute(connection, "CREATE SCHEMA " + name + " AUTHORIZATION DBA");
      });
    }
  }
}
