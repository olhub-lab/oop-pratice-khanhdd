package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseUtil {

  private static final Logger logger = Logger.getLogger(DatabaseUtil.class.getName());

  public static void close(AutoCloseable resource) {
    if (resource != null) {
      try {
        resource.close();
      } catch (Exception e) {
        logger.log(Level.SEVERE, "Could not close JDBC resource", e);
      }
    }
  }

  public static void rollback(Connection conn) {
    if (conn != null) {
      try {
        conn.rollback();
        logger.info("Transaction rolled back successfully.");
      } catch (SQLException e) {
        logger.log(Level.SEVERE, "Failed to rollback transaction", e);
      }
    }
  }

  public static void beginTransaction(Connection conn) throws SQLException {
    if (conn != null && conn.getAutoCommit()) {
      conn.setAutoCommit(false);
    }
  }

  public static void commitTransaction(Connection conn) throws SQLException {
    if (conn != null && !conn.getAutoCommit()) {
      conn.commit();
      conn.setAutoCommit(true);
    }
  }

  private DatabaseUtil() {

  }
}