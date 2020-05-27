package hjem.is.test;

import hjem.is.db.DataAccessException;
import hjem.is.db.PeriodicPlanSqlStore;
import hjem.is.db.StoragePlanSqlStore;
import hjem.is.model.PeriodicPlan;
import hjem.is.model.StorageMetaData;
import hjem.is.model.StoragePlan;
import hjem.is.model.time.Period;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

class PeriodicPlanSqlStoreTest {
    @Test
    public void add() throws DataAccessException {
        PeriodicPlanSqlStore periodicSql = new PeriodicPlanSqlStore();
        StoragePlanSqlStore storageSql = new StoragePlanSqlStore();
        StoragePlan storagePlan = new StoragePlan("test", true, new StorageMetaData(2), new ArrayList<>());
        PeriodicPlan periodicPlan = new PeriodicPlan(new HashMap<>(), new Period(1, 3), new ArrayList<>());
        storageSql.add(storagePlan);
        periodicSql.add(periodicPlan, storagePlan);
        storageSql.delete(storagePlan);
    }
}
