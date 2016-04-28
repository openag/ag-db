package openag.db;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Simple callback for {@link Connection}
 */
public interface ConnectionCallback<R> {
  R exec(Connection connection) throws SQLException;
}
