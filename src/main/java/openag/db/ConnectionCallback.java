package openag.db;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * TODO: add comment
 */
public interface ConnectionCallback<R> {
  R exec(Connection connection) throws SQLException;
}
