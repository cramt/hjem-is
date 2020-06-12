package hjem.is.db;

import hjem.is.model.PeriodicPlan;
import hjem.is.model.Product;
import hjem.is.model.Supplier;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class ProductSqlStore implements IProductStore {
    @Override
    public void add(List<Product> products) throws DataAccessException {
        try {
            String questionMarks = Arrays.stream(new String[products.size()]).map(x -> "(?,?,?)").collect(Collectors.joining(","));
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO products (cost, name, supplier_id) VALUES " + questionMarks, PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 1;
            for (Product product : products) {
                stmt.setInt(i++, product.getCost());
                stmt.setString(i++, product.getName());
                stmt.setInt(i++, product.getSupplier().getId());
            }
            stmt.execute();
            NullableResultSet result = new NullableResultSet(stmt.getGeneratedKeys());
            i = 0;
            while (result.next()) {
                products.get(i++).setId(result.getInt("GENERATED_KEYS"));
            }
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

    @Override
    public Product getByName(String name) throws DataAccessException {
        try {
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("SELECT cost, supplier_id, suppliers.name as s_name, suppliers.delivery_price, suppliers.delivery_speed FROM products INNER JOIN suppliers ON products.supplier_id = suppliers.id WHERE products.name = ?");
            stmt.setString(1, name);
            NullableResultSet result = new NullableResultSet(stmt.executeQuery());
            if (result.next()) {
                return new Product(result.getInt("cost"), name, new Supplier(result.getInt("delivery_speed"), result.getInt("delivery_price"), result.getString("s_name"), result.getInt("supplier_id")), result.getInt("product_id"));
            }
            return null;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<Product> getAll() throws DataAccessException {
        try {
            NullableResultSet result = new NullableResultSet(DBConnection.getInstance().getConnection().prepareStatement("SELECT id, cost, name FROM products").executeQuery());
            List<Product> products = new ArrayList<>();
            while (result.next()) {
                products.add(new Product(result.getInt("cost"), result.getString("name"), null, result.getInt("id")));
            }
            return products;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<Product> getProductsByPeriodicPlan(PeriodicPlan plan) throws DataAccessException {
        try {
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("SELECT amount, name, product_id, cost, supplier_id FROM plan_lines INNER JOIN products ON plan_lines.product_id = products.id WHERE periodic_plan_id = ?");
            stmt.setInt(1, plan.getId());
            NullableResultSet result = new NullableResultSet(stmt.executeQuery());
            Map<Product, Integer> products = new HashMap<>();
            while (result.next()){
                products.put(new Product(result.getInt("cost"), result.getString("name"), null, result.getInt("product_id")), result.getInt("amount"));
            }
            plan.setProductMap(products);
            return new ArrayList<>(products.keySet());

        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }


}
