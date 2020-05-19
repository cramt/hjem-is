package hjem.is.control;

import hjem.is.model.Product;
import hjem.is.model.Supplier;

public class ProductController {
    private Product product;

    public ProductController(Product product) {
        this.product = product;
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
        product.setName();
    }

    public Supplier getSupplier() {
        return product.getSupplier();
    }

    public void setSupplier(Supplier supplier) {
        product.setSupplier(supplier);
    }
}
