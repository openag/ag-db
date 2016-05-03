package openag.db.meta;

public class ResultSetColumnMetaData extends ColumnMetaData {

  private boolean caseSensitive;
  private boolean searchable;
  private boolean currency;
  private boolean signed;
  private boolean readOnly;
  private boolean writable;
  private boolean definitelyWritable;

  private int columnDisplaySize;

  private String columnLabel;
  private String columnClassName;

  public ResultSetColumnMetaData() {
  }

  public ResultSetColumnMetaData(ResultSetColumnMetaData copy) {
    super(copy);
    this.caseSensitive = copy.caseSensitive;
    this.searchable = copy.searchable;
    this.currency = copy.currency;
    this.signed = copy.signed;
    this.readOnly = copy.readOnly;
    this.writable = copy.writable;
    this.definitelyWritable = copy.definitelyWritable;
    this.columnDisplaySize = copy.columnDisplaySize;
    this.columnLabel = copy.columnLabel;
    this.columnClassName = copy.columnClassName;
  }

  public boolean isCaseSensitive() {
    return caseSensitive;
  }

  public void setCaseSensitive(final boolean caseSensitive) {
    this.caseSensitive = caseSensitive;
  }

  public boolean isSearchable() {
    return searchable;
  }

  public void setSearchable(final boolean searchable) {
    this.searchable = searchable;
  }

  public boolean isCurrency() {
    return currency;
  }

  public void setCurrency(final boolean currency) {
    this.currency = currency;
  }

  public boolean isSigned() {
    return signed;
  }

  public void setSigned(final boolean signed) {
    this.signed = signed;
  }

  public boolean isReadOnly() {
    return readOnly;
  }

  public void setReadOnly(final boolean readOnly) {
    this.readOnly = readOnly;
  }

  public boolean isWritable() {
    return writable;
  }

  public void setWritable(final boolean writable) {
    this.writable = writable;
  }

  public boolean isDefinitelyWritable() {
    return definitelyWritable;
  }

  public void setDefinitelyWritable(final boolean definitelyWritable) {
    this.definitelyWritable = definitelyWritable;
  }

  public int getColumnDisplaySize() {
    return columnDisplaySize;
  }

  public void setColumnDisplaySize(final int columnDisplaySize) {
    this.columnDisplaySize = columnDisplaySize;
  }

  public String getColumnLabel() {
    return columnLabel;
  }

  public void setColumnLabel(final String columnLabel) {
    this.columnLabel = columnLabel;
  }

  public String getColumnClassName() {
    return columnClassName;
  }

  public void setColumnClassName(final String columnClassName) {
    this.columnClassName = columnClassName;
  }
}
