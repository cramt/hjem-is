package hjem.is.controller;
import hjem.is.model.PeriodicPlan;
import hjem.is.model.StorageMetaData;
import hjem.is.model.StorageOrder;
import hjem.is.model.StoragePlan;

import java.util.List;

public class StoragePlanController {
    private StoragePlan storagePlan;

    public StoragePlanController(StoragePlan storagePlan) {
        this.storagePlan = storagePlan;
    }

    public StoragePlan generateNew(String name) {
        //Make the method that generates the Plan
        return storagePlan;
    }
    public void createStorageOrder(PeriodicPlan periodicPlan) {

    }

    public void setSPName(String name) {
        storagePlan.setName(name);
    }

    public String getSPName() {
        return storagePlan.getName();
    }

    public void setStorageMetaData(StorageMetaData storageMetaData) {
        storagePlan.setStorageMetaData(storageMetaData);
    }

    public StorageMetaData getStorageMetaData() {
        return storagePlan.getStorageMetaData();
    }

    public void setPeriodicPlans(List<PeriodicPlan> periodicPlans) {
        storagePlan.setPeriodicPlans(periodicPlans);
    }

    public Integer getID() {
        return storagePlan.getId();
    }

    public List<PeriodicPlan> getPeriodicPlans() {
        return storagePlan.getPeriodicPlans();
    }
}
