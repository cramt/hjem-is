package hjem.is.db;

import hjem.is.model.StoragePlan;

import java.util.List;

public interface IStoragePlanStore {
    StoragePlan getById(int id) throws DataAccessException;
    StoragePlan getByName(String name) throws DataAccessException;
    void add(StoragePlan storagePlan) throws DataAccessException;
    List<StoragePlan> getAll() throws DataAccessException;
    StoragePlan getActive() throws DataAccessException;
    void update(StoragePlan storagePlan) throws DataAccessException;
    void delete(StoragePlan storagePlan) throws DataAccessException;
}
