package hjem.is.db;

import hjem.is.model.PeriodicPlan;
import hjem.is.model.StoragePlan;

import java.util.List;

public interface IPeriodicPlanStore {
    PeriodicPlan getById(int id) throws DataAccessException;
    List<PeriodicPlan> getByStoragePlan(StoragePlan storagePlan) throws DataAccessException;
    void add(PeriodicPlan periodicPlan) throws DataAccessException;
}
