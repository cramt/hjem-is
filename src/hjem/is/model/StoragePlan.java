package hjem.is.model;

import java.util.List;

public class StoragePlan {
    private String name;
    private StorageMetaData storageMetaData;
    private List<PeriodicPlan> periodicPlans;
    private boolean active;
    private Integer id = null;

    public StoragePlan(String name, boolean active, StorageMetaData storageMetaData, List<PeriodicPlan> periodicPlans) {
        this.name = name;
        this.storageMetaData = storageMetaData;
        this.periodicPlans = periodicPlans;
        this.active = active;
    }

    public StoragePlan(String name, boolean active, StorageMetaData storageMetaData, List<PeriodicPlan> periodicPlans, Integer id) {
        this(name, active, storageMetaData, periodicPlans);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StorageMetaData getStorageMetaData() {
        return storageMetaData;
    }

    public void setStorageMetaData(StorageMetaData storageMetaData) {
        this.storageMetaData = storageMetaData;
    }

    public List<PeriodicPlan> getPeriodicPlans() {
        return periodicPlans;
    }

    public void setPeriodicPlans(List<PeriodicPlan> periodicPlans) {
        this.periodicPlans = periodicPlans;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
