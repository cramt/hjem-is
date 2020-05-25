package hjem.is.test;

import hjem.is.db.DataAccessException;
import hjem.is.db.PeriodicPlanSqlStore;
import hjem.is.model.PeriodicPlan;
import hjem.is.model.StorageMetaData;
import hjem.is.model.StoragePlan;
import hjem.is.model.time.ControlledTimeProvider;
import hjem.is.model.time.Period;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PeriodicPlanSqlStoreTest {
    @Test
    public void add() throws DataAccessException {
        PeriodicPlanSqlStore sql = new PeriodicPlanSqlStore();
        StoragePlan splan = new StoragePlan("test", true, new StorageMetaData(2), new ArrayList<>());
        PeriodicPlan pplan = new PeriodicPlan(new HashMap<>(), new Period(1, 3), new ArrayList<>());
        sql.add(pplan, splan);
    }
}