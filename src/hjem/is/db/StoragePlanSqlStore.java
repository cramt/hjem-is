package hjem.is.db;

import hjem.is.model.PeriodicPlan;
import hjem.is.model.StorageMetaData;
import hjem.is.model.StoragePlan;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

public class StoragePlanSqlStore implements IStoragePlanStore {

    @Override
    public StoragePlan getById(int id) throws DataAccessException {
        return null;
    }

    @Override
    public StoragePlan getByName(String name) throws DataAccessException {
        return null;
    }

    @Override
    public void add(StoragePlan storagePlan) throws DataAccessException {
        try {
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO storage_plans (name, active) VALUES (?, ?)", new String[]{"id"});
            stmt.setString(1, storagePlan.getName());
            stmt.setInt(2, storagePlan.isActive() ? 1 : 0);
            stmt.execute();
            NullableResultSet resultSet = new NullableResultSet(stmt.getGeneratedKeys());
            int id = resultSet.getInt("id");
            FutureTask<Boolean> metaDataTask = new FutureTask<>(() -> {
                if (storagePlan.getStorageMetaData().getId() == null) {
                    try {
                        PreparedStatement sstmt = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO storage_meta_data (percent_inventory_cost, storage_plan_id) VALUES (?, ?)");
                        stmt.setFloat(1, storagePlan.getStorageMetaData().getPercentInventoryCost());
                        sstmt.setInt(2, id);
                        return sstmt.execute();
                    } catch (SQLException e) {
                        throw new DataAccessException(e.getMessage(), e);
                    }
                }
                return true;
            });
            FutureTask<Boolean> periodicPlanTask = new FutureTask<>(() -> {
                try {
                    PreparedStatement pstmt = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO periodic_plans (start_period, end_period, storage_plan_id) VALUES " + Arrays.stream(new String[storagePlan.getPeriodicPlans().size()]).map(x -> "(?, ?, ?)").collect(Collectors.joining(",")));
                    int i = 1;
                    for (PeriodicPlan periodicPlan : storagePlan.getPeriodicPlans()) {
                        pstmt.setInt(i++, periodicPlan.getPeriod().getStart());
                        pstmt.setInt(i++, periodicPlan.getPeriod().getEnd());
                        pstmt.setInt(i++, id);
                    }
                    return pstmt.execute();
                } catch (SQLException e) {
                    throw new DataAccessException(e.getMessage(), e);
                }
            });
            metaDataTask.get();
            periodicPlanTask.get();
        } catch (SQLException | InterruptedException | ExecutionException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<StoragePlan> getALl() throws DataAccessException {
        return null;
    }

    @Override
    public StoragePlan getActive() throws DataAccessException {
        try {
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("SELECT storage_plans.id as plan_id, name, active, storage_meta_data.id as meta_data_id, percent_inventory_cost FROM storage_plans INNER JOIN storage_meta_data ON storage_plans.id = storage_meta_data.storage_plan_id WHERE active = 1");
            NullableResultSet result = new NullableResultSet(stmt.executeQuery());
            if (result.next()) {
                return new StoragePlan(result.getString("name"), result.getBool("active"), new StorageMetaData(result.getFloat("percent_inventory_cost"), result.getInt("meta_data_id")), null, result.getInt("plan_id"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public void update(StoragePlan storagePlan) throws DataAccessException {
        if (storagePlan.getId() == null) {
            throw new IllegalArgumentException("this object isnt in the database and cant be updated");
        }
        try {
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("UPDATE storage_plans SET name = ?, active = ? WHERE id = ?");
            stmt.setString(1, storagePlan.getName());
            stmt.setInt(2, storagePlan.isActive() ? 1 : 0);
            stmt.setInt(3, storagePlan.getId());
            stmt.execute();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
