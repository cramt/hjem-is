package hjem.is.db;

import hjem.is.model.Product;

import java.util.List;

public interface IProductStore {
    void add(List<Product> products) throws DataAccessException;
    void add(Product[] products) throws DataAccessException;
    void add(Product product) throws DataAccessException;
}
