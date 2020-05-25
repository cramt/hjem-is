package hjem.is.db;

import hjem.is.model.PeriodicPlan;
import hjem.is.model.StoragePlan;

import java.util.List;

public interface IPeriodicPlanStore {
    PeriodicPlan getById(int id) throws DataAccessException;
    List<PeriodicPlan> getByStoragePlan(StoragePlan storagePlan) throws DataAccessException;
    void add(PeriodicPlan periodicPlan, StoragePlan storagePlan) throws DataAccessException;
    void delete(List<PeriodicPlan> plans) throws DataAccessException;
    void update(PeriodicPlan plan) throws DataAccessException;
    void update(PeriodicPlan plan, boolean products) throws DataAccessException;
}
