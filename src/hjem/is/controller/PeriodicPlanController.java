package hjem.is.controller;

import hjem.is.db.*;
import hjem.is.model.PeriodicPlan;
import hjem.is.model.Product;
import hjem.is.model.OrderProductLine;
import hjem.is.model.Supplier;
import hjem.is.model.time.Period;
import hjem.is.model.StorageOrder;
import org.apache.commons.math3.geometry.spherical.twod.Circle;

import javax.print.attribute.standard.ReferenceUriSchemesSupported;
import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PeriodicPlanController {
    private PeriodicPlan current;
    private PeriodicPlan left;
    private PeriodicPlan right;
    private List<PeriodicPlan> toDelete;
    private IPeriodicPlanStore store;
    private List<PeriodicPlan> plans;
    private IProductStore productStore;

    public PeriodicPlanController(StoragePlanController controller, int index) {
        store = new PeriodicPlanSqlStore();
        productStore = new ProductSqlStore();
        if (controller.get().getPeriodicPlans() != null) {
            plans = controller.get().getPeriodicPlans();
        } else {
            try {
                plans = store.getByStoragePlan(controller.get());
            } catch (DataAccessException ignored) {

            }
        }
        current = plans.get(index);
        int leftIndex = index - 1;
        int rightIndex = index + 1;
        if (leftIndex < 0) {
            leftIndex = plans.size() - 1;
        }
        if (rightIndex > plans.size() - 1) {
            rightIndex = 0;
        }
        left = plans.get(leftIndex);
        right = plans.get(rightIndex);
    }

    //creates a new order for each new supplier, stacks those together with the same supplier
    public List<StorageOrder> createOrders() {
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

    private void deleteLeft() {
        int index = plans.indexOf(left);
        index--;
        if (index < 0) {
            index = plans.size() - 1;
        }
        PeriodicPlan newLeft = plans.get(index);
        plans.remove(left);
        toDelete.add(left);
        left = newLeft;
    }

    private void deleteRight() {
        int index = plans.indexOf(right);
        index++;
        if (index < plans.size() - 1) {
            index = 0;
        }
        PeriodicPlan newRight = plans.get(index);
        plans.remove(right);
        toDelete.add(right);
        right = newRight;
    }

    public void setStartPeriod(int startPeriod) {
        current.getPeriod().setStart(startPeriod);
        while (left.getPeriod().getStart() < startPeriod) {
            deleteLeft();
        }
        left.getPeriod().setEnd(startPeriod);
    }

    public void setEndPeriod(int endPeriod) {
        current.getPeriod().setEnd(endPeriod);
        while (right.getPeriod().getEnd() > endPeriod) {
            deleteRight();
        }
        right.getPeriod().setStart(endPeriod);
    }

    private Product getByName(String name) {
        Optional<Product> product = current.getProductMap().keySet().stream().filter(x -> x.getName().equals(name)).findFirst();
        if (product.isEmpty()) {
            return null;
        }
        return product.get();
    }


    public void setProductAmount(String name, int amount) {
        Product product = getByName(name);
        if (product == null) {
            return;
        }

        if (amount == 0) {
            current.getProductMap().remove(product);
        } else {
            current.getProductMap().put(product, amount);
        }
    }

    public boolean addProduct(String name) {
        try {
            Product product = productStore.getByName(name);
            if (product == null) {
                return false;
            }
            current.getProductMap().put(product, 1);
            return true;
        } catch (DataAccessException ignored) {

        }
        return false;
    }

    public void removeProduct(String name) {
        Product product = getByName(name);
        if (product == null) {
            return;
        }
        current.getProductMap().remove(product);
    }
}
