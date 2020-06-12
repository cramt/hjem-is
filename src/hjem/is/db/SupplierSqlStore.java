package hjem.is.db;

import hjem.is.model.Supplier;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SupplierSqlStore implements ISupplierStore {

    @Override
    public void add(Supplier supplier) throws DataAccessException {
        try {
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO suppliers (delivery_speed, delivery_price, name) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, supplier.getDeliverySpeed());
            stmt.setInt(2, supplier.getDeliveryPrice());
            stmt.setString(3, supplier.getName());
            stmt.execute();
            NullableResultSet result = new NullableResultSet(stmt.getGeneratedKeys());
            result.next();
            supplier.setId(result.getGeneratedKey());
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
