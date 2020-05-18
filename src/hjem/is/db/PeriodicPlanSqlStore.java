package hjem.is.db;

import hjem.is.model.PeriodicPlan;
import hjem.is.model.StoragePlan;

import java.util.List;

public class PeriodicPlanSqlStore implements IPeriodicPlanStore {

    @Override
    public PeriodicPlan getById(int id) throws DataAccessException {
        return null;
    }

    @Override
    public List<PeriodicPlan> getByStoragePlan(StoragePlan storagePlan) throws DataAccessException {
        return null;
    }

    @Override
    public void add(PeriodicPlan periodicPlan) throws DataAccessException {

    }
}
