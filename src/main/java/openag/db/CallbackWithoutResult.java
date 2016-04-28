package openag.db;

import java.sql.SQLException;

/**
 * Extension of {@link Callback} that doesn't require to return any result
 */
public interface CallbackWithoutResult<T> extends Callback<T, Object> {

  @Override
  default Object exec(T t) throws SQLException {
    execute(t);
    return null;
  }

  void execute(T t) throws SQLException;
}
