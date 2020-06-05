package hjem.is.controller;

import hjem.is.db.*;
import hjem.is.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PeriodicPlanController {
    private PeriodicPlan current;
    private PeriodicPlan left;
    private PeriodicPlan right;
    private List<PeriodicPlan> toDelete = new ArrayList<>();
    private IPeriodicPlanStore store;
    private List<PeriodicPlan> plans;
    private IProductStore productStore;
    private List<Product> products;
    private StoragePlanController spc;

    public PeriodicPlanController() {
        store = new PeriodicPlanSqlStore();
        productStore = new ProductSqlStore();
        spc = new StoragePlanController();
    }

    public PeriodicPlanController(StoragePlanController controller, int index) {
        store = new PeriodicPlanSqlStore();
        productStore = new ProductSqlStore();
        spc = controller;
        init(index);
    }

    public void init(int index) {
        Thread plansThread = new Thread(() -> {
            try {
                plans = store.getByStoragePlan(spc.get());
            } catch (DataAccessException ignored) {

            }
        });
        Thread productThread = new Thread(() -> {
            try {
                products = productStore.getAll();
            } catch (DataAccessException ignored) {

            }
        });
        plansThread.start();
        productThread.start();
        try {
            plansThread.join();
            productThread.join();
        } catch (InterruptedException ignored) {

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
        //This doesn't work
        /*
        while (left.getPeriod().getStart() < startPeriod) {
            deleteLeft();
        }
        left.getPeriod().setEnd(startPeriod);
        */
    }

    public void setEndPeriod(int endPeriod) {
        current.getPeriod().setEnd(endPeriod);
        //This doesn't work
        /*
        while (right.getPeriod().getEnd() > endPeriod) {
            deleteRight();
        }
        right.getPeriod().setStart(endPeriod);
        */
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
        Optional<Product> product = products.stream().filter(x -> x.getName().equals(name)).findFirst();
        if (product.isEmpty()) {
            return false;
        }
        current.getProductMap().put(product.get(), 1);
        return true;
    }

    public void removeProduct(String name) {
        Product product = getByName(name);
        if (product == null) {
            return;
        }
        current.getProductMap().remove(product);
    }

    public void save() {
        Thread deleteThread = new Thread(() -> {
            try {
                store.delete(toDelete);
            } catch (DataAccessException ignored) {

            }
        });
        Thread update1Thread = new Thread(() -> {
            try {
                store.update(left, false);
            } catch (DataAccessException ignored) {

            }
        });
        Thread update2Thread = new Thread(() -> {
            try {
                store.update(current);
            } catch (DataAccessException ignored) {

            }
        });
        Thread update3Thread = new Thread(() -> {
            try {
                store.update(right, false);
            } catch (DataAccessException ignored) {

            }
        });
        deleteThread.start();
        update1Thread.start();
        update2Thread.start();
        update3Thread.start();
        try {
            deleteThread.join();
            update1Thread.join();
            update2Thread.join();
            update3Thread.join();
        } catch (InterruptedException ignored) {

        }
    }

    public int getStartPeriod() {
        return current.getPeriod().getStart();
    }

    public int getEndPeriod() {
        return current.getPeriod().getEnd();
    }

    public List<String> getUsedNames() {
        return current.getProductMap().keySet().stream().map(Product::getName).collect(Collectors.toList());
    }

    public Integer getAmountByName(String name) {
        return current.getProductMap().get(getByName(name));
    }

    public List<String> getUnusedNames() {
        return products.stream().filter(x -> !current.getProductMap().containsKey(x)).map(Product::getName).collect(Collectors.toList());
    }
    
    public List<Product> getProducts() {
    	return products;
    }
}
