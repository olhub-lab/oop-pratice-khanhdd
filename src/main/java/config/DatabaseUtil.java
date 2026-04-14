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
        logger.fine(() -> "Giao dịch bắt đầu (Đã tắt auto-commit)");
      }
    } catch (SQLException e) {
      throw new DatabaseException("Không thể bắt đầu giao dịch", e);
    }
  }

  public static void commitTransaction(Connection conn) {
    try {
      if (conn != null && !conn.getAutoCommit()) {
        conn.commit();
        conn.setAutoCommit(true);
        logger.info(() -> "Giao dịch đã được xác nhận (Commit) thành công.");
      }
    } catch (SQLException e) {
      throw new DatabaseException("Lỗi khi xác nhận giao dịch", e);
    }
  }

  public static void rollbackTransaction(Connection conn) {
    if (conn != null) {
      try {
        conn.rollback();
        conn.setAutoCommit(true);
        logger.info(() -> "Đã hoàn tác dữ liệu (Rollback) do có lỗi xảy ra.");
      } catch (SQLException e) {
        logger.log(Level.SEVERE, "Lỗi nghiêm trọng khi rollback!", e);
      }
    }
  }

  public static void close(AutoCloseable resource) {
    if (resource != null) {
      try {
        resource.close();
      } catch (Exception e) {
        logger.log(Level.SEVERE, "Không thể đóng tài nguyên hệ thống", e);
      }
    }
  }
}