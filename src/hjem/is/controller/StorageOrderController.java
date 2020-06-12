package hjem.is.controller;

import hjem.is.model.OrderProductLine;
import hjem.is.model.Product;
import hjem.is.model.PeriodicPlan;
import hjem.is.model.StorageOrder;
import hjem.is.model.Supplier;
import hjem.is.db.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StorageOrderController {
    private StorageOrder storageOrder;
    private IStorageOrderStore sOrderStore;

    public StorageOrderController() {
    	sOrderStore = new StorageOrderSqlStore();
    }
    public StorageOrderController(StorageOrder storageOrder) {
        this.storageOrder = storageOrder;
        sOrderStore = new StorageOrderSqlStore();
    }

    public List<StorageOrder> createOrders(PeriodicPlan current) {
        Supplier supplier = null;
        Supplier previous = null;
        Map<Product, Integer> map;
        ArrayList<OrderProductLine> orderProductLines = new ArrayList<>();
        ArrayList<StorageOrder> orders = new ArrayList<>();

        //for each product and amount, add that product and amount to ProductLine List on StorageOrder
        for (Map.Entry<Product, Integer> entry : current.getProductMap().entrySet()) {
            supplier = entry.getKey().getSupplier();
            //if not new supplier, add productLine
            if (previous == null || previous.equals(supplier)) {
                orderProductLines.add(new OrderProductLine(entry.getKey(), entry.getValue()));
                previous = supplier;
            } else {
                //if new supplier, make storageOrder and run again
                orders.add(new StorageOrder(null, null, previous, orderProductLines));
                orderProductLines.clear();
                orderProductLines.add(new OrderProductLine(entry.getKey(), entry.getValue()));
                previous = supplier;
            }
        }
        orders.add(new StorageOrder(null, null, supplier, orderProductLines));

        return orders;
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

    public StorageOrder getOrder() {
    	return storageOrder;
    }
    
    public void setOrder(StorageOrder order) {
    	this.storageOrder = order;
    }
    
    public List<OrderProductLine> getOrders() {
        return storageOrder.getOrders();
    }

    public void setOrders(List<OrderProductLine> orders) {
        storageOrder.setOrders(orders);
    }
}
