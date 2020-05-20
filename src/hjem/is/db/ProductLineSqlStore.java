package hjem.is.db;

import hjem.is.model.Product;
import hjem.is.model.ProductLine;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductLineSqlStore implements IProductLineStore {

    @Override
    public List<ProductLine> getCurrentlyStored() throws DataAccessException {
        try {
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("SELECT products.id as p_id, amount, product_id, storage_order_id, product_lines.id as pl_id, cost, name FROM product_lines INNER JOIN products ON product_lines.product_id = products.id WHERE storage_order_id IS NULL");
            NullableResultSet result = new NullableResultSet(stmt.executeQuery());
            List<ProductLine> lines = new ArrayList<>();
            while (result.next()) {
                lines.add(new ProductLine(new Product(result.getInt("cost"), result.getString("name"), null, result.getInt("p_id")), result.getInt("amount"), result.getInt("pl_id")));
            }
            return lines;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
