package openag.db.sql;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * Default parent class for custom {@link DataSource} implementations
 */
public abstract class AbstractDataSource implements DataSource {

  private int loginTimeout;
  private PrintWriter logWriter;

  @Override
  public Connection getConnection(final String username, final String password) throws SQLException {
    return getConnection();
  }

  @Override
  public <T> T unwrap(final Class<T> iface) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  @Override
  public boolean isWrapperFor(final Class<?> iface) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    return logWriter;
  }

  @Override
  public void setLogWriter(final PrintWriter out) throws SQLException {
    this.logWriter = out;
  }

  @Override
  public void setLoginTimeout(final int seconds) throws SQLException {
    this.loginTimeout = seconds;
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    return loginTimeout;
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    throw new SQLFeatureNotSupportedException();
  }
}
