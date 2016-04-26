package openag.db;

import openag.db.sql.AbstractDataSource;
import openag.db.sql.DelegatingConnection;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * TODO: add comment
 */
public class SingleConnectionDataSource extends AbstractDataSource implements Closeable {

  private final Connection connection;

  public SingleConnectionDataSource(String url, String user, String password) throws SQLException {
    this(DriverManager.getConnection(url, user, password));
  }

  public SingleConnectionDataSource(final Connection connection) {
    this.connection = new DelegatingConnection(connection) {
      @Override
      public void close() throws SQLException {
        //no-op
      }
    };
  }

  @Override
  public Connection getConnection() throws SQLException {
    return connection;
  }

  @Override
  public <T> T unwrap(final Class<T> iface) throws SQLException {
    return connection.unwrap(iface);
  }

  @Override
  public boolean isWrapperFor(final Class<?> iface) throws SQLException {
    return connection.isWrapperFor(iface);
  }

  @Override
  public void close() throws IOException {
    DBUtil.closeQuietly(connection);
  }
}
