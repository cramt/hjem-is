package hjem.is.model;

public class Product {
    private int cost;
    private String name;
    private Supplier supplier;
    private Integer id = null;

    public Product(int cost, String name, Supplier supplier) {
        this.cost = cost;
        this.name = name;
        this.supplier = supplier;
    }

    public Product(int cost, String name, Supplier supplier, Integer id) {
        this(cost, name, supplier);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
