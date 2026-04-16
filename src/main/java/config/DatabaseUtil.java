package config;

import exception.database.DatabaseException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseUtil {
  private static final Logger logger = Logger.getLogger(DatabaseUtil.class.getName());

  private DatabaseUtil() {}

  public static void beginTransaction(Connection conn) {
    try {
      if (conn != null && conn.getAutoCommit()) {
        conn.setAutoCommit(false);
        logger.fine("Transaction started (Auto-commit disabled).");
      }
    } catch (SQLException e) {
      throw new DatabaseException("Could not start transaction.", e);
    }
  }

  public static void commitTransaction(Connection conn) {
    try {
      if (conn != null && !conn.getAutoCommit()) {
        conn.commit();
        conn.setAutoCommit(true);
        logger.info("Transaction committed successfully.");
      }
    } catch (SQLException e) {
      throw new DatabaseException("Could not commit transaction.", e);
    }
  }

  public static void rollbackTransaction(Connection conn) {
    if (conn != null) {
      try {
        conn.rollback();
        conn.setAutoCommit(true);
        logger.warning("Transaction rolled back due to an error.");
      } catch (SQLException e) {
        logger.log(Level.SEVERE, "Critical error: Failed to rollback transaction!", e);
      }
    }
  }

  public static void close(AutoCloseable resource) {
    if (resource != null) {
      try {
        resource.close();
      } catch (Exception e) {
        logger.log(Level.SEVERE, "Could not close JDBC resource.", e);
      }
    }
  }
}