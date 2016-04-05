package openag.db;

/**
 * TODO: add comment
 * <p>
 *
 * @author Andrei Maus
 */
public class DBType {

  private String name;
  private int sqlType;

  public String getName() {
    return name;
  }

  public int getSqlType() {
    return sqlType;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setSqlType(final int sqlType) {
    this.sqlType = sqlType;
  }
}
