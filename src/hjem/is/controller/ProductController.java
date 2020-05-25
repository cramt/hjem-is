package hjem.is.controller;

import hjem.is.db.DataAccessException;
import hjem.is.model.Product;
import hjem.is.model.Supplier;
import hjem.is.db.ProductSqlStore;

import java.util.List;
//Should this class "create" a ProductSqlStore object? See DAO pattern example
public class ProductController {
    private Product product;
    ProductSqlStore productSqlStore;

    public void add(List<Product> products) throws DataAccessException {
        productSqlStore.add(products);
    }

    void add(Product[] products) throws DataAccessException {
        productSqlStore.add(products);
    }
    void add(Product product) throws DataAccessException {
        productSqlStore.add(product);
    }

    public Integer getId() {
        return product.getId();
    }

    public int getCost() {
        return product.getCost();
    }

    public void setCost(int cost) {
        product.setCost(cost);
    }

    public String getName() {
        return product.getName();
    }

    public void setName(String name) {
        product.setName(name);
    }

    public Supplier getSupplier() {
        return product.getSupplier();
    }

    public void setSupplier(Supplier supplier) {
        product.setSupplier(supplier);
    }
}
