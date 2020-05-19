package hjem.is.controller;

import hjem.is.model.PeriodicPlan;
import hjem.is.model.Product;
import hjem.is.model.ProductLine;
import hjem.is.model.Supplier;
import hjem.is.model.time.Period;
import hjem.is.model.StorageOrder;
import hjem.is.model.time.Period;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PeriodicPlanController {
    private PeriodicPlan periodicPlan;

    public PeriodicPlanController(PeriodicPlan periodicPlan) {
        this.periodicPlan = periodicPlan;
    }

    //creates a new order for each new supplier, stacks those together with the same supplier
    public List<StorageOrder> createOrders() {
        Supplier supplier = null;
        Supplier previous = null;
        Map<Product, Integer> map;
        ArrayList<ProductLine> productLines = new ArrayList<>();
        ArrayList<StorageOrder> orders = new ArrayList<>();

        //for each product and amount, add that product and amount to ProductLine List on StorageOrder
        for (Map.Entry<Product, Integer> entry : getProductMap().entrySet()) {
            supplier = entry.getKey().getSupplier();
            //if not new supplier, add productLine
            if (previous == null || previous.equals(supplier)) {
                productLines.add(new ProductLine(entry.getKey(), entry.getValue()));
                previous = supplier;
            } else {
                //if new supplier, make storageOrder and run again
                orders.add(new StorageOrder(null, null, previous, productLines));
                productLines.clear();
                productLines.add(new ProductLine(entry.getKey(), entry.getValue()));
                previous = supplier;
            }
        }
        orders.add(new StorageOrder(null, null, supplier, productLines));

        return orders;
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
