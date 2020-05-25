package hjem.is.db;

import hjem.is.model.Product;
import hjem.is.model.OrderProductLine;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductLineSqlStore implements IProductLineStore {

    @Override
    public List<OrderProductLine> getCurrentlyStored() throws DataAccessException {
        try {
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("SELECT products.id as p_id, amount, product_id, storage_order_id, order_product_lines.id as pl_id, cost, name FROM order_product_lines INNER JOIN products ON order_product_lines.product_id = products.id WHERE storage_order_id IS NULL");
            NullableResultSet result = new NullableResultSet(stmt.executeQuery());
            List<OrderProductLine> lines = new ArrayList<>();
            while (result.next()) {
                lines.add(new OrderProductLine(new Product(result.getInt("cost"), result.getString("name"), null, result.getInt("p_id")), result.getInt("amount"), result.getInt("pl_id")));
            }
            return lines;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
