package hjem.is.db;

import hjem.is.model.Product;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProductSqlStore implements IProductStore {
    @Override
    public void add(List<Product> products) throws DataAccessException {
        try {
            String questionMarks = Arrays.stream(new String[products.size()]).map(x -> "(?,?)").collect(Collectors.joining(","));
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO products (cost, name) VALUES " + questionMarks, new String[]{"id"});
            int i = 1;
            for (Product product : products) {
                stmt.setInt(i++, product.getCost());
                stmt.setString(i++, product.getName());

            }
            stmt.execute();
            NullableResultSet result = new NullableResultSet(stmt.getGeneratedKeys());
            i = 0;
            while (result.next()) {
                products.get(i++).setId(result.getInt("id"));
            }
            stmt = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO products_supplier (product_id, supplier_id) VALUES " + questionMarks);
            i = 1;
            for (Product product : products) {
                stmt.setInt(i++, product.getId());
                stmt.setInt(i++, product.getSupplier().getId());
            }
            stmt.execute();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public void add(Product[] products) throws DataAccessException {
        add(Arrays.asList(products));
    }

    @Override
    public void add(Product product) throws DataAccessException {
        add(new Product[]{product});
    }
}
