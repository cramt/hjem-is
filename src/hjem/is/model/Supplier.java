package hjem.is.model;

public class Supplier {
    private int deliverySpeed;
    private int deliveryPrice;
    private String name;
    private Integer id = null;

    public Supplier(int deliverySpeed, int deliveryPrice, String name) {
        this.deliverySpeed = deliverySpeed;
        this.deliveryPrice = deliveryPrice;
        this.name = name;
    }

    public Supplier(int deliverySpeed, int deliveryPrice, String name, int id) {
        this(deliverySpeed, deliveryPrice, name);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public int getDeliverySpeed() {
        return deliverySpeed;
    }

    public void setDeliverySpeed(int deliverySpeed) {
        this.deliverySpeed = deliverySpeed;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(int deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
