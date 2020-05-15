package hjem.is.model;

import java.util.List;
import java.util.Map;

public class PeriodicPlan {
    private Map<Product, Integer> productMap;
    private Period period;
    private List<StorageOrder> storageOrders;
    private Integer id = null;

    public PeriodicPlan(Map<Product, Integer> productMap, Period period, List<StorageOrder> storageOrders) {
        this.productMap = productMap;
        this.period = period;
        this.storageOrders = storageOrders;
    }

    public PeriodicPlan(Map<Product, Integer> productMap, Period period, List<StorageOrder> storageOrders, Integer id) {
        this(productMap, period, storageOrders);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Map<Product, Integer> getProductMap() {
        return productMap;
    }

    public void setProductMap(Map<Product, Integer> productMap) {
        this.productMap = productMap;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public List<StorageOrder> getStorageOrders() {
        return storageOrders;
    }

    public void setStorageOrders(List<StorageOrder> storageOrders) {
        this.storageOrders = storageOrders;
    }

}

