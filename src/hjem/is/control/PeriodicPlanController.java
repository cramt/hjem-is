package hjem.is.control;

import hjem.is.model.PeriodicPlan;
import hjem.is.model.Product;
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
}
