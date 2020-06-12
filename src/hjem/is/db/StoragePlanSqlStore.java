package hjem.is.db;

import hjem.is.model.PeriodicPlan;
import hjem.is.model.StorageMetaData;
import hjem.is.model.StoragePlan;
import hjem.is.model.time.Period;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StoragePlanSqlStore implements IStoragePlanStore {

    @Override
    public StoragePlan getById(int id) throws DataAccessException {
        return null;
    }

    @Override
    public StoragePlan getByName(String name) throws DataAccessException {
        try {
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("SELECT storage_plans.id as p_id, active, storage_meta_data.id as md_id, percent_inventory_cost FROM storage_plans INNER JOIN storage_meta_data ON storage_plans.storage_meta_data_id = storage_meta_data.id WHERE name = ?");
            stmt.setString(1, name);
            NullableResultSet result = new NullableResultSet(stmt.executeQuery());
            if (result.next()) {
                StoragePlan storagePlan = new StoragePlan(name, result.getBool("active"), new StorageMetaData(result.getFloat("percent_inventory_cost"), result.getInt("md_id")), new ArrayList<>(), result.getInt("p_id"));
                stmt = DBConnection.getInstance().getConnection().prepareStatement("SELECT id, start_period, end_period FROM periodic_plans WHERE storage_plan_id = ?");
                stmt.setInt(1, storagePlan.getId());
                result = new NullableResultSet(stmt.executeQuery());
                while (result.next()) {
                    storagePlan.getPeriodicPlans().add(new PeriodicPlan(null, new Period(result.getInt("start_period"), result.getInt("end_period")), null, result.getInt("id")));
                }
                return storagePlan;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public void add(StoragePlan storagePlan) throws DataAccessException {
        if (getByName(storagePlan.getName()) != null) {
            throw new DataAccessException("this name already exists", null);
        }
        try {
            Integer metaDataId = storagePlan.getStorageMetaData().getId();
            if (metaDataId == null) {
                try {
                    PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO storage_meta_data (percent_inventory_cost) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS);
                    stmt.setFloat(1, storagePlan.getStorageMetaData().getPercentInventoryCost());
                    stmt.execute();
                    NullableResultSet result = new NullableResultSet(stmt.getGeneratedKeys());
                    result.next();
                    metaDataId = result.getGeneratedKey();

                } catch (SQLException e) {
                    throw new DataAccessException(e.getMessage(), e);
                }
            }

            if (storagePlan.isActive()) {
                resetActive();
            }
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO storage_plans (name, active, storage_meta_data_id) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, storagePlan.getName());
            stmt.setInt(2, storagePlan.isActive() ? 1 : 0);
            stmt.setInt(3, metaDataId);
            stmt.execute();
            NullableResultSet resultSet = new NullableResultSet(stmt.getGeneratedKeys());
            resultSet.next();
            int id = resultSet.getGeneratedKey();
            storagePlan.setId(id);

            if (storagePlan.getPeriodicPlans().size() != 0) {
                try {
                    PreparedStatement pstmt = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO periodic_plans (start_period, end_period, storage_plan_id) VALUES " + Arrays.stream(new String[storagePlan.getPeriodicPlans().size()]).map(x -> "(?, ?, ?)").collect(Collectors.joining(",")));
                    int i = 1;
                    for (PeriodicPlan periodicPlan : storagePlan.getPeriodicPlans()) {
                        pstmt.setInt(i++, periodicPlan.getPeriod().getStart());
                        pstmt.setInt(i++, periodicPlan.getPeriod().getEnd());
                        pstmt.setInt(i++, id);
                    }
                    pstmt.execute();
                } catch (SQLException e) {
                    throw new DataAccessException(e.getMessage(), e);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<StoragePlan> getAll() throws DataAccessException {
        try {
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("SELECT name, active, storage_plans.id as s_id, percent_inventory_cost, storage_meta_data.id as md_id FROM storage_plans INNER JOIN storage_meta_data ON storage_plans.storage_meta_data_id = storage_meta_data.id");
            NullableResultSet result = new NullableResultSet(stmt.executeQuery());
            List<StoragePlan> plans = new ArrayList<>();
            while (result.next()) {
                plans.add(new StoragePlan(result.getString("name"), result.getBool("active"), new StorageMetaData(result.getFloat("percent_inventory_cost"), result.getInt("md_id")), null, result.getInt("s_id")));
            }
            return plans;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public StoragePlan getActive() throws DataAccessException {
        try {
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("SELECT storage_plans.id as plan_id, name, active, storage_meta_data.id as meta_data_id, percent_inventory_cost FROM storage_plans INNER JOIN storage_meta_data ON storage_plans.storage_meta_data_id = storage_meta_data.id WHERE active = 1");
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
            if (storagePlan.isActive()) {
                resetActive();
            }
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("UPDATE storage_plans SET name = ?, active = ? WHERE id = ?");
            stmt.setString(1, storagePlan.getName());
            stmt.setInt(2, storagePlan.isActive() ? 1 : 0);
            stmt.setInt(3, storagePlan.getId());
            stmt.execute();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(StoragePlan storagePlan) throws DataAccessException {
        if (storagePlan.getId() == null) {
            throw new IllegalArgumentException("this object isnt in the database and cant be updated");
        }
        try {
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM storage_plans WHERE id = ?");
            stmt.setInt(1, storagePlan.getId());
            stmt.execute();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    private void resetActive() throws DataAccessException, SQLException {
        DBConnection.getInstance().getConnection().prepareStatement("UPDATE storage_plans SET active = 0").executeUpdate();
    }
}
