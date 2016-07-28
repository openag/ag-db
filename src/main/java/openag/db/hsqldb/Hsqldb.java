package openag.db.hsqldb;

import org.hsqldb.util.DatabaseManagerSwing;

import java.awt.*;
import java.lang.reflect.Field;

/**
 * Static utility methods for working with HSQLDB (http://hsqldb.org) database
 *
 * @author Andrei Maus
 */
public class Hsqldb {
  private Hsqldb() {
  }

  /**
   * Builds simple in-memory JDBC URL
   */
  public static String mem(final String dbName) {
    return "jdbc:hsqldb:mem:" + dbName;
  }

  /**
   * Starts HSQLDB Swing UI manager which is automatically connected to the in-memory database with specified name. The
   * UI is started in the caller thread, essentially blocking it until UI exits
   *
   * @param dbName in-memory database name to connect
   */
  public static void startManager(String dbName) {
    checkHeadless();
    new ManagerStart(mem(dbName)).run();
  }

  /**
   * Starts HSQLDB Swing UI manager which is automatically connected to the in-memory database with specified name. The
   * UI is started in a separate daemon thread, allowing main thread to continue
   *
   * @param dbName in-memory database name to connect
   */
  public static void startManagerAsync(String dbName) {
    checkHeadless();
    final Thread thread = new Thread(new ManagerStart(mem(dbName)));
    thread.setDaemon(true);
    thread.start();
  }

  private static void checkHeadless() {
    if (GraphicsEnvironment.isHeadless()) {
      throw new RuntimeException("You're running java in headless mode. Set startup flag '-Djava.awt.headless=false' " +
          "in order to disable it. For more information check http://www.oracle.com/technetwork/articles/javase/headless-136834.html");
    }
  }

  /**
   * The HSQLDB Swing UI starter; wrapped into {@link Runnable} to run in separate thread if needed
   */
  private static class ManagerStart implements Runnable {
    private final String url;

    public ManagerStart(final String url) {
      this.url = url;
    }

    @Override
    public void run() {
      DatabaseManagerSwing.main(new String[]{"--url", url, "--noexit"});

      final DatabaseManagerSwing swing = val(DatabaseManagerSwing.class, "refForFontDialogSwing");
      final Thread updaterThread = val(swing, "buttonUpdaterThread");

      try {
        updaterThread.join(); // waiting for the UI thread to finish
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    @SuppressWarnings("unchecked") //has to suppress, reflection...
    private static <T> T val(final Class<?> clazz, final String name) {
      try {
        return (T) accessible(clazz.getDeclaredField(name)).get(null);
      } catch (IllegalAccessException | NoSuchFieldException e) {
        throw new RuntimeException(e);
      }
    }

    @SuppressWarnings("unchecked") //has to suppress, reflection...
    private static <T> T val(final Object target, final String name) {
      try {
        return (T) accessible(target.getClass().getDeclaredField(name)).get(target);
      } catch (IllegalAccessException | NoSuchFieldException e) {
        throw new RuntimeException(e);
      }
    }

    private static Field accessible(Field f) {
      if (!f.isAccessible()) {
        f.setAccessible(true);
      }
      return f;
    }
  }
}
