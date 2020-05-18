package hjem.is.db;

import hjem.is.model.Period;
import hjem.is.model.PeriodicPlan;
import hjem.is.model.Product;
import hjem.is.model.StoragePlan;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class PeriodicPlanSqlStore implements IPeriodicPlanStore {

    @Override
    public PeriodicPlan getById(int id) throws DataAccessException {
        try {
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("SELECT start_period, end_period FROM periodic_plans WHERE id = ?");
            stmt.setInt(1, id);
            NullableResultSet result = new NullableResultSet(stmt.executeQuery());
            if (result.next()) {
                return new PeriodicPlan(null, new Period(result.getInt("start_period"), result.getInt("end_period")), null, id);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<PeriodicPlan> getByStoragePlan(StoragePlan storagePlan) throws DataAccessException {
        try {
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("SELECT start_period, end_period, id FROM periodic_plans WHERE storage_plan_id = ?");
            stmt.setInt(1, storagePlan.getId());
            NullableResultSet result = new NullableResultSet(stmt.executeQuery());
            List<PeriodicPlan> plans = new ArrayList<>();
            while (result.next()) {
                plans.add(new PeriodicPlan(null, new Period(result.getInt("start_period"), result.getInt("end_period")), null, result.getInt("id")));
            }
            storagePlan.setPeriodicPlans(plans);
            return plans;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public void add(PeriodicPlan periodicPlan, StoragePlan storagePlan) throws DataAccessException {
        try {
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO periodic_plans (start_period, end_period, storage_plan_id) VALUES (?, ?, ?)");
            stmt.setInt(1, periodicPlan.getPeriod().getStart());
            stmt.setInt(2, periodicPlan.getPeriod().getEnd());
            stmt.setInt(3, storagePlan.getId());
            stmt.execute();
            if (!storagePlan.getPeriodicPlans().contains(periodicPlan)) {
                storagePlan.getPeriodicPlans().add(periodicPlan);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
