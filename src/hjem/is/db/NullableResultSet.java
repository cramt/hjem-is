package hjem.is.db;

import org.apache.commons.math3.analysis.function.Sqrt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class NullableResultSet {
    private ResultSet set;

    public NullableResultSet(ResultSet set) {
        this.set = set;
    }

    public ResultSet getSet() {
        return set;
    }

    public boolean next() throws SQLException {
        return set.next();
    }

    public Float getFloat(String str) throws SQLException {
        Float res = set.getFloat(str);
        if (set.wasNull()) {
            res = null;
        }
        return res;
    }

    public Float getFloat(int i) throws SQLException {
        Float res = set.getFloat(i);
        if (set.wasNull()) {
            res = null;
        }
        return res;
    }

    public Integer getInt(String str) throws SQLException {
        Integer res = set.getInt(str);
        if (set.wasNull()) {
            res = null;
        }
        return res;
    }

    public Integer getInt(int i) throws SQLException {
        Integer res = set.getInt(i);
        if (set.wasNull()) {
            res = null;
        }
        return res;
    }

    public Byte getByte(String str) throws SQLException {
        Byte res = set.getByte(str);
        if (set.wasNull()) {
            res = null;
        }
        return res;
    }

    public Byte getByte(int i) throws SQLException {
        Byte res = set.getByte(i);
        if (set.wasNull()) {
            res = null;
        }
        return res;
    }

    public String getString(int i) throws SQLException{
        return set.getString(i);
    }

    public String getString(String str) throws SQLException{
        return set.getString(str);
    }

    public LocalDateTime getDateTime(int i) throws SQLException {
        java.sql.Timestamp res = set.getTimestamp(i);
        if (res == null) {
            return null;
        }
        return res.toLocalDateTime();
    }

    public LocalDateTime getDateTime(String str) throws SQLException {
        java.sql.Timestamp res = set.getTimestamp(str);
        if (res == null) {
            return null;
        }
        return res.toLocalDateTime();
    }

}
