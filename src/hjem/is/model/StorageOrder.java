package hjem.is.model;

import java.util.Date;

public class StorageOrder {
    private Date sentDate;
    private String trackingId;
    private Supplier supplier;
    private ProductLine orders;
    private Integer id = null;

    public StorageOrder(Date sentDate, String trackingId, Supplier supplier, ProductLine orders) {
        this.sentDate = sentDate;
        this.trackingId = trackingId;
        this.supplier = supplier;
        this.orders = orders;
    }

    public StorageOrder(Date sentDate, String trackingId, Supplier supplier, ProductLine orders, Integer id) {
        this(sentDate, trackingId, supplier, orders);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public ProductLine getOrders() {
        return orders;
    }

    public void setOrders(ProductLine orders) {
        this.orders = orders;
    }
}
