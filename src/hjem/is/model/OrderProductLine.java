package hjem.is.model;

public class OrderProductLine {
    private Product product;
    private int amount;
    private Integer id = null;

    public OrderProductLine(Product product, int amount) {
        this.product = product;
        this.amount = amount;
    }

    public OrderProductLine(Product product, int amount, int id) {
        this(product, amount);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
