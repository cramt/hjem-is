package hjem.is.test;

import hjem.is.db.DBConnection;
import hjem.is.db.DataAccessException;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseAccessTest {
    DBConnection db = DBConnection.getInstance();

    public DatabaseAccessTest() throws DataAccessException {
    }

    @Test
    public void isConnected() throws SQLException {
        PreparedStatement stmt = db.getConnection().prepareStatement("SELECT * FROM (VALUES ('hello world')) t (a)");
        ResultSet results = stmt.executeQuery();
        results.next();
        assertEquals("hello world", results.getString(1));
    }

    @Test
    public void setupDatabase() throws SQLException {
        PreparedStatement stmt = db.getConnection().prepareStatement("SELECT name FROM SYSOBJECTS WHERE xtype = 'U';");
        ResultSet results = stmt.executeQuery();
        ArrayList<String> got = new ArrayList<>();
        while (results.next()) {
            got.add(results.getString(1));
        }
        assertArrayEquals(new String[]{"storage_meta_data", "storage_plans", "periodic_plans", "sysdiagrams", "suppliers", "products", "storage_orders", "order_product_lines", "plan_lines"}, got.toArray());
    }
}
