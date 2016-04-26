package openag.db;

import openag.db.meta.ColumnMetaData;
import openag.db.meta.TableMetaData;
import openag.db.meta.TableType;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static openag.db.DBUtil.withConnection;

/**
 * TODO: add comment
 */
public class Database implements Closeable {

  private final DataSource dataSource;

  public Database(final DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public Database(String url, String user, String password) throws SQLException {
    this(new SingleConnectionDataSource(url, user, password));
  }

  public List<Table> getTables(TableFilter filter) throws SQLException {
    return withConnection(dataSource, connection -> {
      final List<Table> result = new ArrayList<>();

      for (TableMetaData table : DBUtil.getTables(connection, filter.catalog, filter.schemaPattern, filter.tableNamePattern, filter.types)) {
        final Table t = new Table(table);
        result.add(t);

        for (ColumnMetaData column : DBUtil.getColumns(connection, table.getCatalog(), table.getSchema(), table.getName(), null)) {
          t.addColumn(new Column(column));
        }

      }

      return result;
    });
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
}
