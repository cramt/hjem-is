package hjem.is.control;

import hjem.is.model.PeriodicPlan;
import hjem.is.model.Product;
import hjem.is.model.StorageOrder;
import hjem.is.model.time.Period;

import java.util.List;
import java.util.Map;

public class PeriodicPlanController {
    private PeriodicPlan periodicPlan;

    public PeriodicPlanController(PeriodicPlan periodicPlan) {
        this.periodicPlan = periodicPlan;
    }

    public Integer getID() {
        return periodicPlan.getId();
    }

    public Map<Product, Integer> getProductMap() {
        return periodicPlan.getProductMap();
    }

    void setProductMap(Map<Product, Integer> productMap) {
        periodicPlan.setProductMap(productMap);
    }

    public Period getPeriod() {
        return periodicPlan.getPeriod();
    }

    public void setPeriod(Period period) {
        periodicPlan.setPeriod(period);
    }

    public List<StorageOrder> getStorageOrders() {
        return periodicPlan.getStorageOrders();
    }

    public void setStorageOrders(List<StorageOrder> storageOrders) {
        periodicPlan.setStorageOrders(storageOrders);
    }
}
