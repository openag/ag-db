package openag.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Simple {@link Iterator} wrapper around provided {@link ResultSet}
 * <p>
 *
 * @author Andrei Maus
 */
public class ResultSetIterator implements Iterator<ResultSet>, AutoCloseable {

  private final ResultSet rs;

  /**
   * Workaround for case when provided ResultSet doesn't support {@link ResultSet#previous()} operation
   */
  private boolean readAhead;

  /**
   * Indicates that ResultSet was closed; automatic closure will happen when the end of ResultSet is reached
   */
  private boolean closed;

  public ResultSetIterator(final ResultSet rs) {
    this.rs = rs;
  }

  @Override
  public boolean hasNext() {
    if (closed) {
      return false;
    }

    if (readAhead) {
      return true;
    }

    readAhead = true;

    try {
      final boolean next = rs.next();

      if (!next) {
        close();
      }

      return next;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ResultSet next() {
    if (closed) {
      return rs;
    }

    if (readAhead) {
      readAhead = false;
    } else {
      try {
        if (!rs.next()) {
          close();
        }
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
    return rs;
  }

  @Override
  public void close() {
    closed = true;

    DBUtil.closeQuietly(rs);

    try {
      if (rs.getStatement() != null) {
        DBUtil.closeQuietly(rs.getStatement());
      }
    } catch (SQLException e) {
      //ignore
    }
  }
}
