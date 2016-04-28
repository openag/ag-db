package openag.db;

import java.sql.SQLException;

/**
 * Generic callback interface for SQL operations
 */
public interface Callback<T, R> {

  R exec(T t) throws SQLException;

}
