package hjem.is.db;

import hjem.is.model.PeriodicPlan;
import hjem.is.model.Product;

import java.util.List;

public interface IProductStore {
    void add(List<Product> products) throws DataAccessException;
    void add(Product[] products) throws DataAccessException;
    void add(Product product) throws DataAccessException;
    Product getByName(String name) throws DataAccessException;
    List<Product> getAll() throws DataAccessException;
    List<Product> getProductsByPeriodicPlan(PeriodicPlan plan) throws DataAccessException;
}
