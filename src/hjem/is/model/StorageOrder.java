package hjem.is.model;

import java.time.LocalDateTime;
import java.util.Date;

public class StorageOrder {
    private LocalDateTime sentDate;
    private String trackingId;
    private Supplier supplier;
    private ProductLine orders;
    private Integer id = null;

    public StorageOrder(LocalDateTime sentDate, String trackingId, Supplier supplier, ProductLine orders) {
        this.sentDate = sentDate;
        this.trackingId = trackingId;
        this.supplier = supplier;
        this.orders = orders;
    }

    public StorageOrder(LocalDateTime sentDate, String trackingId, Supplier supplier, ProductLine orders, Integer id) {
        this(sentDate, trackingId, supplier, orders);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDateTime sentDate) {
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
