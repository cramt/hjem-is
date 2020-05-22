package hjem.is.controller;

import hjem.is.db.DataAccessException;
import hjem.is.db.IPeriodicPlanStore;
import hjem.is.db.PeriodicPlanSqlStore;
import hjem.is.model.PeriodicPlan;
import hjem.is.model.Product;
import hjem.is.model.OrderProductLine;
import hjem.is.model.Supplier;
import hjem.is.model.time.Period;
import hjem.is.model.StorageOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PeriodicPlanController {
    private PeriodicPlan current;
    private IPeriodicPlanStore store;
    private List<PeriodicPlan> plans;

    public PeriodicPlanController(StoragePlanController controller, int index) {
        store = new PeriodicPlanSqlStore();
        try {
            plans = store.getByStoragePlan(controller.get());
        } catch (DataAccessException ignored) {

        }
        current = plans.get(index);
    }

    //creates a new order for each new supplier, stacks those together with the same supplier
    public List<StorageOrder> createOrders() {
        Supplier supplier = null;
        Supplier previous = null;
        Map<Product, Integer> map;
        ArrayList<OrderProductLine> orderProductLines = new ArrayList<>();
        ArrayList<StorageOrder> orders = new ArrayList<>();

        //for each product and amount, add that product and amount to ProductLine List on StorageOrder
        for (Map.Entry<Product, Integer> entry : getProductMap().entrySet()) {
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

    public Integer getID() {
        return current.getId();
    }

    public Map<Product, Integer> getProductMap() {
        return current.getProductMap();
    }

    void setProductMap(Map<Product, Integer> productMap) {
        current.setProductMap(productMap);
    }

    public Period getPeriod() {
        return current.getPeriod();
    }

    public void setPeriod(Period period) {
        current.setPeriod(period);
    }

    public List<StorageOrder> getStorageOrders() {
        return current.getStorageOrders();
    }

    public void setStorageOrders(List<StorageOrder> storageOrders) {
        current.setStorageOrders(storageOrders);
    }
}
