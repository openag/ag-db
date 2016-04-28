package openag.db.meta;

/**
 * Database data type metadata
 */
public class TypeMetaData {

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
