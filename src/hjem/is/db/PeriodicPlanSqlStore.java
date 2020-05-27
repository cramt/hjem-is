package hjem.is.db;

import hjem.is.model.time.Period;
import hjem.is.model.PeriodicPlan;
import hjem.is.model.Product;
import hjem.is.model.StoragePlan;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

public class PeriodicPlanSqlStore implements IPeriodicPlanStore {

    @Override
    public PeriodicPlan getById(int id) throws DataAccessException {
        try {
            FutureTask<Map<Product, Integer>> mapFuture = new FutureTask<>(() -> {
                PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("SELECT product_id, amount, periodic_plan_id, cost, name FROM plan_lines INNER JOIN products ON plan_lines.product_id = products.id WHERE periodic_plan_id = ?");
                stmt.setInt(1, id);
                NullableResultSet result = new NullableResultSet(stmt.executeQuery());
                Map<Product, Integer> map = new HashMap<>();
                while (result.next()) {
                    map.put(new Product(result.getInt("cost"), result.getString("name"), null, result.getInt("product_id")), result.getInt("amount"));
                }
                return map;
            });
            FutureTask<PeriodicPlan> planFuture = new FutureTask<>(() -> {
                PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("SELECT start_period, end_period FROM periodic_plans WHERE id = ?");
                stmt.setInt(1, id);
                NullableResultSet result = new NullableResultSet(stmt.executeQuery());
                if (result.next()) {
                    return new PeriodicPlan(null, new Period(result.getInt("start_period"), result.getInt("end_period")), null, id);
                } else {
                    return null;
                }
            });
            PeriodicPlan plan = planFuture.get();
            if (plan == null) {
                return null;
            }
            plan.setProductMap(mapFuture.get());
            return plan;
        } catch (ExecutionException | InterruptedException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<PeriodicPlan> getByStoragePlan(StoragePlan storagePlan) throws DataAccessException {
        try {
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("SELECT start_period, end_period, id FROM periodic_plans WHERE storage_plan_id = ?");
            stmt.setInt(1, storagePlan.getId());
            NullableResultSet result = new NullableResultSet(stmt.executeQuery());
            Map<Integer, PeriodicPlan> plans = new HashMap<>();
            while (result.next()) {
                int id = result.getInt("id");
                plans.put(id, new PeriodicPlan(new HashMap<>(), new Period(result.getInt("start_period"), result.getInt("end_period")), null, id));
            }
            stmt = DBConnection.getInstance().getConnection().prepareStatement("SELECT product_id, amount, periodic_plan_id, cost, name FROM plan_lines INNER JOIN products ON plan_lines.product_id = products.id WHERE " + Arrays.stream(new String[plans.size()]).map(x -> "periodic_plan_id = ?").collect(Collectors.joining(" OR ")));
            int i = 1;
            for (Integer key : plans.keySet()) {
                stmt.setInt(i++, key);
            }
            result = new NullableResultSet(stmt.executeQuery());
            while (result.next()) {
                plans.get(result.getInt("periodic_plan_id")).getProductMap().put(new Product(result.getInt("cost"), result.getString("name"), null, result.getInt("product_id")), result.getInt("amount"));
            }

            List<PeriodicPlan> plansList = plans.values().stream().collect(Collectors.toList());
            storagePlan.setPeriodicPlans(plansList);
            return plansList;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public void add(PeriodicPlan periodicPlan, StoragePlan storagePlan) throws DataAccessException {
        if (storagePlan.getId() == null) {
            new StoragePlanSqlStore().add(storagePlan);
        }
        try {
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO periodic_plans (start_period, end_period, storage_plan_id) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, periodicPlan.getPeriod().getStart());
            stmt.setInt(2, periodicPlan.getPeriod().getEnd());
            stmt.setInt(3, storagePlan.getId());
            stmt.execute();
            if (!storagePlan.getPeriodicPlans().contains(periodicPlan)) {
                storagePlan.getPeriodicPlans().add(periodicPlan);
            }
            NullableResultSet result = new NullableResultSet(stmt.getGeneratedKeys());
            result.next();
            periodicPlan.setId(result.getGeneratedKey());
            addProducts(periodicPlan);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(List<PeriodicPlan> plans) throws DataAccessException {
        try {
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM periodic_plans WHERE " + Arrays.stream(new String[plans.size()]).map(x -> ("id = ?")).collect(Collectors.joining(" OR ")));
            int i = 1;
            for (PeriodicPlan plan : plans) {
                stmt.setInt(i++, plan.getId());
            }
            stmt.execute();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public void update(PeriodicPlan plan, boolean products) throws DataAccessException {
        try {
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("UPDATE periodic_plans SET start_period = ?, end_period = ? WHERE id = ?");
            stmt.setInt(1, plan.getPeriod().getStart());
            stmt.setInt(2, plan.getPeriod().getEnd());
            stmt.setInt(3, plan.getId());
            stmt.execute();
            if (products) {
                stmt = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM plan_lines WHERE periodic_plan_id = ?");
                stmt.setInt(1, plan.getId());
                stmt.execute();
                addProducts(plan);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public void update(PeriodicPlan plan) throws DataAccessException {
        update(plan, true);
    }

    private void addProducts(PeriodicPlan plan) throws SQLException, DataAccessException {
        if(plan.getProductMap().size() == 0){
            return;
        }
        PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO plan_lines (amount, product_id, periodic_plan_id) VALUES " + Arrays.stream(new String[plan.getProductMap().size()]).map(x -> ("(?, ?, ?)")).collect(Collectors.joining(",")));
        int i = 1;
        for (Map.Entry<Product, Integer> productIntegerEntry : plan.getProductMap().entrySet()) {
            stmt.setInt(i++, productIntegerEntry.getValue());
            stmt.setInt(i++, productIntegerEntry.getKey().getId());
            stmt.setInt(i++, plan.getId());
        }
        stmt.execute();
    }
}
