package hjem.is.controller;

import hjem.is.db.DataAccessException;
import hjem.is.db.IStoragePlanStore;
import hjem.is.db.StoragePlanSqlStore;
import hjem.is.model.PeriodicPlan;
import hjem.is.model.StoragePlan;
import hjem.is.model.time.Period;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class StoragePlanController {
    private StoragePlan current;
    private IStoragePlanStore store;
    private List<Consumer<StoragePlan>> onSaveListeners = new ArrayList<>();
    //private Consumer<StoragePlan> plans;

    public StoragePlanController() {
        store = new StoragePlanSqlStore();
    }

    public StoragePlan get() {
        return current;
    }

    public StoragePlan generateNew(String name) {
        final int MAX_DAYS_IN_YEAR = 366;
        final int DAYS_IN_WEEK = 7;
        List<PeriodicPlan> periodicPlans = new ArrayList<>();
        current = new StoragePlan(name, false, new StorageMetaDataController().get(), periodicPlans);
        int i = 0;
        while (i < MAX_DAYS_IN_YEAR) {
            int s = i;
            i += DAYS_IN_WEEK;
            int e = i;
            periodicPlans.add(new PeriodicPlan(new HashMap<>(), new Period(s, e), new ArrayList<>()));
        }
        return current;
    }

    public String getName() {
        return current.getName();
    }

    public void setName(String name) {
        current.setName(name);
    }

    public List<Period> getPeriods() {
    	List<Period> periods = new ArrayList<>();;
    	for (int i = 0; i < current.getPeriodicPlans().size(); i++) {
    		periods.add(current.getPeriodicPlans().get(i).getPeriod());
    	}
        return periods;
        //current.getPeriodicPlans().stream().map(PeriodicPlan::getPeriod).collect(Collectors.toList());
    }

    public PeriodicPlanController getPeriodicPlanController(int index) {
        return new PeriodicPlanController(this, index);
    }

    public void select(String name) {
        try {
            current = store.getByName(name);
        } catch (DataAccessException ignored) {
        	System.out.println(ignored);
        }
    }

    public boolean isActive() {
        return current.isActive();
    }

    public void setActive(boolean active) {
        current.setActive(active);
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

    public void save() {
        for (Consumer<StoragePlan> onSaveListener : onSaveListeners) {
            onSaveListener.accept(current);
        }
        try {
            if (current.getId() == null) {
                store.add(current);
            } else {
                store.update(current);
            }
        } catch (DataAccessException ignored) {

        }
    }

    public void addOnSaveListener(Consumer<StoragePlan> consumer) {
        onSaveListeners.add(consumer);
    }

    public void removeOnSaveListener(Consumer<StoragePlan> consumer) {
        onSaveListeners.remove(consumer);
    }

    public void onceOnSaveListener(Consumer<StoragePlan> consumer) {
        addOnSaveListener(new Consumer<StoragePlan>() {
            @Override
            public void accept(StoragePlan o) {
                consumer.accept(o);
                removeOnSaveListener(this);
            }
        });
    }

    public void delete() {
        if (current.getId() != null) {
            try {
                store.delete(current);
            } catch (DataAccessException ignored) {

            }
        }
        current = null;
    }
}
