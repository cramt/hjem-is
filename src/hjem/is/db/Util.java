package hjem.is.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Util {
    public static Float getFloat(ResultSet set, String str) throws SQLException {
        Float res = set.getFloat(str);
        if (set.wasNull()) {
            res = null;
        }
        return res;
    }

    public static Float getFloat(ResultSet set, int i) throws SQLException {
        Float res = set.getFloat(i);
        if (set.wasNull()) {
            res = null;
        }
        return res;
    }

    public static Integer getInt(ResultSet set, String str) throws SQLException {
        Integer res = set.getInt(str);
        if (set.wasNull()) {
            res = null;
        }
        return res;
    }

    public static Integer getInt(ResultSet set, int i) throws SQLException {
        Integer res = set.getInt(i);
        if (set.wasNull()) {
            res = null;
        }
        return res;
    }

    public static Byte getByte(ResultSet set, String str) throws SQLException {
        Byte res = set.getByte(str);
        if (set.wasNull()) {
            res = null;
        }
        return res;
    }

    public static Byte getByte(ResultSet set, int i) throws SQLException {
        Byte res = set.getByte(i);
        if (set.wasNull()) {
            res = null;
        }
        return res;
    }

    public static LocalDateTime getDateTime(ResultSet set, int i) throws SQLException {
        java.sql.Timestamp res = set.getTimestamp(i);
        if (res == null) {
            return null;
        }
        return res.toLocalDateTime();
    }

    public static LocalDateTime getDateTime(ResultSet set, String str) throws SQLException {
        java.sql.Timestamp res = set.getTimestamp(str);
        if (res == null) {
            return null;
        }
        return res.toLocalDateTime();
    }
}
