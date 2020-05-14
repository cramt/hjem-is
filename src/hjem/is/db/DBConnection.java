package hjem.is.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DBConnection {
    private Connection connection = null;
    private static DBConnection dbConnection;

    private static final String driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String dbName = "dmab0919_1080622";
    private static final String serverAddress = "hildur.ucn.dk";
    private static final int serverPort = 1433;
    private static final String userName = "dmab0919_1080622";
    private static final String password = "Password1!";

    private DBConnection() throws DataAccessException {
        String connectionString = String.format("jdbc:sqlserver://%s:%d;databaseName=%s;user=%s;password=%s",
                serverAddress, serverPort, dbName, userName, password);
        try {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(connectionString);
        } catch (ClassNotFoundException e) {
            throw new DataAccessException("Missing JDBC driver", e);
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Could not connect to database %s@%s:%d user %s", dbName,
                    serverAddress, serverPort, userName), e);
        }
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT name FROM SYSOBJECTS WHERE xtype = 'U';");
            ResultSet results = stmt.executeQuery();
            ArrayList<String> expected = new ArrayList<>();
            expected.add("storage_plans");
            expected.add("storage_meta_data");
            expected.add("seasonal_plans");
            expected.add("suppliers");
            expected.add("storage_orders");
            expected.add("products");
            expected.add("product_lines");
            expected.add("products_supplier");
            expected.add("seasonal_plans_products_map");
            while (results.next()) {
                expected.remove(results.getString(1));
            }
            if (expected.size() != 0) {
                Scanner scanner = new Scanner(new File("res/script.sql"));
                StringBuilder data = new StringBuilder();
                while (scanner.hasNext()) {
                    data.append(scanner.nextLine());
                }
                scanner.close();
                connection.prepareStatement(data.toString()).execute();
            }
        } catch (SQLException | FileNotFoundException ignored) {

        }
    }

    public static synchronized DBConnection getInstance() throws DataAccessException {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }

    public void startTransaction() throws DataAccessException {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            // e.printStackTrace();
            throw new DataAccessException("Could not start transaction.", e);
        }
    }

    public void commitTransaction() throws DataAccessException {
        try {
            try {
                connection.commit();
            } catch (SQLException e) {
                throw e;
                // e.printStackTrace();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not commit transaction", e);
        }
    }

    public void rollbackTransaction() throws DataAccessException {
        try {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw e;
                // e.printStackTrace();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not rollback transaction", e);
        }
    }

    public int executeInsertWithIdentity(String sql) throws DataAccessException {
        System.out.println("DBConnection, Inserting: " + sql);
        int res = -1;
        try (Statement s = connection.createStatement()) {
            res = s.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            if (res > 0) {
                ResultSet rs = s.getGeneratedKeys();
                rs.next();
                res = rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Could not execute insert (" + sql + ").", e);
        }
        return res;
    }

    public int executeInsertWithIdentity(PreparedStatement ps) throws DataAccessException {
        int res = -1;
        try {
            res = ps.executeUpdate();
            if (res > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                res = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not execute insert", e);
        }
        return res;
    }

    public Connection getConnection() {
        return connection;
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
