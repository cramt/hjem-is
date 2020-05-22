package hjem.is.controller;

import hjem.is.db.DataAccessException;
import hjem.is.db.IStoragePlanStore;
import hjem.is.db.StoragePlanSqlStore;
import hjem.is.model.PeriodicPlan;
import hjem.is.model.StoragePlan;
import hjem.is.model.time.Period;

import javax.print.attribute.standard.ReferenceUriSchemesSupported;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class StoragePlanController {
    private StoragePlan current;
    private IStoragePlanStore store;

    public StoragePlanController() {
        store = new StoragePlanSqlStore();
    }

    public StoragePlan generateNew(String name) {
        List<PeriodicPlan> periodicPlans = new ArrayList<>();
        current = new StoragePlan(name, false, new StorageMetaDataController().get(), periodicPlans);
        int i = 0;
        while (i < 356) {
            int s = i;
            i += 7;
            int e = i;
            periodicPlans.add(new PeriodicPlan(new HashMap<>(), new Period(s, e), new ArrayList<>()));
        }
        return current;
    }

    public String getName() {
        return current.getName();
    }

    public List<Period> getPeriods() {
        return current.getPeriodicPlans().stream().map(PeriodicPlan::getPeriod).collect(Collectors.toList());
    }

    public PeriodicPlanController getPeriodicPlan(int index) {
        return new PeriodicPlanController(current.getPeriodicPlans().get(index));
    }

    public List<String> getNames() {
        try {
            List<StoragePlan> all = store.getAll();
            if (all.size() == 0) {
                return new ArrayList<>();
            }
            List<StoragePlan> active = all.stream().filter(StoragePlan::isActive).collect(Collectors.toList());
            if (active.size() == 0) {
                all.get(0).setActive(true);
                new Thread(() -> {
                    try {
                        store.update(all.get(0));
                    } catch (DataAccessException ignored) {

                    }
                }).start();
            } else {
                all.remove(active.get(0));
                all.set(0, active.get(0));
            }
            return all.stream().map(StoragePlan::getName).collect(Collectors.toList());
        } catch (DataAccessException e) {
            return null;
        }
    }

}
