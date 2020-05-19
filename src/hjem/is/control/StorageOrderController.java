package hjem.is.control;

import hjem.is.model.ProductLine;
import hjem.is.model.StorageOrder;
import hjem.is.model.Supplier;

import java.time.LocalDateTime;
import java.util.List;

public class StorageOrderController {
    private StorageOrder storageOrder;

    public StorageOrderController(StorageOrder storageOrder) {
        this.storageOrder = storageOrder;
    }

    public Integer getId() {
        return storageOrder.getId();
    }

    public LocalDateTime getSentDate() {
        return storageOrder.getSentDate();
    }

    public void setSentDate(LocalDateTime sentDate) {
        storageOrder.setSentDate(sentDate);
    }

    public String getTrackingId() {
        return storageOrder.getTrackingId();
    }

    public void setTrackingId(String trackingId) {
        storageOrder.setTrackingId(trackingId);
    }

    public Supplier getSupplier() {
        return storageOrder.getSupplier();
    }

    public void setSupplier(Supplier supplier) {
        storageOrder.setSupplier(supplier);
    }

    public List<ProductLine> getOrders() {
        return storageOrder.getOrders();
    }

    public void setOrders(List<ProductLine> orders) {
        storageOrder.setOrders(orders);
    }
}
